package com.ngocminh.batdongsan_be.dto.request;

import com.ngocminh.batdongsan_be.model.Notification;
import lombok.Data;

@Data
public class BroadcastRequest {
    private String title;
    private String message;
    private String link;
    private Notification.NotificationType type;
}