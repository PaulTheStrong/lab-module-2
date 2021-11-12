package com.epam.esm.repository.api;

import com.epam.esm.entities.User;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

/**
 * Repository for {@link User} entity.
 */
@EnableJpaAuditing
public interface UserRepository extends PagingAndSortingRepository<User, Integer> {
    Optional<User> findByUsername(String username);
}
