package eu.nabahilfe.webapp.members;

import java.time.LocalDate;

public class MemberBirthdayForm {

    private final String firstName;
    private final String lastName;
    private final LocalDate birthdate;
    private final int age;

    public MemberBirthdayForm(String firstName, String lastName, LocalDate birthdate, int age) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthdate = birthdate;
        this.age = age;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public LocalDate getBirthdate() {
        return birthdate;
    }

    public int getAge() {
        return age;
    }
}
