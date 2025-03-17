package ru.jabka.x6user.model;

import java.io.Serializable;

public record UserExists(boolean exists) implements Serializable {
}