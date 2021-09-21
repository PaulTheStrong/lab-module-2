package com.epam.esm.repository;

import com.epam.esm.entities.GiftCertificate;
import org.springframework.jdbc.core.PreparedStatementCreator;

import java.util.List;

public interface GiftCertificateRepository extends Repository<GiftCertificate> {

    void update(GiftCertificate entity);

    List<GiftCertificate> findBySpecification(PreparedStatementCreator preparedStatementCreator);

}
