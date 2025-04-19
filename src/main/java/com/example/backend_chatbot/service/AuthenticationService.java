package com.example.backend_chatbot.service;

import com.example.backend_chatbot.dto.request.AuthenticationRequest;
import com.example.backend_chatbot.dto.request.IntrospectRequest;
import com.example.backend_chatbot.dto.request.LogoutRequest;
import com.example.backend_chatbot.dto.request.RefreshTokenRequest;
import com.example.backend_chatbot.dto.response.AuthenticationResponse;
import com.example.backend_chatbot.dto.response.IntrospectResponse;
import com.example.backend_chatbot.entity.InvalidatedToken;
import com.example.backend_chatbot.entity.User;
import com.example.backend_chatbot.repository.InvalidateTokenRepo;
import com.example.backend_chatbot.repository.UserRepo;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collection;
import java.util.Date;
import java.util.StringJoiner;
import java.util.UUID;


@Service
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepo userRepo;
    private final InvalidateTokenRepo invalidateTokenRepo;

    @NonFinal
    @Value("${jwt.signerKey}")
    protected String SIGN_KEY;

    public IntrospectResponse introspect(IntrospectRequest request) throws JOSEException, ParseException {
        String token = request.getToken();

        boolean isvalid = true;
        try{
            verifyToken(token);
        }catch (RuntimeException e){
            isvalid = false;
        }

        return IntrospectResponse.builder()
                .valid(isvalid)
                .build();
    }

    /**
     * Xác thực thông tin đăng nhập từ user
     * @param request AuthenticationRequest
     * @return AuthenticationResponse
     */
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        PasswordEncoder encoder = new BCryptPasswordEncoder(10);

        User user = userRepo.findByUserName(request.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        boolean authenticated = encoder.matches(request.getPassword(), user.getHashPassword());

        if (!authenticated){
            throw new RuntimeException("Invalid username or password");
        }
        var token = generateToken(user);
        return AuthenticationResponse.builder()
                .token(token)
                .authenticated(true)
                .build();
    }

    /**
     * Tạo JSON web token (JWT)
     * @param user User
     * @return String
     */
    private String generateToken(User user){

        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512); /// Nội dung header chứa thuật toán

        /// Data trong body thì được gọi là claim
        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .subject(user.getUserName()) /// Đại diện cho user đăng nhập
                .issuer("trungpham") /// Xác định token đc issue từ ai
                .issueTime(new Date()) /// tgian issue ( hiện tại )
                .expirationTime(new Date(Instant.now().plus(1, ChronoUnit.HOURS).toEpochMilli())) /// Thời hạn của token
                .claim("scope", buildScope(user))
                .jwtID(UUID.randomUUID().toString())
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

    private String buildScope(User user){
        StringJoiner stringJoiner = new StringJoiner(" "); /// Trong oauth2 thì trong scope phân cách nhau bằng space
        if(!CollectionUtils.isEmpty(user.getRoles())){
            user.getRoles().forEach(stringJoiner::add);
        }
        return stringJoiner.toString();
    }

    public void logout(LogoutRequest token) throws ParseException, JOSEException {
        SignedJWT tok = verifyToken(token.getToken());

        String JWTid = tok.getJWTClaimsSet().getJWTID();
        Date expirationTime = tok.getJWTClaimsSet().getExpirationTime();

        InvalidatedToken invalidatedToken = new InvalidatedToken(JWTid, expirationTime);

        invalidateTokenRepo.save(invalidatedToken);

    }


    private SignedJWT verifyToken(String token) throws JOSEException, ParseException {
        JWSVerifier verifier = new MACVerifier(SIGN_KEY);

        SignedJWT signedJWT = SignedJWT.parse(token);


        /// Kiểm tra token có hết hạn k
        Date expireTime = signedJWT.getJWTClaimsSet().getExpirationTime();

        /// Kiểm tra token có hợp lệ k (có bị thay đổi gì k)
        boolean verified = signedJWT.verify(verifier);

        if (!(verified && expireTime.after(new Date()))){
            throw new RuntimeException("Invalid token");
        }

        /// kiểm tra token logout chưa
        if(invalidateTokenRepo.findById(signedJWT.getJWTClaimsSet().getJWTID()).isPresent()){
            throw new RuntimeException("Invalid token");
        }

        return signedJWT;
    }

    public AuthenticationResponse refreshToken(RefreshTokenRequest request) throws ParseException, JOSEException {

        /// Check xem token còn hiệu lực k
        SignedJWT jwt = verifyToken(request.getToken());

        /// Lưu token đó vào list token hết hạn
        String JWTid = jwt.getJWTClaimsSet().getJWTID();
        Date expirationTime = jwt.getJWTClaimsSet().getExpirationTime();

        InvalidatedToken invalidatedToken = new InvalidatedToken(JWTid, expirationTime);
        invalidateTokenRepo.save(invalidatedToken);

        /// Tìm user để gen token mới
        String username = jwt.getJWTClaimsSet().getSubject();

        User user = userRepo.findByUserName(username).orElseThrow(() -> new RuntimeException("User not found"));

        String newToken = generateToken(user);

        return AuthenticationResponse.builder()
                .token(newToken)
                .authenticated(true)
                .build();

    }
}
