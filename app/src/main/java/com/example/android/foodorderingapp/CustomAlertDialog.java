package com.example.android.foodorderingapp;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

public class CustomAlertDialog extends Dialog  implements View.OnClickListener{
    public Activity activity;

    Food mFood;

    public CustomAlertDialog(@NonNull Context context , Food food) {
        super(context);
        this.activity = (Activity) context;
        mFood = food;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.custom_dialog);
        ImageView imageView = findViewById(R.id.custom_alert_food_icon);
        imageView.setImageResource(mFood.getImageResourceId());
        TextView textView = findViewById(R.id.custom_alert_food_name);
        textView.setText(mFood.getName());
    }

    @Override
    public void onClick(View v) {

    }

}
