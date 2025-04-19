package com.example.backend_chatbot.config;

import com.example.backend_chatbot.enums.Roles;
import lombok.RequiredArgsConstructor;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.AbstractConfiguredSecurityBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final String[] PUBLIC_ENDPOINTS = {"/api/guest", "/auth/**", "/user/**", "api/request"};
    private final String[] SWAGGER_ENDPOINTS = { // Do phiên bản openapi k tương thích nên khó chạy, phải chỉnh lên 2.8.5
            "/v3/api-docs/**",          // 🧩 API docs
            "/swagger-ui/**",           // 🧩 Swagger UI
            "/swagger-ui.html",          // 🧩 UI redirect
            "/webjars/**",
            "/v3/api-docs/public"
    };
    @Value(value = "${jwt.signerKey}")
    private String SECRET_KEY;

    private final CustomJWTDecoder customJWTDecoder;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception{
        // Cơ bản nhất: chặn những endpoint nào user có thể vào
        httpSecurity.authorizeHttpRequests(request ->
                request.requestMatchers(PUBLIC_ENDPOINTS).permitAll() /// Tất cả request của user đều có thể qua
                        .requestMatchers(SWAGGER_ENDPOINTS).permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/test")
//                        .hasAuthority("ROLE_USER")  /// Xài 1 trong 2 cách đều ổn, cách dưới thì nó sẽ tìm trong authority xem có user nào có role đó k
                        .hasRole(Roles.USER.name())
                        .anyRequest().authenticated() /// Còn lại thì phải authenticate
        );

        httpSecurity.oauth2ResourceServer(oauth2 ->
            oauth2.jwt(jwtConfigurer ->
                    jwtConfigurer.decoder(customJWTDecoder) /// Đăng ký authentication provider (jwt)
                            .jwtAuthenticationConverter(jwtAuthenticationConverter()) /// Custom cái prefix của authority
            )
        );

        httpSecurity.csrf(AbstractHttpConfigurer::disable); /// Spring security sẽ tự động cấu hình csrf để chặn request tấn công cross site jj đó
        return httpSecurity.build();
    }

    /**
     * Do mặc định tạo JwtGrantedAuthoritiesConverter mà không gọi setAuthoritiesClaimName(...) thì:
     * mặc định là: private String authoritiesClaimName = "scope";
     * Chính vì vậy, Spring sẽ tự động tìm claim "scope" trong JWT để lấy danh sách authorities nếu bạn không override.
     * @return
     */
    @Bean
    JwtAuthenticationConverter jwtAuthenticationConverter(){
        JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        jwtGrantedAuthoritiesConverter.setAuthorityPrefix("ROLE_"); /// Set cái prefix của authority

        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);
        return jwtAuthenticationConverter;
    }

    @Bean
    public CorsFilter corsFilter(){
        CorsConfiguration corsConfiguration = new CorsConfiguration();

        corsConfiguration.addAllowedOrigin("http://localhost:3000"); /// Cho phép api được gọi từ trang web nào
        corsConfiguration.addAllowedMethod("*"); /// Cho phép tất cả method có thể được gọi từ phía browser
        corsConfiguration.addAllowedHeader("*");

        UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();
        urlBasedCorsConfigurationSource.registerCorsConfiguration("/**", corsConfiguration); /// Áp dụng cors config cho tất cả endpoint

        return new CorsFilter(urlBasedCorsConfigurationSource);
    }

//    @Bean
//    JwtDecoder jwtDecoder(){
//        SecretKeySpec secretKeySpec = new SecretKeySpec(SECRET_KEY.getBytes(StandardCharsets.UTF_8), "HS512");
//        return NimbusJwtDecoder.withSecretKey(secretKeySpec)
//                .macAlgorithm(MacAlgorithm.HS512)
//                .build();
//    }
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }
}