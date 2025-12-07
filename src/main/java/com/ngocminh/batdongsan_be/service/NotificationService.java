package com.ngocminh.batdongsan_be.service;

import com.ngocminh.batdongsan_be.dto.response.NotificationResponse;
import com.ngocminh.batdongsan_be.maper.NotificationMapper;
import com.ngocminh.batdongsan_be.model.Notification;
import com.ngocminh.batdongsan_be.model.User;
import com.ngocminh.batdongsan_be.repository.NotificationRepository;
import com.ngocminh.batdongsan_be.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    private final NotificationMapper notificationMapper;
    private final SimpMessagingTemplate messagingTemplate;

    // Gửi broadcast cho tất cả USER + AGENT
    public List<NotificationResponse> broadcast(User sender, String title, String message, String link, Notification.NotificationType type) {
        List<User> receivers = userRepository.findByRoleIn(List.of(User.Role.USER, User.Role.AGENT));

        List<Notification> notifications = receivers.stream().map(receiver -> Notification.builder()
                .sender(sender)
                .receiver(receiver)
                .title(title)
                .message(message)
                .link(link)
                .type(type)
                .createdAt(LocalDateTime.now())
                .build()
        ).toList();

        return notificationRepository.saveAll(notifications)
                .stream()
                .map(notificationMapper::toResponse)
                .toList();
    }

    // Lấy notify theo user
    public List<NotificationResponse> getUserNotifications(User receiver) {
        return notificationRepository.findByReceiverOrderByCreatedAtDesc(receiver)
                .stream()
                .map(notificationMapper::toResponse)
                .toList();
    }

    // Lấy tất cả notify (Admin)
    public List<NotificationResponse> getAllNotifications() {
        return notificationRepository.findAllByOrderByCreatedAtDesc()
                .stream()
                .map(notificationMapper::toResponse)
                .toList();
    }

    // Đánh dấu đã đọc
    public void markAsRead(UUID id) {
        notificationRepository.findById(id).ifPresent(n -> {
            n.setRead(true);
            Notification saved = notificationRepository.save(n);

            // Push realtime update
            NotificationResponse response = notificationMapper.toResponse(saved);
            messagingTemplate.convertAndSend("/topic/notifications/read", response);
        });
    }

    // Gửi thông báo cho user
    public NotificationResponse sendToUser(User sender, UUID receiverId, String title, String message, String link, Notification.NotificationType type) {
        User receiver = userRepository.findById(receiverId)
                .orElseThrow(() -> new IllegalArgumentException("Receiver not found with id: " + receiverId));

        Notification notification = Notification.builder()
                .sender(sender)
                .receiver(receiver)
                .title(title)
                .message(message)
                .link(link)
                .type(type)
                .createdAt(LocalDateTime.now())
                .read(false)
                .build();

        Notification saved = notificationRepository.save(notification);
        NotificationResponse response = notificationMapper.toResponse(saved);

        messagingTemplate.convertAndSend("/topic/notifications/" + receiver.getId(), response);

        return response;
    }

}
