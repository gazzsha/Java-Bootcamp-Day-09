package edu.school21.sockets.services;

import java.sql.SQLException;
import java.time.LocalDateTime;

public interface UserService {
    Boolean SignUp(String username,String password);

    Boolean SignIn(String username,String password);

    void SendMessageToDataBase(String username, String message, LocalDateTime time);
}
