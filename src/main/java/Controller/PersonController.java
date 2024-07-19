package Controller;

import UTIL.Console;
import Model.Person;
import View.ViewPerson;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.List;

public class PersonController {
    private static final String PEOPLE_DATA = Paths.get("src/main/java/long").toString();
    private static final String PEOPLE_SERIALIZED = Paths.get("long serialized").toString();
    private static List<String> peopleList;
    private static int currentId = 0;
    private static Map<Integer, Person> readPersonDict = new HashMap<>();
    private static Map<String, List<Person>> lastName = new HashMap<>();
    ViewPerson menu = new ViewPerson();

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

        if (Files.exists(Paths.get(PEOPLE_SERIALIZED))) {
            Files.walk(Paths.get(PEOPLE_SERIALIZED))
                    .filter(Files::isRegularFile)
                    .map(Path::toFile)
                    .forEach(File::delete);
        }
        Files.createDirectories(Paths.get(PEOPLE_SERIALIZED));

        peopleListRead();
        while (true) {
            switch (menu.startUp()){
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
                    break;
            }
        }
    }

    private static void peopleListRead() throws IOException, ClassNotFoundException {
        long startTime = System.nanoTime();
        for (String people : peopleList) {
            try (BufferedReader br = new BufferedReader(new FileReader(PEOPLE_DATA + "/" + people))) {
                String peopleRead = br.readLine();
                String[] personStrip = peopleRead.split(",");
                int ID = Integer.parseInt(personStrip[0].strip());
                String newFName = personStrip[1].strip();
                String newLName = personStrip[2].strip();
                String newHireYear = personStrip[3].strip();
                Person newPerson = new Person(ID, newFName, newLName, newHireYear);
                readPersonDict.put(currentId, newPerson);
                if (lastName.containsKey(newLName)) {
                    if (lastName.get(newLName) != null) {
                        ((List<Person>)lastName.get(newLName)).add(newPerson);
                    } else {
                        List<Person> tempList = new ArrayList<>();
                        tempList.add(lastName.get(newLName).get(currentId));
                        tempList.add(newPerson);
                        lastName.put(newLName, tempList);
                    }
                } else {
                    lastName.put(newLName, Collections.singletonList(newPerson));
                }
                currentId = ID;
                try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(PEOPLE_SERIALIZED + "/" + ID + ".ppkl"))) {
                    oos.writeObject(newPerson);
                }
            }
        }
        long endTime = System.nanoTime();
        System.out.println(String.format("Run Time of Reading the List: %.6f seconds", (endTime - startTime) / 1_000_000_000.0));
    }

    private static void addPeople() throws IOException {
        currentId++;
        long startTime = System.nanoTime();
        while (true) {
            String newPerson = Console.getStringInput("Enter First Name, Last Name and Hire date separated by commas: ");
            String[] personInfo = newPerson.split(",");
            String firstName = personInfo[0].strip();
            String lastName = personInfo[1].strip();
            String hireYear = personInfo[2].strip();
            if (!(2 <= firstName.length() && firstName.length() <= 16 && !firstName.matches("\\d+"))) {
                System.out.println("First Name not within character limit or is digit. Limit = 16");
                continue;
            }
            if (!(2 <= lastName.length() && lastName.length() <= 16 && !lastName.matches("\\d+"))) {
                System.out.println("Last Name not within character limit or is digit. Limit = 16");
                continue;
            }
            if (!(hireYear.matches("\\d{4}") && Integer.parseInt(hireYear) >= 1900 && Integer.parseInt(hireYear) <= 2023)) {
                System.out.println("Invalid Year");
                continue;
            }
            Person newPersonObj = new Person(currentId, firstName, lastName, hireYear);
            readPersonDict.put(currentId, newPersonObj);
            System.out.println(currentId + " " + firstName + " " + lastName + " " + hireYear);
            String writtenPerson = currentId + ", " + firstName + ", " + lastName + ", " + hireYear;
            try (PrintWriter pw = new PrintWriter(new FileWriter(PEOPLE_DATA + "/" + currentId + ".txt"))) {
                pw.println(writtenPerson);
            }
            currentId++;
            break;
        }
        long endTime = System.nanoTime();
        System.out.println(String.format("Run Time of Adding a New Person: %.6f seconds", (endTime - startTime) / 1_000_000_000.0));
    }

    private static void deletePeople() throws IOException {
        long startTime = System.nanoTime();
        int userSelectedID = Console.getUserInt("Enter user ID to delete: ", true);
        for (Person person : readPersonDict.values()) {
            if (userSelectedID == person.getID()) {
                System.out.println(person.getID() + " " + person.getFirstName() + " " + person.getLastName() + " " + person.getHireYear());
                readPersonDict.remove(person.getID());
                Files.deleteIfExists(Paths.get(PEOPLE_DATA + "/" + userSelectedID + ".txt"));
                System.out.println("File has been deleted");
                break;
            }
        }
        long endTime = System.nanoTime();
        System.out.println(String.format("Run Time of Deleting a Person: %.6f seconds", (endTime - startTime) / 1_000_000_000.0));
    }
    private static void updatePeople() throws IOException, ClassNotFoundException {
        long startTime = System.nanoTime();
        int userSelectedID = Console.getUserInt("Enter user ID to delete: ", true);
        for (Person person : readPersonDict.values()) {
            if (userSelectedID == person.getID()) {
                System.out.println(person.getID() + " " + person.getFirstName() + " " + person.getLastName() + " " + person.getHireYear());
                update(person, userSelectedID);
                break;
            }
            if (userSelectedID > readPersonDict.size()) {
                System.out.println("NO USER FOUND");
            }
        }
        long endTime = System.nanoTime();
        System.out.println(String.format("Run Time of UPDATING: %.6f seconds", (endTime - startTime) / 1_000_000_000.0));
    }

    private static void update(Person person, int ID) throws IOException {
        while (true) {
            System.out.println(" 1. Change Firstname\n" +
                    " 2. Change Lastname\n" +
                    " 3. Change Hire Year\n" +
                    "*Person ID CAN NOT be Updated*");
            System.out.print("Enter choice: ");
            int choice = Console.getUserInt("Enter choice: ", true);
            if (choice == 1) {
                String userNewFirstName = Console.getUserStr("Enter new first name: ",true);
                if (!(2 <= userNewFirstName.length() && userNewFirstName.length() <= 16 && !userNewFirstName.matches("\\d+"))) {
                    System.out.println("First Name not within character limit or is digit. Limit = 16");
                    continue;
                } else {
                    person.setFirstName(userNewFirstName);
                }
            } else if (choice == 2) {
                String userNewLastName = Console.getUserStr("Enter new last name: ",true);
                if (!(2 <= userNewLastName.length() && userNewLastName.length() <= 16 && !userNewLastName.matches("\\d+"))) {
                    System.out.println("Last Name not within character limit or is digit. Limit = 16");
                    continue;
                } else {
                    person.setLastName(userNewLastName);
                }
            } else if (choice == 3) {
                String newHireYear = Console.getUserStr("Enter new hire year: ",true);
                if (!(newHireYear.matches("\\d{4}") && Integer.parseInt(newHireYear) >= 1900 && Integer.parseInt(newHireYear) <= 2023)) {
                    System.out.println("Invalid Year");
                    continue;
                } else {
                    person.setHireYear(newHireYear);
                }
            }
            String writtenPerson = person.getID() + ", " + person.getFirstName() + ", " + person.getLastName() + ", " + person.getHireYear();
            try (PrintWriter pw = new PrintWriter(new FileWriter(PEOPLE_DATA + "/" + ID + ".txt"))) {
                pw.println(writtenPerson);
            }
            break;
        }
    }

    private static void viewPerson() throws IOException, ClassNotFoundException {
        long startTime = System.nanoTime();
        String userSelectedInfo = Console.getUserStr("Enter user ID or last name to view contents: ",true);
        for (Person person : readPersonDict.values()) {
            if (userSelectedInfo.matches("\\d+")) {
                if (Integer.parseInt(userSelectedInfo) == person.getID()) {
                    System.out.println(person.getID() + " " + person.getFirstName() + " " + person.getLastName() + " " + person.getHireYear());
                    break;
                }
            } else {
                if (lastName.containsKey(userSelectedInfo.toUpperCase())) {
                    for (Person p : lastName.get(userSelectedInfo.toUpperCase())) {
                        System.out.println(p.getID() + " " + p.getFirstName() + " " + p.getLastName() + " " + p.getHireYear());
                    }
                    break;
                }
            }
        }
        long endTime = System.nanoTime();
        System.out.println(String.format("Run Time of Viewing a Person: %.6f seconds", (endTime - startTime) / 1_000_000_000.0));
    }
    private static void viewAllPeople() throws IOException, ClassNotFoundException {
        long startTime = System.nanoTime();
        for (Person person : readPersonDict.values()) {
            System.out.println(person.getID() + " " + person.getFirstName() + " " + person.getLastName() + " " + person.getHireYear());
        }
        long endTime = System.nanoTime();
        System.out.println(String.format("Run Time of Viewing All People: %.6f seconds", (endTime - startTime) / 1_000_000_000.0));
    }




}