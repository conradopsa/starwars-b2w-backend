package io.conrado.api.starwars.controllers;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.bson.conversions.Bson;

import io.conrado.api.starwars.models.Planeta;
import io.conrado.api.starwars.pojo.RequestPlaneta;
import io.conrado.api.starwars.services.PlanetaService;

import static com.mongodb.client.model.Filters.*;
import static io.conrado.api.starwars.helpers.ResponseException.*;

@Path("/planetas")
public class PlanetaController {

    private PlanetaService planetaService = new PlanetaService();

    @GET
    @Path("/{idPlaneta}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPlaneta(@PathParam("idPlaneta") Integer id) {

        try {
            return Response.ok(planetaService.queryPlanetas(eq("idPlaneta", id)).get(0)).build();
        } catch (Exception ex) {
            return ResponseException500(ex);
        }

    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPlanetas(@QueryParam("nome") String nome) {

        try {
            Bson filter = null;

            if (nome != null)
                filter = eq("nome", nome);

            return Response.ok(planetaService.queryPlanetas(filter)).build();
        } catch (Exception ex) {
            return ResponseException500(ex);
        }

    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response postPlaneta(RequestPlaneta requestPlaneta) {

        try {
            return Response.ok(planetaService.insertPlaneta(requestPlaneta)).build();
        } catch (Exception ex) {
            return ResponseException500(ex);
        }

    }

    @DELETE
    @Path("/{idPlaneta}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deletePlaneta(@PathParam("idPlaneta") Integer idPlaneta) {
        try {

            Planeta deleted = planetaService.deletePlanetas(idPlaneta);

            if (deleted == null)
                return Response.status(404).build();

            return Response.ok(deleted).build();
        } catch (Exception ex) {
            return ResponseException500(ex);
        }
    }

}
