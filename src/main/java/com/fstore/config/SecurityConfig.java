package com.fstore.config;

import com.fstore.properties.ApplicationProperties;
import com.fstore.repository.UserRepository;
import com.fstore.service.auth.UserDetailStorageService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import static com.fstore.model.auth.RoleType.ROLE_ADMIN;

@Configuration
public class SecurityConfig {
    @Bean
    public UserDetailsService userDetailsService(UserRepository repository) {
        return new UserDetailStorageService(repository);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider(
            PasswordEncoder encoder,
            UserDetailsService detailsService) {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();

        authenticationProvider.setPasswordEncoder(encoder);
        authenticationProvider.setUserDetailsService(detailsService);

        return authenticationProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public AuthTokenFilter authTokenFilter(UserDetailsService userDetailsService) {
        return new AuthTokenFilter(userDetailsService);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            AuthenticationProvider authProvider,
            AuthTokenFilter authFilter,
            ApplicationProperties properties) throws Exception {
        http.authenticationProvider(authProvider);
        http.addFilterBefore(authFilter, UsernamePasswordAuthenticationFilter.class);

        return http.cors(corsConfigurer ->
                        corsConfigurer
                                .configurationSource(request -> {
                                    CorsConfiguration corsConfig = new CorsConfiguration();
                                    corsConfig.addAllowedOrigin(properties.allowedHosts());
                                    corsConfig.addAllowedHeader("*");
                                    corsConfig.addAllowedMethod("*");
                                    corsConfig.setAllowCredentials(true);
                                    return corsConfig;
                                }))
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(request ->
                        request
                                // auth controller
                                .requestMatchers(HttpMethod.GET, "/v1/auth/**").permitAll()
                                .requestMatchers(HttpMethod.POST, "/v1/auth/logout").authenticated()
                                .requestMatchers(HttpMethod.POST, "/v1/auth/**").permitAll()
                                // product controller
                                .requestMatchers(HttpMethod.GET, "/v1/products/**").permitAll()
                                .requestMatchers(HttpMethod.POST, "/v1/products/**").hasAuthority(ROLE_ADMIN.name())
                                .requestMatchers(HttpMethod.PATCH, "/v1/products/**").hasAuthority(ROLE_ADMIN.name())
                                .requestMatchers(HttpMethod.DELETE, "/v1/products/**").hasAuthority(ROLE_ADMIN.name())
                                // order controller
                                .requestMatchers(HttpMethod.GET, "/v1/orders/info").permitAll()
                                .requestMatchers(HttpMethod.GET, "/v1/orders/**").hasAuthority(ROLE_ADMIN.name())
                                .requestMatchers(HttpMethod.POST, "/v1/orders/**").permitAll()
                                .requestMatchers(HttpMethod.PATCH, "/v1/orders/**").hasAuthority(ROLE_ADMIN.name())
                                .requestMatchers(HttpMethod.DELETE, "/v1/orders/**").hasAuthority(ROLE_ADMIN.name())
                                .anyRequest().authenticated())
                .build();
    }
}
