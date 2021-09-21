package com.epam.esm.repository.impl;

import com.epam.esm.entities.Tag;
import com.epam.esm.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.util.List;
import java.util.Optional;

@org.springframework.stereotype.Repository
public class TagRepositoryImpl implements TagRepository {

    public static final String GET_BY_ID = "SELECT * FROM tag WHERE id=?";
    public static final String GET_ALL = "SELECT * FROM tag";
    public static final String SAVE = "INSERT INTO tag (name) VALUE (LOWER(?)) ON DUPLICATE KEY UPDATE id=id;";
    public static final String DELETE_BY_ID = "DELETE FROM tag WHERE id = ?";
    private static final String SELECT_TAGS_BY_CERTIFICATE_ID = "SELECT t.name FROM tag t " +
            "INNER JOIN tag_certificate tc on t.id = tc.tag_id " +
            "INNER JOIN gift_certificate gc on tc.certificate_id = gc.id " +
            "WHERE gc.id = ?";
    private static final String ADD_TAG_TO_CERTIFICATE = "INSERT INTO tag_certificate VALUES (?, (SELECT id FROM tag WHERE name = LOWER(?)))";
    private static final String REMOVE_TAG_FROM_CERTIFICATE = "DELETE FROM tag_certificate WHERE tag_id=(SELECT id FROM tag WHERE name = ?) AND certificate_id=?";


    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Tag> mapper;

    @Autowired
    public TagRepositoryImpl(JdbcTemplate jdbcTemplate, RowMapper<Tag> mapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.mapper = mapper;
    }

    @Override
    public List<Tag> findAll() {
        return jdbcTemplate.query(GET_ALL, mapper);
    }

    @Override
    public void save(Tag entity) {
        String name = entity.getName();
        jdbcTemplate.update(SAVE, name);
    }

    @Override
    public Optional<Tag> findById(int id) {
        List<Tag> tag = jdbcTemplate.query(GET_BY_ID, mapper, id);
        if (tag.size() == 0) {
            return Optional.empty();
        } else {
            return Optional.of(tag.get(0));
        }
    }

    @Override
    public boolean delete(int id) {
        return jdbcTemplate.update(DELETE_BY_ID, id) == 0;
    }

    @Override
    public List<String> findTagsByCertificateId(int id) {
        return jdbcTemplate.query(SELECT_TAGS_BY_CERTIFICATE_ID, (resultSet, rowNum) -> resultSet.getString(1), id);
    }

    @Override
    public void removeCertificateTagAssociation(int certificateId, String tag) {
        jdbcTemplate.update(REMOVE_TAG_FROM_CERTIFICATE, tag, certificateId);
    }

    @Override
    public void addCertificateTagAssociation(int certificateId, String tag) {
        jdbcTemplate.update(ADD_TAG_TO_CERTIFICATE, certificateId, tag);
    }
}

