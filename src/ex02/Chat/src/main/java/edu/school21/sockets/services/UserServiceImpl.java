package edu.school21.sockets.services;


import edu.school21.sockets.entity.Room;
import edu.school21.sockets.entity.User;
import edu.school21.sockets.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Component
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository<User> userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @Override
    public Boolean SignUp(String email, String password) {
        if (!userRepository.findByUsername(email).isPresent()) {
            User user = new User();
            user.setUsername(email);
            user.setPassword(passwordEncoder.encode(password));
            userRepository.save(user);
            return true;
        }
        return false;
    }

    @Override
    public Boolean SignIn(String username, String password) {
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isPresent()) {
            return passwordEncoder.matches(password, user.get().getPassword());
        }
        return false;
    }

    @Override
    public void saveRoom(String nameRoom) {
        userRepository.saveRoom(nameRoom);
    }

    @Override
    public Room findByNameRoom(String nameRoom) {
        return userRepository.findByNameRoom(nameRoom);
    }

    @Override
    public User findByUsername(String username) {
        return  userRepository.findByUsername(username).stream().findAny().orElseThrow();
    }

    @Override
    public List<Room> findAllRooms() {
        return userRepository.findAllRoom();
    }

    @Override
    public void SendMessageToDataBase(String username, String message, LocalDateTime time) {
        userRepository.sendMessage(username,message,time);
    }
}
