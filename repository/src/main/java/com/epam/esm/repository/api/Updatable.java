package com.epam.esm.repository.api;

import com.epam.esm.entities.Identifiable;

import java.util.Optional;

public interface Updatable<T extends Identifiable> {

    Optional<T> update(T entity);

}
