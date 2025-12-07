package com.ngocminh.batdongsan_be.repository;

import com.ngocminh.batdongsan_be.model.Message;
import com.ngocminh.batdongsan_be.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface MessageRepository extends JpaRepository<Message, UUID> {

    // Lấy toàn bộ tin nhắn giữa 2 user (cả chiều đi và về)
    @Query("SELECT m FROM Message m " +
            "WHERE (m.sender = :user1 AND m.receiver = :user2) " +
            "   OR (m.sender = :user2 AND m.receiver = :user1) " +
            "ORDER BY m.sentAt ASC")
    List<Message> findConversation(User user1, User user2);

    // tất cả người mà user đã gửi tin đến
    @Query("SELECT DISTINCT m.receiver FROM Message m WHERE m.sender.id = :userId")
    List<User> findReceivers(UUID userId);

    // tất cả người đã gửi tin cho user
    @Query("SELECT DISTINCT m.sender FROM Message m WHERE m.receiver.id = :userId")
    List<User> findSenders(UUID userId);
}
