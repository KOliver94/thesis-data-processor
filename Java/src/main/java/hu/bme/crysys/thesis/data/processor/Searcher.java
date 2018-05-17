package hu.bme.crysys.thesis.data.processor;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import hu.bme.crysys.thesis.data.processor.adapter.mongoDb;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

public class Searcher {
    static mongoDb adapter = new mongoDb();

    /* Return the different developers of Microsoft apps */
    public static ArrayList<String> microsoftDevelopers(String databaseName){
        MongoDatabase db = adapter.connectDb(databaseName);
        MongoCollection<Document> collection = adapter.getCollection(db, "apps");
        List<Document> apps = collection.find().into(new ArrayList<Document>());
        ArrayList<String> result = new ArrayList<String>();

        for(Document app : apps){
            String developer = ((Document)app.get("detail")).getString("developer");
            if (result.isEmpty() || !result.contains(developer)){
                result.add(developer);
            }
        }

        return result;
    }

    /* Count the cross developers between Microsoft and Android */
    public static int countCrossDevelopers(ArrayList<String> developers, String googleDatabaseName, String collectionName){
        MongoDatabase db = adapter.connectDb(googleDatabaseName);
        MongoCollection<Document> collection = adapter.getCollection(db, collectionName);
        List<Document> apps = collection.find().into(new ArrayList<Document>());
        int count = 0;

        for(String developer : developers){
            for(Document app : apps) {
                if (((Document)app.get("details")).getString("developer")
                        .contentEquals(developer)){
                    System.out.println(developer);
                    count++;
                    break;
                }
            }
        }

        System.out.println(count);
        return count;
    }
}
