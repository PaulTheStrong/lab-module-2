package com.epam.esm.repository.impl.jpa;

import com.epam.esm.entities.Order;
import com.epam.esm.entities.User;
import com.epam.esm.repository.api.UserOrderUtil;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
@Profile("jpa")
public class JpaUserOrderUtil implements UserOrderUtil {

    private static final String SELECT_USER_ORDERS_BY_ID = "SELECT orders FROM User user JOIN user.orders orders WHERE user.id =: user_id";
    private static final String COUNT_USER_ORDERS = "SELECT count(o) FROM TheOrder o where o.user.id = :id";

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Order> getUserOrders(int userId) {
        User user = entityManager.find(User.class, userId);
        return user.getOrders();
    }

    @Override
    public List<Order> getUserOrders(int userId, int pageNumber, int pageSize) {
        TypedQuery<Order> userOrdersQuery = entityManager.createQuery(SELECT_USER_ORDERS_BY_ID, Order.class);
        userOrdersQuery.setParameter("user_id", userId);
        userOrdersQuery.setFirstResult((pageNumber - 1) * pageSize);
        userOrdersQuery.setMaxResults(pageSize);
        return userOrdersQuery.getResultList();
    }

    @Override
    public Integer countUserOrders(int userId) {
        TypedQuery<Long> query = entityManager.createQuery(COUNT_USER_ORDERS, Long.class);
        query.setParameter("id", userId);
        return query.getSingleResult().intValue();
    }
}
