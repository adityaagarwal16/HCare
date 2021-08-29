package com.hcare.homeopathy.hcare.FirebaseClasses;

import com.google.firebase.database.PropertyName;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

public class DoctorObject implements Serializable {

    String name;
    String image;
    String doctorID;
    String qualification;
    String sex;
    String languages;
    HashMap<String, String> count, AcceptCount;
    @PropertyName("about yourself")
    public String aboutYourself;

    @PropertyName("register id")
    public String registerID;

    String Availability;
    int experience;

    public HashMap<String, String> getAcceptCount() {
        return AcceptCount;
    }

    public void setAcceptCount(HashMap<String, String> acceptCount) {
        AcceptCount = acceptCount;
    }

    public HashMap<String, String> getCount() {
        return count;
    }

    public void setCount(HashMap<String, String> count) {
        this.count = count;
    }

    public String getLanguages() {
        return languages;
    }

    public void setLanguages(String languages) {
        this.languages = languages;
    }

    public String getAboutYourself() {
        return aboutYourself;
    }

    public void setAboutYourself(String aboutYourself) {
        this.aboutYourself = aboutYourself;
    }

    public String getRegisterID() {
        return registerID;
    }

    public void setRegisterID(String registerID) {
        this.registerID = registerID;
    }

    public String getAvailability() {
        return Availability;
    }

    public void setAvailability(String availability) {
        Availability = availability;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
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

    public void setDoctorID(String doctorID) {
        this.doctorID = doctorID;
    }

    public void setQualification(String qualification) {
        this.qualification = qualification;
    }

    public int getExperience() {
        return experience;
    }

    public void setExperience(int experience) {
        this.experience = experience;
    }

    public String getName() {
        return name;
    }

    public String getDoctorID() {
        return doctorID;
    }

    public String getQualification() {
        return qualification;
    }

}

