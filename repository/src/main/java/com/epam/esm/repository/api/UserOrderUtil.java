package com.epam.esm.repository.api;

import com.epam.esm.entities.Order;

import java.util.List;

public interface UserOrderUtil {

    List<Order> getUserOrders(int userId);

    List<Order> getUserOrders(int userId, int pageNumber, int pageSize);

}
