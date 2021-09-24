package com.epam.esm.repository.impl;

import com.epam.esm.entities.GiftCertificate;
import com.epam.esm.repository.GiftCertificateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class GiftCertificateRepositoryImpl implements GiftCertificateRepository {

    private static final String GET_ALL = "SELECT * FROM gift_certificate";
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
    private static final String GET_CERTIFICATES_BY_TAG_NAME = "SELECT c.* FROM gift_certificate c " +
            "INNER JOIN tag_certificate tc on c.id = tc.certificate_id " +
            "INNER JOIN tag t on tc.tag_id = t.id WHERE t.name = ?";
    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<GiftCertificate> mapper;
    private final SimpleJdbcInsert jdbcInsert;

    @Autowired
    public GiftCertificateRepositoryImpl(JdbcTemplate jdbcTemplate, RowMapper<GiftCertificate> mapper, @Qualifier("GiftCertificate") SimpleJdbcInsert jdbcInsert) {
        this.jdbcTemplate = jdbcTemplate;
        this.mapper = mapper;
        this.jdbcInsert = jdbcInsert;
    }

    @Override
    public Optional<GiftCertificate> findById(int id) {
        List<GiftCertificate> certificate = jdbcTemplate.query(GET_BY_ID, mapper, id);
        if (certificate.size() == 0) {
            return Optional.empty();
        } else {
            return Optional.of(certificate.get(0));
        }
    }

    public Optional<GiftCertificate> save(GiftCertificate entity) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("name", entity.getName());
        parameters.put("description", entity.getDescription());
        parameters.put("price", entity.getPrice());
        parameters.put("duration", entity.getDuration());
        parameters.put("create_date", entity.getCreateDate());
        parameters.put("last_update_date", entity.getLastUpdateDate());
        Number id = jdbcInsert.executeAndReturnKey(parameters);
        entity.setId(id.intValue());
        return Optional.of(entity);
    }

    @Override
    public Optional<GiftCertificate> update(GiftCertificate entity) {
        jdbcTemplate.update(UPDATE,
                entity.getName(), entity.getDescription(), entity.getPrice(),
                entity.getDuration(), entity.getLastUpdateDate(), entity.getId());
        return Optional.of(entity);
    }

    public boolean delete(int id) {
        return jdbcTemplate.update(DELETE_BY_ID, id) == 1;
    }

    public List<GiftCertificate> findAll() {
        return jdbcTemplate.query(GET_ALL, mapper);
    }

    @Override
    public List<GiftCertificate> findBySpecification(PreparedStatementCreator preparedStatementCreator) {
        return jdbcTemplate.query(preparedStatementCreator, mapper);
    }

}
