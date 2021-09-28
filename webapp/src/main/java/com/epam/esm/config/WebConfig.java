package com.epam.esm.config;

import com.epam.esm.exception.ErrorCodeToHttpStatusMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import java.util.Locale;

@Configuration
@EnableWebMvc
@ComponentScan("com.epam.esm")
@PropertySource("classpath:webapp.properties")
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void configureViewResolvers(ViewResolverRegistry registry) {
        registry.enableContentNegotiation(new MappingJackson2JsonView());
    }

    @Bean
    public LocaleResolver localeResolver(@Value("${defaultLocale}") String defaultLocale) {
        AcceptHeaderLocaleResolver acceptHeaderLocaleResolver = new AcceptHeaderLocaleResolver();
        acceptHeaderLocaleResolver.setDefaultLocale(new Locale(defaultLocale));
        return acceptHeaderLocaleResolver;
    }

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertyConfigInDev() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    @Bean
    @Qualifier("errorMessages")
    public ResourceBundleMessageSource messageSource(
            @Value("${exceptionMessagesFilename}") String resourceBundleBaseName,
            @Value("${defaultEncoding}") String defaultEncoding) {
        ResourceBundleMessageSource rs = new ResourceBundleMessageSource();
        rs.setBasename(resourceBundleBaseName);
        rs.setDefaultEncoding(defaultEncoding);
        rs.setUseCodeAsDefaultMessage(true);
        return rs;
    }

    @Bean
    public ErrorCodeToHttpStatusMapper errorCodeToHttpStatusMapper() {
        return new ErrorCodeToHttpStatusMapper();
    }

    @Bean
    public LocalValidatorFactoryBean localValidatorFactory() {
        LocalValidatorFactoryBean localValidatorFactoryBean = new LocalValidatorFactoryBean();
        localValidatorFactoryBean.getValidationPropertyMap().put("hibernate.validator.fail_fast", "true");
        return localValidatorFactoryBean;
    }
}
