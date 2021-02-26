package ru.teacher.mybd;

public class Contact {
    private String firstName, secondName, phoneNumber;
    private long id;

    public Contact() {
        this.firstName = "";
        this.secondName = "";
        this.phoneNumber = "";
        this.id = 0;
    }

    public Contact(String firstName, String secondName, String phoneNumber) {
        this.firstName = firstName;
        this.secondName = secondName;
        this.phoneNumber = phoneNumber;
        this.id = -1;
    }

    public Contact(long id, String firstName, String secondName, String phoneNumber) {
        this.firstName = firstName;
        this.secondName = secondName;
        this.phoneNumber = phoneNumber;
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getSecondName() {
        return secondName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public String toString() {
        String temp = String.format("%5s\t%15s\t%8s\t%15s",id,phoneNumber,firstName,secondName);
        return temp;
    }
}
