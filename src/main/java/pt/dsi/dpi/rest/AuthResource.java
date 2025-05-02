package pt.dsi.dpi.rest;

/*
 

maybe add this to pom.xml !!!

<dependency>
    <groupId>org.eclipse.microprofile.jwt</groupId>
    <artifactId>microprofile-jwt-auth-api</artifactId>
    <version>2.0.0</version>
</dependency>
<dependency>
    <groupId>org.eclipse.microprofile.jwt</groupId>
    <artifactId>microprofile-jwt-auth</artifactId>
    <version>1.2</version>
</dependency>

 */
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.logging.Logger;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

@Path("auth")
public class AuthResource {
    private static final Logger logger = Logger.getLogger(AuthResource.class.getName());

    @Inject
    @ConfigProperty(name = "app.jwt.duration", defaultValue = "3600") // 1 hour default
    long jwt_duration;

    @Inject
    @ConfigProperty(name = "app.jwt.secret", defaultValue = "my-secret-symmetric-key in 32 BY") // 256 bit key
    String jwt_Secret;

    @POST
    @Path("login")
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(
            @QueryParam("username") String username,
            @QueryParam("password") String password) {
        logger.info("Generating token for user: " + username);
        String token = null;
        String u_uid = null;
        if ("123".equals(password))
            u_uid = "1";

        if (u_uid != null) {
            Instant expirationTime = Instant.now().plus(jwt_duration * 1000, ChronoUnit.MILLIS);
            Date expirationDate = Date.from(expirationTime);
            logger.info("JWT expires at " + expirationDate);
            // SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretString));
            Key key = Keys.hmacShaKeyFor(jwt_Secret.getBytes());
            String bp_audience = "https://wwwcert.bportugal.net/adfs/oauth2/token";
            //Collection<String> audienceList = new ArrayList<>();
            //audienceList.add(bp_audience);

            token = Jwts.builder()
                    .claim("id", u_uid)
                    .claim("sub", username)
                    .claim("roles", new String[]{"user", "admin"})
                    .expiration(expirationDate)
                    .claim("aud", bp_audience)
                    .signWith(key)
                    .compact();
        } else {
            logger.log(Level.SEVERE,"Invalid login credentials");
            return Response.status(Response.Status.UNAUTHORIZED).entity("Invalid credentials").build();
        
        }
        Map<String, String> map1 = new HashMap<>();

        map1.put("username", "demo");
        if (token != null) {
            map1.put("token", "Bearer " + token);
            map1.put("error", null);
        } else {
            map1.put("token", null);
            map1.put("error", "Error generating JWT");
        }
        logger.info(String.format("AuthResource.login JWT token=%s", token));
        
        return Response.ok(map1).build();
    }

    @GET
    @Path("/rnd")
    public double rnd(@HeaderParam("Authorization") String authorizationHeader) {
        if (authorizationHeader != null && !authorizationHeader.isEmpty()) {
            logger.info("Authorization Header: " + authorizationHeader);
            try {
                // String subject = jwt.getSubject();
                // String groups = jwt.getClaim("groups").toString(); // Access a custom claim
                // logger.info(String.format("Secured resource accessed by: %s, Groups: %s" ,subject, groups));
            } catch (Exception e) {
                logger.log(Level.SEVERE, "Error reading JWT token" + e);
            }
        }
        double rn = Math.random();
        logger.warning(String.format("/auth Random = %f", rn));
        return rn;
    }
}