package com.first_project.demo.configuration;

import com.first_project.demo.enums.Role;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

  private final String[] PUBLIC_ENDPOINTS = {"/users", "/auth/login", "/auth/logout", "/auth/introspect"};

  @Value("${jwt.signerKey}")
  private String signerKey;

  @Autowired
  private CustomJwtDecoder customJwtDecoder;

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
    httpSecurity.authorizeHttpRequests(
        request -> request.requestMatchers(HttpMethod.POST, PUBLIC_ENDPOINTS).permitAll()
//            .requestMatchers(HttpMethod.GET, "/users").hasRole(Role.ADMIN.name())
            .anyRequest()
            .authenticated());

    httpSecurity.csrf(httpSecurityCsrfConfigurer -> httpSecurityCsrfConfigurer.disable());

    httpSecurity.oauth2ResourceServer(oauth2 -> oauth2.jwt(
        jwtConfigurer -> jwtConfigurer.decoder(customJwtDecoder)
            .jwtAuthenticationConverter(jwtAuthenticationConverter())));

    return httpSecurity.build();
  }

//  @Bean
//  JwtDecoder jwtDecoder() {
//    SecretKeySpec secretKeySpec = new SecretKeySpec(signerKey.getBytes(), "HS512");
//    return NimbusJwtDecoder.withSecretKey(secretKeySpec).macAlgorithm(MacAlgorithm.HS512).build();
//  }

  @Bean
  JwtAuthenticationConverter jwtAuthenticationConverter() {
    JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
    jwtGrantedAuthoritiesConverter.setAuthorityPrefix("");

    JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
    jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);
    return jwtAuthenticationConverter;
  }
}
