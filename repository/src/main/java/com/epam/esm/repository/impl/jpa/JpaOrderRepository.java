package com.epam.esm.repository.impl.jpa;


import com.epam.esm.entities.Order;
import com.epam.esm.repository.api.OrderRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Optional;

@Repository
@Profile("jpa")
public class JpaOrderRepository implements OrderRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Order> save(Order entity) {
        entityManager.persist(entity);
        return Optional.of(entity);
    }
}
