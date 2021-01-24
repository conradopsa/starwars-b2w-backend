package io.conrado.api.starwars.services;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonValue;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


public class FilmesService {
    private final String SW_API_PLANETS_URL = "http://swapi.dev/api/planets/";
    private Client client;
    private List<JsonObject> pages;

    public FilmesService() {
        client = ClientBuilder.newClient();

        pages = getAllPages();
    }

    public Integer countMoviesByName(String name) {
        JsonObject planet = searchPlanet(name);

        if (planet == null)
            return 0;

        Integer count = planet.getJsonArray("films").size();

        return count;
    }

    private JsonObject searchPlanet(String name) {

        JsonObject planet = null;
        for (JsonObject page : this.pages) {

            JsonArray results = page.getJsonArray("results");

            for (JsonValue result : results){
                JsonObject resultObj = result.asJsonObject();
                
                if (resultObj.getString("name").equalsIgnoreCase(name)){
                    planet = resultObj;
                    break;
                }
                   
            }

            if (planet != null)
                break;
        }

        return planet;
    }

    private List<JsonObject> getAllPages() {
        List<JsonObject> pages = new ArrayList<JsonObject>();
        
        String nextURL = SW_API_PLANETS_URL;
        do {
            Response response = getResponse(nextURL);

            if (response.getStatus() != 200)
                break;

            String strResponse = response.readEntity(String.class);
            
            JsonReader jsonReader = Json.createReader(new StringReader(strResponse));
            
            JsonObject page = jsonReader.readObject();
            nextURL = page.isNull("next") ? null : page.getString("next");

            jsonReader.close();

            pages.add(page);

        } while (nextURL != null);

        return pages;
    }

    private Response getResponse(String nextURL) {
        Response res = client.target(nextURL)
            .request(MediaType.APPLICATION_JSON)
            .header("content-type", "application/json")
            .get();

        if (res.getStatus() >= 300 && res.getStatus() < 400){
            res = client.target(res.getLocation())
            .request(MediaType.APPLICATION_JSON)
            .header("content-type", "application/json")
            .get();
        }
            
        
        return res;
    }
}
