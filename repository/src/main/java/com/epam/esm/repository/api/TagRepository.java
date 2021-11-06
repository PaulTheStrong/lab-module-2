package com.epam.esm.repository.api;

import com.epam.esm.entities.Tag;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

/**
 * Repository for Tag entity.
 */
@EnableJpaAuditing
public interface TagRepository extends PagingAndSortingRepository<Tag, Integer>, CustomTagRepository {

    Optional<Tag> findByName(String name);

}
