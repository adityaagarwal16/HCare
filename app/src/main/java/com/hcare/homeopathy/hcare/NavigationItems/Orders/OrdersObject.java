package com.hcare.homeopathy.hcare.NavigationItems.Orders;

public class OrdersObject {

    private String OrderStatus, orderId, Ordertime, Doctor, Amount;

    public String getDoctor() {
        return Doctor;
    }

    public void setDoctor(String doctor) {
        Doctor = doctor;
    }

    public String getAmount() {
        return Amount;
    }

    public void setAmount(String amount) {
        Amount = amount;
    }

    public String getOrderStatus() {
        return OrderStatus;
    }

    public OrdersObject() { }

    public void setOrderStatus(String orderStatus) {
        OrderStatus = orderStatus;
    }

    public OrdersObject(String orderStatus) {
        OrderStatus = orderStatus;
    }

    public String getOrdertime() {
        return Ordertime;
    }

    public void setOrdertime(String ordertime) {
        this.Ordertime = ordertime;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
}
