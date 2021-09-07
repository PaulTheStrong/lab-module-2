package com.epam.esm.repository;

import com.epam.esm.entities.GiftCertificate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.util.List;

@org.springframework.stereotype.Repository
public class CertificateRepository implements Repository<GiftCertificate> {

    private static final String SELECT_FROM_GIFT_CERTIFICATE =
            "SELECT * FROM gift_certificate";
    private static final String UPDATE_CERTIFICATE =
            "UPDATE gift_certificate SET " +
            "name=?, description=?, price=?, duration=?, last_update_date=? " +
            "WHERE id=?";
    private static final String INSERT_CERTIFICATE = "INSERT INTO gift_certificate " +
            "(name, description, price, duration, " +
            "create_date, last_update_date) " +
            "VALUES (?, ?, ?, ?, ?, ?)";
    private static final String GET_BY_ID =
            "SELECT * FROM gift_certificate WHERE id=?";
    public static final String DELETE_BY_ID = "DELETE FROM gift_certificate WHERE id=?";

    private JdbcTemplate jdbcTemplate;
    private RowMapper<GiftCertificate> mapper;

    @Autowired
    public void setMapper(RowMapper<GiftCertificate> mapper) {
        this.mapper = mapper;
    }

    @Autowired
    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public GiftCertificate getById(int id) {
        return jdbcTemplate.queryForObject(GET_BY_ID, mapper, id);
    }

    public void save(GiftCertificate entity) {
        jdbcTemplate.update(
                INSERT_CERTIFICATE,
                entity.getName(), entity.getDescription(), entity.getPrice(),
                entity.getDuration(), entity.getCreateDate(), entity.getLastUpdateDate());
    }

    public void update(GiftCertificate entity) {
        jdbcTemplate.update(UPDATE_CERTIFICATE,
                entity.getName(), entity.getDescription(), entity.getPrice(),
                entity.getDuration(), entity.getCreateDate(), entity.getId());
    }

    public void delete(int id) {
        jdbcTemplate.update(DELETE_BY_ID, id);
    }

    public List<GiftCertificate> getAll() {
        return jdbcTemplate.query(SELECT_FROM_GIFT_CERTIFICATE, mapper);
    }

}
