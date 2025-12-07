package com.ngocminh.batdongsan_be.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;


@Slf4j
@Service
@RequiredArgsConstructor
public class MailService {
    private final JavaMailSender mailSender;

    /**
     * Gửi mail xác minh tài khoản
     */
    public void sendVerificationEmail(String to, String link) {
        String subject = "Xác nhận đăng ký tài khoản Bất Động Sản";
        String html = """
                <h2>Chào mừng bạn đến với <span style='color:#f59e0b;'>BatDongSan.vn</span> 🏠</h2>
                <p>Cảm ơn bạn đã đăng ký tài khoản agent.</p>
                <p>Vui lòng nhấn vào liên kết bên dưới để xác minh email của bạn:</p>
                <p><a href='%s' style='background:#2563eb;color:white;padding:10px 16px;
                border-radius:6px;text-decoration:none;'>Xác nhận email</a></p>
                <p style='color:gray;'>Liên kết này sẽ hết hạn trong 24 giờ.</p>
                """.formatted(link);

        sendHtmlEmail(to, subject, html);
    }

    /**
     * Gửi mail thông báo gói hết hạn
     */
    public void sendPlanExpiredEmail(String to, String companyName, String planName, String endDate) {
        String subject = "Gói " + planName + " của bạn đã hết hạn!";
        String html = """
                <h3>Xin chào %s,</h3>
                <p>Gói <strong>%s</strong> của bạn đã hết hạn vào ngày <strong>%s</strong>.</p>
                <p>Hãy gia hạn ngay để tiếp tục đăng tin và sử dụng các tính năng dành cho Agent.</p>
                <p><a href='http://localhost:5173/agent/plan' 
                      style='background:#f97316;color:white;padding:10px 16px;
                      border-radius:6px;text-decoration:none;'>Gia hạn ngay</a></p>
                <br/>
                <p style='color:gray;'>Trân trọng,<br/>Đội ngũ BatDongSan.vn</p>
                """.formatted(companyName, planName, endDate);

        sendHtmlEmail(to, subject, html);
    }

    /**
     * Gửi mail xác nhận gia hạn thành công
     */
    public void sendPlanSuccessEmail(String to, String companyName, String planName, String endDate) {
        String subject = "Gói " + planName + " của bạn đã được kích hoạt thành công!";
        String html = """
                <h3>Xin chào %s,</h3>
                <p>Bạn đã gia hạn thành công gói <strong>%s</strong>.</p>
                <p>Thời hạn sử dụng đến ngày <strong>%s</strong>.</p>
                <p>Chúc bạn có nhiều giao dịch thành công 🎉</p>
                <br/>
                <p style='color:gray;'>Trân trọng,<br/>Đội ngũ BatDongSan.vn</p>
                """.formatted(companyName, planName, endDate);

        sendHtmlEmail(to, subject, html);
    }

    public void sendPlanExpiringSoonEmail(String to, String companyName, String planName, String endDate) {
        String subject = "⏰ Gói " + planName + " của bạn sắp hết hạn!";
        String html = """
            <h3>Xin chào %s,</h3>
            <p>Gói <strong>%s</strong> của bạn sẽ hết hạn vào ngày <strong>%s</strong>.</p>
            <p>Vui lòng gia hạn sớm để không bị gián đoạn khi đăng tin.</p>
            <p><a href='http://localhost:5173/agent/plan' 
                  style='background:#2563eb;color:white;padding:10px 16px;
                  border-radius:6px;text-decoration:none;'>Gia hạn ngay</a></p>
            <br/>
            <p style='color:gray;'>Trân trọng,<br/>Đội ngũ BatDongSan.vn</p>
            """.formatted(companyName, planName, endDate);

        sendHtmlEmail(to, subject, html);
    }



    private void sendHtmlEmail(String to, String subject, String htmlContent) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, "utf-8");
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlContent, true); // true = gửi HTML
            mailSender.send(message);
            log.info("Mail đã gửi đến {}", to);
        } catch (MessagingException e) {
            log.error(" Gửi mail thất bại đến {}: {}", to, e.getMessage());
            throw new RuntimeException("Không thể gửi email", e);
        }
    }
}
