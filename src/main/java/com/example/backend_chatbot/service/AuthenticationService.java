package com.example.backend_chatbot.service;

import com.example.backend_chatbot.dto.request.AuthenticationRequest;
import com.example.backend_chatbot.dto.request.IntrospectRequest;
import com.example.backend_chatbot.dto.response.AuthenticationResponse;
import com.example.backend_chatbot.dto.response.IntrospectResponse;
import com.example.backend_chatbot.entity.User;
import com.example.backend_chatbot.repository.UserRepo;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAmount;
import java.time.temporal.TemporalUnit;
import java.util.Date;


@Service
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepo userRepo;

    @NonFinal
    protected String SIGN_KEY = "WI5UDs7wSDBKhfb4IWdLriRbkKkfAyirGlVABDfSKrFS+4mRLzYjtBZZTRuNU6eJ";

    public IntrospectResponse introspect(IntrospectRequest request) {

    }


    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        PasswordEncoder encoder = new BCryptPasswordEncoder(10);

        User user = userRepo.findByUserName(request.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        boolean authenticated = encoder.matches(request.getPassword(), user.getHashPassword());

        if (!authenticated){
            throw new RuntimeException("Invalid username or password");
        }
        var token = generateToken(request.getUsername());
        return AuthenticationResponse.builder()
                .token(token)
                .authenticated(true)
                .build();
    }

    private String generateToken(String username){

        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512); // Nội dung header chứa thuật toán

        // Data trong body thì được gọi là claim
        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .subject(username) // Đại diện cho user đăng nhập
                .issuer("trungpham") // Xác định token đc issue từ ai
                .issueTime(new Date()) // tgian issue ( hiện tại )
                .expirationTime(new Date(Instant.now().plus(1, ChronoUnit.HOURS).toEpochMilli())) // Thời hạn của token
                .claim("customClaim", "Custom")
                .build();
        Payload payload = new Payload(claimsSet.toJSONObject());
        JWSObject object = new JWSObject(header, payload);

        // Tạo signature
        try{
            object.sign(new MACSigner(SIGN_KEY.getBytes(StandardCharsets.UTF_8)));
            return object.serialize();
        }catch (Exception e){
            log.error(e.getMessage());
        }


        return object.serialize();
    }
}
