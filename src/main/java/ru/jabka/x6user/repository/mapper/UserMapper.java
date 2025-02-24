package ru.jabka.x6user.repository.mapper;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.jabka.x6user.model.Gender;
import ru.jabka.x6user.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;

@Component
public class UserMapper implements RowMapper<User> {
    @Override
    public User mapRow(ResultSet rs, int rowNum) throws SQLException {
        return User.builder()
                .id(rs.getLong("id"))
                .name(rs.getString("name"))
                .email(rs.getString("email"))
                .phone(rs.getString("phone"))
                .createdAt(rs.getObject("created_at", Timestamp.class).toLocalDateTime())
                .birthday(rs.getObject("birthday", LocalDate.class))
                .gender(Gender.valueOf(rs.getString("gender")))
                .totalSpent(rs.getDouble("total_spent"))
                .build();
    }
}