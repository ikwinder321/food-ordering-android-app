package com.example.android.foodorderingapp;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class BasketAdapter extends ArrayAdapter<Food> {
    Activity activity;

    public BasketAdapter(Activity context, ArrayList<Food> foods){
        super(context , 0 , foods);
        activity = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        if(listItem == null){
            listItem = LayoutInflater.from(getContext()).inflate(R.layout.list_item_for_basket , parent, false);
        }
        Food currentFood = getItem(position);


        TextView foodName = listItem.findViewById(R.id.food_name_bucket);
        foodName.setText(currentFood.getName());


        TextView foodCost = listItem.findViewById(R.id.food_cost_bucket);
        foodCost.setText("$" + currentFood.getCost());


        TextView foodCategory = listItem.findViewById(R.id.food_category_bucket);
        foodCategory.setText(currentFood.getCategory());

        TextView foodCount = listItem.findViewById(R.id.food_count_bucket);
        foodCount.setText("Quantity: " + String.valueOf(currentFood.count));

        return listItem;

    }


}
