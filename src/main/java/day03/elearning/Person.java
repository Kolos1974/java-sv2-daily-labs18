package day03.elearning;

import java.time.LocalDate;

public class Person {
    private String nameOfPerson;

    private LocalDate dateOfBirth;

    private int height;

    private double weight;

    public Person(String nameOfPerson, LocalDate dateOfBirth, int height, double weight) {
        this.nameOfPerson = nameOfPerson;
        this.dateOfBirth = dateOfBirth;
        this.height = height;
        this.weight = weight;
    }

    public String getNameOfPerson() {
        return nameOfPerson;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public int getHeight() {
        return height;
    }

    public double getWeight() {
        return weight;
    }
}
