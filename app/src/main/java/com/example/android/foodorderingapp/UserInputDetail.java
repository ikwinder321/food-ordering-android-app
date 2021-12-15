package com.example.android.foodorderingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.foodorderingapp.R;

import java.util.regex.Pattern;

import at.favre.lib.crypto.bcrypt.BCrypt;

public class UserInputDetail extends AppCompatActivity {

    private SharedPreferences sharedPreferences;
    private static final String SHARED_PREFERENCE_FILE = "shared_preference_file";
    private DBHandler dbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_input_detail);
        sharedPreferences = getSharedPreferences(SHARED_PREFERENCE_FILE, Context.MODE_PRIVATE);
        dbHandler = new DBHandler(UserInputDetail.this);

        findViewById(R.id.submit_user_details).setOnClickListener(v -> {
            // name check
            EditText editText = findViewById(R.id.user_input_name);
            String user_input = editText.getText().toString();
            if (user_input.isEmpty() || user_input.charAt(0) == ' ' || user_input.charAt(user_input.length() - 1) == ' ' || user_input.length() > 30) {
                showToast(false, "Invalid username!!");
                return;
            }
            // number check
            editText = findViewById(R.id.user_input_number);
            user_input = editText.getText().toString();
            if (user_input.isEmpty() || user_input.charAt(0) == '0' || user_input.length() != 10) {
                showToast(false, "Invalid mobile number!!");
                return;
            }


            // email check
            editText = findViewById(R.id.user_input_email);
            user_input = editText.getText().toString();
            if (user_input.isEmpty() || !isValidEmail(user_input)) {
                showToast(false, "Invalid email!!");
                return;
            }

            editText = findViewById(R.id.user_input_password);
            user_input = editText.getText().toString();
            if (user_input.isEmpty() || !isValidPassword(user_input)) {
                showToast(false, "Invalid password!!");
                return;
            }

            // pincode check
            editText = findViewById(R.id.user_input_pincode);
            user_input = editText.getText().toString();
            if (user_input.isEmpty() || user_input.charAt(0) == '0' || user_input.length() != 6) {
                showToast(false, "Invalid pincode!!");
                return;
            }

            // address check
            editText = findViewById(R.id.user_input_address);
            user_input = editText.getText().toString();
            if (user_input.isEmpty() || user_input.length() > 120) {
                showToast(false, "Invalid address!!");
                return;
            }

            // all are correct, let's save it
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("hasUserDetail", true);

            editText = findViewById(R.id.user_input_name);
            editor.putString("userName", editText.getText().toString());
            String username = editText.getText().toString();
            editText = findViewById(R.id.user_input_number);
            editor.putString("userNumber", editText.getText().toString());
            String usernumber = editText.getText().toString();
            editText = findViewById(R.id.user_input_email);
            editor.putString("userEmail", editText.getText().toString());
            String useremail = editText.getText().toString();
            editText = findViewById(R.id.user_input_pincode);
            editor.putString("userPincode", editText.getText().toString());
            String pincode = editText.getText().toString();
            editText = findViewById(R.id.user_input_address);
            editor.putString("userAddress", editText.getText().toString());
            String address = editText.getText().toString();
            editText = findViewById(R.id.user_input_password);
            String password = editText.getText().toString();

            //encoding password
            String bcryptHashString = BCrypt.withDefaults().hashToString(12, password.toCharArray());

            editor.putString("userPassword", bcryptHashString);
            editor.apply();
            dbHandler.addNewUser(username, usernumber, useremail, pincode, address, bcryptHashString);
            dbHandler.addDishes();
            showToast(true, "Details added successfully!!");
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            // can't go back to input detail screen
            finish();
        });

    }

    private boolean isValidPassword(String password) {
        String pattern = "(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}";
        return password.matches(pattern);
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\." +
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";

        Pattern pat = Pattern.compile(emailRegex);
        if (email == null)
            return false;
        return pat.matcher(email).matches();
    }

    private void showToast(boolean feel, String message) {
        LayoutInflater layoutInflater = getLayoutInflater();
        View layout = layoutInflater.inflate(R.layout.custom_toast, (ViewGroup) findViewById(R.id.toast_root));
        TextView toastText = layout.findViewById(R.id.toast_message);
        ImageView toastImage = layout.findViewById(R.id.toast_image);
        toastText.setText(message);
        if (feel) {
            toastImage.setImageResource(R.drawable.happy_mood);
        } else {
            toastImage.setImageResource(R.drawable.sad_mood);
        }
        Toast toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();

    }

}