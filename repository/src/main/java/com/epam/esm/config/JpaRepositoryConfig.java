package com.epam.esm.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import javax.sql.DataSource;

@Configuration
@ComponentScan(basePackages = "com.epam.esm.repository.impl.jpa")
public class JpaRepositoryConfig {
    @Bean
    public JpaProperties jpaProperties() {
        return new JpaProperties();
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(
            DataSource dataSource,
            JpaProperties jpaProperties,
            EntityManagerFactoryBuilder factoryBuilder,
            @Value("{scanPackage}") String basePackage
    ) {
        return factoryBuilder.dataSource(dataSource)
                .packages(basePackage)
                .properties(jpaProperties.getProperties())
                .build();
    }

    @Bean
    public EntityManagerFactoryBuilder entityManagerFactoryBuilder(
            JpaProperties jpaProperties,
            JpaVendorAdapter jpaVendorAdapter
    ) {
        return new EntityManagerFactoryBuilder(jpaVendorAdapter, jpaProperties.getProperties(), null);
    }

    @Bean
    public JpaVendorAdapter jpaVendorAdapter(JpaProperties jpaProperties) {
        return new HibernateJpaVendorAdapter();
    }
}
