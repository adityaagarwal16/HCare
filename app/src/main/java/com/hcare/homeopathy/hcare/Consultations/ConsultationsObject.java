package com.hcare.homeopathy.hcare.Consultations;

public class ConsultationsObject {

    public String name;
    public String image;
    public String thumb_image;

    public ConsultationsObject() {

    }

    public ConsultationsObject(String name, String image, String thumb_image) {
        this.name = name;
        this.image = image;
        this.thumb_image = thumb_image;
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


