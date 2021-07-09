package com.hcare.homeopathy.hcare.Consultations;

public class ConsultationsObject {

    public String name;
    public String image;

    public ConsultationsObject() {

    }

    public ConsultationsObject(String name, String image) {
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


