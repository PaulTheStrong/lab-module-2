package com.epam.esm.repository;

import com.epam.esm.entities.GiftCertificate;
import org.springframework.jdbc.core.PreparedStatementCreator;

import java.util.List;
import java.util.Optional;

public interface GiftCertificateRepository extends Repository<GiftCertificate> {

    Optional<GiftCertificate> update(GiftCertificate entity);

    List<GiftCertificate> findBySpecification(PreparedStatementCreator preparedStatementCreator);

}
