package com.ngocminh.batdongsan_be.controller;

import com.ngocminh.batdongsan_be.model.Plan;
import com.ngocminh.batdongsan_be.service.PlanService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/plans")
@RequiredArgsConstructor
public class PlanController {

    private final PlanService planService;

    // Create
    @PostMapping
    public ResponseEntity<Plan> create(@RequestBody Plan plan) {
        return ResponseEntity.ok(planService.create(plan));
    }

    // Read
    @GetMapping("/{id}")
    public ResponseEntity<Plan> getById(@PathVariable UUID id) {
        Optional<Plan> plan = planService.getById(id);
        return plan.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Update
    @PutMapping("/{id}")
    public ResponseEntity<Plan> update(@PathVariable UUID id, @RequestBody Plan plan) {
        return ResponseEntity.ok(planService.update(id, plan));
    }

    // Delete
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        planService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // Search + Pagination
    @GetMapping
    public ResponseEntity<Page<Plan>> search(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Plan.PlanType type,
            @RequestParam(required = false) Boolean active,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(planService.search(name, type, active, page, size));
    }
}
