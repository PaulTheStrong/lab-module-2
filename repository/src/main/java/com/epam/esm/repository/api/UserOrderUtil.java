package com.epam.esm.repository.api;

import com.epam.esm.entities.GiftCertificate;
import com.epam.esm.entities.Order;
import com.epam.esm.entities.Tag;

import java.util.List;

/**
 * Performs cross {@link com.epam.esm.entities.User} and {@link Order} queries.
 */
public interface UserOrderUtil {

    /**
     * Retrieves all {@link com.epam.esm.entities.User} user's {@link Order} orders.
     * @param userId {@link com.epam.esm.entities.User} user's id.
     * @return list of all {@link com.epam.esm.entities.User} user's {@link Order} orders.
     */
    List<Order> getUserOrders(int userId);

    /**
     * Retrieves {@link com.epam.esm.entities.User} user's {@link Order} orders in pageable format.
     * @param userId {@link com.epam.esm.entities.User} user's id.
     * @param pageNumber the number of the page
     * @param pageSize count of entities on single page.
     * @return list of pageSize {@link com.epam.esm.entities.User} user's {@link Order} orders
     * starting from (pageNumber - 1) * pageSize.
     */
    List<Order> getUserOrders(int userId, int pageNumber, int pageSize);

}
