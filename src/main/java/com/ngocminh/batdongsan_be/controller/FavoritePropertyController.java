package com.ngocminh.batdongsan_be.controller;

import com.ngocminh.batdongsan_be.dto.FavoritePropertyDTO;
import com.ngocminh.batdongsan_be.model.FavoriteProperty;
import com.ngocminh.batdongsan_be.service.FavoritePropertyService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/favorites")
@RequiredArgsConstructor
public class FavoritePropertyController {

    private final FavoritePropertyService favoriteService;

    @PostMapping("/{userId}/{propertyId}")
    public FavoriteProperty addFavorite(@PathVariable UUID userId,
                                        @PathVariable UUID propertyId) {
        return favoriteService.addFavorite(userId, propertyId);
    }

    @GetMapping("/{userId}")
    public List<FavoritePropertyDTO> getFavorites(@PathVariable UUID userId) {
        return favoriteService.getFavorites(userId);
    }

    @DeleteMapping("/{userId}/{propertyId}")
    public String removeFavorite(@PathVariable UUID userId,
                                 @PathVariable UUID propertyId) {
        favoriteService.removeFavorite(userId, propertyId);
        return "Removed from favorites successfully";
    }

}
