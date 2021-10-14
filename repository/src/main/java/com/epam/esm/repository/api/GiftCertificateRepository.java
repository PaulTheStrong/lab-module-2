package com.epam.esm.repository.api;

import com.epam.esm.entities.GiftCertificate;
import com.epam.esm.repository.impl.FilterParameters;

import java.util.List;
import java.util.Optional;

public interface GiftCertificateRepository
        extends Findable<GiftCertificate>, Deletable<GiftCertificate>,
        Updatable<GiftCertificate>, Savable<GiftCertificate> {

    /**
     * Updates gift certificate in database
     * @param entity contains data to be updated.
     * @return Updated Optional.of(GiftCertificate) if saving was successful.
     * Otherwise, Optional.empty().
     */
    Optional<GiftCertificate> update(GiftCertificate entity);

    /**
     * Performs special query with parameters
     * @param filterParameters holds the information about parameters
     *        should be used in query e.g. tag, sorts, etc.
     * @return List of certificates satisfying parameters.
     */
    List<GiftCertificate> findBySpecification(FilterParameters filterParameters);

    /**
     * See {@link #findBySpecification(FilterParameters filterParameters)}
     * @return pageSize entities starting from (pageNumber - 1) * pageSize in pageable format filtered by FilterParameters
     */
    default List<GiftCertificate> findBySpecification(FilterParameters filterParameters, int pageNumber, int pageSize) {
        throw new UnsupportedOperationException();
    }
}
