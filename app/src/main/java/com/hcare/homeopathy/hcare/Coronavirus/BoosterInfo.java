package com.hcare.homeopathy.hcare.Coronavirus;

public class BoosterInfo {

    Boosters booster;

    public BoosterInfo(Boosters booster) {
        this.booster = booster;
    }

    public String getBoosterName() {
        switch(booster) {
            case a:
                return "Thyroid";
            case b:
                return "Diabetes";
            case c:
                return "Skin";
            case d:
                return "Female Issues";
            default:
                return "";
        }
    }

    public String getInfo() {
        switch(booster) {
            case a:
                return "aaaaaaaa";
            case b:
                return "bbbbbbbbb";
            case c:
                return "ccccccc";
            case d:
                return "dddddd";

            default:
                return "No information could be found on the particular disease.";
        }
    }


}

