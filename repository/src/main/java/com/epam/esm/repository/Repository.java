package com.epam.esm.repository;

import com.epam.esm.entities.Identifiable;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface Repository<T extends Identifiable> {

    void save(T entity);
    Optional<T> getById(int id);
    List<T> getAll();
    boolean delete(int id);

}
