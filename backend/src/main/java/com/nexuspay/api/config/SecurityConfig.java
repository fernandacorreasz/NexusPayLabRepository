package com.nexuspay.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import java.util.List;

@Configuration
public class SecurityConfig {
  @Bean
  InMemoryUserDetailsManager users() {
    return new InMemoryUserDetailsManager(
        User.withUsername("alice").password("{noop}alice123").roles("CUSTOMER").build(),
        User.withUsername("bruno").password("{noop}bruno123").roles("CUSTOMER").build(),
        User.withUsername("carla").password("{noop}carla123").roles("CUSTOMER").build(),
        User.withUsername("operator").password("{noop}operator123").roles("OPERATOR").build());
  }

  @Bean
  SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http.csrf(csrf -> csrf.disable()).cors(Customizer.withDefaults()).httpBasic(Customizer.withDefaults())
        .authorizeHttpRequests(auth -> auth
            .requestMatchers("/", "/api", "/swagger-ui/**", "/v3/api-docs/**", "/api/lab/**", "/api/debug/statement")
            .permitAll()
            .anyRequest().authenticated());
    return http.build();
  }

  @Bean
  CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration config = new CorsConfiguration();
    config.setAllowedOriginPatterns(List.of("*"));
    config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
    config.setAllowedHeaders(List.of("*"));
    config.setAllowCredentials(true);
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", config);
    return source;
  }
}
