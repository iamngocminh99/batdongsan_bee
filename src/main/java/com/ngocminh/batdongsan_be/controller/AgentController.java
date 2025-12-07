package com.ngocminh.batdongsan_be.controller;

import com.ngocminh.batdongsan_be.model.Agent;
import com.ngocminh.batdongsan_be.service.AgentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/agents")
@RequiredArgsConstructor
public class AgentController {

    private final AgentService agentService;

    @PostMapping
    public ResponseEntity<Agent> create(@RequestBody Agent agent) {
        return ResponseEntity.ok(agentService.create(agent));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Agent> getById(@PathVariable UUID id) {
        Optional<Agent> agent = agentService.getById(id);
        return agent.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Agent> update(@PathVariable UUID id, @RequestBody Agent agent) {
        return ResponseEntity.ok(agentService.update(id, agent));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        agentService.delete(id);
        return ResponseEntity.noContent().build();
    }

//    @GetMapping("/find-by-email")
//    public ResponseEntity<Agent> findByEmail(@RequestParam String email) {
//        Optional<Agent> agent = agentService.findByEmail(email);
//        return agent.map(ResponseEntity::ok)
//                .orElse(ResponseEntity.notFound().build());
//    }
}
