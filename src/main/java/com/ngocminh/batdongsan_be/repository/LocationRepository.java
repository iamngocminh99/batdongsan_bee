//package com.ngocminh.batdongsan_be.repository;
//
//import com.ngocminh.batdongsan_be.model.Location;
//import com.ngocminh.batdongsan_be.model.Location.LocationType;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.jpa.repository.JpaRepository;
//
//import java.util.UUID;
//
//public interface LocationRepository extends JpaRepository<Location, UUID> {
//
//    // Tìm kiếm theo tên + loại + country
//    Page<Location> findByNameContainingIgnoreCaseAndTypeAndCountryContainingIgnoreCase(
//            String name, LocationType type, String country, Pageable pageable
//    );
//
//    Page<Location> findByNameContainingIgnoreCaseAndType(
//            String name, LocationType type, Pageable pageable
//    );
//
//    Page<Location> findByNameContainingIgnoreCase(
//            String name, Pageable pageable
//    );
//}
