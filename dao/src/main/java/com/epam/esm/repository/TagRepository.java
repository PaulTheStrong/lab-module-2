package com.epam.esm.repository;

import com.epam.esm.entities.Tag;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.util.List;
import java.util.Optional;

@org.springframework.stereotype.Repository
public class TagRepository implements Repository<Tag> {

    public static final String GET_BY_ID = "SELECT * FROM tag WHERE id=?";
    public static final String GET_ALL = "SELECT * FROM tag";
    public static final String SAVE = "INSERT INTO tag (name) VALUES (?)";
    public static final String UPDATE = "UPDATE tag SET name=? WHERE id=?";
    public static final String DELETE_BY_ID = "DELETE FROM tag WHERE id = ?";

    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Tag> mapper;

    public TagRepository(JdbcTemplate jdbcTemplate, RowMapper<Tag> mapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.mapper = mapper;
    }

    @Override
    public List<Tag> getAll() {
        return jdbcTemplate.query(GET_ALL, mapper);
    }

    @Override
    public void save(Tag entity) {
        jdbcTemplate.update(SAVE, entity.getName());
    }

    @Override
    public Optional<Tag> getById(int id) {
        return Optional.ofNullable(jdbcTemplate.queryForObject(GET_BY_ID, mapper, id));
    }

    @Override
    public void update(Tag entity) {
        jdbcTemplate.update(UPDATE, entity.getName(), entity.getId());
    }

    @Override
    public void delete(int id) {
        jdbcTemplate.update(DELETE_BY_ID, id);
    }
}

