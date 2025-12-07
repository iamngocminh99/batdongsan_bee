package com.ngocminh.batdongsan_be.repository;

import com.ngocminh.batdongsan_be.model.FavoriteProperty;
import com.ngocminh.batdongsan_be.model.Property;
import com.ngocminh.batdongsan_be.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FavoritePropertyRepository extends JpaRepository<FavoriteProperty, UUID> {

    List<FavoriteProperty> findByUser(User user);

    Optional<FavoriteProperty> findByUserAndProperty(User user, Property property);

    void deleteByUserAndProperty(User user, Property property);
}
