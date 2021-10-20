package com.epam.esm.config;

import com.epam.esm.entities.GiftCertificate;
import com.epam.esm.entities.Tag;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import javax.sql.DataSource;

@Configuration
@ComponentScan("com.epam.esm.repository.impl.jdbc")
@Profile("jdbc")
public class JdbcRepositoryConfig {
    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    @Bean
    public RowMapper<GiftCertificate> giftCertificateBeanPropertyRowMapper() {
        return new BeanPropertyRowMapper<>(GiftCertificate.class);
    }

    @Bean
    public RowMapper<Tag> tagBeanPropertyRowMapper() {
        return new BeanPropertyRowMapper<>(Tag.class);
    }

    @Bean
    @Qualifier("Tag")
    public SimpleJdbcInsert tagSimpleJdbcInsert(JdbcTemplate jdbcTemplate) {
        return new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("tag")
                .usingGeneratedKeyColumns("id");
    }

    @Bean
    @Qualifier("GiftCertificate")
    public SimpleJdbcInsert giftCertificateSimpleJdbcInsert(JdbcTemplate jdbcTemplate) {
        return new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("gift_certificate")
                .usingGeneratedKeyColumns("id");
    }
}
