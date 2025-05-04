package pt.dsi.dpi.rest;


import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import pt.dsi.dpi.rest.dal.DeptService;
import pt.dsi.dpi.rest.dal.Dept;

import java.util.List;
import java.util.logging.Logger;

@ApplicationScoped
@Path("/api/dept")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class DeptResource {
    private static final Logger logger = Logger.getLogger(DeptResource.class.getName());

    @Inject
    private DeptService myService;

    @POST
    public Response createDept(Dept entity) {
        logger.info("Creating new department: " + entity);
        myService.save(entity);
        return Response.status(Response.Status.CREATED).entity(entity).build();
    }

    @GET
    @Path("/{id}")
    public Response getDept(@PathParam("id") Long id) {
        logger.info("Fetching department with ID: " + id);
        Dept entity = myService.findById(id);
        if (entity != null) {
            return Response.ok(entity).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @GET
    public Response getAllDepts() {
        List<Dept> entities = myService.findAll();
        logger.info("Fetching all departments - count: " + entities.size());
        return Response.ok(entities).build();
    }

    @PUT
    @Path("/{id}")
    public Response updateDept(@PathParam("id") Long id, Dept updatedEntity) {
        logger.info("Updating department with ID: " + id);
        myService.update(id, updatedEntity);
        return Response.ok(updatedEntity).build();
    }

    @DELETE
    @Path("/{id}")
    public Response deleteDept(@PathParam("id") Long id) {
        logger.info("Deleting department with ID: " + id);
        myService.deleteById(id);
        return Response.noContent().build();
    }
}