package pt.dsi.dpi.rest;


import java.util.List;
import java.util.Properties;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.ParameterIn;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameters;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponseSchema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.slf4j.LoggerFactory;

import java.util.logging.Level;
import java.util.logging.Logger;

import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import pt.dsi.dpi.rest.dal.DataInventory;
import pt.dsi.dpi.rest.dal.SystemData;

@Path("/api/sys")
public class PropertiesResource {
    private static final Logger logger = Logger.getLogger(PropertiesResource.class.getName());


    @Inject
    DataInventory inventory;

    @GET
    @Path("/props")
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
        summary = "Get JVM system properties for particular host",
        description = "Retrieves and returns the JVM system properties from the system "
        + "service running on the particular host.")
    public Properties getProperties() {
        return System.getProperties();
    }

    private Response success(String message) {
        return Response.ok("{ \"ok\" : \"" + message + "\" }").build();
    }

    private Response fail(String message) {
        return Response.status(Response.Status.BAD_REQUEST)
                       .entity("{ \"error\" : \"" + message + "\" }")
                       .build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @APIResponseSchema(value = SystemData.class,
        responseDescription = "A list of system data stored within the inventory.",
        responseCode = "200")
    @Operation(
        summary = "List contents.",
        description = "Returns the currently stored system data in the inventory.",
        operationId = "listContents")
    public List<SystemData> listContents() {
        return inventory.getSystems();
    }

    @GET
    @Path("/{hostname}")
    @Produces(MediaType.APPLICATION_JSON)
    @APIResponseSchema(value = SystemData.class,
        responseDescription = "System data of a particular host.",
        responseCode = "200")
    @Operation(
        summary = "Get System",
        description = "Retrieves and returns the system data from the system "
                      + "service running on the particular host.",
        operationId = "getSystem"
    )
    public SystemData getSystem(
            @Parameter(
                name = "hostname", in = ParameterIn.PATH,
                description = "The hostname of the system",
                required = true, example = "localhost",
                schema = @Schema(type = SchemaType.STRING)
            )
            @PathParam("hostname") String hostname) {
        logger.log(Level.INFO, "getSystem called with hostname: {0}", hostname);
        return inventory.getSystem(hostname);
    }

    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    @APIResponses(value = {
        @APIResponse(responseCode = "200",
            description = "Successfully added system to inventory"),
        @APIResponse(responseCode = "400",
            description = "Unable to add system to inventory")
    })
    @Parameters(value = {
        @Parameter(
            name = "hostname", in = ParameterIn.QUERY,
            description = "The hostname of the system",
            required = true, example = "localhost",
            schema = @Schema(type = SchemaType.STRING)),
        @Parameter(
            name = "osName", in = ParameterIn.QUERY,
            description = "The operating system of the system",
            required = true, example = "linux",
            schema = @Schema(type = SchemaType.STRING)),
        @Parameter(
            name = "javaVersion", in = ParameterIn.QUERY,
            description = "The Java version of the system",
            required = true, example = "17",
            schema = @Schema(type = SchemaType.STRING)),
        @Parameter(
            name = "heapSize", in = ParameterIn.QUERY,
            description = "The heap size of the system",
            required = true, example = "1048576",
            schema = @Schema(type = SchemaType.NUMBER)),
    })
    @Operation(
        summary = "Add system",
        description = "Add a system and its data to the inventory.",
        operationId = "addSystem"
    )
    public Response addSystem(
        @FormParam("hostname") String hostname,
        @FormParam("osName") String osName,
        @FormParam("javaVersion") String javaVersion,
        @FormParam("heapSize") Long heapSize) {

        logger.log(Level.WARNING, "addSystem called with hostname: {0}, osName: {1}, javaVersion: {2}, heapSize: {3}",
            new Object[]{hostname, osName, javaVersion, heapSize});
        if (hostname == null || hostname.isEmpty()) {
            return fail("Hostname is required.");
        }
        SystemData s = inventory.getSystem(hostname);
        if (s != null) {
            return fail(hostname + " already exists.");
        }
        inventory.add(hostname, osName, javaVersion, heapSize);
        return success(hostname + " was added.");
    }

    @PUT
    @Path("/{hostname}")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    @APIResponses(value = {
        @APIResponse(responseCode = "200",
            description = "Successfully updated system"),
        @APIResponse(responseCode = "400",
           description =
               "Unable to update because the system does not exist in the inventory.")
    })
    @Parameters(value = {
        @Parameter(
            name = "hostname", in = ParameterIn.PATH,
            description = "The hostname of the system",
            required = true, example = "localhost",
            schema = @Schema(type = SchemaType.STRING)),
        @Parameter(
            name = "osName", in = ParameterIn.QUERY,
            description = "The operating system of the system",
            required = true, example = "linux",
            schema = @Schema(type = SchemaType.STRING)),
        @Parameter(
            name = "javaVersion", in = ParameterIn.QUERY,
            description = "The Java version of the system",
            required = true, example = "17",
            schema = @Schema(type = SchemaType.STRING)),
        @Parameter(
            name = "heapSize", in = ParameterIn.QUERY,
            description = "The heap size of the system",
            required = true, example = "1048576",
            schema = @Schema(type = SchemaType.NUMBER)),
    })
    @Operation(
        summary = "Update system",
        description = "Update a system and its data on the inventory.",
        operationId = "updateSystem"
    )
    public Response updateSystem(
        @PathParam("hostname") String hostname,
        @FormParam("osName") String osName,
        @FormParam("javaVersion") String javaVersion,
        @FormParam("heapSize") Long heapSize) {


        logger.log(Level.INFO, "updateSystem called with hostname: {0}, osName: {1}, javaVersion: {2}, heapSize: {3}",
            new Object[]{hostname, osName, javaVersion, heapSize});
        SystemData s = inventory.getSystem(hostname);
        if (s == null) {
            return fail(hostname + " does not exists.");
        }
        s.setOsName(osName);
        s.setJavaVersion(javaVersion);
        s.setHeapSize(heapSize);
        inventory.update(s);
        return success(hostname + " was updated.");
    }

    @DELETE
    @Path("/{hostname}")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    @APIResponses(value = {
        @APIResponse(responseCode = "200",
            description = "Successfully deleted the system from inventory"),
        @APIResponse(responseCode = "400",
            description =
                "Unable to delete because the system does not exist in the inventory")
    })
    @Parameter(
        name = "hostname", in = ParameterIn.PATH,
        description = "The hostname of the system",
        required = true, example = "localhost",
        schema = @Schema(type = SchemaType.STRING)
    )
    @Operation(
        summary = "Remove system",
        description = "Removes a system from the inventory.",
        operationId = "removeSystem"
    )
    public Response removeSystem(@PathParam("hostname") String hostname) {
        logger.info("removeSystem called with hostname: " + hostname);
        SystemData s = inventory.getSystem(hostname);
        if (s != null) {
            inventory.removeSystem(s);
            return success(hostname + " was removed.");
        } else {
            return fail(hostname + " does not exists.");
        }
    }

}