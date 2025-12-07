package com.ngocminh.batdongsan_be.maper;

import com.ngocminh.batdongsan_be.dto.response.UserResponse;
import com.ngocminh.batdongsan_be.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(target = "planName", source = "agent.planName", qualifiedByName = "planTypeToString")
    @Mapping(target = "planStartDate", source = "agent.planStartDate")
    @Mapping(target = "planEndDate", source = "agent.planEndDate")
    @Mapping(target = "planPrice", source = "agent.planPrice")
    @Mapping(target = "paid", source = "agent.paid")
    UserResponse toUserResponse(User user);

    @Named("planTypeToString")
    default String planTypeToString(Enum<?> planType) {
        return planType != null ? planType.name() : null;
    }
}
