package com.example.android.foodorderingapp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.icu.util.Calendar;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.bumptech.glide.Glide;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Popup extends AppCompatActivity {

    // initializing
    // FusedLocationProviderClient
    // object
    FusedLocationProviderClient mFusedLocationClient;

    // Initializing other items
    // from layout file
    TextView latitudeTextView, longitTextView;
    int PERMISSION_ID = 44;
    String city = "Current Location";
    Geocoder geocoder;
    List<Address> addresses;
    String username = "";
    private SharedPreferences sharedPreferences;
    private static final String SHARED_PREFERENCE_FILE = "shared_preference_file";
    Button goback;
    Button mapButton;
    double lati, longi;
    ImageView imageOfTheDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popup);
        geocoder = new Geocoder(this, Locale.getDefault());
        sharedPreferences = getSharedPreferences(SHARED_PREFERENCE_FILE, Context.MODE_PRIVATE);
        mapButton = findViewById(R.id.map);
        imageOfTheDay = findViewById(R.id.img);
        int Min = 0;
        int Max = 5;
        int pos = Min + (int) (Math.random() * ((Max - Min) + 1));
        String[] breakfastUrls = new String[]{
                "https://insanelygoodrecipes.com/wp-content/uploads/2020/12/Chocolate-Chip-Pancakes.png",
                "https://simply-delicious-food.com/wp-content/uploads/2018/10/breakfast-board.jpg",
                "https://www.indianhealthyrecipes.com/wp-content/uploads/2021/06/masala-dosa-500x500.jpg",
                "https://tmbidigitalassetsazure.blob.core.windows.net/rms3-prod/attachments/37/1200x1200/Breakfast-Wraps_EXPS_TOHPP19_35683_B08_23_6b.jpg",
                "https://assets.bonappetit.com/photos/5bae794a56c47e4ca3babf6c/master/pass/unnamed.jpg",
                "https://cdn.tatlerasia.com/asiatatler/i/ph/2021/05/07105034-gettyimages-1257260385_cover_1280x764.jpg"
        };

        String[] lunchUrls = new String[]{
                "https://c1.staticflickr.com/1/271/32607495025_f45106f1e4_z.jpg",
                "https://loveincorporated.blob.core.windows.net/contentimages/gallery/57bd6dab-c28f-4c83-b9e7-f4aa53bac815-alaska-tommys.jpg",
                "https://www.archanaskitchen.com/images/archanaskitchen/1-Author/sibyl_sunitha/Chinese_Chicken_Sweet_Corn_Soup_Recipe_.jpg",
                "https://img-global.cpcdn.com/recipes/893101ba11fb18f7/1200x630cq70/photo.jpg",
                "https://saladswithanastasia.com/wp-content/uploads/2020/10/GREEN-CORAL-SALAD-FEATURED-1.jpg",
                "https://d2rd7etdn93tqb.cloudfront.net/wp-content/uploads/2019/05/black-forest-ham-subway-featured-051419.png"
        };

        String[] dinnerUrls = new String[]{
                "https://www.eatthis.com/wp-content/uploads/sites/4/2020/06/chimichanga.jpg?quality=82&strip=all",
                "https://myfoodbook.com.au/sites/default/files/collections_image/Family%20Dinner%20recipes.jpg",
                "https://bakeitwithlove.com/wp-content/uploads/2021/05/Air-Fryer-Chicken-Nuggets-sq.jpg",
                "https://i2.wp.com/www.dinedelicious.in/wp-content/uploads/2019/04/Instant-Kadai-Paneer.jpg?fit=614%2C614&ssl=1",
                "https://iamafoodblog.b-cdn.net/wp-content/uploads/2020/07/cheesy-potato-balls-5178.jpg",
                "https://c.ndtvimg.com/2020-01/pedb3bio_masala-soyabean_625x300_24_January_20.jpg"
        };
        String url = "";
        String fType = foodType();
        if (fType.equals("breakfast")) url = breakfastUrls[pos % 6];
        else if (fType.equals("lunch")) url = lunchUrls[pos % 6];
        else url = dinnerUrls[pos % 6];
        Glide.with(Popup.this).load(R.drawable.giff).into(imageOfTheDay);
        new AsyncFetch(imageOfTheDay, foodType()).execute(url);
        mapButton.setOnClickListener(v -> {
            String uri = String.format(Locale.ENGLISH, "http://maps.google.com/maps?q=loc:%f,%f", lati, longi);
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
            startActivity(intent);
        });
        latitudeTextView = findViewById(R.id.latitude);
        goback = findViewById(R.id.goback);
        goback.setOnClickListener(v -> {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("goback", false);
            editor.apply();
            Intent intent = new Intent(Popup.this, MainActivity.class);
            startActivity(intent);
            finish();
        });

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        // method to get the location
        getLastLocation();
    }

    private String foodType() {
        Calendar c = Calendar.getInstance();
        int timeOfDay = c.get(Calendar.HOUR_OF_DAY);

        if (timeOfDay >= 0 && timeOfDay < 12) return "breakfast";
        else if (timeOfDay >= 12 && timeOfDay < 16) return "lunch";
        else return "dinner";
    }

    @SuppressLint("MissingPermission")
    private void getLastLocation() {
        // check if permissions are given
        if (checkPermissions()) {

            // check if location is enabled
            if (isLocationEnabled()) {

                // getting last
                // location from
                // FusedLocationClient
                // object
                mFusedLocationClient.getLastLocation().addOnCompleteListener(task -> {
                    Location location = task.getResult();
                    if (location == null) {
                        requestNewLocationData();
                    } else {
                        try {
                            addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                            city = addresses.get(0).getAddressLine(0);
                            lati = location.getLatitude();
                            longi = location.getLongitude();

                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        Spannable userName = new SpannableString(sharedPreferences.getString("userName", "User"));
                        Spannable locate = new SpannableString("Current Location: " + city + "\n\n\n");
                        locate.setSpan(new StyleSpan(Typeface.BOLD), 0, locate.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        locate.setSpan(new ForegroundColorSpan(Color.BLACK), 0, 18, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        locate.setSpan(new ForegroundColorSpan(Color.RED), 18, locate.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        Spannable hi = new SpannableString("Hi ");
                        hi.setSpan(new StyleSpan(Typeface.BOLD), 0, hi.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        hi.setSpan(new ForegroundColorSpan(Color.BLACK), 0, 3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        userName.setSpan(new StyleSpan(Typeface.BOLD), 0, userName.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        userName.setSpan(new ForegroundColorSpan(Color.RED), 0, userName.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        Spannable afterHi = new SpannableString(", based on your current location, we have dish of the day for your ");
                        afterHi.setSpan(new StyleSpan(Typeface.BOLD), 0, afterHi.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        afterHi.setSpan(new ForegroundColorSpan(Color.BLACK), 0, afterHi.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        Spannable behav = new SpannableString(foodType() + ". ");
                        behav.setSpan(new StyleSpan(Typeface.BOLD), 0, behav.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        behav.setSpan(new ForegroundColorSpan(Color.RED), 0, behav.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        Spannable afterBehav = new SpannableString("You can order it just by going to the menu.");
                        afterBehav.setSpan(new StyleSpan(Typeface.BOLD), 0, afterBehav.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        afterBehav.setSpan(new ForegroundColorSpan(Color.BLACK), 0, afterBehav.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        SpannableStringBuilder sb = new SpannableStringBuilder();
                        sb.append(locate).append(hi).append(userName).append(afterHi).append(behav).append(afterBehav);
                        latitudeTextView.setText(sb);
                    }
                });
            } else {
                Toast.makeText(this, "Please turn on" + " your location...", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        } else {
            // if permissions aren't available,
            // request for permissions
            requestPermissions();
        }
    }

    @SuppressLint("MissingPermission")
    private void requestNewLocationData() {

        // Initializing LocationRequest
        // object with appropriate methods
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(5);
        mLocationRequest.setFastestInterval(0);
        mLocationRequest.setNumUpdates(1);

        // setting LocationRequest
        // on FusedLocationClient
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
    }

    private LocationCallback mLocationCallback = new LocationCallback() {

        @Override
        public void onLocationResult(LocationResult locationResult) {
            Location mLastLocation = locationResult.getLastLocation();
            latitudeTextView.setText("Latitude: " + mLastLocation.getLatitude() + "");
            longitTextView.setText("Longitude: " + mLastLocation.getLongitude() + "");
        }
    };

    // method to check for permissions
    private boolean checkPermissions() {
        return ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;

        // If we want background location
        // on Android 10.0 and higher,
        // use:
        // ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_BACKGROUND_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

    // method to request for permissions
    private void requestPermissions() {
        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_ID);
    }

    // method to check
    // if location is enabled
    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    // If everything is alright then
    @Override
    public void
    onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_ID) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (checkPermissions()) {
            getLastLocation();
        }
    }
}
