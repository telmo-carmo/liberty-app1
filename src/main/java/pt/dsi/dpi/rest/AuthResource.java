package pt.dsi.dpi.rest;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;

//import org.eclipse.microprofile.jwt.Claims;
//import org.eclipse.microprofile.jwt.Jwt;
//import org.eclipse.microprofile.jwt.JwtBuilder;

import java.time.Instant;
import java.util.Arrays;
import java.util.HashSet;
import java.util.logging.Logger;
import java.util.logging.Level;

@Path("auth")
public class AuthResource {
    private static final Logger logger = Logger.getLogger(AuthResource.class.getName());

    //@Inject
    //private JwtBuilder jwtBuilder;

    @POST
    @Path("login")
    @Produces(MediaType.TEXT_PLAIN)
    public String generateToken(
            @QueryParam("username") String username,
            @QueryParam("password") String password) {
        logger.info("Generating token for user: " + username);
        /* 
        Instant now = Instant.now();
        Instant expiry = now.plusSeconds(3600); // Token expires in 1 hour

        String token = jwtBuilder.claim(Claims.iss.name(), "my-issuer")
                               .claim("upn", "bob") // Custom claim for username
                               .claim("groups", new HashSet<>(Arrays.asList("user", "admin")))
                               .issuedAt(now)
                               .expiresAt(expiry)
                               .subject("bob")
                               .build();
        */
        String token = "NONE!"; 
        return token;
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