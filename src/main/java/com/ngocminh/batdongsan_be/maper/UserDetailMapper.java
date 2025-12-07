package com.ngocminh.batdongsan_be.maper;

import com.ngocminh.batdongsan_be.dto.response.AgentResponse;
import com.ngocminh.batdongsan_be.dto.response.UserDetailResponse;
import com.ngocminh.batdongsan_be.model.Agent;
import com.ngocminh.batdongsan_be.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserDetailMapper {

    @Mapping(target = "agent", source = "agent")
    UserDetailResponse toUserDetailResponse(User user);

    AgentResponse toAgentResponse(Agent agent);
}
