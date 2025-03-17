package ru.jabka.x6user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import ru.jabka.x6user.exception.BadRequestException;
import ru.jabka.x6user.model.User;
import ru.jabka.x6user.model.UserExists;
import ru.jabka.x6user.repository.UserRepository;
import ru.jabka.x6user.util.EmailValidator;

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
        return userRepository.update(user);
    }

    @Transactional(rollbackFor = Throwable.class)
    public User getById(Long id) {
        userRepository.setUsages(id);
        return userRepository.getById(id);
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "userExists", key = "#id")
    public UserExists isUserExistsById(Long id) {
        return new UserExists(userRepository.isExists(id));
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
    }
}