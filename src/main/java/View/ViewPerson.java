package View;
import UTIL.Console;


public class ViewPerson {
    //startup  create update add delete view

    public int startUpM(){
        Console.write("Hello, Welcome to our Person Database \n", Console.TextColor.PURPLE);
        return Console.getIntInput("""
                1. Add a Person
                2. Delete a Person
                3. Update a Person
                4. View a Person
                5. View all People
                6. Exit
                """, 1,7, Console.TextColor.PURPLE);
    }

    public void lengthWarning(){
        Console.write("\n First Name not within character limit or is digit. Limit = 16", Console.TextColor.RED);
    }

    public void yearWarning(){
        Console.write("\n Invalid Year. Year must be between 1900 - 2023", Console.TextColor.RED);
    }

    public void updatePeopleWarning(){
        Console.write("\n NO USER FOUND", Console.TextColor.RED);
    }

    public int updateM(){
        Console.write("What Attribute would you like to Update \n", Console.TextColor.PURPLE);
        return Console.getIntInput("""
                1. Change First Name
                2. Change Last Name
                3. Change Hire Year
                *Person ID CAN NOT be Updated*
                """, 1,3, Console.TextColor.PURPLE);
    }


    public void goodbye(){
        Console.write("Thank you for visiting! Goodbye", Console.TextColor.PURPLE);
    }



    //MONGO VIEW

    public void allPeopleMovedToRedis(){
        Console.write("All people imported to Redis! \n", Console.TextColor.GREEN);
    }
    public void personAddedtoAmazonDynamo(){
        Console.write("Person added to Amazon Dynamo \n", Console.TextColor.GREEN);
    }
    public void personDeletedFromAmazonDynamo(){
        Console.write("Person removed from Amazon Dynamo \n", Console.TextColor.GREEN);;
    }
    public void personUpdatedIntoAmazonDynamo(){
        Console.write("Person updated in Amazon Dynamo \n", Console.TextColor.GREEN);;
    }









}
