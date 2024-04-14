package mainPackage.models;


import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class Person {
    private int id;
    @NotEmpty
    @Size(min = 2, max = 50, message = "Name should have 2-50 characters")
    @Pattern(regexp = "^[A-Z][a-z]+ [A-Z][a-z]+ [A-Z][a-z]+$", message = "Example of name: Surname Name Patronymic ")
    private String fullName;



    @Min(value = 1800, message = "Date of birth should be equal or greater than 1800")
    private int dateOfBirth;

    public Person(int id, String fullName, int dateOfBirth) {
        this.id = id;
        this.fullName = fullName;
        this.dateOfBirth = dateOfBirth;
    }

    public Person() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public int getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(int dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    @Override
    public String toString() {
        return "Person{" +
                "id=" + id +
                ", fullName='" + fullName + '\'' +
                ", dateOfBirth=" + dateOfBirth +
                '}';
    }
}
