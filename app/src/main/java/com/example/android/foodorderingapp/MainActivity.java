package com.example.android.foodorderingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferences;
    private static final String SHARED_PREFERENCE_FILE = "shared_preference_file";
    static int bucketCost;
    FloatingActionButton catFloat;

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;
    private ActionBarDrawerToggle toggle;
    static int getCount = 0;


    final static ArrayList<Food> BIRTHDAY_FOOD = new ArrayList<>();
    final static ArrayList<Food> WEDDING_FOOD = new ArrayList<>();
    final static ArrayList<Food> VALENTINE_FOOD = new ArrayList<>();

    static ArrayList<OrderModal> last_orders = new ArrayList<>();

    static int[] birthdayCount = new int[]{0, 0, 0, 0, 0, 0};
    static int[] weddingCount = new int[]{0, 0, 0, 0, 0, 0};
    static int[] valentineCount = new int[]{0, 0, 0, 0, 0, 0};
    Timer timer;
    DBHandler dbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // getting shared preferences file
        sharedPreferences = getSharedPreferences(SHARED_PREFERENCE_FILE, Context.MODE_PRIVATE);
        timer = new Timer();
        dbHandler = new DBHandler(this.getApplicationContext());
        if (!sharedPreferences.contains("goback") || getCount == 0) {
            getCount++;
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    if (sharedPreferences.contains("goback")) {
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putBoolean("goback", !sharedPreferences.getBoolean("goback", true));
                        editor.apply();
                    }
                    Intent intent = new Intent(MainActivity.this, Popup.class);
                    startActivity(intent);
                    finish();
                }
            }, 50);
        }

        bucketCost = 0;
        //setTitle("");
        // Objects.requireNonNull(getSupportActionBar()).setIcon(R.drawable.home_icon);


        // creating toolbar, with hamburger button
        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navigation_drawer);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        catFloat = findViewById(R.id.show_cart);
        catFloat.setOnClickListener(v -> replaceFragment(1));


        // setting navigation header text
        View view = navigationView.getHeaderView(0);
        TextView nameInDrawer = view.findViewById(R.id.navigation_drawer_text);
        nameInDrawer.setText("Hi, " + sharedPreferences.getString("userName", "User"));

        // navigation , on menu item click
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                drawerLayout.close();
                switch (item.getItemId()) {
                    case R.id.menu_item_basket:
                        replaceFragment(1);
                        break;
                    case R.id.menu_item_placed_orders:
                        replaceFragment(2);
                        break;
                    case R.id.menu_item_user_detail:
                        replaceFragment(3);
                        break;
                    case R.id.menu_item_about_us:
                        replaceFragment(4);
                        break;
                    case R.id.menu_item_logout:
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.clear();
                        editor.apply();
                        startActivity(new Intent(getApplicationContext(), SplashScreen.class));
                        finish();
                    default:
                        return false;
                }
                return true;
            }
        });

        dbHandler.addDishes();
        ArrayList<FoodModal> foodData = dbHandler.readFoodData("breakfast");

        int[] drawables = new int[]{R.drawable.breakfast_1, R.drawable.breakfast_2, R.drawable.breakfast_3, R.drawable.breakfast_4, R.drawable.breakfast_5, R.drawable.breakfast_6};
        int count = 0;
        for (FoodModal i : foodData) {
            WEDDING_FOOD.add(new Food(i.getFoodName(), "wedding", drawables[count++ % 6], i.getFoodPrice(), 0));
        }
        count = 0;
        drawables = new int[]{R.drawable.lunch_1, R.drawable.lunch_2, R.drawable.lunch_3, R.drawable.lunch_4, R.drawable.lunch_5, R.drawable.lunch_6};
        foodData = dbHandler.readFoodData("lunch");
        for (FoodModal i : foodData) {
            BIRTHDAY_FOOD.add(new Food(i.getFoodName(), "birthday", drawables[count++ % 6], i.getFoodPrice(), 0));
        }

        count = 0;
        drawables = new int[]{R.drawable.dinner_1, R.drawable.dinner_2, R.drawable.dinner_3, R.drawable.dinner_4, R.drawable.dinner_5, R.drawable.dinner_6};
        foodData = dbHandler.readFoodData("dinner");
        for (FoodModal i : foodData) {
            VALENTINE_FOOD.add(new Food(i.getFoodName(), "valentine", drawables[count++ % 6], i.getFoodPrice(), 0));
        }
        replaceFragment(0);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Gson gson = new Gson();
//        String json = sharedPreferences.getString("last_orders", null);
//        Type type = new TypeToken<ArrayList<Order>>() {
//        }.getType();
//        last_orders = gson.fromJson(json, type);

        DBHandler dbHandler = new DBHandler(this.getApplicationContext());
        last_orders = dbHandler.readOrdersData(sharedPreferences.getString("userEmail", "xxx@gmail.com"));

        if (last_orders == null) {
            last_orders = new ArrayList<>();
        }

    }

    @Override
    protected void onStop() {
        super.onStop();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(last_orders);
        editor.putString("last_orders", json);
        editor.apply();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.option_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.go_to_home) {
            replaceFragment(0);
        }
//        else if (item.getItemId() == R.id.switchToggle) {
        //replaceFragment(1);
//            SharedPreferences.Editor editor = sharedPreferences.edit();
//            String mode = (Configuration.UI_MODE_NIGHT_YES == 32 ? "ON":"OFF");
//            editor.putString("Night_Mode", (Configuration.UI_MODE_NIGHT_YES == 32 ? "ON":"OFF"));
//            editor.apply();
//            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
//            setContentView(R.layout.activity_main);
//            editor.putString("Night_Mode", (Configuration.UI_MODE_NIGHT_YES == 32 ? "ON":"OFF"));
//            editor.apply();
        // }
//        ToggleButton btn = findViewById(R.id.switchToggle);
//        btn.setOnClickListener(v -> {
//            replaceFragment(1);
//        });
        return super.onOptionsItemSelected(item);
    }

    private void replaceFragment(int idx) {
        getSupportFragmentManager()
                .beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.host_container, idx == 0 ? HomeFragment.class : (idx == 1 ? Basket.class : (idx == 2 ? PlacedOrders.class : (idx == 3 ? UserDetail.class : AboutUs.class))), null)
                .commit();
    }

    @Override
    public void onBackPressed() {

        if (drawerLayout.isOpen()) {
            drawerLayout.close();
        }
        moveTaskToBack(true);
    }

}