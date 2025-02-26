package ru.jabka.x6user.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.jabka.x6user.model.User;
import ru.jabka.x6user.model.UserExists;
import ru.jabka.x6user.service.UserService;

@RestController
@RequiredArgsConstructor
@Tag(name = "Пользователи")
@RequestMapping("/api/v1/user")
public class UserController {
    public final UserService userService;

    @PostMapping
    public User create(@RequestBody final User user) {
        return userService.create(user);
    }

    @PatchMapping
    public User update(@RequestBody final User user) {
        return userService.update(user);
    }

    @GetMapping("/{id}")
    public User get(@PathVariable final Long id) {
        return userService.getById(id);
    }

    @GetMapping("/exists")
    public UserExists isUserExistsById(@RequestParam final Long id) {
        return userService.isUserExistsById(id);
    }
}