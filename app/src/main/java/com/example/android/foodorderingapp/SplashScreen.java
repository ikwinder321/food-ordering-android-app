package com.example.android.foodorderingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.example.android.foodorderingapp.R;

import java.util.Timer;
import java.util.TimerTask;

public class SplashScreen extends AppCompatActivity {

    private SharedPreferences sharedPreferences;
    private static final String SHARED_PREFERENCE_FILE = "shared_preference_file";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        sharedPreferences = getSharedPreferences(SHARED_PREFERENCE_FILE , Context.MODE_PRIVATE);
        boolean hasUserDetail = sharedPreferences.getBoolean("hasUserDetail" , false);
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                if(hasUserDetail){
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                }else{
                    startActivity(new Intent(getApplicationContext(), UserInputDetail.class));
                }
                // finish() : if back is pressed in main Activity, it shouldn't go to splash screen
                finish();
            }
        } , 3000);

    }

}