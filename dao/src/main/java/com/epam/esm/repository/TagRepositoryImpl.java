package com.epam.esm.repository;

import com.epam.esm.entities.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

@org.springframework.stereotype.Repository
public class TagRepositoryImpl implements TagRepository {

    public static final String GET_BY_ID = "SELECT * FROM tag WHERE id=?";
    public static final String GET_ALL = "SELECT * FROM tag";
    public static final String SAVE = "INSERT INTO tag (name) VALUE (LOWER(?)) ON DUPLICATE KEY UPDATE name = LOWER(?);";
    public static final String DELETE_BY_ID = "DELETE FROM tag WHERE id = ?";

    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Tag> mapper;

    @Autowired
    public TagRepositoryImpl(JdbcTemplate jdbcTemplate, RowMapper<Tag> mapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.mapper = mapper;
    }

    @Override
    public List<Tag> getAll() {
        return jdbcTemplate.query(GET_ALL, mapper);
    }

    @Override
    public void save(Tag entity) {
        String name = entity.getName();
        jdbcTemplate.update(SAVE, name, name);
    }

    @Override
    public Optional<Tag> getById(int id) {
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(GET_BY_ID, mapper, id));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public boolean delete(int id) {
        return jdbcTemplate.update(DELETE_BY_ID, id) == 0;
    }
}

