package com.epam.esm.repository.impl.jpa;

import com.epam.esm.entities.Tag;
import com.epam.esm.repository.api.TagRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

@Repository
@Profile("jpa")
public class JpaTagRepository implements TagRepository {

    private static final String SELECT_BY_NAME = "SELECT tag FROM Tag tag WHERE tag.name = :tagName AND tag.isAvailable=true";
    private static final String DELETE_BY_ID = "UPDATE Tag tag SET tag.isAvailable=false WHERE id =: id AND tag.isAvailable=true";
    private static final String SELECT_ALL = "SELECT tag FROM Tag tag WHERE tag.isAvailable = true";
    private static final String COUNT_TAGS = "SELECT count(tag) FROM Tag tag WHERE tag.isAvailable = true";

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Tag> save(Tag entity) {
        String name = entity.getName();
        Optional<Tag> byName = findByName(name);
        Tag result = byName.orElseGet(() -> entityManager.merge(entity));
        return Optional.of(result);
    }

    @Override
    public Optional<Tag> findById(int id) {
        Tag tag = entityManager.find(Tag.class, id);
        if (tag != null && !tag.isAvailable()) {
            return Optional.empty();
        }
        return Optional.ofNullable(tag);
    }

    @Override
    public List<Tag> findAll() {
        TypedQuery<Tag> findAllQuery = entityManager.createQuery(SELECT_ALL, Tag.class);
        return findAllQuery.getResultList();
    }

    @Override
    public boolean delete(int id) {
        Query deleteQuery = entityManager.createQuery(DELETE_BY_ID);
        deleteQuery.setParameter("id", id);
        int rows = deleteQuery.executeUpdate();
        return rows == 1;
    }

    @Override
    public Optional<Tag> findByName(String name) {
        TypedQuery<Tag> byNameQuery = entityManager.createQuery(SELECT_BY_NAME, Tag.class);
        byNameQuery.setParameter("tagName", name);
        Tag tag;
        try {
            tag = byNameQuery.getSingleResult();
        } catch (NoResultException e) {
            tag = null;
        }
        return Optional.ofNullable(tag);
    }

    @Override
    public List<Tag> findAll(int pageNumber, int pageSize) {
        TypedQuery<Tag> findAllQuery = entityManager.createQuery(SELECT_ALL, Tag.class);
        findAllQuery.setFirstResult((pageNumber - 1) * pageSize);
        findAllQuery.setMaxResults(pageSize);
        return findAllQuery.getResultList();
    }

    @Override
    public int countAll() {
        TypedQuery<Long> query = entityManager.createQuery(COUNT_TAGS, Long.class);
        return query.getSingleResult().intValue();
    }
}
