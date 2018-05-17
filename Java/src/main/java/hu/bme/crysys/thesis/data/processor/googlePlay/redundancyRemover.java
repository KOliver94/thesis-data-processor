package hu.bme.crysys.thesis.data.processor.googlePlay;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import hu.bme.crysys.thesis.data.processor.adapter.mongoDb;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

public class redundancyRemover {

    static mongoDb adapter = new mongoDb();

    public static void run(String databaseName, String collectionName) {
        MongoDatabase db = adapter.connectDb(databaseName);
        MongoCollection<Document> collection = adapter.getCollection(db, collectionName);
        MongoCollection<Document> filteredCollection = adapter.getCollection(db, collectionName+"_filtered");

        List<Document> apps = collection.find().into(new ArrayList<Document>());
        List<Document> filtered = (List<Document>)((ArrayList<Document>) apps).clone();
        int count = 0;

        for (int i = 0; i < apps.size(); i++) {
            for (int j = filtered.size()-1; j > i; j--){
                if (((Document) apps.get(i).get("details")).getString("appId")
                        .contentEquals(((Document) filtered.get(j).get("details")).getString("appId"))){
                    filtered.remove(j);
                    count++;
                }
            }
        }

        System.out.println(count + " item removed.");
        filteredCollection.insertMany(filtered);
        System.out.println("Data inserted!");
    }
}