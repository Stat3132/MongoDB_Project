package Controller;
import Model.Person;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.ServerApi;
import com.mongodb.ServerApiVersion;
import com.mongodb.client.*;
import org.bson.Document;

import javax.print.Doc;
import java.util.List;

public class MongoController {
    MongoClient mongoClient;
    MongoDatabase database;
    MongoCollection<Document> collection;

    public void mongoControl() {
        mongoClient = connectToMongo();
        database = mongoClient.getDatabase("EmployeeDatabase");
        collection = database.getCollection("EmployeeCollection");

    }
    public MongoClient connectToMongo(){
        String connectionString = "mongodb://localhost:27017/";
        ServerApi serverApi = ServerApi.builder()
                .version(ServerApiVersion.V1)
                .build();
        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(new ConnectionString(connectionString))
                .serverApi(serverApi)
                .build();

        MongoClient mongoClient = MongoClients.create(settings);
        MongoDatabase database = mongoClient.getDatabase("EmployeeDatabase");
        database.runCommand(new Document("ping", 1));
        System.out.println("Connected to MongoDB!");
        return mongoClient;
    }


    public void addEmployeeToDataBase(Person person){

        Document document = new Document("ID", person.getID())
                .append("First name", person.getFirstName())
                .append("Last name", person.getLastName())
                .append("Hire year", person.getHireYear());
        collection.insertOne(document);
    }

    public void deleteEmployeeFromDatabase(Person person){
        Document document = new Document("ID", person.getID());
        collection.deleteOne(document);
    }

    public void updateRecord(Person person){
        Document document = new Document("ID", person.getID());
        Document update = new Document("$set", new Document()
                .append("First name", person.getFirstName())
                .append("Last name", person.getLastName())
                .append("Hire year", person.getHireYear()));
        collection.updateOne(document, update);
    }

    public void viewEmployee(Person person){
        Document filter = new Document("ID", person.getID());
        for (Document doc : collection.find(filter)) {
            System.out.println(doc.toJson());
        }
    }
    public void closeMongo(){
        mongoClient.close();
    }
}
