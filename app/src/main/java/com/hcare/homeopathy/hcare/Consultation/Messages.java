package com.hcare.homeopathy.hcare.Consultation;

import android.content.Context;

/**
 * Created by Vinith pc on 9/10/2017.
 */

public class Messages {

    private String message;
    private String type;

    public String getMedicineId() {

        return medicineId;
    }

    public void setMedicineId(String medicineId) {
        this.medicineId = medicineId;
    }

    private String medicineId;

    public String getOrdering() {
        return ordering;
    }

    public void setOrdering(String ordering) {
        this.ordering = ordering;
    }

    private String ordering;
    private long time;
    private boolean seen;


    public String getImage() {
        return image;
    }

    public void setImage(Context ctx, String image) {
        this.image = image;
    }

    private String image;


    public Messages(String from) {
        this.from = from;
    }

    private String from;

    public Messages(String message, boolean seen, long time, String type) {
        this.message = message;
        this.seen = seen;
        this.time = time;
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean getSeen() {
        return seen;
    }

    public void setSeen(boolean seen) {
        this.seen = seen;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }public Messages(){

    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }
}
