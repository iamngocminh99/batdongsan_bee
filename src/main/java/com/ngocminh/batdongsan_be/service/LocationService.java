//package com.ngocminh.batdongsan_be.service;
//
//import com.ngocminh.batdongsan_be.model.Location;
//import com.ngocminh.batdongsan_be.model.Location.LocationType;
//import com.ngocminh.batdongsan_be.repository.LocationRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.data.domain.Pageable;
//import org.springframework.stereotype.Service;
//
//import java.util.Optional;
//import java.util.UUID;
//
//@Service
//@RequiredArgsConstructor
//public class LocationService {
//
//    private final LocationRepository locationRepository;
//
//    public Location create(Location location) {
//        return locationRepository.save(location);
//    }
//
//    public Optional<Location> getById(UUID id) {
//        return locationRepository.findById(id);
//    }
//
//    public Location update(UUID id, Location locationDetails) {
//        return locationRepository.findById(id).map(loc -> {
//            loc.setName(locationDetails.getName());
//            loc.setType(locationDetails.getType());
//            loc.setCountry(locationDetails.getCountry());
//            loc.setLatitude(locationDetails.getLatitude());
//            loc.setLongitude(locationDetails.getLongitude());
//            loc.setParentLocation(locationDetails.getParentLocation());
//            return locationRepository.save(loc);
//        }).orElseThrow(() -> new RuntimeException("Location not found"));
//    }
//
//    public void delete(UUID id) {
//        locationRepository.deleteById(id);
//    }
//
//    public Page<Location> search(String name, LocationType type, String country, int page, int size) {
//        Pageable pageable = PageRequest.of(page, size);
//
//        if (type != null && country != null && !country.isBlank()) {
//            return locationRepository.findByNameContainingIgnoreCaseAndTypeAndCountryContainingIgnoreCase(
//                    name != null ? name : "",
//                    type,
//                    country,
//                    pageable
//            );
//        } else if (type != null) {
//            return locationRepository.findByNameContainingIgnoreCaseAndType(
//                    name != null ? name : "",
//                    type,
//                    pageable
//            );
//        } else {
//            return locationRepository.findByNameContainingIgnoreCase(
//                    name != null ? name : "",
//                    pageable
//            );
//        }
//    }
//}
