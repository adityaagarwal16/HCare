package com.hcare.homeopathy.hcare.Consultations.Doctor;

import android.annotation.SuppressLint;
import android.app.Application;
import android.util.Log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

/**
 * Created by AkshayeJH on 16/07/17.
 */

public class GetTime {

    private static final int SECOND_MILLIS = 1000;
    private static final int MINUTE_MILLIS = 60 * SECOND_MILLIS;
    private static final int HOUR_MILLIS = 60 * MINUTE_MILLIS;
    private static final int DAY_MILLIS = 24 * HOUR_MILLIS;

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

    /*final long diff = now - time;
        if (diff < MINUTE_MILLIS) {
            return "just now";
        } else if (diff < 2 * MINUTE_MILLIS) {
            return "a minute ago";
        } else if (diff < 50 * MINUTE_MILLIS) {
            return diff / MINUTE_MILLIS + " minutes ago";
        } else if (diff < 90 * MINUTE_MILLIS) {
            return "an hour ago";
        } else if (diff < 24 * HOUR_MILLIS) {
            return diff / HOUR_MILLIS + " hours ago";
        } else if (diff < 48 * HOUR_MILLIS) {
            return "yesterday";
        } else {
            return diff / DAY_MILLIS + " days ago";
        }*/

    public int getDays() {
        return (int) (time / (1000 * 60 * 60 * 24));
    }

}