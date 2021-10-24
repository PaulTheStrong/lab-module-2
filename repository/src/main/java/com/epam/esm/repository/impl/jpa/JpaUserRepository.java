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
    private static final String FIND_MOST_USED_TAG_OF_USER_WITH_HIGHEST_COST_OF_ALL_ORDERS =
            "SELECT t.id, t.name FROM `order` o\n" +
            "    JOIN gift_certificate gc on o.certificate_id = gc.id\n" +
            "     JOIN tag_certificate tc on gc.id = tc.certificate_id\n" +
            "     JOIN tag t on tc.tag_id = t.id\n" +
            "WHERE o.user_id = (SELECT user_id id\n" +
            "       FROM `order`\n" +
            "       GROUP BY user_id\n" +
            "       ORDER BY sum(total_price) DESC\n" +
            "       LIMIT 1)\n" +
            "GROUP BY (t.id)\n" +
            "ORDER BY COUNT(t.id) DESC\n" +
            "    LIMIT 1";
    private static final String COUNT_USERS = "SELECT count(u) FROM User u";

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
        return Optional.ofNullable(user);
    }

    @Override
    public List<User> findAll(int pageNumber, int pageSize) {
        TypedQuery<User> findAllQuery = entityManager.createQuery(SELECT_ALL_USERS, User.class);
        findAllQuery.setFirstResult((pageNumber - 1) * pageSize);
        findAllQuery.setMaxResults(pageSize);
        return findAllQuery.getResultList();
    }

    @Override
    public Optional<Tag> findMostUsedTagOfUserWithHighestCostOfAllOrders() {
        Query nativeQuery = entityManager.createNativeQuery(FIND_MOST_USED_TAG_OF_USER_WITH_HIGHEST_COST_OF_ALL_ORDERS, Tag.class);
        Tag tag = (Tag) nativeQuery.getSingleResult();
        return Optional.of(tag);
    }

    @Override
    public int countAll() {
        TypedQuery<Long> query = entityManager.createQuery(COUNT_USERS, Long.class);
        return query.getSingleResult().intValue();
    }
}
