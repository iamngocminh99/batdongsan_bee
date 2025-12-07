package com.ngocminh.batdongsan_be.maper;

import com.ngocminh.batdongsan_be.dto.response.NotificationResponse;
import com.ngocminh.batdongsan_be.model.Notification;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface NotificationMapper {

    @Mapping(target = "senderId", source = "sender.id")
    @Mapping(
            target = "senderName",
            expression = "java(notification.getSender() != null ? " +
                    "notification.getSender().getFirstName() + \" \" + notification.getSender().getLastName() : null)"
    )
    @Mapping(target = "receiverId", source = "receiver.id")
    @Mapping(
            target = "receiverName",
            expression = "java(notification.getReceiver() != null ? " +
                    "notification.getReceiver().getFirstName() + \" \" + notification.getReceiver().getLastName() : null)"
    )
    NotificationResponse toResponse(Notification notification);
}
