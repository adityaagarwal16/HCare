package com.hcare.homeopathy.hcare.Consultations;

public class AllChatsObject {

    public String name;
    public String image;

    public AllChatsObject() {

    }

    public AllChatsObject(String name, String image) {
        this.name = name;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}


