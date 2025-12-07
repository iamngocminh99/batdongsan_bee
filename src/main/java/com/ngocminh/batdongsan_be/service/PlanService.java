package com.ngocminh.batdongsan_be.service;

import com.ngocminh.batdongsan_be.model.Plan;
import com.ngocminh.batdongsan_be.repository.PlanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PlanService {

    private final PlanRepository planRepository;

    public Plan create(Plan plan) {
        return planRepository.save(plan);
    }

    public Optional<Plan> getById(UUID id) {
        return planRepository.findById(id);
    }

    public Plan update(UUID id, Plan planDetails) {
        return planRepository.findById(id)
                .map(plan -> {
                    plan.setName(planDetails.getName());
                    plan.setPrice(planDetails.getPrice());
                    plan.setUnit(planDetails.getUnit());
                    plan.setDescription(planDetails.getDescription());
                    plan.setFeatures(planDetails.getFeatures());
                    plan.setType(planDetails.getType());
                    plan.setActive(planDetails.getActive());
                    return planRepository.save(plan);
                })
                .orElseThrow(() -> new RuntimeException("Plan not found"));
    }

    public void delete(UUID id) {
        planRepository.deleteById(id);
    }

    public Page<Plan> search(String name, Plan.PlanType type, Boolean active, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Plan> all = planRepository.findAll(pageable);

        var filtered = all.getContent().stream()
                .filter(p -> (name == null || p.getName().toLowerCase().contains(name.toLowerCase())) &&
                        (type == null || p.getType() == type) &&
                        (active == null || p.getActive().equals(active)))
                .collect(Collectors.toList());

        return new PageImpl<>(filtered, pageable, filtered.size());
    }
}
