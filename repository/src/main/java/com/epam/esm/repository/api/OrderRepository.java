package com.epam.esm.repository.api;

import com.epam.esm.entities.Order;

/**
 * Repository for order entity. May perform {@link Savable} and
 * {@link Findable} operations.
 */
public interface OrderRepository extends Savable<Order>, Findable<Order> {
}
