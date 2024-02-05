package edu.school21.sockets.repository;

import edu.school21.sockets.entity.Room;
import edu.school21.sockets.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Component
public class UserRepositoryImpl implements UserRepository<User> {

    private final JdbcTemplate jdbcTemplate;


    @Autowired
    public UserRepositoryImpl(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public Optional<User> findByID(Integer id) {
        return jdbcTemplate.query("SELECT * FROM users WHERE id = ?",
                (rs, numRow) -> {
                    User user = new User();
                    user.setId(rs.getInt(1));
                    user.setUsername(rs.getString(2));
                    user.setPassword(rs.getString(3));
                    return user;
                }, id).stream().findAny();
    }

    @Override
    public List<User> findAll() {
        return jdbcTemplate.query("SELECT * FROM users", (rs, rowNum) -> {
            User user = new User();
            user.setId(rs.getInt(1));
            user.setUsername(rs.getString(2));
            user.setPassword(rs.getString(3));
            return user;
        });
    }

    @Override
    public void save(User entity) {
        jdbcTemplate.update("INSERT INTO users(username,password) VALUES (?,?)",
                entity.getUsername(), entity.getPassword());
    }

    @Override
    public void update(User entity) {
        jdbcTemplate.update("UPDATE users SET username = ?,password = ? WHERE id = ?",
                entity.getUsername(), entity.getPassword(), entity.getId());
    }

    @Override
    public void delete(Integer id) {
        jdbcTemplate.update("DELETE FROM users WHERE id = ?", id);

    }

    @Override
    public Optional<User> findByUsername(String username) {
        return jdbcTemplate.query("SELECT * FROM users WHERE username = ?", (rs, rowNumber) -> {
            User user = new User();
            user.setId(rs.getInt(1));
            user.setUsername(rs.getString(2));
            user.setPassword(rs.getString(3));
            return user;
        }, username).stream().findAny();
    }

    @Override
    public void sendMessage(String username, String message, LocalDateTime time) {
        jdbcTemplate.update("INSERT INTO messages(Sender,message,time) VALUES " +
                "(?,?,?)",username,message,time);
    }

    @Override
    public void saveRoom(String roomName) {
        jdbcTemplate.update("INSERT INTO chat_rooms(name) VALUES (?)",roomName);
    }

    @Override
    public Room findByNameRoom(String roomName) {
        return jdbcTemplate.query("SELECT * FROM chat_rooms WHERE name = ?", (rs, rowNumber) -> {
            Room room = new Room();
            room.setId(rs.getInt(1));
            room.setName(rs.getString(2));
            return room;
        }, roomName).stream().findAny().orElseThrow();
    }

    @Override
    public List<Room> findAllRoom() {
        return jdbcTemplate.query("SELECT * FROM chat_rooms", (rs, rowNum) -> {
            Room room = new Room();
            room.setId(rs.getInt(1));
            room.setName(rs.getString(2));
            return room;
        });
    }


}
