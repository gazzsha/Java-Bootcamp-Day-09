package edu.school21.sockets.repository;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface CrudRepository<T> {
    Optional<T> findByID(Integer id);

    List<T> findAll();

    void save(T entity);

    void update(T entity);

    void delete(Integer id);
}
