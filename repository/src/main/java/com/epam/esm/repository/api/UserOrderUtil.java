package com.epam.esm.repository.api;

import com.epam.esm.entities.Order;
import com.epam.esm.entities.User;

import java.util.List;

public interface UserOrderUtil {

    void addOrderToUser(int userId, int orderId);

    List<Order> getUserOrders(int userId);

    List<Order> getUserOrders(int userId, int pageNumber, int pageSize);

}
