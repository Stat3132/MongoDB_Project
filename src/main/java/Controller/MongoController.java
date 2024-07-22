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
        //Global variables for my database and collection
        database = mongoClient.getDatabase("EmployeeDatabase");
        collection = database.getCollection("EmployeeCollection");

    }
    public MongoClient connectToMongo(){
        //Connects to my local MongoDB server
        String connectionString = "mongodb://localhost:27017/";
        //API to be able to access server
        ServerApi serverApi = ServerApi.builder()
                .version(ServerApiVersion.V1)
                .build();
        //Setting up the connection and logic related to the connection
        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(new ConnectionString(connectionString))
                .serverApi(serverApi)
                .build();

        //Creating the mongo DB connection with these settings.
        MongoClient mongoClient = MongoClients.create(settings);
        //Solidifying the database I am trying to access
        MongoDatabase database = mongoClient.getDatabase("EmployeeDatabase");
        //Ping just to makes sure database is implemented correctly
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
