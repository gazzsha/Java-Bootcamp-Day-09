package edu.school21.sockets.repository;

import java.sql.SQLException;
import java.util.Optional;

public interface UserRepository<T> extends CrudRepository<T> {
    Optional<T> findByUsername(String username);

}
