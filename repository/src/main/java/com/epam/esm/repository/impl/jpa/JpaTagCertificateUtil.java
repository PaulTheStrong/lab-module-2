package com.epam.esm.repository.impl.jpa;

import com.epam.esm.entities.GiftCertificate;
import com.epam.esm.entities.Tag;
import com.epam.esm.repository.api.TagCertificateUtil;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.Collections;
import java.util.List;

@Repository
@Profile("jpa")
public class JpaTagCertificateUtil implements TagCertificateUtil {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Tag> findTagsByCertificateId(int id) {
        GiftCertificate certificate = entityManager.find(GiftCertificate.class, id);
        List<Tag> tags = certificate.getTags();
        return tags == null ? Collections.emptyList() : tags;
    }

    @Override
    public void addCertificateTagAssociation(int certificateId, int tagId) {
        GiftCertificate certificate = entityManager.find(GiftCertificate.class, certificateId);
        Tag tag = entityManager.find(Tag.class, tagId);
        List<Tag> certificateTags = certificate.getTags();
        certificateTags.add(tag);
    }

    @Override
    public void removeCertificateTagAssociation(int certificateId, int tagId) {
        GiftCertificate certificate = entityManager.find(GiftCertificate.class, certificateId);
        Tag tag = entityManager.find(Tag.class, tagId);
        List<Tag> certificateTags = certificate.getTags();
        certificateTags.remove(tag);
    }

    @Override
    public int countAssociatedCertificates(int tagId) {
        TypedQuery<Integer> query = entityManager.createQuery("SELECT count(gc) From Tag tag JOIN tag.certificates gc WHERE tag.id = :id", Integer.class);
        query.setParameter("id", tagId);
        return query.getSingleResult();
    }
}
