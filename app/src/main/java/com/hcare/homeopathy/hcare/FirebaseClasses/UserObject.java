package com.hcare.homeopathy.hcare.FirebaseClasses;

import com.google.firebase.database.PropertyName;

import java.io.Serializable;
import java.util.HashMap;

public class UserObject implements Serializable {


    String name, image, email, age, ReferredBy, device_token, status, sex, phoneNumber;
    int wallet;
    //ReferralObject referral;
    long createdAt;

    @PropertyName("phone number")
    public String getPhoneNumber() {
        return phoneNumber;
    }

    @PropertyName("phone number")
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @PropertyName("Wallet")
    public int getWallet() {
        return wallet;
    }

    @PropertyName("Wallet")
    public void setWallet(int wallet) {
        this.wallet = wallet;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getReferredBy() {
        return ReferredBy;
    }

    public void setReferredBy(String referredBy) {
        ReferredBy = referredBy;
    }

    public String getDevice_token() {
        return device_token;
    }

    public void setDevice_token(String device_token) {
        this.device_token = device_token;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }
}

