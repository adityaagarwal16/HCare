package com.hcare.homeopathy.hcare.NavigationItems.Orders;

/**
 * Created by Vinith pc on 2/21/2018.
 */

public class Orders {
    public String getOrderStatus() {
        return OrderStatus;
    }
public Orders(){

    }
    public void setOrderStatus(String orderStatus) {
        OrderStatus = orderStatus;
    }

    public Orders(String orderStatus) {

        OrderStatus = orderStatus;
    }

    private String OrderStatus;
    private String orderId;

    public String getOrdertime() {
        return Ordertime;
    }

    public void setOrdertime(String ordertime) {
        Ordertime = ordertime;
    }

    private String Ordertime;

    public Orders(String orderId, String time) {
        this.orderId = orderId;
        this.time = time;
    }

    public String getOrderId() {

        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    private String time;
}
