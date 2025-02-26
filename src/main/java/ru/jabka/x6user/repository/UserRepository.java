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
            INSERT INTO x6.user (name, email)
            VALUES (:name, :email)
            RETURNING *
            """;

    private static final String UPDATE = """
            UPDATE x6.user
            SET name = :name, email = :email, updated_at = CURRENT_TIMESTAMP
            WHERE id = :id
            RETURNING *
            """;

    private static final String SET_USAGES = """
            UPDATE x6.user
            SET used_at = CURRENT_TIMESTAMP
            WHERE id = :id
            RETURNING *
            """;

    private static final String GET_BY_ID = """
            SELECT *
            FROM x6.user
            WHERE id = :id
            """;

    private static final String EXISTS = """
            SELECT EXISTS (
                SELECT 1
                FROM x6.user
                WHERE id = :id
            )
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

    public boolean isExists(Long id) {
        return jdbcTemplate.queryForObject(EXISTS, new MapSqlParameterSource("id", id), Boolean.class);
    }

    public User setUsages(Long id) {
        return jdbcTemplate.queryForObject(SET_USAGES, new MapSqlParameterSource("id", id), userMapper);
    }

    private MapSqlParameterSource userToSql(User user) {
        return new MapSqlParameterSource()
                .addValue("id", user.id())
                .addValue("name", user.name())
                .addValue("email", user.email())
                .addValue("created_at", user.createdAt())
                .addValue("updated_at", user.updatedAt())
                .addValue("used_at", user.usedAt());
    }
}