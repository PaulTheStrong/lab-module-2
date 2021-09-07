package com.epam.esm.repository;

import com.epam.esm.entities.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.util.List;

@org.springframework.stereotype.Repository
public class TagRepository implements Repository<Tag> {

    public static final String SELECT_BY_ID = "SELECT * FROM tag WHERE id=?";
    public static final String GET_ALL = "SELECT * FROM tag";
    private JdbcTemplate jdbcTemplate;
    private RowMapper<Tag> mapper;

    @Autowired
    public void setMapper(RowMapper<Tag> mapper) {
        this.mapper = mapper;
    }

    @Autowired
    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Tag> getAll() {
        return jdbcTemplate.query(GET_ALL, mapper);
    }

    @Override
    public void save(Tag entity) {
        jdbcTemplate.update("INSERT INTO tag (name) VALUES (?)", entity.getName());
    }

    @Override
    public Tag getById(int id) {
        return jdbcTemplate.queryForObject(SELECT_BY_ID, mapper, id);
    }

    @Override
    public void update(Tag entity) {
        jdbcTemplate.update("UPDATE tag SET name=? WHERE id=?", entity.getName(), entity.getId());
    }

    @Override
    public void delete(int id) {
        jdbcTemplate.update("DELETE FROM tag WHERE id = ?", id);
    }
}

