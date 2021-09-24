package com.epam.esm.repository;

import com.epam.esm.entities.Tag;
import lombok.NonNull;

import java.util.List;
import java.util.Optional;

public interface TagRepository extends Repository<Tag> {

    List<Tag> findTagsByCertificateId(int id);

    void addCertificateTagAssociation(int certificateId, int tagId);

    void removeCertificateTagAssociation(int certificateId, int tagId);

    Optional<Tag> findByName(String name);

    int countAssociatedCertificates(int tagId);
}
