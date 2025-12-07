package com.ngocminh.batdongsan_be.service;

import com.ngocminh.batdongsan_be.config.security.JwtService;
import com.ngocminh.batdongsan_be.dto.request.AgentRequest;
import com.ngocminh.batdongsan_be.dto.request.AuthRequest;
import com.ngocminh.batdongsan_be.model.Agent;
import com.ngocminh.batdongsan_be.model.User;
import com.ngocminh.batdongsan_be.repository.AgentRepository;
import com.ngocminh.batdongsan_be.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final MailService mailService;

    private final AgentRepository agentRepository;

    public String registerAgent(AgentRequest request) {
        String token = UUID.randomUUID().toString();
        LocalDateTime expiresAt = LocalDateTime.now().plusHours(24);

        // Tạo user
        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(User.Role.AGENT)
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .phone(request.getPhone())
                .emailVerified(false)
                .emailVerificationToken(token)
                .emailVerificationExpires(expiresAt)
                .build();
        userRepository.save(user);

        // Tạo agent
        Agent agent = Agent.builder()
                .user(user)
                .companyName(request.getCompanyName())
                .address(request.getAddress())
                .city(request.getCity())
                .mobile(request.getMobile())
                .logo(request.getLogo())
                .description(request.getDescription())
                .planName(Agent.PlanType.FREE)
                .planStartDate(LocalDateTime.now())
                .planEndDate(LocalDateTime.now().plusMonths(1))
                .planPrice(0.0)
                .maxProperties(10)
                .paid(true)
                .build();
        agentRepository.save(agent);

        String verifyLink = "http://localhost:8080/api/auth/verify-email?token=" + token;
        mailService.sendVerificationEmail(user.getEmail(), verifyLink);

        return "Registered successfully. Please check your email to verify.";
    }



    public String registerUser(AuthRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Email đã tồn tại!");
        }

        String token = UUID.randomUUID().toString();
        LocalDateTime expiresAt = LocalDateTime.now().plusHours(24);

        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .role(User.Role.USER)
                .emailVerified(false)
                .emailVerificationToken(token)
                .emailVerificationExpires(expiresAt)
                .build();

        userRepository.save(user);

        String verifyLink = "http://localhost:8080/api/auth/verify-email?token=" + token;
        mailService.sendVerificationEmail(user.getEmail(), verifyLink);

        return "Registered successfully. Please check your email to verify.";
    }

    public String verifyEmail(String token) {
        User user = userRepository.findByEmailVerificationToken(token)
                .orElseThrow(() -> new RuntimeException("Mã xác minh không hợp lệ"));

        if (user.getEmailVerificationExpires().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Link xác minh đã hết hạng");
        }

        user.setEmailVerified(true);
        user.setEmailVerificationToken(null);
        user.setEmailVerificationExpires(null);
        userRepository.save(user);

        return "Email đã xác thực thành công! Bạn có thể đăng nhập.";
    }

    public String login(AuthRequest request) {
        // 1. Tìm user theo email
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 2. Kiểm tra xác minh email
        if (!user.isEmailVerified()) {
            throw new RuntimeException("Vui lòng xác minh email của bạn trước khi đăng nhập.");
        }

        //3. Nếu là AGENT thì kiểm tra thông tin gói
        LocalDateTime planEndDate = null;
        Agent.PlanType planName = null;

        if (user.getRole() == User.Role.AGENT) {
            Agent agent = agentRepository.findByUser(user)
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy hồ sơ Agent."));

            // Nếu gói đã hết hạn
            if (agent.getPlanEndDate() != null && agent.getPlanEndDate().isBefore(LocalDateTime.now())) {
                throw new RuntimeException("Gói của bạn đã hết hạn. Vui lòng gia hạn để tiếp tục sử dụng.");
            }

            // kiểm tra thanh toán
            // if (!agent.isPaid()) {
            //     throw new RuntimeException("Bạn chưa thanh toán cho kỳ hiện tại. Vui lòng đóng phí để tiếp tục.");
            // }

            planEndDate = agent.getPlanEndDate();
            planName = agent.getPlanName();
        }

        // 4. Xác thực đăng nhập
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        // 5. Sinh token JWT
        return jwtService.generateToken(
                new org.springframework.security.core.userdetails.User(
                        user.getEmail(),
                        user.getPassword(),
                        List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()))
                ),
                planEndDate, // chỉ có giá trị nếu là AGENT, còn lại = null
                planName,    // chỉ có giá trị nếu là AGENT, còn lại = null
                user.getRole().name(),
                user.getId()
        );
    }

}

