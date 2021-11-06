package com.epam.esm.repository.impl.jpa;

import com.epam.esm.entities.Tag;
import com.epam.esm.repository.api.CustomTagRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.Optional;

@Repository
public class TagRepositoryImpl implements CustomTagRepository {

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


    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Tag> findMostUsedTagOfUserWithHighestCostOfAllOrders() {
        Query nativeQuery = entityManager.createNativeQuery(FIND_MOST_USED_TAG_OF_USER_WITH_HIGHEST_COST_OF_ALL_ORDERS, Tag.class);
        Tag tag = (Tag) nativeQuery.getSingleResult();
        return Optional.of(tag);
    }
}
