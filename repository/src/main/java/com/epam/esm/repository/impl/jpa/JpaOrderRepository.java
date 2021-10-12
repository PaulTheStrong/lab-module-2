package com.epam.esm.repository.impl.jpa;


import com.epam.esm.entities.Order;
import com.epam.esm.repository.api.OrderRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

@Repository
@Profile("jpa")
public class JpaOrderRepository implements OrderRepository {

    private static final String FIND_ALL = "SELECT o FROM TheOrder o";
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Order> save(Order entity) {
        entityManager.persist(entity);
        return Optional.of(entity);
    }

    @Override
    public List<Order> findAll() {
        TypedQuery<Order> query = entityManager.createQuery(FIND_ALL, Order.class);
        return query.getResultList();
    }

    @Override
    public Optional<Order> findById(int id) {
        Order order = entityManager.find(Order.class, id);
        return Optional.ofNullable(order);
    }

    @Override
    public List<Order> findAll(int pageNumber, int pageSize) {
        TypedQuery<Order> query = entityManager.createQuery(FIND_ALL, Order.class);
        query.setFirstResult((pageNumber - 1) * pageSize);
        query.setFirstResult(pageSize);
        return query.getResultList();
    }
}
