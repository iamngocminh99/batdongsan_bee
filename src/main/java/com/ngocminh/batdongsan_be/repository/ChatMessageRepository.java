package com.ngocminh.batdongsan_be.repository;

import com.ngocminh.batdongsan_be.model.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;


@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, UUID> {
}


