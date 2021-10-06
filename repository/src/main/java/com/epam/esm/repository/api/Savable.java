package com.epam.esm.repository.api;

import com.epam.esm.entities.Identifiable;

import java.util.Optional;

public interface Savable<T extends Identifiable> {

    Optional<T> save(T entity);

}
