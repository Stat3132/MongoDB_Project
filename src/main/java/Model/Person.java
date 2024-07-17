/**
 * @author cwatson
 * @createdOn 7/17/2024 at 10:39 AM
 * @projectName MongoDB_Project
 * @packageName Model;
 */
package Model;

public class Person{
    private int ID;
    private String firstName;
    private String lastName;
    private String hireYear;

    public Person(int ID, String firstName, String lastName, String hireYear) {
        this.ID = ID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.hireYear = hireYear;
    }

    // getters and setters
    public int getID() { return ID; }
    public void setID(int ID) { this.ID = ID; }
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public String getHireYear() { return hireYear; }
    public void setHireYear(String hireYear) { this.hireYear = hireYear; }
}