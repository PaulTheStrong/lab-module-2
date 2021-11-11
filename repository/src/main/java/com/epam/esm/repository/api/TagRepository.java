package com.epam.esm.repository.api;

import com.epam.esm.entities.Tag;

import java.util.Optional;

/**
 * Repository for order entity. May perform {@link Savable}, {@link Deletable}, {@link Countable} and
 * {@link Findable} operations.
 */
public interface TagRepository extends Findable<Tag>, Deletable<Tag>, Savable<Tag>, Countable {
    Optional<Tag> findByName(String name);
}
