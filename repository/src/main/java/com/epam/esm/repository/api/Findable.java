package com.epam.esm.repository.api;

import java.util.List;
import java.util.Optional;

public interface Findable<T> {

    List<T> findAll();
    Optional<T> findById(int id);
    default List<T> findAll(int pageNumber, int pageSize) {throw new UnsupportedOperationException("Not implemented"); }
}
