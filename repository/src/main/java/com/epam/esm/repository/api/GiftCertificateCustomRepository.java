package com.epam.esm.repository.api;

import com.epam.esm.entities.GiftCertificate;
import com.epam.esm.repository.impl.FilterParameters;

import java.util.List;

public interface GiftCertificateCustomRepository {

    /**
     * @return pageSize entities starting from (pageNumber - 1) * pageSize
     * in pageable format filtered by FilterParameters
     */
    List<GiftCertificate> findBySpecification(FilterParameters filterParameters, int pageNumber, int pageSize);
    int countEntitiesBySpecification(FilterParameters filterParameters);
}
