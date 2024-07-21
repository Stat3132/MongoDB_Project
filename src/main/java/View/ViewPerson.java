package View;
import UTIL.Console;

import java.sql.SQLOutput;


public class ViewPerson {
    //startup  create update add delete view

    public int startUp(){
        Console.write("Hello, Welcome to our Person Database", Console.TextColor.PURPLE);
        return Console.getIntInput("""
                \n1. Add a Person
                2. Delete a Person
                3. Update a Person
                4. View a Person
                5. View all People
                6. Exit
                """, 1,6, Console.TextColor.PURPLE);
    }

    public void goodbye(){
        System.out.println("Thank you for visiting! Goodbye");
    }

    //MONGO VIEW

    public void allPeopleMovedToMongo(){
        System.out.println(Console.getStringWithColor("All people imported to mongo", Console.TextColor.RED));
    }

    public void personAddedToMongo(){
        System.out.println(Console.getStringWithColor("Person added to mongo", Console.TextColor.RED));
    }
    public void personRemovedFromMongo(){
        System.out.println(Console.getStringWithColor("Person removed from mongo", Console.TextColor.RED));
    }
    public void personUpdatedIntoMongo(){
        System.out.println(Console.getStringWithColor("Person updated in mongo", Console.TextColor.RED));
    }









}
