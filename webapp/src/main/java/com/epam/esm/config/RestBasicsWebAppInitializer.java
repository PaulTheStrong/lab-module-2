package com.epam.esm.config;

import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

public class RestBasicsWebAppInitializer
        extends AbstractAnnotationConfigDispatcherServletInitializer {

    private static final String SPRING_PROFILES_ACTIVE = "spring.profiles.active";
    private static final String PROD_PROFILE = "prod";
    private static final String DEV_PROFILE = "dev";

    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class[] {RootConfig.class, RepositoryConfig.class};
    }

    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class[] { WebConfig.class };
    }

    @Override
    protected String[] getServletMappings() {
        return new String[] {"/v1/*"};
    }

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        super.onStartup(servletContext);
        servletContext.setInitParameter(SPRING_PROFILES_ACTIVE, PROD_PROFILE);
    }
}
