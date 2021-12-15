package com.example.android.foodorderingapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.android.foodorderingapp.R;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;


public class PlacedOrders extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_placed_orders, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ListView listView = view.findViewById(R.id.placed_orders_list_view);
        DBHandler dbHandler = new DBHandler(view.getContext());
        final String SHARED_PREFERENCE_FILE = "shared_preference_file";
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(SHARED_PREFERENCE_FILE, Context.MODE_PRIVATE);
        ArrayList<OrderModal> al = dbHandler.readOrdersData(sharedPreferences.getString("userEmail", "Unknown\n"));
        int mod = (int) (1e9 + 7);
        al.sort((o1, o2) -> (int) (o2.getId() % mod - o1.getId() % mod));
        OrderAdapter orderAdapter = new OrderAdapter(getActivity(), al);
        listView.setAdapter(orderAdapter);
    }
}