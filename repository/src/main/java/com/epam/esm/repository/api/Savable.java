package com.epam.esm.repository.api;

import com.epam.esm.entities.Identifiable;

import java.util.Optional;

/**
 * Allows saving items in repository
 * @param <T> The type of the entity that extends {@link Identifiable}
 *        associated with repository
 */
public interface Savable<T extends Identifiable> {

    /**
     * Saves {@link Identifiable} entity in repository
     * @param entity {@link Identifiable} entity to be saved.
     * @return Saved {@link Identifiable} entity
     */
    Optional<T> save(T entity);

}
