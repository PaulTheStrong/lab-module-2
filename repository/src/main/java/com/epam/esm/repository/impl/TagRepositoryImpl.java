package com.epam.esm.repository.impl;

import com.epam.esm.entities.Tag;
import com.epam.esm.repository.TagRepository;
import com.epam.esm.repository.exception.MultipleResultException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@org.springframework.stereotype.Repository
public class TagRepositoryImpl implements TagRepository {

    public static final String FIND_BY_ID = "SELECT * FROM tag WHERE id=?";
    public static final String FIND_ALL = "SELECT * FROM tag";
    public static final String SAVE = "INSERT INTO tag (name) VALUE (LOWER(?)) ON DUPLICATE KEY UPDATE id=id;";
    public static final String DELETE_BY_ID = "DELETE FROM tag WHERE id = ?";
    private static final String SELECT_TAGS_BY_CERTIFICATE_ID = "SELECT t.id, t.name FROM tag t " +
            "INNER JOIN tag_certificate tc on t.id = tc.tag_id " +
            "INNER JOIN gift_certificate gc on tc.certificate_id = gc.id " +
            "WHERE gc.id = ?";
    private static final String ADD_CERTIFICATE_TAG_ASSOCIATION = "INSERT INTO tag_certificate (certificate_id, tag_id) VALUES (?, ?)";
    private static final String REMOVE_CERTIFICATE_TAG_ASSOCIATION = "DELETE FROM tag_certificate WHERE tag_id=? AND certificate_id=?";
    private static final String FIND_BY_NAME = "SELECT id, name FROM tag WHERE name = ?";
    private static final String COUNT_ASSOCIATED_WITH_TAG_CERTIFICATES = "SELECT count(tag_id) FROM tag_certificate WHERE tag_id = ?";

    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Tag> mapper;
    private final SimpleJdbcInsert jdbcInsert;

    @Autowired
    public TagRepositoryImpl(JdbcTemplate jdbcTemplate, RowMapper<Tag> mapper, @Qualifier("Tag") SimpleJdbcInsert jdbcInsert) {
        this.jdbcTemplate = jdbcTemplate;
        this.mapper = mapper;
        this.jdbcInsert = jdbcInsert;
    }

    @Override
    public List<Tag> findAll() {
        return jdbcTemplate.query(FIND_ALL, mapper);
    }

    @Override
    public Optional<Tag> save(Tag entity) {
        Optional<Tag> tagByName = findByName(entity.getName());
        if (!tagByName.isPresent()) {
            int id = jdbcInsert.executeAndReturnKey(
                    Collections.singletonMap("name", entity.getName()))
                    .intValue();
            return findById(id);
        } else {
            return tagByName;
        }
    }

    @Override
    public Optional<Tag> findById(int id) {
        return findOne(FIND_BY_ID, id);
    }

    @Override
    public boolean delete(int id) {
        return jdbcTemplate.update(DELETE_BY_ID, id) > 0;
    }

    @Override
    public List<Tag> findTagsByCertificateId(int id) {
        return jdbcTemplate.query(SELECT_TAGS_BY_CERTIFICATE_ID, mapper, id);
    }

    @Override
    public void removeCertificateTagAssociation(int certificateId, int tagId) {
        jdbcTemplate.update(REMOVE_CERTIFICATE_TAG_ASSOCIATION, tagId, certificateId);
    }

    @Override
    public void addCertificateTagAssociation(int certificateId, int tagId) {
        jdbcTemplate.update(ADD_CERTIFICATE_TAG_ASSOCIATION, certificateId, tagId);
    }

    @Override
    public Optional<Tag> findByName(String name) {
        return findOne(FIND_BY_NAME, name);
    }

    public int countAssociatedCertificates(int tagId) {
        return Optional.ofNullable(
                jdbcTemplate.queryForObject(COUNT_ASSOCIATED_WITH_TAG_CERTIFICATES, Integer.class, tagId))
                .orElse(0);
    }

    private Optional<Tag> findOne(String query, Object... arguments) {
        List<Tag> tags = jdbcTemplate.query(query, mapper, arguments);
        if (tags.size() == 0) {
            return Optional.empty();
        } else if (tags.size() == 1) {
            return Optional.of(tags.get(0));
        } else {
            throw new MultipleResultException();
        }
    }
}

