package com.example.android.foodorderingapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.foodorderingapp.R;

import java.util.ArrayList;


public class UserDetail extends Fragment {

    private SharedPreferences sharedPreferences;
    private static final String SHARED_PREFERENCE_FILE = "shared_preference_file";
    private DBHandler dbHandler;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        sharedPreferences = this.getActivity().getSharedPreferences(SHARED_PREFERENCE_FILE, Context.MODE_PRIVATE);

        TextView nonEditView = view.findViewById(R.id.non_editable_detail);
        dbHandler = new DBHandler(this.getActivity());
        ArrayList<UserModal> userD = dbHandler.readUserData(sharedPreferences.getString("userEmail", "xxx@gmail.com"));
        String uName = userD.get(0).getUserName();
        String uMobile = userD.get(0).getUserNumber();
        String uEmail = userD.get(0).getUserEmail();

        Spannable uname = new SpannableString("Name: " + uName + "\n\n");
        uname.setSpan(new StyleSpan(Typeface.BOLD), 0, uname.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        uname.setSpan(new ForegroundColorSpan(Color.RED), 0, 6, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        uname.setSpan(new ForegroundColorSpan(Color.BLACK), 6, 6 + uName.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        Spannable umobile = new SpannableString("Mobile: " + uMobile + "\n\n");
        umobile.setSpan(new StyleSpan(Typeface.BOLD), 0, umobile.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        umobile.setSpan(new ForegroundColorSpan(Color.RED), 0, 8, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        umobile.setSpan(new ForegroundColorSpan(Color.BLACK), 8, 8 + uMobile.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        Spannable uemail = new SpannableString("Email: " + uEmail + "\n\n");
        uemail.setSpan(new StyleSpan(Typeface.BOLD), 0, uemail.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        uemail.setSpan(new ForegroundColorSpan(Color.RED), 0, 7, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        uemail.setSpan(new ForegroundColorSpan(Color.BLACK), 7, 7 + uEmail.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        SpannableStringBuilder uDetails = new SpannableStringBuilder(uname).append(umobile).append(uemail);
        nonEditView.setText(uDetails);

        EditText temp = view.findViewById(R.id.update_user_input_pincode);
        temp.setText(userD.get(0).getUserPincode());

        temp = view.findViewById(R.id.update_user_input_address);
        temp.setText(userD.get(0).getUserAddress());

        Button updateButton = view.findViewById(R.id.account_update_button);
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // pincode check
                EditText editText = view.findViewById(R.id.update_user_input_pincode);
                String user_input = editText.getText().toString();
                if (user_input.isEmpty() || user_input.charAt(0) == '0' || user_input.length() != 6) {
                    showToast(view, false, "Invalid pincode!!");
                    return;
                }

                // address check
                editText = view.findViewById(R.id.update_user_input_address);
                user_input = editText.getText().toString();
                if (user_input.isEmpty() || user_input.length() > 120) {
                    showToast(view, false, "Invalid address!!");
                    return;
                }

                SharedPreferences.Editor editor = sharedPreferences.edit();
                editText = view.findViewById(R.id.update_user_input_pincode);
                String editPin = editText.getText().toString();
                editor.putString("userPincode", editText.getText().toString());
                editText = view.findViewById(R.id.update_user_input_address);
                String editAddress = editText.getText().toString();
                editor.putString("userAddress", editText.getText().toString());
                editor.apply();
                dbHandler.updateUserTable(userD.get(0).getUserEmail(), editPin, editAddress);
                showToast(view, true, "Details updated successfully!!");

                getActivity()
                        .getSupportFragmentManager()
                        .beginTransaction()
                        .setReorderingAllowed(true)
                        .replace(R.id.host_container, HomeFragment.class, null)
                        .commit();

            }
        });

    }

    private void showToast(View view, boolean feel, String message) {
        LayoutInflater layoutInflater = getLayoutInflater();
        View layout = layoutInflater.inflate(R.layout.custom_toast, (ViewGroup) view.findViewById(R.id.toast_root));
        TextView toastText = layout.findViewById(R.id.toast_message);
        ImageView toastImage = layout.findViewById(R.id.toast_image);
        toastText.setText(message);
        if (feel) {
            toastImage.setImageResource(R.drawable.happy_mood);
        } else {
            toastImage.setImageResource(R.drawable.sad_mood);
        }
        Toast toast = new Toast(getContext());
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();

    }
}