package com.example.ecommerce.Model;

public class AdminOrders {
    private String Address,city,date,name,totalAmount,state,time,phone;

    public AdminOrders() {
    }

    public AdminOrders(String address, String city, String date, String name, String totalAmount, String state, String time, String phone) {
        Address = address;
        this.city = city;
        this.date = date;
        this.name = name;
        this.totalAmount = totalAmount;
        this.state = state;
        this.time = time;
        this.phone = phone;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
