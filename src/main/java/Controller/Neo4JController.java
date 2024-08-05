package Controller;
import Model.Person;
import org.neo4j.driver.*;

import java.io.*;
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
    public void setUpRelationships(){
        //builds session to allow querying onto the database
        try (Session session = driver.session(SessionConfig.builder().withDatabase("neo4j").build())) {
            String filepath = "data/friendships.csv"; //friendship info
            BufferedReader reader = new BufferedReader(new FileReader(filepath)); //reader to read info
            reader.readLine(); //reads the header so they are not used

            //while loop that reads file, parses to int, then creates the query to create the relationship
            while (reader.ready()){
                String line = reader.readLine();
                int pid;
                int friendshipID;

                //need to parse ID to int because it won't query as a string
                try {
                    pid = Integer.parseInt(line.split(",")[0]);
                    friendshipID = Integer.parseInt(line.split(",")[1]);
                }catch(NumberFormatException e){
                    System.out.println("Invalid ID format");
                    continue;
                }

                //build the query
                StringBuilder relationshipQuery = new StringBuilder();
                relationshipQuery.append("Match(a:Person {ID:$id1}) ");
                relationshipQuery.append("Match(b:Person {ID:$id2}) ");
                relationshipQuery.append("Merge(a)-[:FRIENDS_WITH]->(b)");

                //run the query, adding in the ids to there preselected spot
                session.run(relationshipQuery.toString(),
                        Values.parameters("id1", pid, "id2", friendshipID));
            }
        } catch (IOException ignore){

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
