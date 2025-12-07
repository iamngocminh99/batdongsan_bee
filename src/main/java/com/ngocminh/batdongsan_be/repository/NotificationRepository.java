package com.ngocminh.batdongsan_be.repository;

import com.ngocminh.batdongsan_be.model.Notification;
import com.ngocminh.batdongsan_be.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface NotificationRepository extends JpaRepository<Notification, UUID> {
    List<Notification> findByReceiverOrderByCreatedAtDesc(User receiver);
    List<Notification> findAllByOrderByCreatedAtDesc();
}
