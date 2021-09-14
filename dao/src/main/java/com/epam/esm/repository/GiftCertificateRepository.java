package com.epam.esm.repository;

import com.epam.esm.entities.GiftCertificate;
import com.epam.esm.entities.Tag;

import java.util.List;
import java.util.Optional;

public interface GiftCertificateRepository extends Repository<GiftCertificate> {

    void addTagToCertificate(int certificateId, String tag);

    void removeTagFromCertificate(int certificateId, String tag);

    void update(GiftCertificate entity);

    List<GiftCertificate> getByTagName(String tagName);

    List<GiftCertificate> getByNameOrDescription(String search);
    

}
