package com.epam.esm.repository.api;

import com.epam.esm.entities.Tag;

import java.util.Optional;

public interface CustomTagRepository {
    /**
     Get the most widely used tag of a user with the highest cost of all orders
     */
    Optional<Tag> findMostUsedTagOfUserWithHighestCostOfAllOrders();
}
