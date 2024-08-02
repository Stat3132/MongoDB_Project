package Controller;
import Model.Person;
import org.neo4j.driver.*;

import java.util.Collections;

public class Neo4JController {
    Driver driver;


    public Neo4JController(){
        driver = buildConnection();

    }

    private Driver buildConnection(){
        final String dbUri = "neo4j://localhost:7687";
        final String dbUser = "neo4j";
        final String dbPassword = "password";

        try (var driver = GraphDatabase.driver(dbUri, AuthTokens.basic(dbUser, dbPassword))) {
            driver.verifyConnectivity();
            System.out.println("Connection established.");
        }
        return driver;
    }

    public void addIntoNeo4J(Person person) {
        try (Session session = driver.session()) {
            String personCypher = "CREATE (p:Person {ID: " + person.getID() + ", Name: " + person.getFirstName() + " " + person.getLastName() + ",HireYear: " + person.getHireYear() + "}";
            session.writeTransaction(tx -> { tx.run(personCypher, Collections.singletonMap("ID", person.getID()));

                return null;
            });
        }
    }
    public void deleteFromNeo4J() {

    }
    public void updateFromNeo4J(){

    }
    public void readFromNeo4J(){

    }



}
