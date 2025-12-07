package com.ngocminh.batdongsan_be.dto.response;

import com.ngocminh.batdongsan_be.model.Notification;
import lombok.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NotificationResponse {
    private UUID id;
    private String title;
    private String message;
    private String link;
    private String senderId;
    private String senderName;
    private String receiverId;
    private String receiverName;
    private Notification.NotificationType type;
    private boolean read;
    private LocalDateTime createdAt;
}

