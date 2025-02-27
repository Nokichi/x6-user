package ru.jabka.x6user.model;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record User(
        Long id,
        String name,
        String email,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        LocalDateTime usedAt) {
}