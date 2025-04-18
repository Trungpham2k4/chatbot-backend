package com.example.backend_chatbot.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.AbstractConfiguredSecurityBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final String[] PUBLIC_ENDPOINTS = {"/api/guest", "/auth/login", "/auth/introspect", "/user/**"};
    private final String[] SWAGGER_ENDPOINTS = { // Do phiên bản openapi k tương thích nên khó chạy, phải chỉnh lên 2.8.5
            "/v3/api-docs/**",          // 🧩 API docs
            "/swagger-ui/**",           // 🧩 Swagger UI
            "/swagger-ui.html",          // 🧩 UI redirect
            "/webjars/**",
            "/v3/api-docs/public"
    };

    private final String SECRET_KEY = "WI5UDs7wSDBKhfb4IWdLriRbkKkfAyirGlVABDfSKrFS+4mRLzYjtBZZTRuNU6eJ";

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception{
        // Cơ bản nhất: chặn những endpoint nào user có thể vào
        httpSecurity.authorizeHttpRequests(request ->
                request.requestMatchers(PUBLIC_ENDPOINTS).permitAll() // Tất cả request của user đều có thể qua
                        .requestMatchers(SWAGGER_ENDPOINTS).permitAll()
                        .anyRequest().authenticated() // Còn lại thì phải authenticate
        );

        httpSecurity.oauth2ResourceServer(oauth2 ->
            oauth2.jwt(jwtConfigurer ->
                    jwtConfigurer.decoder(jwtDecoder()) // Đăng ký authentication provider (jwt)
            )
        );

        httpSecurity.csrf(AbstractHttpConfigurer::disable); // Spring security sẽ tự động cấu hình csrf để chặn request tấn công cross site jj đó
        return httpSecurity.build();
    }

    @Bean
    JwtDecoder jwtDecoder(){
        SecretKeySpec secretKeySpec = new SecretKeySpec(SECRET_KEY.getBytes(StandardCharsets.UTF_8), "HS512");
        return NimbusJwtDecoder.withSecretKey(secretKeySpec)
                .macAlgorithm(MacAlgorithm.HS512)
                .build();
    }
}