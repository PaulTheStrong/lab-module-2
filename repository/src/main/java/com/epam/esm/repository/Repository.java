package com.epam.esm.repository;

import com.epam.esm.entities.Identifiable;
import lombok.NonNull;

import java.util.List;
import java.util.Optional;

public interface Repository<T extends Identifiable> {

    Optional<T> save(T entity);
    Optional<T> findById(int id);
    List<T> findAll();
    boolean delete(int id);
}
