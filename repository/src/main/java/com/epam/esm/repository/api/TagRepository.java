package com.epam.esm.repository.api;

import com.epam.esm.entities.Tag;

import java.util.Optional;

/**
 * Repository for order entity. May perform {@link Savable}, {@link Deletable} and
 * {@link Findable} operations.
 */
public interface TagRepository extends Findable<Tag>, Deletable<Tag>, Savable<Tag> {
    Optional<Tag> findByName(String name);
}
