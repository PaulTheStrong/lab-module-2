package com.epam.esm.repository.api;

import com.epam.esm.entities.Identifiable;

/**
 * Allows deletion of items from repository
 * @param <T> The type of the entity that extends {@link Identifiable} associated with repository
 */
public interface Deletable<T extends Identifiable> {

    /**
     * Deletes entity from repository.
     * @param id of the {@link Identifiable} entity to be deleted
     * @return true if item has been deleted. Otherwise, false.
     */
    boolean delete(int id);

}
