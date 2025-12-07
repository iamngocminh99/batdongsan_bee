package com.ngocminh.batdongsan_be.service;

import com.ngocminh.batdongsan_be.dto.response.ChatPartnerResponse;
import com.ngocminh.batdongsan_be.dto.response.MessageResponse;
import com.ngocminh.batdongsan_be.dto.response.UserResponse;
import com.ngocminh.batdongsan_be.maper.UserMapper;
import com.ngocminh.batdongsan_be.model.Message;
import com.ngocminh.batdongsan_be.model.User;
import com.ngocminh.batdongsan_be.repository.MessageRepository;
import com.ngocminh.batdongsan_be.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public Message sendMessage(UUID senderId, UUID receiverId, String content) {
        User sender = userRepository.findById(senderId)
                .orElseThrow(() -> new RuntimeException("Sender not found"));
        User receiver = userRepository.findById(receiverId)
                .orElseThrow(() -> new RuntimeException("Receiver not found"));

        Message message = Message.builder()
                .sender(sender)
                .receiver(receiver)
                .content(content)
                .build();

        return messageRepository.save(message);
    }

    public List<MessageResponse> getConversation(UUID user1Id, UUID user2Id) {
        User user1 = userRepository.findById(user1Id)
                .orElseThrow(() -> new RuntimeException("User1 not found"));
        User user2 = userRepository.findById(user2Id)
                .orElseThrow(() -> new RuntimeException("User2 not found"));

        return messageRepository.findConversation(user1, user2)
                .stream()
                .map(msg -> {
                    MessageResponse dto = new MessageResponse();
                    dto.setId(msg.getId());
                    dto.setSenderId(msg.getSender().getId());
                    dto.setSenderName(msg.getSender().getFirstName() + " " + msg.getSender().getLastName());
                    dto.setReceiverId(msg.getReceiver().getId());
                    dto.setReceiverName(msg.getReceiver().getFirstName() + " " + msg.getReceiver().getLastName());
                    dto.setContent(msg.getContent());
                    dto.setSentAt(msg.getSentAt());
                    dto.setReadAt(msg.getReadAt());
                    return dto;
                })
                .toList();
    }


    public List<ChatPartnerResponse> getChatPartners(UUID userId) {
        Set<User> partners = new HashSet<>();
        partners.addAll(messageRepository.findReceivers(userId));
        partners.addAll(messageRepository.findSenders(userId));

        return partners.stream().map(user -> {
            ChatPartnerResponse dto = new ChatPartnerResponse();
            dto.setId(user.getId());
            dto.setFirstName(user.getFirstName());
            dto.setLastName(user.getLastName());
            dto.setEmail(user.getEmail());
            dto.setPhone(user.getPhone());
            return dto;
        }).toList();
    }

}
