package com.hcare.homeopathy.hcare.FirebaseClasses;

import java.io.Serializable;

public class OrderObject implements Serializable {

    String  address, userID, city, state, doctorID, OrderID, status;
    long time, shipRocketOrderID, shipmentID;
    int pinCode;
    float amount;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getPinCode() {
        return pinCode;
    }

    public void setPinCode(int pinCode) {
        this.pinCode = pinCode;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getDoctorID() {
        return doctorID;
    }

    public void setDoctorID(String doctorID) {
        this.doctorID = doctorID;
    }

    public String getOrderID() {
        return OrderID;
    }

    public void setOrderID(String orderID) {
        OrderID = orderID;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public long getShipRocketOrderID() {
        return shipRocketOrderID;
    }

    public void setShipRocketOrderID(long shipRocketOrderID) {
        this.shipRocketOrderID = shipRocketOrderID;
    }

    public long getShipmentID() {
        return shipmentID;
    }

    public void setShipmentID(long shipmentID) {
        this.shipmentID = shipmentID;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }
}
