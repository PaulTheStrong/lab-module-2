package com.epam.esm.repository.impl.jdbc;

import com.epam.esm.entities.Tag;
import com.epam.esm.repository.api.TagRepository;
import com.epam.esm.repository.exception.MultipleResultException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Repository
@Profile("jdbc")
public class JdbcTagRepository implements TagRepository {

    public static final String FIND_BY_ID = "SELECT * FROM tag WHERE id=?";
    public static final String FIND_ALL = "SELECT * FROM tag";
    public static final String DELETE_BY_ID = "DELETE FROM tag WHERE id = ?";
    private static final String FIND_BY_NAME = "SELECT id, name FROM tag WHERE name = ?";

    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Tag> mapper;
    private final SimpleJdbcInsert jdbcInsert;

    @Autowired
    public JdbcTagRepository(JdbcTemplate jdbcTemplate, RowMapper<Tag> mapper, @Qualifier("Tag") SimpleJdbcInsert jdbcInsert) {
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
    public Optional<Tag> findByName(String name) {
        return findOne(FIND_BY_NAME, name);
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

