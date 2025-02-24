package ru.jabka.x6user.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.jabka.x6user.exception.BadRequestException;
import ru.jabka.x6user.model.User;
import ru.jabka.x6user.repository.mapper.UserMapper;

@Repository
@RequiredArgsConstructor
public class UserRepository {
    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final UserMapper userMapper;

    private static final String INSERT = """
            INSERT INTO x6.user (name, email, phone, birthday, gender)
            VALUES (:name, :email, :phone, :birthday, :gender)
            RETURNING *
            """;

    private static final String UPDATE = """
            UPDATE x6.user
            SET name = :name, email = :email, phone = :phone, birthday = :birthday, gender = :gender, total_spent = :total_spent
            WHERE id = :id
            RETURNING *
            """;

    private static final String GET_BY_ID = """
            SELECT *
            FROM x6.user
            WHERE id = :id
            """;

    public User insert(User user) {
        return jdbcTemplate.queryForObject(INSERT, userToSql(user), userMapper);
    }

    public User update(User user) {
        return jdbcTemplate.queryForObject(UPDATE, userToSql(user), userMapper);
    }

    public User getById(Long id) {
        try {
            return jdbcTemplate.queryForObject(GET_BY_ID, new MapSqlParameterSource("id", id), userMapper);
        } catch (Throwable e) {
            throw new BadRequestException(String.format("Пользователь с id %d не найден", id));
        }
    }

    private MapSqlParameterSource userToSql(User user) {
        return new MapSqlParameterSource()
                .addValue("id", user.id())
                .addValue("name", user.name())
                .addValue("email", user.email())
                .addValue("phone", user.phone())
                .addValue("created_at", user.createdAt())
                .addValue("birthday", user.birthday())
                .addValue("gender", user.gender().name())
                .addValue("total_spent", user.totalSpent());
    }
}