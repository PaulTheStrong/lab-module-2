package com.epam.esm.repository;

import com.epam.esm.entities.GiftCertificate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.util.List;
import java.util.Optional;

@org.springframework.stereotype.Repository
public class CertificateRepository implements Repository<GiftCertificate> {

    private static final String GET_ALL =
            "SELECT * FROM gift_certificate";
    private static final String UPDATE =
            "UPDATE gift_certificate SET " +
            "name=?, description=?, price=?, duration=?, last_update_date=? " +
            "WHERE id=?";
    private static final String SAVE = "INSERT INTO gift_certificate " +
            "(name, description, price, duration, " +
            "create_date, last_update_date) " +
            "VALUES (?, ?, ?, ?, ?, ?)";
    private static final String GET_BY_ID =
            "SELECT * FROM gift_certificate WHERE id=?";
    public static final String DELETE_BY_ID = "DELETE FROM gift_certificate WHERE id=?";

    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<GiftCertificate> mapper;

    @Autowired
    public CertificateRepository(JdbcTemplate jdbcTemplate, RowMapper<GiftCertificate> mapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.mapper = mapper;
    }

    @Override
    public Optional<GiftCertificate> getById(int id) {
        return Optional.ofNullable(jdbcTemplate.queryForObject(GET_BY_ID, mapper, id));
    }

    public void save(GiftCertificate entity) {
        jdbcTemplate.update(
                SAVE,
                entity.getName(), entity.getDescription(), entity.getPrice(),
                entity.getDuration(), entity.getCreateDate(), entity.getLastUpdateDate());
    }

    public void update(GiftCertificate entity) {
        jdbcTemplate.update(UPDATE,
                entity.getName(), entity.getDescription(), entity.getPrice(),
                entity.getDuration(), entity.getLastUpdateDate(), entity.getId());
    }

    public void delete(int id) {
        jdbcTemplate.update(DELETE_BY_ID, id);
    }

    public List<GiftCertificate> getAll() {
        List<GiftCertificate> query = jdbcTemplate.query(GET_ALL, mapper);
        return query;
    }

}
