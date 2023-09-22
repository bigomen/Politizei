package com.api.politizei.jwt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Autowired
    private Environment env;

    private static final RequestMatcher ADMIN_URLS = new OrRequestMatcher(
            new AntPathRequestMatcher("/admin/**", "GET"),
            new AntPathRequestMatcher("/admin/**", "POST"),
            new AntPathRequestMatcher("/admin/**", "PUT"),
            new AntPathRequestMatcher("/admin/**", "DELETE")
    );

    private static final RequestMatcher PUBLIC_URLS = new OrRequestMatcher(
            new AntPathRequestMatcher("/login", "POST")
    );

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        http
                .cors(Customizer.withDefaults())
                .authorizeHttpRequests((requests) -> requests
                        .requestMatchers(PUBLIC_URLS).permitAll()
                        .anyRequest().authenticated())
                .addFilterBefore(new JWTAuthenticationFilter(env), UsernamePasswordAuthenticationFilter.class);
        http.csrf(AbstractHttpConfigurer::disable);
        return http.build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration configAdmin = new CorsConfiguration().applyPermitDefaultValues();
        configAdmin.setAllowCredentials(true);
        configAdmin.addAllowedHeader("X-Requested-With");
        configAdmin.addAllowedHeader("Origin");
        configAdmin.addAllowedHeader("Content-Type");
        configAdmin.addAllowedHeader("Accept");
        configAdmin.addAllowedHeader("Authorization");
        configAdmin.addAllowedHeader("Refresh");
        configAdmin.addAllowedHeader("Access-Control-Allow-Origin");
        configAdmin.addAllowedHeader("Access-Control-Request-Method");
        configAdmin.addAllowedHeader("Access-Control-Request-Headers");

        configAdmin.addExposedHeader("Authorization");
        configAdmin.addExposedHeader("Content-Type");
        configAdmin.addExposedHeader("Access-Control-Allow-Origin");
        configAdmin.addExposedHeader("Access-Control-Allow-Headers");
        configAdmin.addExposedHeader("Access-Control-Request-Method");
        configAdmin.addExposedHeader("Access-Control-Request-Headers");
        configAdmin.addExposedHeader("Refresh");
        configAdmin.addExposedHeader("Allow");

        configAdmin.addAllowedMethod(HttpMethod.PUT);
        configAdmin.addAllowedMethod(HttpMethod.DELETE);
        configAdmin.addAllowedMethod(HttpMethod.GET);
        configAdmin.addAllowedMethod(HttpMethod.POST);
        configAdmin.addAllowedMethod(HttpMethod.OPTIONS);
        configAdmin.addAllowedOriginPattern("*");

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/admin/**", configAdmin);
        source.registerCorsConfiguration("/publico/**", configAdmin);

        source.registerCorsConfiguration("/swagger-ui/**", configAdmin);
        source.registerCorsConfiguration("/login", configAdmin);
        source.registerCorsConfiguration("/updatePassword", configAdmin);
        source.registerCorsConfiguration("/reset/**", configAdmin);
        source.registerCorsConfiguration("/forgot", configAdmin);
        source.registerCorsConfiguration("/refresh", configAdmin);
        return source;
    }
}
