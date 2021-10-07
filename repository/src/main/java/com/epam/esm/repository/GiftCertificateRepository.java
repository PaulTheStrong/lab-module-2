package com.epam.esm.repository;

import com.epam.esm.entities.GiftCertificate;
import org.springframework.jdbc.core.PreparedStatementCreator;

import java.util.List;
import java.util.Optional;

public interface GiftCertificateRepository extends Repository<GiftCertificate> {

    /**
     * Updates gift certificate in database
     * @param entity contains data to be updated.
     * @return Updated Optional.of(GiftCertificate) if saving was successful.
     * Otherwise, Optional.empty().
     */
    Optional<GiftCertificate> update(GiftCertificate entity);

    /**
     * Performs special query with parameters
     * @param preparedStatementCreator holds the information about parameters
     *        should be used in query e.g. tag, sorts, etc.
     * @return List of certificates satisfying parameters.
     */
    List<GiftCertificate> findBySpecification(PreparedStatementCreator preparedStatementCreator);

}
