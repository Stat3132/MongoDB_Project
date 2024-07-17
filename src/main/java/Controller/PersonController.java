package Controller;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class PersonController {
    private static final String PEOPLE_DATA = Paths.get("long").toString();
    private static final String PEOPLE_SERIALIZED = Paths.get("long serialized").toString();
    private static List<String> peopleList;
    private static int currentId = 0;
    private static Map<Integer, Person> readPersonDict = new HashMap<>();
    private static Map<String, List<Person>> lastName = new HashMap<>();

    static class Person implements Serializable {
        private int ID;
        private String firstName;
        private String lastName;
        private int hireYear;

        public Person(int ID, String firstName, String lastName, int hireYear) {
            this.ID = ID;
            this.firstName = firstName;
            this.lastName = lastName;
            this.hireYear = hireYear;
        }

        public void update() {
            Scanner scanner = new Scanner(System.in);
            while (true) {
                System.out.println(" 1.Change Firstname\n 2.Change Lastname\n 3.Change Hire Year\n *Person ID CAN NOT be Updated*");
                System.out.print("Enter choice: ");
                int intChoice = scanner.nextInt();
                scanner.nextLine(); // Consume newline

                if (intChoice == 1) {
                    System.out.print("Enter new first name: ");
                    String userNewFirstName = scanner.nextLine();
                    if (userNewFirstName.isEmpty() || userNewFirstName.length() > 16 || userNewFirstName.length() < 2 || userNewFirstName.matches("\\d+")) {
                        System.out.println("First Name not within character limit or is digit. Limit = 16");
                        continue;
                    } else {
                        this.firstName = userNewFirstName;
                    }
                }
                if (intChoice == 2) {
                    System.out.print("Enter new last name: ");
                    String userNewLastName = scanner.nextLine();
                    if (userNewLastName.isEmpty() || userNewLastName.length() > 16 || userNewLastName.length() < 2 || userNewLastName.matches("\\d+")) {
                        System.out.println("Last Name not within character limit or is digit. Limit = 16");
                        continue;
                    } else {
                        this.lastName = userNewLastName;
                    }
                }
                if (intChoice == 3) {
                    System.out.print("Enter new hire year: ");
                    String newHireYear = scanner.nextLine();
                    if (newHireYear.isEmpty() || newHireYear.length() > 4 || !newHireYear.matches("\\d+") ||
                            Integer.parseInt(newHireYear) < 1900 || Integer.parseInt(newHireYear) > 2023) {
                        System.out.println("Invalid Year");
                        continue;
                    } else {
                        this.hireYear = Integer.parseInt(newHireYear);
                    }
                }
                String writtenPerson = String.format("%d, %s, %s, %d", ID, firstName, lastName, hireYear);
                try {
                    Files.write(Paths.get(PEOPLE_DATA, ID + ".txt"), writtenPerson.getBytes());
                    System.out.printf("%d, %s, %s, %d%n", ID, firstName, lastName, hireYear);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            }
        }
    }

    public static void peopleListRead() {
        long startTime = System.nanoTime();
        for (String people : peopleList) {
            try {
                List<String> lines = Files.readAllLines(Paths.get(PEOPLE_DATA, people));
                String[] personStrip = lines.get(0).split(",");
                int ID = Integer.parseInt(personStrip[0].trim());
                String newFName = personStrip[1].trim();
                String newLName = personStrip[2].trim();
                int newHireYear = Integer.parseInt(personStrip[3].trim());
                Person newPerson = new Person(ID, newFName, newLName, newHireYear);
                readPersonDict.put(currentId, newPerson);

                lastName.computeIfAbsent(newLName, k -> new ArrayList<>()).add(newPerson);

                currentId = ID;
                try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(Paths.get(PEOPLE_SERIALIZED, ID + ".ppkl").toFile()))) {
                    oos.writeObject(newPerson);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        long endTime = System.nanoTime();
        System.out.printf("Run Time of Read: %.6f seconds%n", (endTime - startTime) / 1e9);
    }
}