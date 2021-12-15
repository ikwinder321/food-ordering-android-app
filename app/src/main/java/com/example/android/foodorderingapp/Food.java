package com.example.android.foodorderingapp;



public class Food {
    private final String name;
    private final String category;
    private final int imageResourceId;
    private final int cost;
    public int count;
    public Food(String name , String category , int imageResourceId, int cost , int count){
        this.name = name;
        this.category = category;
        this.imageResourceId = imageResourceId;
        this.cost = cost;
        this.count = count;
    }
    public String getName(){return name;}
    public String getCategory(){return category;}
    public int getImageResourceId(){return imageResourceId;}
    public String getCost(){return String.valueOf(cost);}
}
