package com.hcare.homeopathy.hcare.Consultations.Doctor;

import android.content.Context;

public class ChatObject {

    private String message, type, image, ordering, medicineId, from;
    private long time;
    private boolean seen;

    public ChatObject() { }

    public ChatObject(String message, boolean seen, long time, String type) {
        this.message = message;
        this.seen = seen;
        this.time = time;
        this.type = type;
    }


    public String getMedicineId() {

        return medicineId;
    }

    public String getOrdering() {
        return ordering;
    }

    public String getImage() {
        return image;
    }

    public void setImage(Context ctx, String image) {
        this.image = image;
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
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }
}
