package com.example.android.foodorderingapp;
import java.lang.System.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Order {
    int orderCost ;
    long orderId;
    public Order(int cost){
        orderCost = cost;
        orderId = System.currentTimeMillis();
    }
    public String getOrderCost(){
        return String.valueOf(orderCost);
    }
    public String getOrderId(){
        return String.valueOf(orderId);
    }
    public String getDate(){

        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(orderId);
        return formatter.format(calendar.getTime());

    }

}
