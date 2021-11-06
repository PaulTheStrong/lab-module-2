package com.epam.esm.repository.api;

import com.epam.esm.entities.Role;
import com.epam.esm.entities.Tag;
import com.epam.esm.entities.User;

import java.util.Optional;

/**
 * Repository for {@link User} entity. May perform {@link Findable} and {@link Countable} operations.
 */
public interface UserRepository extends Findable<User>, Countable, Savable<User> {

    /**
     Get the most widely used tag of a user with the highest cost of all orders
     */
    Optional<Tag> findMostUsedTagOfUserWithHighestCostOfAllOrders();

    Optional<User> findByUsername(String username);

    Optional<Role> findRoleByName(String roleName);
}
