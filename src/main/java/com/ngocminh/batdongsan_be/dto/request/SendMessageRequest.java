package com.ngocminh.batdongsan_be.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SendMessageRequest {
    private UUID senderId;
    private UUID receiverId;
    private String content;
    private UUID propertyId;
}
