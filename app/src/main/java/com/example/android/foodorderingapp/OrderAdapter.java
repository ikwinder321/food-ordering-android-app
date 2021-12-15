package com.example.android.foodorderingapp;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.android.foodorderingapp.R;

import java.util.ArrayList;

public class OrderAdapter extends ArrayAdapter<OrderModal> {


    public OrderAdapter(Activity context, ArrayList<OrderModal> orders) {
        super(context, 0, orders);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        if (listItem == null) {
            listItem = LayoutInflater.from(getContext()).inflate(R.layout.list_item_for_placed_orders, parent, false);
        }
        final String SHARED_PREFERENCE_FILE = "shared_preference_file";
        SharedPreferences sharedPreferences = this.getContext().getSharedPreferences(SHARED_PREFERENCE_FILE, Context.MODE_PRIVATE);
        ;
        DBHandler dbHandler = new DBHandler(this.getContext());
        ArrayList<OrderModal> al = dbHandler.readOrdersData(sharedPreferences.getString("userEmail", "xxx@gmail.com"));
        OrderModal currentOrder = al.get(position % al.size());
//        if (al.size() == 1) currentOrder = al.get(0);
        //OrderModal currentOrder = list

        TextView textView = listItem.findViewById(R.id.order_id_of_placed_orders);
        textView.setText("ORDER ID: " + currentOrder.getId());

        textView = listItem.findViewById(R.id.date_of_placed_orders);
        textView.setText("ORDER DATE:" + currentOrder.getDate());

        textView = listItem.findViewById(R.id.order_cost);
        textView.setText("$" + currentOrder.getPrice());

        return listItem;

    }
}
