package com.ngocminh.batdongsan_be.controller;

import com.ngocminh.batdongsan_be.dto.response.UserDetailResponse;
import com.ngocminh.batdongsan_be.dto.response.UserResponse;
import com.ngocminh.batdongsan_be.model.User;
import com.ngocminh.batdongsan_be.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // Create
    @PostMapping
    public ResponseEntity<User> create(@RequestBody User user) {
        return ResponseEntity.ok(userService.create(user));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDetailResponse> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(userService.getUserDetail(id));
    }


    // Update
    @PutMapping("/{id}")
    public ResponseEntity<User> update(@PathVariable UUID id, @RequestBody User user) {
        return ResponseEntity.ok(userService.update(id, user));
    }

    // Delete
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // Search + Pagination
    @GetMapping
    public ResponseEntity<Page<UserResponse>> searchUsers(
            @RequestParam(required = false) String email,
            @RequestParam(required = false) User.Role role,
            @RequestParam(required = false) String planName,
            @RequestParam(required = false) Boolean expiring,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<UserResponse> users = userService.search(email, role, planName, expiring, page, size);
        return ResponseEntity.ok(users);
    }

    @GetMapping("/count")
    public ResponseEntity<Map<String, Long>> countUsers() {
        long total = userService.countUsers();
        return ResponseEntity.ok(Map.of("totalUsers", total));
    }

    @GetMapping("/stats")
    public ResponseEntity<Map<String, Long>> getUserStats() {
        return ResponseEntity.ok(userService.getUserStats());
    }



}
