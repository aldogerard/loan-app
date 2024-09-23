package com.enigmacamp.loanapp.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.enigmacamp.loanapp.entity.AppUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class JwtUtil {
    private static final Logger log = LoggerFactory.getLogger(JwtUtil.class);

    @Value("${app.loan-app.jwt-secret}")
    private String secret;

    @Value("${app.loan-app.app-name}")
    private String appName;

    @Value("${app.loan-app.jwtExpirationInSeconds}")
    private Long jwtExpirationInSeconds;

    public String generateToken(AppUser appUser) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(this.secret.getBytes());

            List<String> roles = new ArrayList<>();
            for (GrantedAuthority role : appUser.getAuthorities()){
                roles.add(role.toString());
            }

            return JWT.create()
                    .withSubject(appUser.getId())
                    .withIssuer(this.appName)
                    .withExpiresAt(Instant.now().plusSeconds(jwtExpirationInSeconds))
                    .withClaim("userId", appUser.getId())
                    .withClaim("role", roles)
                    .withClaim("email", appUser.getEmail())
                    .withIssuedAt(Instant.now())
                    .sign(algorithm);
        } catch (JWTCreationException e){
            log.error("JWT generation failed : {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public Boolean verifyJwtToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(this.secret.getBytes());
            JWTVerifier verifier = JWT.require(algorithm).build();
            DecodedJWT jwt = verifier.verify(token);
            return jwt.getIssuer().equals(this.appName);
        }catch (JWTVerificationException e){
            this.getError(e);
            return false;
        }
    }

    public Map<String, String> getUserInfoFromToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(this.secret.getBytes());
            JWTVerifier verifier = JWT.require(algorithm).build();
            DecodedJWT jwt = verifier.verify(token);

            Map<String, String> userInfo = new HashMap<>();
            userInfo.put("userId", jwt.getSubject());
            userInfo.put("role", jwt.getClaim("role").asString());
            return userInfo;
        }catch (JWTVerificationException e){
            this.getError(e);
            return null;
        }
    }

    private void getError(JWTVerificationException e) {
        log.error("JWT verification failed : {}", e.getMessage());
    }
}
