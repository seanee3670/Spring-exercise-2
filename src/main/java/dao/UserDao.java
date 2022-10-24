package dao;


import domain.User;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import java.sql.*;
import java.util.List;
import java.util.Map;

public class UserDao {
    private JdbcTemplate jdbcTemplate;

    public UserDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void add(final User user) throws SQLException {
        this.jdbcTemplate.update("INSERT INTO users(id, name, password) VALUES (?, ?, ?)",
                user.getId(), user.getName(), user.getPassword());
    }

    public User findById(String id) {
        try {
            return this.jdbcTemplate.queryForObject("SELECT * From users WHERE USERS.ID = ?", new RowMapper<User>() {
                @Override
                public User mapRow(ResultSet rs, int rowNum) throws SQLException {
                    System.out.println(rowNum);
                    return new User(rs.getString("id"), rs.getString("name"), rs.getString("password"));

                }
            }, id);
        } catch (DataAccessException e) {
            return null;
        }
    }

    public List<User> getAll() {
        return this.jdbcTemplate.query("SELECT * FROM users ORDER BY id", new RowMapper<User>() {
            @Override
            public User mapRow(ResultSet rs, int rowNum) throws SQLException {
                User user = new User(rs.getString("id"), rs.getString("name"), rs.getString("password"));
                return user;
            }
        });
    }

    public int getCountAll() throws SQLException {
        return this.jdbcTemplate.queryForObject("SELECT COUNT(*) FROM users",Integer.class);
    }

    public void deleteAll() throws SQLException{
        this.jdbcTemplate.update("DELETE FROM users");
    }

    public static void main(String[] args) {
        UserDao userDao = new UserDao(new JdbcTemplate());
//        userDao.add();
        User user = userDao.findById("6");
        System.out.println(user.getName());
    }
}
