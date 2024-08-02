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

         Driver driver = GraphDatabase.driver(dbUri, AuthTokens.basic(dbUser, dbPassword), config);
        try  {
            driver.verifyConnectivity();
            System.out.println("Connection established.");
        } catch (Exception e) {
            System.out.println("Error connecting to database: " + e);
            return null;
        }
        return driver;
    }

    public void addIntoNeo4J(Person person) {
        try (Session session = driver.session(SessionConfig.builder().withDatabase("neo4j").build())) {
            String createPersonQuery = "CREATE (p:Person {ID: $id, firstName: $firstName, lastName: $lastName, hireYear: $hireYear})";
            session.run(createPersonQuery, Values.parameters("id", person.getID(),
                    "firstName", person.getFirstName(),
                    "lastName", person.getLastName(),
                    "hireYear", person.getHireYear()));
        }
    }
    public void deleteFromNeo4J(Person person) {
      try(Session session = driver.session(SessionConfig.builder().withDatabase("neo4j").build())){
          String deletingPerson = "Match (p:Person) where p.ID = $id DELETE p";
          session.run(deletingPerson, Values.parameters("id", person.getID()));
      }
    }
    public void updateFromNeo4J(){

    }
    public void readFromNeo4J(){

    }

    public Driver getDriver() {
        return driver;
    }



}
