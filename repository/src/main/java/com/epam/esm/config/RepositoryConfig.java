package com.epam.esm.config;

import com.epam.esm.entities.GiftCertificate;
import com.epam.esm.entities.Tag;
import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@Configuration
@Import({DevRepositoryConfig.class, ProdRepositoryConfig.class})
@EnableTransactionManagement
public class RepositoryConfig {

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

    @Bean
    public PlatformTransactionManager transactionManager(DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }
}
