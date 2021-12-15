package com.example.android.foodorderingapp;

public class OrderModal {
    long id;
    String date;
    int price;
    String email;

    public OrderModal(long id, String date, int price, String email) {
        this.id = id;
        this.date = date;
        this.price = price;
        this.email = email;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
