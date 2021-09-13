package com.epam.esm.repository;

import com.epam.esm.entities.GiftCertificate;

import java.util.List;
import java.util.Optional;

public interface GiftCertificateRepository extends Repository<GiftCertificate> {
    void update(GiftCertificate entity);

    List<GiftCertificate> getAllByTagName(String tagName);

}
