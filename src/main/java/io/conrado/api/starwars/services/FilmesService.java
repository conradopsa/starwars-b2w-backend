package io.conrado.api.starwars.services;

import java.io.StringReader;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import  javax.ws.rs.core.Response;

public class FilmesService {
    private final String SW_API_PLANETS_URL = "https://swapi.dev/api/planets/";
    private Client client;

    public FilmesService() {
        client = ClientBuilder.newClient();
    }

    public Integer countMoviesByName(String name) {
        JsonObject planet = getResponseJson(name);

        if (planet == null)
            return 0;

        JsonArray results = planet.getJsonArray("results");

        if (results.size() == 0)
            return 0;

        Integer count = results
            .get(0).asJsonObject()
            .getJsonArray("films").size();

        return count;
    }

    private JsonObject getResponseJson(String name) {
        Response res = client.target(SW_API_PLANETS_URL)
            .queryParam("search", name)    
            .request(MediaType.APPLICATION_JSON)
            .header("content-type", "application/json")
            .get();
            
        JsonReader jsonReader = Json.createReader(
            new StringReader(res.readEntity(String.class)));
        
        JsonObject resJson = jsonReader.readObject();

        jsonReader.close();
        
        return resJson;
    }
}
