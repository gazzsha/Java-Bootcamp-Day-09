package edu.school21.sockets.repository;

import edu.school21.sockets.entity.Room;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface UserRepository<T> extends CrudRepository<T> {
    Optional<T> findByUsername(String username);

    void sendMessage(String username, String message, LocalDateTime time);

    void saveRoom(String roomName);

    Room findByNameRoom(String roomName);

    List<Room> findAllRoom();


}
