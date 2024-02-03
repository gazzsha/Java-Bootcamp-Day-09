package edu.school21.sockets.services;


import edu.school21.sockets.entity.User;
import edu.school21.sockets.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.sql.SQLException;

@Component
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository<User> userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @Override
    public Boolean SignUp(String email,String password) {
            if (userRepository.findByUsername(email).isEmpty()) {
                User user = new User();
                user.setUsername(email);
                user.setPassword(passwordEncoder.encode(password));
                userRepository.save(user);
                return true;
            }
            return false;
    }
}
