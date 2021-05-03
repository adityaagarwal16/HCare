package com.hcare.homeopathy.hcare.OrderTreatment;

import android.content.Context;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

public class AddressSharedPref {


    final String MY_PREFS_NAME = "MyPrefsFile";
    static final String PIN_CODE = "PIN_CODE", ADDRESS = "ADDRESS", CITY = "CITY", STATE = "STATE";
    Context context;

    public AddressSharedPref(Context context) {
        this.context = context;
    }

    public void save(String key, String value) {
        SharedPreferences.Editor editor = context
                .getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE)
                .edit();
        editor.putString(key, value);
        editor.apply();
    }

    public String get(String key) {
        SharedPreferences prefs = context
                .getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        return prefs.getString(key, "");
    }
}
