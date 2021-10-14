package com.epam.esm.repository.api;

import com.epam.esm.entities.Identifiable;

import java.util.List;
import java.util.Optional;

/**
 * Allows extracting items from repository
 * @param <T> The type of the entity that extends {@link Identifiable}
 *        associated with repository
 */
public interface Findable<T extends Identifiable> {

    /**
     * @return all entities found in repository
     */
    List<T> findAll();

    /**
     * Retrieves entity from repository
     * @param id identifier of the {@link Identifiable} entity to be found
     * @return {@link Optional} of entity if entity found. Otherwise, empty {@link Optional}
     */
    Optional<T> findById(int id);

    /**
     * @param pageNumber - the number of the page
     * @param pageSize - the number of entities on one page
     * @return pageSize entities starting from (pageNumber - 1) * pageSize in pageable format
     */
    default List<T> findAll(int pageNumber, int pageSize) {throw new UnsupportedOperationException("Not implemented"); }
}
