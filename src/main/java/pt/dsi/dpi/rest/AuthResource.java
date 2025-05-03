package pt.dsi.dpi.rest;

/*
 

JWT auth stuff

 */
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import java.util.logging.Logger;
import java.util.logging.Level;

@Path("/auth")
public class    AuthResource {
    private static final Logger logger = Logger.getLogger(AuthResource.class.getName());

    @Inject
    JWTTokenService jwtTokenService;

    public static class LoginReq {
        public String uid;
        public String pwd;
        public String aut;
    }

        
    @POST
    @Path("/login")
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(LoginReq loginReq) {
        String username = loginReq.uid;
        logger.info("Generating JWToken for user: " + username);
       
        String token = null;
        UserInfo uinfo = null;
        if ("123".equals(loginReq.pwd) ) {
            uinfo = new UserInfo(2,username,"***", List.of("user"));
        }
        else if ( "321".equals(loginReq.pwd)) {
            uinfo = new UserInfo(1,username,"***", List.of("user","admin"));
        }

        if (uinfo != null) {
            token = jwtTokenService.generateToken(uinfo);
        } else {
            logger.log(Level.SEVERE,"Invalid login credentials");
            return Response.status(Response.Status.UNAUTHORIZED).entity("Invalid credentials").build();
        }
        Map<String, String> map1 = new HashMap<>();

        map1.put("username", username);
        if (token != null) {
            map1.put("token",  token);
            map1.put("error", null);
        } else {
            map1.put("token", null);
            map1.put("error", "Error generating JWT");
        }
        logger.info(String.format("AuthResource.login JWT token=%s", token));
        
        return Response.ok(map1,MediaType.APPLICATION_JSON).build();
    }

    @POST
    @Path("/loginfm")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    public Response loginForm(
            @FormParam("username") String username,
            @FormParam("password") String password) {
        logger.info("Generating JWToken for user: " + username);
       
        String token = null;
        UserInfo uinfo = null;
        if ("123".equals(password) ) {
            uinfo = new UserInfo(2,username,"***", List.of("user"));
        }
        else if ( "321".equals(password)) {
            uinfo = new UserInfo(1,username,"***", List.of("user","admin"));
        }

        if (uinfo != null) {
            token = jwtTokenService.generateToken(uinfo);
        } else {
            logger.log(Level.SEVERE,"Invalid login credentials");
            return Response.status(Response.Status.UNAUTHORIZED).entity("Invalid credentials").build();
        }
        Map<String, String> map1 = new HashMap<>();

        map1.put("username", username);
        if (token != null) {
            map1.put("token",  token);
            map1.put("error", null);
        } else {
            map1.put("token", null);
            map1.put("error", "Error generating JWT");
        }
        logger.info(String.format("AuthResource.loginfm JWT token=%s", token));
        
        return Response.ok(map1,MediaType.APPLICATION_JSON).build();
    }

    @GET
    @Path("/check")
    public Response checkJwt(@QueryParam("jwt") String jwt) {
        if (jwt == null || jwt.isEmpty())
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("The 'jwt' query parameter is required.").build();
        try {
            UserInfo userInfo = jwtTokenService.parseToken(jwt);
            if (userInfo != null) {
                logger.info("JWT token is valid. User: " + userInfo.getUsername());
                return Response.ok(userInfo,MediaType.APPLICATION_JSON).build();
            } else {
                logger.warning("JWT token is invalid.");
                return Response.status(Response.Status.UNAUTHORIZED)
                        .entity("Invalid JWT token.").build();
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error reading JWT token" + e);
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity("Invalid JWT token. error: " + e.getMessage()).build();
        }
    }
    @GET
    @Path("/rnd")
    public double rnd(@HeaderParam("Authorization") String authorizationHeader) {
        if (authorizationHeader != null && !authorizationHeader.isEmpty()) {
            logger.info("Authorization Header: " + authorizationHeader);
            try {
                UserInfo userInfo = jwtTokenService.parseToken(authorizationHeader.replace("Bearer ", ""));
                logger.info(String.format("Secured resource accessed by: %s" ,userInfo));
            } catch (Exception e) {
                logger.log(Level.SEVERE, "Error reading JWT token" + e);
            }
        }
        double rn = Math.random();
        logger.warning(String.format("/auth Random = %f", rn));
        return rn;
    }
}