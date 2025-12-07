package com.ngocminh.batdongsan_be.controller;

import com.ngocminh.batdongsan_be.dto.request.BroadcastRequest;
import com.ngocminh.batdongsan_be.dto.response.NotificationResponse;
import com.ngocminh.batdongsan_be.model.User;
import com.ngocminh.batdongsan_be.service.NotificationService;
import com.ngocminh.batdongsan_be.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;
    private final UserService userService;
    private final SimpMessagingTemplate messagingTemplate;

    @PostMapping("/broadcast")
    public List<NotificationResponse> broadcast(@RequestBody BroadcastRequest req, Principal principal) {
        User sender = userService.getByEmail(principal.getName());
        List<NotificationResponse> notis = notificationService.broadcast(
                sender, req.getTitle(), req.getMessage(), req.getLink(), req.getType());

        // Gửi 1 message cho tất cả client
        NotificationResponse broadcastMsg = NotificationResponse.builder()
                .title(req.getTitle())
                .message(req.getMessage())
                .link(req.getLink())
                .createdAt(LocalDateTime.now())
                .read(false)
                .build();

        messagingTemplate.convertAndSend("/topic/notifications", broadcastMsg);

        return notis;
    }


    @GetMapping
    public List<NotificationResponse> getUserNotifications(Principal principal) {
        User receiver = userService.getByEmail(principal.getName());
        return notificationService.getUserNotifications(receiver);
    }

    @GetMapping("/all")
    public List<NotificationResponse> getAllNotifications() {
        return notificationService.getAllNotifications();
    }

    @PostMapping("/{id}/read")
    public void markAsRead(@PathVariable UUID id) {
        notificationService.markAsRead(id);
    }

    @PostMapping("/send/{userId}")
    public NotificationResponse sendToUser(
            @PathVariable UUID userId,
            @RequestBody BroadcastRequest req,
            Principal principal
    ) {
        User sender = userService.getByEmail(principal.getName());
        return notificationService.sendToUser(
                sender,
                userId,
                req.getTitle(),
                req.getMessage(),
                req.getLink(),
                req.getType()
        );
    }

}
