package com.ngocminh.batdongsan_be.service;

import com.ngocminh.batdongsan_be.config.VNPayConfig;
import com.ngocminh.batdongsan_be.model.Agent;
import com.ngocminh.batdongsan_be.model.Payment;
import com.ngocminh.batdongsan_be.repository.AgentRepository;
import com.ngocminh.batdongsan_be.repository.PaymentRepository;
import com.ngocminh.batdongsan_be.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.*;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final AgentRepository agentRepository;
    private final UserRepository userRepository;

    @Transactional
    public String createPaymentUrl(HttpServletRequest req, BigDecimal totalVNPay, UUID userId, String planType)
            throws UnsupportedEncodingException {

        long amount = totalVNPay.multiply(BigDecimal.valueOf(100)).longValue();
        String vnp_TxnRef = VNPayConfig.getRandomNumber(8);
        String vnp_IpAddr = VNPayConfig.getIpAddress(req);

        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", VNPayConfig.vnp_Version);
        vnp_Params.put("vnp_Command", VNPayConfig.vnp_Command);
        vnp_Params.put("vnp_TmnCode", VNPayConfig.vnp_TmnCode);
        vnp_Params.put("vnp_Amount", String.valueOf(amount));
        vnp_Params.put("vnp_CurrCode", "VND");
        vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
        vnp_Params.put("vnp_OrderInfo", "Thanh toán gói: " + planType);
        vnp_Params.put("vnp_OrderType", "billpayment");
        vnp_Params.put("vnp_Locale", "vn");
        vnp_Params.put("vnp_IpAddr", vnp_IpAddr);
        vnp_Params.put("vnp_ReturnUrl", VNPayConfig.vnp_ReturnUrl);

        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        vnp_Params.put("vnp_CreateDate", formatter.format(cld.getTime()));

        List<String> fieldNames = new ArrayList<>(vnp_Params.keySet());
        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();

        for (Iterator<String> itr = fieldNames.iterator(); itr.hasNext(); ) {
            String fieldName = itr.next();
            String fieldValue = vnp_Params.get(fieldName);
            if (fieldValue != null && fieldValue.length() > 0) {
                hashData.append(fieldName).append('=')
                        .append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString()))
                        .append('=')
                        .append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                if (itr.hasNext()) {
                    hashData.append('&');
                    query.append('&');
                }
            }
        }

        String vnp_SecureHash = VNPayConfig.hmacSHA512(VNPayConfig.secretKey, hashData.toString());
        query.append("&vnp_SecureHash=").append(vnp_SecureHash);

        String paymentUrl = VNPayConfig.vnp_PayUrl + "?" + query.toString();


        paymentRepository.save(Payment.builder()
                .txnRef(vnp_TxnRef)
                .amount(totalVNPay)
                .createdAt(LocalDateTime.now())
                .planName(Agent.PlanType.valueOf(planType))
                .user(userRepository.findById(userId).orElse(null))
                .build());

        return paymentUrl;
    }

    @Transactional
    public void handleVNPayCallback(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String query = request.getQueryString();
        if (query == null) {
            response.sendRedirect("http://localhost:5173/agent/plan?status=fail&msg=missing_data");
            return;
        }

        // Parse tham số callback
        Map<String, String> params = new LinkedHashMap<>();
        for (String pair : query.split("&")) {
            int idx = pair.indexOf('=');
            if (idx > 0) {
                params.put(pair.substring(0, idx), pair.substring(idx + 1));
            }
        }

        // Xác thực chữ ký VNPay
        String vnp_SecureHash = params.remove("vnp_SecureHash");
        params.remove("vnp_SecureHashType");
        String hashData = VNPayConfig.hashAllFields(params);
        String signValue = VNPayConfig.hmacSHA512(VNPayConfig.secretKey.trim(), hashData);

        if (!signValue.equalsIgnoreCase(vnp_SecureHash)) {
            response.sendRedirect("http://localhost:5173/agent/plan?status=fail&msg=invalid_signature");
            return;
        }

        String txnRef = params.get("vnp_TxnRef");
        String responseCode = params.get("vnp_ResponseCode");
        String bankCode = params.get("vnp_BankCode");

        Optional<Payment> optPayment = paymentRepository.findByTxnRef(txnRef);
        if (optPayment.isEmpty()) {
            response.sendRedirect("http://localhost:5173/agent/plan?status=fail&msg=payment_not_found");
            return;
        }

        Payment payment = optPayment.get();
        payment.setResponseCode(responseCode);
        payment.setBankCode(bankCode);
        payment.setTransactionStatus(params.get("vnp_TransactionStatus"));
        payment.setUpdatedAt(LocalDateTime.now());

        if ("00".equals(responseCode)) {
            Agent agent = payment.getUser().getAgent();
            if (agent == null) {
                response.sendRedirect("http://localhost:5173/agent/plan?status=fail&msg=agent_not_found");
                return;
            }

            agent.setPaid(true);
            agent.setPlanName(payment.getPlanName());
            agent.setPlanStartDate(LocalDateTime.now());

            switch (payment.getPlanName()) {
                case MONTHLY:
                    agent.setMaxProperties(100);
                    agent.setPlanEndDate(LocalDateTime.now().plusMonths(1));
                    break;
                case YEARLY:
                    agent.setMaxProperties(200);
                    agent.setPlanEndDate(LocalDateTime.now().plusYears(1));
                    break;
                case THREE_YEAR:
                    agent.setMaxProperties(400);
                    agent.setPlanEndDate(LocalDateTime.now().plusYears(3));
                    break;
                case FREE:
                default:
                    agent.setMaxProperties(3);
                    agent.setPlanEndDate(LocalDateTime.now().plusDays(30));
                    break;
            }

            agentRepository.save(agent);
            payment.setStatus("SUCCESS");
            paymentRepository.save(payment);

            // Redirect về giao diện Agent Plan thành công
            response.sendRedirect("http://localhost:5173/agent/plan?status=success&plan=" + payment.getPlanName());
            return;
        }

        payment.setStatus("FAILED");
        paymentRepository.save(payment);
        response.sendRedirect("http://localhost:5173/agent/plan?status=fail&msg=payment_failed");
    }

    public BigDecimal getTotalRevenue() {
        return paymentRepository.getTotalRevenue();
    }

    public List<Map<String, Object>> getMonthlyRevenue() {
        // Lấy dữ liệu doanh thu (group theo tháng)
        List<Object[]> rawData = paymentRepository.findMonthlyRevenue();

        // Chuyển dữ liệu thành dạng { month: "Jan", revenue: 12500 }
        List<Map<String, Object>> result = new ArrayList<>();
        for (Object[] row : rawData) {
            Integer monthNum = ((Number) row[0]).intValue();
            BigDecimal revenue = (BigDecimal) row[1];

            Map<String, Object> map = new HashMap<>();
            map.put("month", Month.of(monthNum).name().substring(0, 3)); // Jan, Feb, Mar...
            map.put("revenue", revenue);
            result.add(map);
        }
        return result;
    }

}
