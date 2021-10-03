package com.epam.esm.repository.api;

import com.epam.esm.entities.Tag;

import java.util.List;

public interface TagCertificateUtil {
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

    /**
     * @param tagId id of the tag, that should be used in search
     * @return quantity of certificates, associated with tag specified tag
     */
    int countAssociatedCertificates(int tagId);
}
