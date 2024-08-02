package Controller;
import Model.Person;
import org.neo4j.driver.*;

import java.util.Collections;
import java.util.concurrent.TimeUnit;

public class Neo4JController {
    final Driver driver;


    public Neo4JController(){
        driver = buildConnection();

    }

    private Driver buildConnection(){
        final String dbUri = "neo4j://localhost:7687";
        final String dbUser = "neo4j";
        final String dbPassword = "personLibrary";
        Config config = Config.builder()
                .withMaxConnectionPoolSize(50)
                .withConnectionAcquisitionTimeout(60, TimeUnit.SECONDS)
                .withMaxTransactionRetryTime(15, TimeUnit.SECONDS)
                .build();


        try (var driver = GraphDatabase.driver(dbUri, AuthTokens.basic(dbUser, dbPassword), config)) {
            driver.verifyConnectivity();
            System.out.println("Connection established.");
            return driver;
        } catch (Exception e) {
            System.out.println("Error connecting to database: " + e);
            return null;
        }
    }

    public void addIntoNeo4J(Person person) {
        try (Session session = driver.session()) {
            String connectToDatabase = ":use People Library";
            String createPersonQuery = "CREATE (p:Person {ID: " + person.getID() + ", Name: " + person.getFirstName() + " " + person.getLastName() + ",HireYear: " + person.getHireYear() + "}";
            session.run(connectToDatabase);
            session.run(createPersonQuery);
        }
    }
    public void deleteFromNeo4J() {

    }
    public void updateFromNeo4J(){

    }
    public void readFromNeo4J(){

    }



}
