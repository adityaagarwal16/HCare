package com.hcare.homeopathy.hcare.Mainmenus;

/**
 * Created by Vinith pc on 7/31/2017.
 */

public class Doctors {

    public String name;
    public String image;
    public String qualification;
    public String experience;

    public Doctors(String languages) {
        this.languages = languages;
    }

    public String getLanguages() {

        return languages;
    }

    public void setLanguages(String languages) {
        this.languages = languages;
    }

    public String languages;
    public String thumb_image;

    public Doctors(){

    }

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    public Doctors(String name, String image, String age, String email , String thumb_image) {
        this.name = name;
        this.image = image;
        this.qualification = qualification;
        this.experience = experience;
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

    public String getAge() {
        return qualification;
    }

    public void setAge(String qualification) {
        this.qualification = qualification;
    }

    public String getEmail() {
        return experience;
    }

    public void setEmail(String experience) {
        this.experience = experience;
    }

    public String getThumb_image(){return thumb_image;}

    public void setThumb_image(String thumb_image){
        this.thumb_image = thumb_image;
    }
}
