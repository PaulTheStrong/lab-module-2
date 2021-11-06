package com.epam.esm.security;

import com.epam.esm.entities.Role;
import com.epam.esm.entities.User;
import com.epam.esm.filter.CustomAuthenticationFilter;
import com.epam.esm.filter.CustomAuthorizationFilter;
import com.epam.esm.service.UserService;
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
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Objects;

import static com.epam.esm.entities.Role.ADMIN;
import static com.epam.esm.entities.Role.USER;
import static org.springframework.http.HttpMethod.*;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@Slf4j
public class ApplicationSecurity extends WebSecurityConfigurerAdapter {

    public static final String ROLE_ANONYMOUS = "ROLE_ANONYMOUS";
    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final UserService userService;

    @Value("${spring.mvc.servlet.path}")
    private String SERVLET_PATH;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        CustomAuthenticationFilter filter = new CustomAuthenticationFilter(authenticationManager());
        filter.setFilterProcessesUrl(SERVLET_PATH + "/login");
        http.csrf().disable();
        http.sessionManagement().sessionCreationPolicy(STATELESS);
        configureCertificateAccessRules(http);
        configureTagAccessRules(http);
        configureUserAccessRules(http);
        http.authorizeRequests().anyRequest().authenticated();
        http.addFilter(filter);
        http.addFilterBefore(new CustomAuthorizationFilter(), CustomAuthenticationFilter.class);
    }

    @Bean
    public AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }

    private void configureCertificateAccessRules(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers(GET, SERVLET_PATH + "/certificates").permitAll()
                .antMatchers(GET, SERVLET_PATH + "/certificates/**").permitAll()
                .antMatchers(POST, SERVLET_PATH + "/certificates").hasAuthority(ADMIN)
                .antMatchers(PATCH, SERVLET_PATH + "/certificates/**").hasAuthority(ADMIN)
                .antMatchers(DELETE, SERVLET_PATH + "/certificates/**").hasAuthority(ADMIN);
    }

    private void configureTagAccessRules(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers(GET, SERVLET_PATH + "/tags").permitAll()
                .antMatchers(GET, SERVLET_PATH + "/most_popular_tag").permitAll()
                .antMatchers(GET, SERVLET_PATH + "/tags/**").permitAll()
                .antMatchers(POST, SERVLET_PATH + "/tags").hasAuthority(ADMIN)
                .antMatchers(DELETE, SERVLET_PATH + "/tags/**").hasAuthority(ADMIN);
    }

    private void configureUserAccessRules(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers(POST, SERVLET_PATH + "/users").anonymous()
                .antMatchers(GET, SERVLET_PATH + "/users").hasAnyAuthority(ADMIN)
                .antMatchers(GET, SERVLET_PATH + "/users/{id}").access("@applicationSecurity.checkUserPermission(authentication, #id)")
                .antMatchers(GET, SERVLET_PATH + "/users/{id}/**").access("@applicationSecurity.checkUserPermission(authentication, #id)")
                .antMatchers(POST, SERVLET_PATH + "/users/{id}/orders").access("@applicationSecurity.checkUserPermission(authentication, #id)");
    }

    public boolean checkUserPermission(Authentication authentication, int id) {
        if (authentication.getAuthorities().contains(new SimpleGrantedAuthority(ROLE_ANONYMOUS))) {
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
