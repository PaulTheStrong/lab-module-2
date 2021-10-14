package com.epam.esm.repository.api;

import com.epam.esm.entities.Tag;
import com.epam.esm.entities.GiftCertificate;

import java.util.List;

/**
 * Performs cross {@link Tag} and {@link GiftCertificate} queries.
 */
public interface TagCertificateUtil {
    /**
     * Finds all the {@link Tag} entities associated with {@link GiftCertificate} entities
     * with specified id
     */
    List<Tag> findTagsByCertificateId(int id);

    /**
     * Adds association between {@link GiftCertificate} and {@link Tag} in bundle table
     */
    void addCertificateTagAssociation(int certificateId, int tagId);

    /**
     * Removes association between {@link GiftCertificate} and {@link Tag} in bundle table
     */
    void removeCertificateTagAssociation(int certificateId, int tagId);

    /**
     * @param tagId id of the {@link Tag}, that should be used in search
     * @return quantity of {@link GiftCertificate} entities, associated with {@link Tag} specified tag
     */
    int countAssociatedCertificates(int tagId);
}
