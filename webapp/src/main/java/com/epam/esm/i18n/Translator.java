package com.epam.esm.i18n;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class Translator {

    private final ResourceBundleMessageSource messageSource;

    @Autowired
    public Translator(@Qualifier("exceptionMessages") ResourceBundleMessageSource messageSource) {
        this.messageSource = messageSource;
    }

    public String toLocale(String code, Object... params) {
        Locale locale = LocaleContextHolder.getLocale();
        return messageSource.getMessage(code, params, locale);
    }
}
