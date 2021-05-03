package com.hcare.homeopathy.hcare.Consultations.Doctor;

import android.annotation.SuppressLint;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by AkshayeJH on 16/07/17.
 */

public class GetTime {

    long time;

    public GetTime(long time) {
        this.time = time;
        if (time < 1000000000000L) {
            // if timestamp given in seconds, convert to millis
            this.time *= 1000;
        }
    }

    public String getTimeAgo() {
        if (time > System.currentTimeMillis() || time <= 0) {
            return "";
        } else {
            @SuppressLint("SimpleDateFormat")
            DateFormat simple = new SimpleDateFormat("MMM dd, hh:mm a");
            Date result = new Date(time);
            return simple.format(result);
        }
    }

    public int getDays() {
        return (int) (time / (1000 * 60 * 60 * 24));
    }

}