package pt.dsi.dpi.rest;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;

import io.jsonwebtoken.security.Keys;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.crypto.SecretKey;


@ApplicationScoped
public class JWTTokenService {
    private static final Logger logger = LoggerFactory.getLogger(JWTTokenService.class);


    @Inject @ConfigProperty(name = "app.jwt.duration", defaultValue = "3600") // 1 hour default
    long jwt_duration;

    @Inject @ConfigProperty(name = "app.jwt.secret", defaultValue = "my-secret-symmetric-key in 32 BY") // 256 bit key
    String jwt_Secret;

    public String generateToken(UserInfo user) {
        Instant expirationTime = Instant.now().plus(jwt_duration, ChronoUnit.SECONDS);
        Date expirationDate = Date.from(expirationTime);
        logger.info("JWT duration: " + jwt_duration);
        logger.info("JWT expires at " + expirationDate);
        // SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretString));
        Key key = Keys.hmacShaKeyFor(jwt_Secret.getBytes());
        String bp_aud = "https://dsicert.novobanco.pt/oauth2/token";
        Collection<String> audienceList = new ArrayList<>();
        audienceList.add(bp_aud);


        List<String> lro = user.getRoles();
        String roles = "";
        if (lro != null && lro.size() > 0)
            roles = String.join(";", lro);
        String compactTokenString = Jwts.builder()
                .claim("id", user.getId())
                .claim("sub", user.getUsername())
                .claim("roles", roles)  // .setIssuedAt(Date.from(Instant.ofEpochSecond(1466796822L)))
                .expiration(expirationDate)
                .issuedAt(new Date())
                .audience().add(bp_aud).and()
                .signWith(key)
                .compact();

        return "Bearer " + compactTokenString;
    }

    /**
     * @param token - the compact JWT token stripped from "Bearer " prefix
     */
    public UserInfo parseToken(String token) {
        Jws<Claims> jwsClaims;

        try {
            SecretKey key = Keys.hmacShaKeyFor(jwt_Secret.getBytes());
            jwsClaims = Jwts.parser().verifyWith(key).build().parseSignedClaims(token);

        } catch (io.jsonwebtoken.security.SignatureException | io.jsonwebtoken.MalformedJwtException ex) {
            logger.error("Invalid JWT",ex);
            return null;
        }
        Claims claims = jwsClaims.getPayload();
        String username = claims.getSubject();
        Integer userId = claims.get("id", Integer.class);
        String sroles = claims.get("roles", String.class);
        List<String> roles = null;
        if (sroles != null && sroles.length()>0)
            roles =  Arrays.asList(sroles.split(";"));
        //logger.info("JWT Issued at: {}" , claims.getIssuedAt());
        logger.info("parseToken JWT Expiration: {}", claims.getExpiration());
        UserInfo u1 = new UserInfo(userId, username, "***", roles);
        logger.info("parseToken User: {}", u1);
        return u1;
    }

}