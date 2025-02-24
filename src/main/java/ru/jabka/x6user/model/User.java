package ru.jabka.x6user.model;

import lombok.Builder;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Builder
public record User(
        Long id,
        String name,
        String email,
        String phone,
        LocalDateTime createdAt,
        LocalDate birthday,
        Gender gender,
        Double totalSpent) {
}