package pt.dsi.dpi.rest;

/*
 
mvn liberty:run
mvn liberty:stop

mvn liberty:dev
mvn liberty:dev -DskipTests

 */
import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;

@ApplicationPath("/v1")
public class RestApplication extends Application {

}