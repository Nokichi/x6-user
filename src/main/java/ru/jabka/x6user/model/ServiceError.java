package ru.jabka.x6user.model;

import lombok.Builder;

@Builder
public record ServiceError(Boolean success, String message) {
}