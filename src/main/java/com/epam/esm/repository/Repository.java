package com.epam.esm.repository;

import com.epam.esm.entities.Identifiable;

import java.util.List;

public interface Repository<T extends Identifiable> {

    void save(T entity);
    T getById(int id);
    List<T> getAll();
    void update(T entity);
    void delete(int id);

}
