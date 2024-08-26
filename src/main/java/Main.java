import Controller.AWSDynamoDBController;
import Controller.MongoController;
import Controller.Neo4JController;
import Controller.PersonController;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        PersonController personController = new PersonController();
        personController.startUp();
        //Neo4JController neo4j = new Neo4JController();
        //AWSDynamoDBController awsDynamoDBController = new AWSDynamoDBController();
        //MongoController mongoController = new MongoController();
        //mongoController.closeMongo();

    }
}
