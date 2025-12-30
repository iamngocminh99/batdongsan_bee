package com.ngocminh.batdongsan_be.controller;

import com.ngocminh.batdongsan_be.dto.request.SendMessageRequest;
import com.ngocminh.batdongsan_be.dto.response.ChatPartnerResponse;
import com.ngocminh.batdongsan_be.dto.response.MessageResponse;
import com.ngocminh.batdongsan_be.dto.response.UserResponse;
import com.ngocminh.batdongsan_be.model.Message;
import com.ngocminh.batdongsan_be.model.User;
import com.ngocminh.batdongsan_be.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;

    // Gửi tin nhắn
    @PostMapping("/send")
    public MessageResponse sendMessage(@RequestBody SendMessageRequest request) {
        Message msg = messageService.sendMessage(
                request.getSenderId(),
                request.getReceiverId(),
                request.getContent(),
                request.getPropertyId()
        );
        return messageService.toResponse(msg);
    }


    // Lấy toàn bộ lịch sử chat giữa 2 user
    @GetMapping("/conversation")
    public List<MessageResponse> getConversation(@RequestParam UUID user1Id,
                                                 @RequestParam UUID user2Id) {
        return messageService.getConversation(user1Id, user2Id);
    }

    // những người đã nhắn tin
    @GetMapping("/partners/{userId}")
    public List<ChatPartnerResponse> getChatPartners(@PathVariable UUID userId) {
        return messageService.getChatPartners(userId);
    }
}
