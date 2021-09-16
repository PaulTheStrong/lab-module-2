package com.epam.esm.repository;

import com.epam.esm.entities.GiftCertificate;
import com.epam.esm.repository.impl.GiftCertificatePreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementCreator;

import java.util.List;

public interface GiftCertificateRepository extends Repository<GiftCertificate> {

    void addTagToCertificate(int certificateId, String tag);

    void removeTagFromCertificate(int certificateId, String tag);

    void update(GiftCertificate entity);

    List<GiftCertificate> customQuery(PreparedStatementCreator preparedStatementCreator);

}
