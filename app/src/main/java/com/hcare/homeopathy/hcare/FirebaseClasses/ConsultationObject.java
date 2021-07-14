package com.hcare.homeopathy.hcare.FirebaseClasses;

import androidx.annotation.NonNull;

import com.google.firebase.database.PropertyName;

import java.io.Serializable;
import java.util.HashMap;

public class ConsultationObject implements Serializable {

    String userID;
    String consultationID;
    long time;
    String disease;
    String issue;

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getConsultationID() {
        return consultationID;
    }

    public void setConsultationID(String consultationID) {
        this.consultationID = consultationID;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getDisease() {
        return disease;
    }

    public void setDisease(String disease) {
        this.disease = disease;
    }

    public String getIssue() {
        return issue;
    }

    @NonNull
    @Override
    public String toString() {
        return "ConsultationObject{" +
                "userID='" + userID + '\'' +
                ", consultationID='" + consultationID + '\'' +
                ", time=" + time +
                ", disease='" + disease + '\'' +
                ", issue='" + issue + '\'' +
                '}';
    }

    public void setIssue(String issue) {
        this.issue = issue;
    }
}

