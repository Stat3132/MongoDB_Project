import Controller.MongoController;
import Controller.PersonController;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        PersonController personController = new PersonController();
        personController.startUp();
        MongoController mongoController = new MongoController();
        mongoController.closeMongo();
    }
}
