package com.epam.esm.repository;

import com.epam.esm.entities.Tag;

import java.util.List;
import java.util.Optional;

public interface TagRepository extends Repository<Tag> {

    /**
     * Finds all the tags associated with certificate with specified id
     */
    List<Tag> findTagsByCertificateId(int id);

    /**
     * Adds association between certificate and tag in bundle table
     */
    void addCertificateTagAssociation(int certificateId, int tagId);

    /**
     * Removes association between certificate and tag in bundle table
     */
    void removeCertificateTagAssociation(int certificateId, int tagId);

    Optional<Tag> findByName(String name);

    /**
     * @param tagId id of the tag, that should be used in search
     * @return quantity of certificates, associated with tag specified tag
     */
    int countAssociatedCertificates(int tagId);
}
