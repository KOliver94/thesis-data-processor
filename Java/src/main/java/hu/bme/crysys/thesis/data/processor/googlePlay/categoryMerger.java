package hu.bme.crysys.thesis.data.processor.googlePlay;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoIterable;
import hu.bme.crysys.thesis.data.processor.adapter.mongoDb;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

public class categoryMerger {

    static mongoDb adapter = new mongoDb();

    public static void merge(String databaseName) {

        MongoDatabase db = adapter.connectDb(databaseName);
        MongoIterable<String> collectionNames = db.listCollectionNames();
        MongoCollection<Document> mergedCollection = adapter.getCollection(db, "_MERGED");

        for (final String name : collectionNames){
            MongoCollection<Document> collection = adapter.getCollection(db, name);
            List<Document> apps = collection.find().into(new ArrayList<Document>());
            mergedCollection.insertMany(apps);
        }
    }
}
