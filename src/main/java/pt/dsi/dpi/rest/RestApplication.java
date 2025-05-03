package pt.dsi.dpi.rest;

/*
https://openliberty.io/guides/liberty-deep-dive.html

https://www.baeldung.com/java-open-liberty

https://openliberty.io/guides/liberty-deep-dive.html#configuring-the-json-web-token
---
 
mvn liberty:run
mvn liberty:stop

mvn liberty:dev
mvn liberty:dev -DskipTests

 visit the http://localhost:9080/openapi/ui URL to see the OpenAPI UI

 */
import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;


@ApplicationPath("/v1")
public class RestApplication extends Application {

}