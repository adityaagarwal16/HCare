package com.hcare.homeopathy.hcare;

public class blogs {

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCategory() {
        return Category;
    }

    public void setCategory(String category) {
        Category = category;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public blogs(String title, String image, String category, String description) {
        Title = title;
        this.image = image;
        Category = category;
        Description = description;
    }

    public String Title;

    public blogs(String titlename) {
        this.titlename = titlename;
    }

    public String getTitlename() {
        return titlename;
    }

    public void setTitlename(String titlename) {
        this.titlename = titlename;
    }

    public String titlename;
    public String image;
    public String Category;
    public String Description;

}
