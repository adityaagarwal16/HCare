package com.hcare.homeopathy.hcare.FirebaseClasses;

import java.io.Serializable;
import java.util.HashMap;

public class ReferralObject implements Serializable {

    HashMap<Long, String> Consultations, Orders;

    public HashMap<Long, String> getConsultations() {
        return Consultations;
    }

    public void setConsultations(HashMap<Long, String> consultations) {
        Consultations = consultations;
    }

    public HashMap<Long, String> getOrders() {
        return Orders;
    }

    public void setOrders(HashMap<Long, String> orders) {
        Orders = orders;
    }
}
