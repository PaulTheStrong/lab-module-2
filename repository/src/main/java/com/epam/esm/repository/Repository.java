package com.epam.esm.repository;

import com.epam.esm.entities.Identifiable;

import java.util.List;
import java.util.Optional;

public interface Repository<T extends Identifiable> {

    void save(T entity);
    Optional<T> findById(int id);
    List<T> findAll();
    boolean delete(int id);

}
