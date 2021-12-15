package com.example.android.foodorderingapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.Telephony;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.android.foodorderingapp.R;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;


public class PlaceOrder extends Fragment {

    private SharedPreferences sharedPreferences;
    private static final String SHARED_PREFERENCE_FILE = "shared_preference_file";
    DBHandler dbHandler;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_place_order, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        sharedPreferences = getActivity().getSharedPreferences(SHARED_PREFERENCE_FILE, Context.MODE_PRIVATE);
        dbHandler = new DBHandler(this.getContext());
        TextView amount = view.findViewById(R.id.payable_amount);
        amount.setText("$" + String.valueOf(MainActivity.bucketCost));

        TextView orderId = view.findViewById(R.id.order_id);
        orderId.setText("Order ID: " + String.valueOf(System.currentTimeMillis()));
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDateTime now = LocalDateTime.now();
        dbHandler.addNewOrder(System.currentTimeMillis(), String.valueOf(dtf.format(now)), MainActivity.bucketCost, sharedPreferences.getString("userEmail", "xxx@gmail.com"));
        String message = "";

        message += "Name: " + sharedPreferences.getString("userName", "Unknown\n") + '\n';
        message += "Number: " + sharedPreferences.getString("userNumber", "Unknown\n") + '\n';
        message += "Email: " + sharedPreferences.getString("userEmail", "Unknown\n") + '\n';
        message += "Pincode: " + sharedPreferences.getString("userPincode", "Unknown\n") + '\n';
        message += "Address: " + sharedPreferences.getString("userAddress", "Unknown\n") + '\n';
        message += "______________________________\n";
        message += "Birthday Food order\n";
        for (int i = 0; i < 6; i++) {
            if (MainActivity.birthdayCount[i] > 0) {
                message += i + "->" + MainActivity.birthdayCount[i] + " | ";
            }
        }
        message += "\n";
        message += "Wedding Food order\n";
        for (int i = 0; i < 6; i++) {
            if (MainActivity.weddingCount[i] > 0) {
                message += i + "->" + MainActivity.weddingCount[i] + " | ";
            }
        }
        message += "\n";
        message += "Valentine Food order\n";
        for (int i = 0; i < 6; i++) {
            if (MainActivity.valentineCount[i] > 0) {
                message += i + "->" + MainActivity.valentineCount[i] + " | ";
            }
        }
        message += "\n";

        Button button = view.findViewById(R.id.order_through_message);
        final String finalMessage1 = message;
        button.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("IntentReset")
            @Override
            public void onClick(View v) {
                MainActivity.last_orders.add(new OrderModal(0L, "", MainActivity.bucketCost, ""));
                if (getDefaultSmsAppPackageName(view.getContext()) != null) {
                    String phoneNumber = "7717394559";
                    Uri smsUri=  Uri.parse("smsto:" + phoneNumber);
                    Intent smsIntent = new Intent(Intent.ACTION_VIEW, smsUri);
                    smsIntent.putExtra("sms_body", finalMessage1);
                    smsIntent.setType("vnd.android-dir/mms-sms");
                    smsIntent.setFlags(android.content.Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(smsIntent);
                }
                finalStep();
                //TODO: clean bucket, set bucketText = 0, move to home screen, add item to last_orders, create message, showToast
            }
        });

        button = view.findViewById(R.id.order_through_email);
        final String finalMessage = message;
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.last_orders.add(new OrderModal(0L, "", MainActivity.bucketCost, ""));

                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:" + "mcknightconner555@gmail.com"));
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Food order");
                emailIntent.putExtra(Intent.EXTRA_TEXT, finalMessage);
                startActivity(Intent.createChooser(emailIntent, "Send email through"));

                finalStep();

                //TODO: clean bucket, set bucketText = 0, move to home screen, add item to last_orders, create message , showToast
            }
        });

    }

    public static String getDefaultSmsAppPackageName(@NonNull Context context) {
        String defaultSmsPackageName;
        defaultSmsPackageName = Telephony.Sms.getDefaultSmsPackage(context);
        return defaultSmsPackageName;
    }

    private void finalStep() {
        MainActivity.bucketCost = 0;
        for (int i = 0; i < 6; i++) {
            MainActivity.birthdayCount[i] = 0;
            MainActivity.weddingCount[i] = 0;
            MainActivity.valentineCount[i] = 0;
        }
        getActivity().getSupportFragmentManager().beginTransaction().setReorderingAllowed(true).replace(R.id.host_container, HomeFragment.class, null).commit();

    }
}