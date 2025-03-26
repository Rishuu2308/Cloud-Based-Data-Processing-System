package com.dataspace.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity  // If you want @PreAuthorize, etc. (optional)
public class SecurityConfig {

    /**
     * 1) Define a UserDetailsService with in-memory credentials
     */
    @Bean
    public UserDetailsService userDetailsService() {
        // Creates a single in-memory user:
        UserDetails user = User.withUsername("user")
                // {noop} means "NoOp" password encoder (plain text) - DO NOT use in production
                .password("{noop}password")
                .roles("USER")
                .build();

        return new InMemoryUserDetailsManager(user);
    }

    /**
     * 2) Define the SecurityFilterChain that configures HTTP Basic and URL access rules
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable()) // disable CSRF for simplicity; consider enabling if needed
                .authorizeHttpRequests(auth -> auth
                        // All /api/orders/** endpoints must be authenticated
                        .requestMatchers("/api/**").authenticated()
                        // anything else is permitted (adjust as needed)
                        .anyRequest().permitAll()
                )
                // use basic authentication
                .httpBasic(Customizer.withDefaults())
                // build the filter chain
                .build();
    }
}
