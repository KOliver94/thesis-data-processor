package hu.bme.crysys.thesis.data.processor.adapter;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

public class mongoDb {
    public static MongoDatabase connectDb(String databaseName){
        MongoClient mongoClient = MongoClients.create();
        return mongoClient.getDatabase(databaseName);
    }

    public static MongoCollection<Document> getCollection(MongoDatabase database, String collectionName){
        return database.getCollection(collectionName);
    }
}
