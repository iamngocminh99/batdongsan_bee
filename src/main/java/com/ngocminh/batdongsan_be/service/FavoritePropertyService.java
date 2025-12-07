package com.ngocminh.batdongsan_be.service;

import com.ngocminh.batdongsan_be.dto.FavoritePropertyDTO;
import com.ngocminh.batdongsan_be.maper.FavoritePropertyMapper;
import com.ngocminh.batdongsan_be.model.FavoriteProperty;
import com.ngocminh.batdongsan_be.model.Property;
import com.ngocminh.batdongsan_be.model.User;
import com.ngocminh.batdongsan_be.repository.FavoritePropertyRepository;
import com.ngocminh.batdongsan_be.repository.PropertyRepository;
import com.ngocminh.batdongsan_be.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FavoritePropertyService {

    private final FavoritePropertyRepository favoriteRepo;
    private final UserRepository userRepo;
    private final PropertyRepository propertyRepo;
    private final FavoritePropertyMapper favoritePropertyMapper;

    @Transactional
    public FavoriteProperty addFavorite(UUID userId, UUID propertyId) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Property property = propertyRepo.findById(propertyId)
                .orElseThrow(() -> new RuntimeException("Property not found"));

        // Kiểm tra đã favorite chưa
        if (favoriteRepo.findByUserAndProperty(user, property).isPresent()) {
            throw new RuntimeException("Property already favorited");
        }

        FavoriteProperty favorite = FavoriteProperty.builder()
                .user(user)
                .property(property)
                .build();

        // tăng counter
        property.setFavoriteCount(property.getFavoriteCount() + 1);
        propertyRepo.save(property);

        return favoriteRepo.save(favorite);
    }

    public List<FavoritePropertyDTO> getFavorites(UUID userId) {
        var user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return favoritePropertyMapper.toDTOs(favoriteRepo.findByUser(user));
    }

    @Transactional
    public void removeFavorite(UUID userId, UUID propertyId) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Property property = propertyRepo.findById(propertyId)
                .orElseThrow(() -> new RuntimeException("Property not found"));

        FavoriteProperty favorite = favoriteRepo.findByUserAndProperty(user, property)
                .orElseThrow(() -> new RuntimeException("Favorite not found"));

        // giảm counter (chặn âm)
        property.setFavoriteCount(Math.max(0, property.getFavoriteCount() - 1));
        propertyRepo.save(property);

        favoriteRepo.delete(favorite);
    }

}
