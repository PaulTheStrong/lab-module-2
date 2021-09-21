package com.epam.esm.repository;

import com.epam.esm.entities.Tag;

import java.util.List;

public interface TagRepository extends Repository<Tag> {

    List<String> findTagsByCertificateId(int id);

    void addCertificateTagAssociation(int certificateId, String tag);

    void removeCertificateTagAssociation(int certificateId, String tag);

}
