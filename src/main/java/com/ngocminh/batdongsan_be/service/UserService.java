package com.ngocminh.batdongsan_be.service;

import com.ngocminh.batdongsan_be.dto.response.UserDetailResponse;
import com.ngocminh.batdongsan_be.dto.response.UserResponse;
import com.ngocminh.batdongsan_be.maper.UserDetailMapper;
import com.ngocminh.batdongsan_be.maper.UserMapper;
import com.ngocminh.batdongsan_be.model.Agent;
import com.ngocminh.batdongsan_be.model.User;
import com.ngocminh.batdongsan_be.model.User.Role;
import com.ngocminh.batdongsan_be.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final UserDetailMapper userDetailMapper;

    public User create(User user) {
        // có thể hash password ở đây nếu cần
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Email đã tồn tại!");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public User update(UUID id, User userDetails) {
        return userRepository.findById(id).map(u -> {
            u.setEmail(userDetails.getEmail());
            u.setFirstName(userDetails.getFirstName());
            u.setLastName(userDetails.getLastName());
            u.setPhone(userDetails.getPhone());
            u.setRole(userDetails.getRole());
            // password có thể update riêng endpoint khác cho an toàn
            return userRepository.save(u);
        }).orElseThrow(() -> new RuntimeException("User not found"));
    }

    public void delete(UUID id) {
        userRepository.deleteById(id);
    }

    public Page<UserResponse> search(String email, Role role, String planName, Boolean expiring, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<User> users;

        // Nếu yêu cầu lọc sắp hết hạn
        if (Boolean.TRUE.equals(expiring)) {
            LocalDateTime sevenDaysLater = LocalDateTime.now().plusDays(7);
            users = userRepository.findByAgent_PlanEndDateBefore(sevenDaysLater, pageable);

            // Nếu lọc theo gói
        } else if (planName != null && !planName.isBlank()) {
            users = userRepository.findByAgent_PlanName(Agent.PlanType.valueOf(planName), pageable);

        } else if (email != null && !email.isBlank() && role != null) {
            users = userRepository.findByEmailContainingIgnoreCaseAndRole(email, role, pageable);
        } else if (email != null && !email.isBlank()) {
            users = userRepository.findByEmailContainingIgnoreCase(email, pageable);
        } else if (role != null) {
            users = userRepository.findByRole(role, pageable);
        } else {
            users = userRepository.findAll(pageable);
        }

        return new PageImpl<>(
                users.getContent().stream().map(userMapper::toUserResponse).collect(Collectors.toList()),
                pageable,
                users.getTotalElements()
        );
    }

    public User getByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
    }

    public UserDetailResponse getUserDetail(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return userDetailMapper.toUserDetailResponse(user);
    }

    public long countUsers() {
        return userRepository.count();
    }

    public Map<String, Long> getUserStats() {
        long totalUsers = userRepository.countByRole(User.Role.USER);
        long totalAgents = userRepository.countByRole(User.Role.AGENT);

        return Map.of(
                "totalUsers", totalUsers,
                "totalAgents", totalAgents
        );
    }

    public long countByRole(User.Role role) {
        return userRepository.countByRole(role);
    }

}
