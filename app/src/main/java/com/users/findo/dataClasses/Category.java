package com.users.findo.dataClasses;

public class Category {
    String Name;
    String link;

    public Category(){

    }

    public Category(String name, String link) {
        Name = name;
        this.link = link;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
