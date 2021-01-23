package io.conrado.api.starwars.services;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.conversions.Bson;

import io.conrado.api.starwars.config.MongoConfig;
import io.conrado.api.starwars.models.Planeta;
import io.conrado.api.starwars.pojo.RequestPlaneta;

import static io.conrado.api.starwars.utils.IteratorsUtils.toList;
import static com.mongodb.client.model.Filters.*;

import java.util.List;

public class PlanetaService {

    public final String DATABASE = System.getenv("MONGO_DB");
    public final String COLLECTION = "planets";

    public List<Object> queryPlanetas(Bson filter) throws Exception {

        try (MongoClient mongoClient = MongoConfig.getMongoClient()) {

            MongoDatabase db = mongoClient.getDatabase(DATABASE);
            MongoCollection<Planeta> planetsCollection = db.getCollection(COLLECTION, Planeta.class);

            if (filter != null)
                return toList(planetsCollection.find(filter));
            else
                return toList(planetsCollection.find());
        }
    }

    public Planeta insertPlaneta(RequestPlaneta requestPlaneta) throws Exception {

        try (MongoClient mongoClient = MongoConfig.getMongoClient()) {

            MongoDatabase db = mongoClient.getDatabase(DATABASE);
            MongoCollection<Planeta> planetsCollection = db.getCollection(COLLECTION, Planeta.class);

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
