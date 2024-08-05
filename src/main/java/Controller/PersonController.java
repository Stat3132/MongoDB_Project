package Controller;

import UTIL.Console;
import Model.Person;
import View.ViewPerson;
import org.bson.types.BSONTimestamp;
import org.neo4j.driver.Driver;
import org.neo4j.driver.Session;

import java.io.*;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class PersonController {
    private static final String PEOPLE_DATA = Paths.get("data/long").toString();
    private static List<String> peopleList;
    private static int currentId = 0;
    private static Map<Integer, Person> readPersonHash = new HashMap<>();
    private static Map<String, List<Person>> lastName = new HashMap<>();

    //MONGO COMMANDS:

    //static MongoController mongoControl = new MongoController();

    static ViewPerson menu = new ViewPerson();

    //Neo4JController:

    static Neo4JController neo4j = new Neo4JController();
    Driver driver = neo4j.getDriver();

    //region startUp
    public void startUp() throws IOException, ClassNotFoundException {
        try {
            peopleList = Files.list(Paths.get(PEOPLE_DATA))
                    .filter(Files::isRegularFile)
                    .map(Path::getFileName)
                    .map(String::valueOf)
                    .sorted(Comparator.comparingInt(s -> Integer.parseInt(s.split("\\.")[0])))
                    .toList();

        } catch (IOException e){
            System.out.println("No files found");
        }
        peopleListRead();
        for (Person person: readPersonHash.values()) {
            try(Session session2 = driver.session()) {
                if (session2.run("MATCH (p:Person) RETURN p").list().size() != readPersonHash.size()) {
                    neo4j.addIntoNeo4J(person);
                } else {
                    System.out.println("ALL PEOPLE MOVED TO NEO");
                    break;
                }
            }
        }

        neo4j.setUpRelationships();
        //menu.allPeopleMovedToMongo();
        while (true) {
            switch (menu.startUpM()){
                case 1:
                    //add
                    addPeople();
                    break;
                case 2:
                    //delete
                    deletePeople();
                    break;
                case 3:
                    //update
                    updatePeople();
                    break;
                case 4:
                    //view a person
                    viewPerson();
                    break;
                case 5:
                    //view all
                    viewAllPeople();
                    break;
                default:
                    //exit
                    menu.goodbye();
                    return;
            }
        }
    }
    //endregion

    //region read people
    private static void peopleListRead() throws IOException, ClassNotFoundException {
        long startTime = System.currentTimeMillis();
        for (String people : peopleList) {
            Path filePath = Paths.get(PEOPLE_DATA + "/" + people);
            try (BufferedReader br = new BufferedReader(new FileReader(filePath.toFile()))) {
                String peopleRead = br.readLine();
                String[] personStrip = peopleRead.split(",");
                int ID = Integer.parseInt(personStrip[0].strip());
                String newFName = personStrip[1].strip();
                String newLName = personStrip[2].strip();
                String newHireYear = personStrip[3].strip();
                Person newPerson = new Person(ID, newFName, newLName, newHireYear);
                readPersonHash.put(currentId, newPerson);
                if (lastName.containsKey(newLName)) {
                    if (lastName.get(newLName).getClass() == ArrayList.class) {
                        lastName.get(newLName).add(newPerson);
                    } else {
                        List<Person> newLastName = new ArrayList<>();
                        newLastName.add(newPerson);
                        lastName.put(newLName, newLastName);

                    }
                } else {
                    lastName.put(newLName, Collections.singletonList(newPerson));
                }
                currentId = ID;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        long endTime = System.currentTimeMillis();
        long duration = (endTime - startTime)/ 1000;
        System.out.println("Time to read: " + duration+ " seconds");
    }
    //endregion

    //region add
    private static void addPeople() throws IOException {
        long startTime = System.currentTimeMillis();
        currentId++;
        while (true) {
            String newPerson = Console.getStringInput("Enter First Name, Last Name and Hire date separated by commas: ");
            String[] personInfo = newPerson.split(",");
            String firstName = personInfo[0].strip();
            String lastName = personInfo[1].strip();
            String hireYear = personInfo[2].strip();
            if (!(2 <= firstName.length() && firstName.length() <= 16 && !firstName.matches("\\d+"))) {
                menu.lengthWarning();
                continue;
            }
            if (!(2 <= lastName.length() && lastName.length() <= 16 && !lastName.matches("\\d+"))) {
                menu.lengthWarning();
                continue;
            }
            if (!(hireYear.matches("\\d{4}") && Integer.parseInt(hireYear) >= 1900 && Integer.parseInt(hireYear) <= 2023)) {
                menu.yearWarning();
                continue;
            }
            Person newPersonObj = new Person(currentId, firstName, lastName, hireYear);
            //mongoControl.addEmployeeToDataBase(newPersonObj);
            neo4j.addIntoNeo4J(newPersonObj);
            menu.personAddedToMongo();
            readPersonHash.put(currentId, newPersonObj);
            System.out.println(currentId + " " + firstName + " " + lastName + " " + hireYear);
            String writtenPerson = currentId + ", " + firstName + ", " + lastName + ", " + hireYear;
            try (PrintWriter pw = new PrintWriter(new FileWriter(PEOPLE_DATA + "/" + currentId + ".txt"))) {
                pw.println(writtenPerson);
            }
            currentId++;
            break;
        }
        long endTime = System.currentTimeMillis();
        long Duration = (endTime - startTime)/ 1000;
        System.out.println("Time to add: " + Duration + " seconds");

    }
    //endregion

    //region Delete
    private static void deletePeople() throws IOException {
        long startTime = System.currentTimeMillis();
        int userSelectedID = Console.getUserInt("Enter user ID to delete: ", true);
        for (Person person : readPersonHash.values()) {
            if (userSelectedID == person.getID()) {
                System.out.println(person.getID() + " " + person.getFirstName() + " " + person.getLastName() + " " + person.getHireYear());
                readPersonHash.remove(person.getID());
                neo4j.deleteFromNeo4J(person);
                //mongoControl.deleteEmployeeFromDatabase(person);
                menu.personRemovedFromMongo();
                Files.deleteIfExists(Paths.get(PEOPLE_DATA + "/" + userSelectedID + ".txt"));
                System.out.println("File has been deleted");
                break;
            }
        }
        long endTime = System.currentTimeMillis();
        long duration = (endTime - startTime)/ 1000;
        System.out.println("Time to delete: " + duration + " seconds");
    }
    //endregion

    //region update person
    private static void updatePeople() throws IOException, ClassNotFoundException {
        long startTime = System.currentTimeMillis();
        int userSelectedID = Console.getUserInt("Enter user ID to alter employee: ", true);
        for (Person person : readPersonHash.values()) {
            if (userSelectedID == person.getID()) {
                System.out.println(person.getID() + " " + person.getFirstName() + " " + person.getLastName() + " " + person.getHireYear());
                update(person, userSelectedID);
                break;
            }
            if (userSelectedID > readPersonHash.size()) {
                menu.updatePeopleWarning();
            }
        }
        long endTime = System.currentTimeMillis();
        long duration = (endTime - startTime)/ 1000;
        System.out.println("Time to update:" + duration+ " seconds");
    }
    //endregion

    //region update logic
    private static void update(Person person, int ID) throws IOException {
        while (true) {
            switch(menu.updateM()) {
                case 1:
                    String userNewFirstName = Console.getUserStr("Enter new first name: ", true);
                    if (!(2 <= userNewFirstName.length() && userNewFirstName.length() <= 16 && !userNewFirstName.matches("\\d+"))) {
                        menu.lengthWarning();
                        continue;
                    } else {
                        person.setFirstName(userNewFirstName);
                    }
                    break;
                case 2:
                    String userNewLastName = Console.getUserStr("Enter new last name: ", true);
                    if (!(2 <= userNewLastName.length() && userNewLastName.length() <= 16 && !userNewLastName.matches("\\d+"))) {
                        menu.lengthWarning();
                        continue;
                    } else {
                        person.setLastName(userNewLastName);
                    }
                    break;
                case 3:
                    String newHireYear = Console.getUserStr("Enter new hire year: ", true);
                    if (!(newHireYear.matches("\\d{4}") && Integer.parseInt(newHireYear) >= 1900 && Integer.parseInt(newHireYear) <= 2023)) {
                        menu.yearWarning();
                        continue;
                    } else {
                        person.setHireYear(newHireYear);
                    }
                }

            Person updatedPerson = new Person(person.getID(), person.getFirstName(), person.getLastName(), person.getHireYear());
            //mongoControl.updateRecord(updatedPerson);
            menu.personUpdatedIntoMongo();
            String writtenPerson = person.getID() + ", " + person.getFirstName() + ", " + person.getLastName() + ", " + person.getHireYear();
            try (PrintWriter pw = new PrintWriter(new FileWriter(PEOPLE_DATA + "/" + ID + ".txt"))) {
                pw.println(writtenPerson);

            }
        }
    }
    //endregion

    //region view person
    private static void viewPerson() throws IOException, ClassNotFoundException {
        long startTime = System.currentTimeMillis();
        String userSelectedInfo = Console.getUserStr("Enter user ID or last name to view contents: ",true);
        for (Person person : readPersonHash.values()) {
            if (userSelectedInfo.matches("\\d+")) {
                if (Integer.parseInt(userSelectedInfo) == person.getID()) {
                    System.out.println(person.getID() + " " + person.getFirstName() + " " + person.getLastName() + " " + person.getHireYear());
                    //mongoControl.viewEmployee(person);
                    break;
                }
            } else {
                if (lastName.containsKey(userSelectedInfo.toUpperCase())) {
                    for (Person p : lastName.get(userSelectedInfo.toUpperCase())) {
                        System.out.println(p.getID() + " " + p.getFirstName() + " " + p.getLastName() + " " + p.getHireYear());
                        //mongoControl.viewEmployee(person);
                    }
                    break;
                }
            }
        }
        long endtime = System.currentTimeMillis();
        long duration = (endtime - startTime)/ 1000;
        System.out.println("Time to view: " + duration+ " seconds");
    }
    //endregion

    //region view all people
    private static void viewAllPeople() throws IOException, ClassNotFoundException {
        long startTime = System.currentTimeMillis();
        for (Person person : readPersonHash.values()) {
            System.out.println(person.getID() + " " + person.getFirstName() + " " + person.getLastName() + " " + person.getHireYear());
            //mongoControl.viewEmployee(person);
        }
        long endTime = System.currentTimeMillis();
        long duration = (endTime - startTime)/ 1000;
        System.out.println("Time to view all: " + duration + " seconds");
    }
    //endregion




}