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

    public JsonObject searchPlanet(String name) {

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

    public List<JsonObject> getAllPages() {
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

    /*
    
    {
    "count": 60, 
    "next": "http://swapi.dev/api/planets/?page=2", 
    "previous": null, 
    "results": [
        {
            "name": "Tatooine", 
            "rotation_period": "23", 
            "orbital_period": "304", 
            "diameter": "10465", 
            "climate": "arid", 
            "gravity": "1 standard", 
            "terrain": "desert", 
            "surface_water": "1", 
            "population": "200000", 
            "residents": [
                "http://swapi.dev/api/people/1/", 
                "http://swapi.dev/api/people/2/", 
                "http://swapi.dev/api/people/4/", 
                "http://swapi.dev/api/people/6/", 
                "http://swapi.dev/api/people/7/", 
                "http://swapi.dev/api/people/8/", 
                "http://swapi.dev/api/people/9/", 
                "http://swapi.dev/api/people/11/", 
                "http://swapi.dev/api/people/43/", 
                "http://swapi.dev/api/people/62/"
            ], 
            "films": [
                "http://swapi.dev/api/films/1/", 
                "http://swapi.dev/api/films/3/", 
                "http://swapi.dev/api/films/4/", 
                "http://swapi.dev/api/films/5/", 
                "http://swapi.dev/api/films/6/"
            ], 
            "created": "2014-12-09T13:50:49.641000Z", 
            "edited": "2014-12-20T20:58:18.411000Z", 
            "url": "http://swapi.dev/api/planets/1/"
        }, 
    */
}
