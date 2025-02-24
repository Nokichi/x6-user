package ru.jabka.x6user.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.jabka.x6user.exception.BadRequestException;
import ru.jabka.x6user.model.Gender;
import ru.jabka.x6user.model.User;
import ru.jabka.x6user.model.UserExists;
import ru.jabka.x6user.repository.UserRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void create_success() {
        final User user = getValidUser();
        Mockito.when(userRepository.insert(user)).thenReturn(user);
        User result = userService.create(user);
        Assertions.assertEquals(user, result);
        Mockito.verify(userRepository).insert(user);
    }

    @Test
    void update_success() {
        final User user = getValidUser();
        Mockito.when(userRepository.update(user)).thenReturn(user);
        User result = userService.update(user);
        Assertions.assertEquals(user, result);
        Mockito.verify(userRepository).update(user);
    }

    @Test
    void getById_success() {
        final User user = getValidUser();
        Mockito.when(userRepository.getById(user.id())).thenReturn(user);
        User result = userService.getById(user.id());
        Assertions.assertEquals(user, result);
        Mockito.verify(userRepository).getById(user.id());
    }

    @Test
    void isUserExistsById_found() {
        final User user = getValidUser();
        Mockito.when(userRepository.getById(user.id())).thenReturn(user);
        UserExists exists = new UserExists(true);
        UserExists result = userService.isUserExistsById(user.id());
        Assertions.assertEquals(exists, result);
        Mockito.verify(userRepository).getById(user.id());
    }

    @Test
    void isUserExistsById_notFound() {
        long fakeId = 1L;
        Mockito.when(userRepository.getById(fakeId)).thenThrow(BadRequestException.class);
        UserExists exists = new UserExists(false);
        UserExists result = userService.isUserExistsById(fakeId);
        Assertions.assertEquals(exists, result);
        Mockito.verify(userRepository).getById(fakeId);
    }

    @Test
    void create_error_nullUser() {
        final User user = null;
        final BadRequestException exception = Assertions.assertThrows(
                BadRequestException.class,
                () -> userService.create(user)
        );
        Assertions.assertEquals(exception.getMessage(), "Введите информацию о пользователе");
        Mockito.verify(userRepository, Mockito.never()).insert(Mockito.any());
    }

    @Test
    void create_error_nullName() {
        final User user = User.builder()
                .id(2L)
                .name(null)
                .email("vito@mail.ru")
                .phone("+79995556611")
                .gender(Gender.MALE)
                .birthday(LocalDate.of(1991, 2, 3))
                .build();
        final BadRequestException exception = Assertions.assertThrows(
                BadRequestException.class,
                () -> userService.create(user)
        );
        Assertions.assertEquals(exception.getMessage(), "Укажите имя пользователя!");
        Mockito.verify(userRepository, Mockito.never()).insert(Mockito.any());
    }

    @Test
    void create_error_nullEmail() {
        final User user = User.builder()
                .id(2L)
                .name("Витька")
                .email(null)
                .phone("+79995556611")
                .gender(Gender.MALE)
                .birthday(LocalDate.of(1991, 2, 3))
                .build();
        final BadRequestException exception = Assertions.assertThrows(
                BadRequestException.class,
                () -> userService.create(user)
        );
        Assertions.assertEquals(exception.getMessage(), "Укажите email пользователя!");
        Mockito.verify(userRepository, Mockito.never()).insert(Mockito.any());
    }

    @Test
    void create_error_invalidEmail() {
        final User user = User.builder()
                .id(2L)
                .name("Витька")
                .email("mail")
                .phone("+79995556611")
                .gender(Gender.MALE)
                .birthday(LocalDate.of(1991, 2, 3))
                .build();
        final BadRequestException exception = Assertions.assertThrows(
                BadRequestException.class,
                () -> userService.create(user)
        );
        Assertions.assertEquals(exception.getMessage(), "Email пользователя не соответствует маске");
        Mockito.verify(userRepository, Mockito.never()).insert(Mockito.any());
    }

    @Test
    void create_error_nullPhone() {
        final User user = User.builder()
                .id(2L)
                .name("Витька")
                .email("vito@mail.ru")
                .phone(null)
                .gender(Gender.MALE)
                .birthday(LocalDate.of(1991, 2, 3))
                .build();
        final BadRequestException exception = Assertions.assertThrows(
                BadRequestException.class,
                () -> userService.create(user)
        );
        Assertions.assertEquals(exception.getMessage(), "Укажите телефон пользователя!");
        Mockito.verify(userRepository, Mockito.never()).insert(Mockito.any());
    }

    @Test
    void create_error_invalidPhone() {
        final User user = User.builder()
                .id(2L)
                .name("Витька")
                .email("vito@mail.ru")
                .phone("123")
                .gender(Gender.MALE)
                .birthday(LocalDate.of(1991, 2, 3))
                .build();
        final BadRequestException exception = Assertions.assertThrows(
                BadRequestException.class,
                () -> userService.create(user)
        );
        Assertions.assertEquals(exception.getMessage(), "Телефон пользователя не соответствует маске");
        Mockito.verify(userRepository, Mockito.never()).insert(Mockito.any());
    }

    @Test
    void create_error_nullBirthday() {
        final User user = User.builder()
                .id(2L)
                .name("Витька")
                .email("vito@mail.ru")
                .phone("+79995556611")
                .gender(Gender.MALE)
                .birthday(null)
                .build();
        final BadRequestException exception = Assertions.assertThrows(
                BadRequestException.class,
                () -> userService.create(user)
        );
        Assertions.assertEquals(exception.getMessage(), "Укажите дату рождения пользователя");
        Mockito.verify(userRepository, Mockito.never()).insert(Mockito.any());
    }

    @Test
    void create_error_nullGender() {
        final User user = User.builder()
                .id(2L)
                .name("Витька")
                .email("vito@mail.ru")
                .phone("+79995556611")
                .gender(null)
                .birthday(LocalDate.of(1991, 2, 3))
                .build();
        final BadRequestException exception = Assertions.assertThrows(
                BadRequestException.class,
                () -> userService.create(user)
        );
        Assertions.assertEquals(exception.getMessage(), "Укажите пол пользователя");
        Mockito.verify(userRepository, Mockito.never()).insert(Mockito.any());
    }

    @Test
    void update_error_nullUser() {
        final User user = null;
        final BadRequestException exception = Assertions.assertThrows(
                BadRequestException.class,
                () -> userService.update(user)
        );
        Assertions.assertEquals(exception.getMessage(), "Введите информацию о пользователе");
        Mockito.verify(userRepository, Mockito.never()).update(Mockito.any());
    }

    @Test
    void update_error_nullName() {
        final User user = User.builder()
                .id(2L)
                .name(null)
                .email("vito@mail.ru")
                .phone("+79995556611")
                .gender(Gender.MALE)
                .birthday(LocalDate.of(1991, 2, 3))
                .build();
        final BadRequestException exception = Assertions.assertThrows(
                BadRequestException.class,
                () -> userService.update(user)
        );
        Assertions.assertEquals(exception.getMessage(), "Укажите имя пользователя!");
        Mockito.verify(userRepository, Mockito.never()).update(Mockito.any());
    }

    @Test
    void update_error_nullEmail() {
        final User user = User.builder()
                .id(2L)
                .name("Витька")
                .email(null)
                .phone("+79995556611")
                .gender(Gender.MALE)
                .birthday(LocalDate.of(1991, 2, 3))
                .build();
        final BadRequestException exception = Assertions.assertThrows(
                BadRequestException.class,
                () -> userService.update(user)
        );
        Assertions.assertEquals(exception.getMessage(), "Укажите email пользователя!");
        Mockito.verify(userRepository, Mockito.never()).update(Mockito.any());
    }

    @Test
    void update_error_invalidEmail() {
        final User user = User.builder()
                .id(2L)
                .name("Витька")
                .email("mail")
                .phone("+79995556611")
                .gender(Gender.MALE)
                .birthday(LocalDate.of(1991, 2, 3))
                .build();
        final BadRequestException exception = Assertions.assertThrows(
                BadRequestException.class,
                () -> userService.update(user)
        );
        Assertions.assertEquals(exception.getMessage(), "Email пользователя не соответствует маске");
        Mockito.verify(userRepository, Mockito.never()).update(Mockito.any());
    }

    @Test
    void update_error_nullPhone() {
        final User user = User.builder()
                .id(2L)
                .name("Витька")
                .email("vito@mail.ru")
                .phone(null)
                .gender(Gender.MALE)
                .birthday(LocalDate.of(1991, 2, 3))
                .build();
        final BadRequestException exception = Assertions.assertThrows(
                BadRequestException.class,
                () -> userService.update(user)
        );
        Assertions.assertEquals(exception.getMessage(), "Укажите телефон пользователя!");
        Mockito.verify(userRepository, Mockito.never()).update(Mockito.any());
    }

    @Test
    void update_error_invalidPhone() {
        final User user = User.builder()
                .id(2L)
                .name("Витька")
                .email("vito@mail.ru")
                .phone("123")
                .gender(Gender.MALE)
                .birthday(LocalDate.of(1991, 2, 3))
                .build();
        final BadRequestException exception = Assertions.assertThrows(
                BadRequestException.class,
                () -> userService.update(user)
        );
        Assertions.assertEquals(exception.getMessage(), "Телефон пользователя не соответствует маске");
        Mockito.verify(userRepository, Mockito.never()).update(Mockito.any());
    }

    @Test
    void update_error_nullBirthday() {
        final User user = User.builder()
                .id(2L)
                .name("Витька")
                .email("vito@mail.ru")
                .phone("+79995556611")
                .gender(Gender.MALE)
                .birthday(null)
                .build();
        final BadRequestException exception = Assertions.assertThrows(
                BadRequestException.class,
                () -> userService.update(user)
        );
        Assertions.assertEquals(exception.getMessage(), "Укажите дату рождения пользователя");
        Mockito.verify(userRepository, Mockito.never()).update(Mockito.any());
    }

    @Test
    void update_error_nullGender() {
        final User user = User.builder()
                .id(2L)
                .name("Витька")
                .email("vito@mail.ru")
                .phone("+79995556611")
                .gender(null)
                .birthday(LocalDate.of(1991, 2, 3))
                .build();
        final BadRequestException exception = Assertions.assertThrows(
                BadRequestException.class,
                () -> userService.update(user)
        );
        Assertions.assertEquals(exception.getMessage(), "Укажите пол пользователя");
        Mockito.verify(userRepository, Mockito.never()).update(Mockito.any());
    }

    private User getValidUser() {
        return User.builder()
                .id(1L)
                .name("Ванька")
                .email("vano@mail.ru")
                .phone("+79995556622")
                .createdAt(LocalDateTime.now())
                .gender(Gender.MALE)
                .birthday(LocalDate.of(1999, 3, 10))
                .totalSpent(0.0)
                .build();
    }
}