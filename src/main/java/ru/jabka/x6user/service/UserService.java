package ru.jabka.x6user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import ru.jabka.x6user.exception.BadRequestException;
import ru.jabka.x6user.model.User;
import ru.jabka.x6user.model.UserExists;
import ru.jabka.x6user.repository.UserRepository;
import ru.jabka.x6user.util.EmailValidator;
import ru.jabka.x6user.util.PhoneValidator;

import static java.util.Optional.ofNullable;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    @Transactional(rollbackFor = Throwable.class)
    public User create(final User user) {
        validateUser(user);
        return userRepository.insert(user);
    }

    @Transactional(rollbackFor = Throwable.class)
    public User update(final User user) {
        validateUser(user);
        ofNullable(user.totalSpent()).orElseThrow(() -> new BadRequestException("Укажите total затраты по пользователю"));
        return userRepository.update(user);
    }

    @Transactional(readOnly = true)
    public User getById(Long id) {
        return userRepository.getById(id);
    }

    @Transactional(readOnly = true)
    public UserExists isUserExistsById(Long id) {
        try {
            getById(id);
            return new UserExists(true);
        } catch (BadRequestException e) {
            return new UserExists(false);
        }
    }

    private void validateUser(User user) {
        ofNullable(user).orElseThrow(() -> new BadRequestException("Введите информацию о пользователе"));
        if (!StringUtils.hasText(user.name())) {
            throw new BadRequestException("Укажите имя пользователя!");
        }
        if (!StringUtils.hasText(user.email())) {
            throw new BadRequestException("Укажите email пользователя!");
        }
        if (!EmailValidator.isValidEmail(user.email())) {
            throw new BadRequestException("Email пользователя не соответствует маске");
        }
        if (!StringUtils.hasText(user.phone())) {
            throw new BadRequestException("Укажите телефон пользователя!");
        }
        if (!PhoneValidator.isValidPhone(user.phone())) {
            throw new BadRequestException("Телефон пользователя не соответствует маске");
        }
        ofNullable(user.birthday()).orElseThrow(() -> new BadRequestException("Укажите дату рождения пользователя"));
        ofNullable(user.gender()).orElseThrow(() -> new BadRequestException("Укажите пол пользователя"));
    }
}