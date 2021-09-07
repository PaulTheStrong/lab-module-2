package com.epam.esm.repository;

import com.epam.esm.entities.GiftCertificate;
import com.epam.esm.entities.Identifiable;
import org.springframework.jdbc.core.RowMapper;

public abstract class AbstractRepository<T extends Identifiable> implements Repository<T> {

    protected abstract String getTableName();
    protected abstract RowMapper<T> getMapper();

    private static final String GET_BY_ID = "SELECT * FROM gift_certificate WHERE id=?";

//    @Override
//    public GiftCertificate getById(int id) {
//        return jdbcTemplate.queryForObject(GET_BY_ID, mapper, id);
//    }

}
