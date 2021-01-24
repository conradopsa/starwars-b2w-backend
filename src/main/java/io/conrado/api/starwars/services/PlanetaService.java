package io.conrado.api.starwars.services;

import com.mongodb.BasicDBObject;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.IndexOptions;

import org.bson.Document;
import org.bson.conversions.Bson;

import io.conrado.api.starwars.config.MongoConfig;
import io.conrado.api.starwars.models.Planeta;
import io.conrado.api.starwars.pojo.RequestPlaneta;
import io.conrado.api.starwars.pojo.ResponsePlaneta;

import static io.conrado.api.starwars.utils.IteratorsUtils.toList;
import static com.mongodb.client.model.Filters.*;

import java.util.List;
import java.util.stream.Collectors;

public class PlanetaService {

    public final String DATABASE = System.getenv("MONGO_DB");
    public final String COLLECTION = "planets";

    private FilmesService filmesService;

    public PlanetaService() {
        try {
            filmesService = new FilmesService();
        } catch (Exception ex) {
            ex.printStackTrace(System.err);
        }
    }

    public List<ResponsePlaneta> queryPlanetas(Bson filter) throws Exception {

        try (MongoClient mongoClient = MongoConfig.getMongoClient()) {

            MongoDatabase db = mongoClient.getDatabase(DATABASE);
            MongoCollection<Planeta> planetsCollection = db.getCollection(COLLECTION, Planeta.class);

            List<Planeta> listModels;

            if (filter != null)
                listModels = toList(planetsCollection.find(filter));
            else
                listModels = toList(planetsCollection.find());

            return listModels.stream()
                .map(model -> createResponse(model))
                .collect(Collectors.toList());
        }
    }

    public ResponsePlaneta createResponse(Planeta model) {
        ResponsePlaneta response = new ResponsePlaneta();
        
        response.setClima(model.getClima());
        response.setIdPlaneta(model.getIdPlaneta());
        response.setNome(model.getNome());
        response.setTerreno(model.getTerreno());

        if (filmesService != null)
            response.setQuantidadeFilmes(filmesService.countMoviesByName(model.getNome()));

        return response;
    }

    public Planeta insertPlaneta(RequestPlaneta requestPlaneta) throws Exception {

        try (MongoClient mongoClient = MongoConfig.getMongoClient()) {

            MongoDatabase db = mongoClient.getDatabase(DATABASE);
            MongoCollection<Planeta> planetsCollection = db.getCollection(COLLECTION, Planeta.class);
            setUniqueFields(planetsCollection);
            
            Planeta planeta = mount(requestPlaneta);
            planetsCollection.insertOne(planeta);

            return planeta;

        }

    }

    public Planeta deletePlanetas(Integer idPlaneta) throws Exception {

        try (MongoClient mongoClient = MongoConfig.getMongoClient()) {

            MongoDatabase db = mongoClient.getDatabase(DATABASE);
            MongoCollection<Planeta> planetsCollection = db.getCollection(COLLECTION, Planeta.class);

            return planetsCollection.findOneAndDelete(eq("idPlaneta", idPlaneta));
        }
    }

    private void setUniqueFields(MongoCollection<Planeta> planetsCollection) {
        IndexOptions indexUnique = new IndexOptions().unique(true);
        
        planetsCollection.createIndex(new BasicDBObject("idPlaneta", 1), indexUnique);
        planetsCollection.createIndex(new BasicDBObject("nome", 1), indexUnique);        
    }

    /**
     * Find Max ID and increment one
     */
    private Integer nextSequenceID() {
        try (MongoClient mongoClient = MongoConfig.getMongoClient()) {

            MongoDatabase db = mongoClient.getDatabase(DATABASE);
            MongoCollection<Planeta> planetsCollection = db.getCollection(COLLECTION, Planeta.class);
            
            FindIterable<Planeta> it = planetsCollection.find();

            if (toList(it).size() == 0)
                return 0;

            return planetsCollection.find()
                .sort(new Document("idPlaneta", -1))
                .first().getIdPlaneta() + 1;
        }
    } 

    private Planeta mount(RequestPlaneta requestPlaneta){
        Planeta p = new Planeta();

        p.setIdPlaneta(nextSequenceID());
        p.setClima(requestPlaneta.getClima());
        p.setNome(requestPlaneta.getNome());
        p.setTerreno(requestPlaneta.getTerreno());
        
        return p;
    }

    
    
}
