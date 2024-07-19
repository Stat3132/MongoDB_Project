package View;
import UTIL.Console;

import java.sql.SQLOutput;


public class ViewPerson {
    //startup  create update add delete view

    public int startUp(){
        Console.write("Hello, Welcome to our Person Database", Console.TextColor.PURPLE);
        return Console.getIntInput("""
                1. Add a Person
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









}
