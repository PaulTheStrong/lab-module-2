package com.epam.esm.repository.api;

import com.epam.esm.entities.Identifiable;

import java.util.Optional;

/**
 * Allows updating items in repository
 * @param <T> The type of the entity that extends {@link Identifiable}
 *        associated with repository
 */
public interface Updatable<T extends Identifiable> {

    /**
     * Updated {@link Identifiable} entity in repository
     * @param entity {@link Identifiable} entity to be saved.
     * @return Updated {@link Identifiable} entity
     */
    Optional<T> update(T entity);

}
