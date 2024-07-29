package Controller;
import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;
import org.neo4j.driver.Session;

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

    public void addIntoNeo4J(){

    }
    public void deleteFromNeo4J() {

    }
    public void updateFromNeo4J(){

    }
    public void readFromNeo4J(){

    }



}
