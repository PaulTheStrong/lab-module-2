package com.epam.esm.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "webapp")
public class WebApplicationProperties {

    private String defaultEncoding;
    private String defaultLocale;
    private String exceptionMessagesFilename;

    public String getDefaultEncoding() {
        return defaultEncoding;
    }

    public void setDefaultEncoding(String defaultEncoding) {
        this.defaultEncoding = defaultEncoding;
    }

    public String getDefaultLocale() {
        return defaultLocale;
    }

    public void setDefaultLocale(String defaultLocale) {
        this.defaultLocale = defaultLocale;
    }

    public String getExceptionMessagesFilename() {
        return exceptionMessagesFilename;
    }

    public void setExceptionMessagesFilename(String exceptionMessagesFilename) {
        this.exceptionMessagesFilename = exceptionMessagesFilename;
    }
}
