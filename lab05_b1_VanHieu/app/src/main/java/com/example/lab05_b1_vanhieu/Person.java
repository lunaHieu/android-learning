package com.example.lab05_b1_vanhieu;

public class Person {
    private int image;
    private String namePerson;

    // Constructor
    public Person(int image, String namePerson) {
        this.image = image;
        this.namePerson = namePerson;
    }

    // Getter for Image
    public int getImage() {
        return image;
    }

    // Setter for Image
    public void setImage(int image) {
        this.image = image;
    }

    // Getter for Name (Must match the call in MainActivity: .getNamePerson())
    public String getNamePerson() {
        return namePerson;
    }

    // Setter for Name
    public void setNamePerson(String namePerson) {
        this.namePerson = namePerson;
    }
}