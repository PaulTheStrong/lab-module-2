package com.epam.esm.repository.api;

import com.epam.esm.entities.GiftCertificate;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.repository.PagingAndSortingRepository;

@EnableJpaAuditing
public interface GiftCertificateRepository extends PagingAndSortingRepository<GiftCertificate, Integer>,
        GiftCertificateCustomRepository {

}
