package com.epam.esm.repository.impl.jdbc;

import com.epam.esm.entities.GiftCertificate;
import com.epam.esm.repository.api.GiftCertificateRepository;
import com.epam.esm.repository.impl.FilterParameters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
@Profile("jdbc")
public class JdbcGiftCertificateRepository implements GiftCertificateRepository {

    private static final String GET_ALL = "SELECT * FROM gift_certificate";
    private static final String UPDATE =
            "UPDATE gift_certificate SET " +
            "name=?, description=?, price=?, duration=?, last_update_date=? " +
            "WHERE id=?";
    private static final String GET_BY_ID =
            "SELECT * FROM gift_certificate WHERE id=?";
    public static final String DELETE_BY_ID = "DELETE FROM gift_certificate WHERE id=?";

    private static final String NAME = "name";
    private static final String DESCRIPTION = "description";
    private static final String PRICE = "price";
    private static final String DURATION = "duration";
    private static final String CREATE_DATE = "create_date";
    private static final String LAST_UPDATE_DATE = "last_update_date";

    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<GiftCertificate> mapper;
    private final SimpleJdbcInsert jdbcInsert;

    @Autowired
    public JdbcGiftCertificateRepository(JdbcTemplate jdbcTemplate, RowMapper<GiftCertificate> mapper, @Qualifier("GiftCertificate") SimpleJdbcInsert jdbcInsert) {
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
        prepareParametersMapForInsert(entity, parameters);
        Number id = jdbcInsert.executeAndReturnKey(parameters);
        entity.setId(id.intValue());
        return Optional.of(entity);
    }

    private void prepareParametersMapForInsert(GiftCertificate entity, Map<String, Object> parameters) {
        parameters.put(NAME, entity.getName());
        parameters.put(DESCRIPTION, entity.getDescription());
        parameters.put(PRICE, entity.getPrice());
        parameters.put(DURATION, entity.getDuration());
        parameters.put(CREATE_DATE, entity.getCreateDate());
        parameters.put(LAST_UPDATE_DATE, entity.getLastUpdateDate());
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
    public List<GiftCertificate> findBySpecification(FilterParameters filterParameters) {
        GiftCertificatePreparedStatementCreator preparedStatementCreator
                = new GiftCertificatePreparedStatementCreator(filterParameters);
        return jdbcTemplate.query(preparedStatementCreator, mapper);
    }

}
