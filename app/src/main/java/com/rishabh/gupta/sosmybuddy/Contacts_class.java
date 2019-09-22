package com.rishabh.gupta.sosmybuddy;

public class Contacts_class {

String name;
String number;

    public Contacts_class(String name, String number) {
        this.name = name;
        this.number = number;
    }

    public Contacts_class() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}
