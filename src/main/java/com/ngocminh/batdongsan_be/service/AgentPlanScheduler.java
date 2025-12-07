package com.ngocminh.batdongsan_be.service;

import com.ngocminh.batdongsan_be.model.Agent;
import com.ngocminh.batdongsan_be.repository.AgentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

// Service chạy ngầm mỗi ngày để kiểm tra agent hết hạn gói.
@Service
@RequiredArgsConstructor
@Slf4j
public class AgentPlanScheduler {

    private final AgentRepository agentRepository;
    private final MailService mailService;

    // set chạy 00:00 mỗi ngày
    @Scheduled(cron = "0 0 0 * * *")
    // Chạy ngay lập tức khi app khởi động, rồi lặp lại mỗi 60 giây (tính từ khi bắt đầu
    // @Scheduled(fixedRate = 60000)
    public void checkAndUpdateExpiredPlans() {
        System.out.println("=== Bắt đầu kiểm tra agent hết hạn gói ===");

        LocalDateTime now = LocalDateTime.now();
        List<Agent> agents = agentRepository.findAll();

        System.out.println("Tổng số agent: " + agents.size());

        for (Agent agent : agents) {
            if (agent.getUser() == null) {
                System.out.println("Agent " + agent.getId() + " không có user liên kết, bỏ qua.");
                continue;
            }
            if (agent.getPlanEndDate() == null) continue;

            LocalDateTime endDate = agent.getPlanEndDate();
            long daysLeft = ChronoUnit.DAYS.between(now.toLocalDate(), endDate.toLocalDate());

            System.out.println("→ Agent: " + agent.getCompanyName()
                    + " | Gói: " + agent.getPlanName()
                    + " | Còn " + daysLeft + " ngày | Hết hạn: " + endDate);

            // Nếu còn 3 ngày hoặc ít hơn => gửi mail cảnh báo
            if (agent.isPaid() && daysLeft <= 3 && daysLeft >= 0) {
                try {
                    mailService.sendPlanExpiringSoonEmail(
                            agent.getUser().getEmail(),
                            agent.getCompanyName(),
                            agent.getPlanName().name(),
                            endDate.toLocalDate().toString()
                    );
                    System.out.println("Đã gửi cảnh báo sắp hết hạn cho agent: " + agent.getCompanyName());
                } catch (Exception e) {
                    System.out.println("Gửi mail cảnh báo thất bại cho agent " + agent.getId() + ": " + e.getMessage());
                }
            }

            // Nếu đã hết hạn => cập nhật và gửi mail
            if (agent.isPaid() && now.isAfter(endDate)) {
                agent.setPaid(false);
                agentRepository.save(agent);
                System.out.println("Agent " + agent.getCompanyName() + " đã hết hạn gói " + agent.getPlanName());

                try {
                    mailService.sendPlanExpiredEmail(
                            agent.getUser().getEmail(),
                            agent.getCompanyName(),
                            agent.getPlanName().name(),
                            endDate.toLocalDate().toString()
                    );
                    System.out.println("Đã gửi email hết hạn cho agent: " + agent.getCompanyName());
                } catch (Exception e) {
                    System.out.println("Gửi mail hết hạn thất bại cho agent " + agent.getId() + ": " + e.getMessage());
                }
            }
        }

        System.out.println("=== Kết thúc kiểm tra agent hết hạn gói ===");
    }

}
