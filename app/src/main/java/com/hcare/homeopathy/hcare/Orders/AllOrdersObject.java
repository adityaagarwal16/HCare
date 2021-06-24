package com.hcare.homeopathy.hcare.Orders;

import java.io.Serializable;

public class AllOrdersObject implements Serializable {

    private String OrderStatus, orderId, FullName, PatientId, Ordertime, Doctor,
            Amount, Address, City, State, emailId, PinCode, PhoneNumber, time ;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    private long shipRocketOrderID, shipmentID;

    public String getFullName() {
        return FullName;
    }

    public void setFullName(String fullName) {
        FullName = fullName;
    }

    public String getPatientId() {
        return PatientId;
    }

    public void setPatientId(String patientId) {
        PatientId = patientId;
    }

    public String getOrderStatus() {
        return OrderStatus;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getOrdertime() {
        return Ordertime;
    }

    public String getDoctor() {
        return Doctor;
    }

    public String getAmount() {
        return Amount;
    }

    public String getAddress() {
        return Address;
    }

    public String getCity() {
        return City;
    }

    public String getState() {
        return State;
    }

    public String getEmailId() {
        return emailId;
    }

    public String getPinCode() {
        return PinCode;
    }

    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public long getShipRocketOrderID() {
        return shipRocketOrderID;
    }

    public long getShipmentID() {
        return shipmentID;
    }

    public void setOrderStatus(String orderStatus) {
        OrderStatus = orderStatus;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public void setOrdertime(String ordertime) {
        Ordertime = ordertime;
    }

    public void setDoctor(String doctor) {
        Doctor = doctor;
    }

    public void setAmount(String amount) {
        Amount = amount;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public void setCity(String city) {
        City = city;
    }

    public void setState(String state) {
        State = state;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public void setPinCode(String pinCode) {
        PinCode = pinCode;
    }

    public void setPhoneNumber(String phoneNumber) {
        PhoneNumber = phoneNumber;
    }

    public void setShipRocketOrderID(long shipRocketOrderID) {
        this.shipRocketOrderID = shipRocketOrderID;
    }

    public void setShipmentID(long shipmentID) {
        this.shipmentID = shipmentID;
    }
}
