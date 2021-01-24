package io.conrado.api.starwars.helpers;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

/**
 * Print and response helper for controllers
 */
public class ResponseException {

    public static Response ResponseException500(Exception ex) {
        ex.printStackTrace(System.err);
        return Response.status(Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).build();
    }
}
