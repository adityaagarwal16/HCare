package com.hcare.homeopathy.hcare.JvFiles;

/**
 * Created by Vinith pc on 9/6/2017.
 */

public class Publicreq {
    public String name;
    public String image;
    public String thumb_image;
    public Boolean seen;

    public Publicreq(Boolean seen) {
        this.seen = seen;
    }

    public Boolean getSeen() {

        return seen;
    }

    public void setSeen(Boolean seen) {
        this.seen = seen;
    }

    public Publicreq(){

    }

    public Publicreq(String name, String image, String thumb_image) {
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

    public String getThumb_image() {
        return thumb_image;
    }

    public void setThumb_image(String thumb_image) {
        this.thumb_image = thumb_image;
    }
}


