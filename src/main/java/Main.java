import Controller.MongoController;
import Controller.Neo4JController;
import Controller.PersonController;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        Neo4JController neo4j = new Neo4JController();
        PersonController personController = new PersonController();
        personController.startUp();
        MongoController mongoController = new MongoController();
        mongoController.closeMongo();
    }
}
