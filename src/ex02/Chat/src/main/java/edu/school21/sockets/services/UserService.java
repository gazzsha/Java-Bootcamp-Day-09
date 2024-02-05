package edu.school21.sockets.services;

import edu.school21.sockets.entity.Room;
import edu.school21.sockets.entity.User;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

public interface UserService {
    Boolean SignUp(String username,String password);

    Boolean SignIn(String username,String password);

    void saveRoom(String nameRoom);

    Room findByNameRoom(String nameRoom);


    User findByUsername(String username);
    List<Room> findAllRooms();

    void SendMessageToDataBase(String username, String message, LocalDateTime time);
}
