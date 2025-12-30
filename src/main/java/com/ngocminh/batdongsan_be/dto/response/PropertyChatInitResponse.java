package com.ngocminh.batdongsan_be.dto.response;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class PropertyChatInitResponse {
    private UUID ownerId;
    private String ownerFirstName;
    private String ownerLastName;
    private String ownerEmail;
    private String ownerPhone;

    private UUID propertyId;
    private String propertyTitle;
    private Double propertyPrice;
}
