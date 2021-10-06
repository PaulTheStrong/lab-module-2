package com.epam.esm.repository.impl.jpa;

import com.epam.esm.entities.Tag;
import com.epam.esm.entities.User;
import com.epam.esm.repository.api.UserRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

@Repository
@Profile("jpa")
public class JpaUserRepository implements UserRepository {

    private static final String SELECT_ALL_USERS = "SELECT user FROM User user";

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<User> findAll() {
        TypedQuery<User> findAllQuery = entityManager.createQuery(SELECT_ALL_USERS, User.class);
        return findAllQuery.getResultList();
    }

    @Override
    public Optional<User> findById(int id) {
        User user = entityManager.find(User.class, id);
        return Optional.of(user);
    }

    @Override
    public List<User> findAll(int pageNumber, int pageSize) {
        TypedQuery<User> findAllQuery = entityManager.createQuery(SELECT_ALL_USERS, User.class);
        findAllQuery.setFirstResult((pageNumber - 1) * pageSize);
        findAllQuery.setMaxResults(pageSize);
        return findAllQuery.getResultList();
    }

    @Override
    public Tag getMostUsedTagOfUserWithHighestCostOfAllOrders() {
        TypedQuery<Tag> query = entityManager.createQuery(
                "SELECT u.id, sum(o.totalPrice) FROM TheOrder o JOIN o.user u GROUP BY u.id",
                Tag.class);
        return query.getSingleResult();
    }
}
