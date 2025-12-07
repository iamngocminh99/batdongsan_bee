//package com.ngocminh.batdongsan_be.controller;
//
//import com.ngocminh.batdongsan_be.model.Location;
//import com.ngocminh.batdongsan_be.model.Location.LocationType;
//import com.ngocminh.batdongsan_be.service.LocationService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.data.domain.Page;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.Optional;
//import java.util.UUID;
//
//@RestController
//@RequestMapping("/api/locations")
//@RequiredArgsConstructor
//public class LocationController {
//
//    private final LocationService locationService;
//
//    // Create
//    @PostMapping
//    public ResponseEntity<Location> create(@RequestBody Location location) {
//        return ResponseEntity.ok(locationService.create(location));
//    }
//
//    // Read
//    @GetMapping("/{id}")
//    public ResponseEntity<Location> getById(@PathVariable UUID id) {
//        Optional<Location> location = locationService.getById(id);
//        return location.map(ResponseEntity::ok)
//                .orElse(ResponseEntity.notFound().build());
//    }
//
//    // Update
//    @PutMapping("/{id}")
//    public ResponseEntity<Location> update(@PathVariable UUID id, @RequestBody Location location) {
//        return ResponseEntity.ok(locationService.update(id, location));
//    }
//
//    // Delete
//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> delete(@PathVariable UUID id) {
//        locationService.delete(id);
//        return ResponseEntity.noContent().build();
//    }
//
//    // Search + Pagination
//    @GetMapping
//    public ResponseEntity<Page<Location>> search(
//            @RequestParam(required = false) String name,
//            @RequestParam(required = false) LocationType type,
//            @RequestParam(required = false) String country,
//            @RequestParam(defaultValue = "0") int page,
//            @RequestParam(defaultValue = "10") int size
//    ) {
//        return ResponseEntity.ok(locationService.search(name, type, country, page, size));
//    }
//}
