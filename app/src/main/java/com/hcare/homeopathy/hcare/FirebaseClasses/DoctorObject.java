package com.hcare.homeopathy.hcare.FirebaseClasses;

import java.io.Serializable;

public class DoctorObject implements Serializable {

    String name;
    String thumb_image;
    String doctorID;
    String qualification;
    String experience;

    public void setName(String name) {
        this.name = name;
    }

    public void setThumb_image(String thumb_image) {
        this.thumb_image = thumb_image;
    }

    public void setDoctorID(String doctorID) {
        this.doctorID = doctorID;
    }

    public void setQualification(String qualification) {
        this.qualification = qualification;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    public String getName() {
        return name;
    }

    public String getThumb_image() {
        return thumb_image;
    }

    public String getDoctorID() {
        return doctorID;
    }

    public String getQualification() {
        return qualification;
    }

    public String getExperience() {
        return experience;
    }
}
