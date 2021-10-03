package com.epam.esm.repository.impl.jdbc;

import com.epam.esm.entities.Tag;
import com.epam.esm.repository.api.TagCertificateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@Profile("jdbc")
public class JdbcTagCertificateUtil implements TagCertificateUtil {

    private static final String SELECT_TAGS_BY_CERTIFICATE_ID = "SELECT t.id, t.name FROM tag t " +
            "INNER JOIN tag_certificate tc on t.id = tc.tag_id " +
            "INNER JOIN gift_certificate gc on tc.certificate_id = gc.id " +
            "WHERE gc.id = ?";
    private static final String ADD_CERTIFICATE_TAG_ASSOCIATION = "INSERT INTO tag_certificate (certificate_id, tag_id) VALUES (?, ?)";
    private static final String REMOVE_CERTIFICATE_TAG_ASSOCIATION = "DELETE FROM tag_certificate WHERE tag_id=? AND certificate_id=?";
    private static final String COUNT_ASSOCIATED_WITH_TAG_CERTIFICATES = "SELECT count(tag_id) FROM tag_certificate WHERE tag_id = ?";

    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Tag> tagRowMapper;

    @Autowired
    public JdbcTagCertificateUtil(JdbcTemplate jdbcTemplate, RowMapper<Tag> tagRowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.tagRowMapper = tagRowMapper;
    }

    @Override
    public List<Tag> findTagsByCertificateId(int id) {
        return jdbcTemplate.query(SELECT_TAGS_BY_CERTIFICATE_ID, tagRowMapper, id);
    }

    @Override
    public void removeCertificateTagAssociation(int certificateId, int tagId) {
        jdbcTemplate.update(REMOVE_CERTIFICATE_TAG_ASSOCIATION, tagId, certificateId);
    }

    @Override
    public void addCertificateTagAssociation(int certificateId, int tagId) {
        jdbcTemplate.update(ADD_CERTIFICATE_TAG_ASSOCIATION, certificateId, tagId);
    }

    @Override
    public int countAssociatedCertificates(int tagId) {
        return Optional.ofNullable(
                        jdbcTemplate.queryForObject(COUNT_ASSOCIATED_WITH_TAG_CERTIFICATES, Integer.class, tagId))
                .orElse(0);
    }
}
