package com.ngocminh.batdongsan_be.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UuidGenerator;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "payments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payment {

    @Id
    @UuidGenerator
    @JdbcTypeCode(SqlTypes.CHAR)
    @Column(columnDefinition = "CHAR(36)", updatable = false, nullable = false)
    private UUID id;

    @Column(nullable = false, unique = true)
    private String txnRef; // Mã giao dịch VNPay (vnp_TxnRef)

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal amount;

    private String bankCode;           // Mã ngân hàng (VD: NCB, VCB, ...)
    private String orderInfo;          // Thông tin đơn hàng (VD: “Thanh toán gói: MONTHLY”)
    private String transactionStatus;  // Trạng thái từ VNPay (VD: "00", "01")
    private String responseCode;       // vnp_ResponseCode
    private String status;             // SUCCESS / FAILED / PENDING
    private String ipAddress;          // IP client khi tạo giao dịch

    @Column(nullable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Enumerated(EnumType.STRING)
    private Agent.PlanType planName;
}
