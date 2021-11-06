package com.epam.esm.repository.api;

import com.epam.esm.entities.GiftCertificate;
import com.epam.esm.entities.Tag;

/**
 * Performs cross {@link Tag} and {@link GiftCertificate} queries.
 */
public interface TagCertificateUtil {
    /**
     * @param tagId id of the {@link Tag}, that should be used in search
     * @return quantity of {@link GiftCertificate} entities, associated with {@link Tag} specified tag
     */
    int countAssociatedCertificates(int tagId);
}
