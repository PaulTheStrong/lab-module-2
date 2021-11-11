package com.epam.esm.security;

import com.epam.esm.entities.User;
import com.epam.esm.filter.CustomAuthenticationFilter;
import com.epam.esm.filter.CustomAuthorizationFilter;
import com.epam.esm.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.ExceptionTranslationFilter;

import java.util.Collection;
import java.util.Objects;

import static com.epam.esm.security.ApplicationSecurityUserDetails.ADMIN;
import static com.epam.esm.security.ApplicationSecurityUserDetails.USER;
import static org.springframework.http.HttpMethod.DELETE;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.PATCH;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@Slf4j
public class ApplicationSecurity extends WebSecurityConfigurerAdapter {

    private static final String ROLE_ANONYMOUS = "ROLE_ANONYMOUS";
    private final PasswordEncoder passwordEncoder;
    private final UserService userService;
    private CustomAuthorizationFilter authorizationFilter;
    private CustomAuthenticationFilter authenticationFilter;
    private final ExceptionEntryPoint exceptionEntryPoint;
    private final SecurityAccessDeniedHandler accessDeniedHandler;

    @Value("${spring.mvc.servlet.path}")
    private String SERVLET_PATH;

    @Bean
    public CustomAuthenticationFilter authenticationFilter(AuthenticationManager manager, ObjectMapper objectMapper) {
        authenticationFilter = new CustomAuthenticationFilter(objectMapper, manager);
        authenticationFilter.setFilterProcessesUrl(SERVLET_PATH + "/login");
        return authenticationFilter;
    }

    @Bean
    public CustomAuthorizationFilter authorizationFilter(ObjectMapper objectMapper) {
        authorizationFilter = new CustomAuthorizationFilter(objectMapper);
        return authorizationFilter;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService).passwordEncoder(passwordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.sessionManagement().sessionCreationPolicy(STATELESS);
        configureCertificateAccessRules(http);
        configureTagAccessRules(http);
        configureUserAccessRules(http);
        http.authorizeRequests().anyRequest().authenticated();
        http.addFilter(authenticationFilter);
        http.addFilterBefore(authorizationFilter, CustomAuthenticationFilter.class);
        http.exceptionHandling()
                .accessDeniedHandler(accessDeniedHandler);
    }

    @Bean
    public AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }

    private void configureCertificateAccessRules(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers(GET, SERVLET_PATH + "/certificates").permitAll()
                .antMatchers(GET, SERVLET_PATH + "/certificates/**").permitAll()
                .antMatchers(POST, SERVLET_PATH + "/certificates").hasRole(ADMIN)
                .antMatchers(PATCH, SERVLET_PATH + "/certificates/**").hasRole(ADMIN)
                .antMatchers(DELETE, SERVLET_PATH + "/certificates/**").hasRole(ADMIN);
    }

    private void configureTagAccessRules(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers(GET, SERVLET_PATH + "/tags").permitAll()
                .antMatchers(GET, SERVLET_PATH + "/most_popular_tag").permitAll()
                .antMatchers(GET, SERVLET_PATH + "/tags/**").permitAll()
                .antMatchers(POST, SERVLET_PATH + "/tags").hasRole(ADMIN)
                .antMatchers(DELETE, SERVLET_PATH + "/tags/**").hasRole(ADMIN);
    }

    private void configureUserAccessRules(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers(POST, SERVLET_PATH + "/users").anonymous()
                .antMatchers(GET, SERVLET_PATH + "/users").hasRole(ADMIN)
                .antMatchers(GET, SERVLET_PATH + "/users/{id}").access("@applicationSecurity.checkUserPermission(authentication, #id)")
                .antMatchers(GET, SERVLET_PATH + "/users/{id}/**").access("@applicationSecurity.checkUserPermission(authentication, #id)")
                .antMatchers(POST, SERVLET_PATH + "/users/{id}/orders").access("@applicationSecurity.checkUserPermission(authentication, #id)");
    }

    public boolean checkUserPermission(Authentication authentication, int id) {
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        SimpleGrantedAuthority anonymous = new SimpleGrantedAuthority(ROLE_ANONYMOUS);
        if (authorities.contains(anonymous)) {
            return false;
        }
        String username = (String) authentication.getPrincipal();
        User user = userService.getUserByUsername(username);
        Integer expectedUserId = user.getId();
        log.info("Checking if {}'s id. Expected id = {}. Actual id = {}", username, expectedUserId, id);
        String roleName = user.getRole().getName();
        return (Objects.equals(roleName, ADMIN)) || (expectedUserId == id && Objects.equals(roleName, USER));
    }
}
