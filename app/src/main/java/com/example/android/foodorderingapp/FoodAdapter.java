package com.example.android.foodorderingapp;

import android.app.Activity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class FoodAdapter extends ArrayAdapter<Food> {
    Activity activity;

    public FoodAdapter(Activity context, ArrayList<Food> foods) {
        super(context, 0, foods);
        activity = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        if (listItem == null) {
            listItem = LayoutInflater.from(getContext()).inflate(R.layout.list_item_for_home, parent, false);
        }
        Food currentFood = getItem(position);

        ImageView imageView = listItem.findViewById(R.id.home_icon);
        imageView.setImageResource(currentFood.getImageResourceId());


        TextView foodName = listItem.findViewById(R.id.home_food_name);
        foodName.setText(currentFood.getName());

        TextView foodCost = listItem.findViewById(R.id.home_food_cost);
        foodCost.setText("$" + currentFood.getCost());

        imageView.setOnClickListener(v -> {
            CustomAlertDialog customAlertDialog = new CustomAlertDialog(getContext(), currentFood);
            customAlertDialog.show();
        });

        TextView foodCount = listItem.findViewById(R.id.home_food_count);
        foodCount.setText(String.valueOf(currentFood.count));

        ImageButton minusButton = listItem.findViewById(R.id.home_minus_button);
        minusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentFood.count == 0) {
                    showToast(false, "Invalid input!!");
                } else {
                    currentFood.count -= 1;
                    foodCount.setText(String.valueOf(currentFood.count));
                }
            }
        });

        ImageButton plusButton = listItem.findViewById(R.id.home_plus_button);
        plusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentFood.count == 5) {
                    showToast(false, "Should not exceed 5");
                } else {
                    currentFood.count += 1;
                    foodCount.setText(String.valueOf(currentFood.count));
                }
            }
        });

        ImageButton addToCart = listItem.findViewById(R.id.home_add_to_cart);
        addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentFood.count == 0) {
                    showToast(false, "Invalid input!!");
                } else {
                    if (currentFood.getCategory().equals("wedding")) {
                        MainActivity.weddingCount[position % 6] = currentFood.count;
                    } else if (currentFood.getCategory().equals("birthday")) {
                        MainActivity.birthdayCount[position % 6] = currentFood.count;
                    } else MainActivity.valentineCount[position % 6] = currentFood.count;

                    showToast(true, "Items added/updated successfully!!");
                }
            }
        });

        return listItem;

    }

    private void showToast(boolean feel, String message) {
        LayoutInflater layoutInflater = activity.getLayoutInflater();
        View layout = layoutInflater.inflate(R.layout.custom_toast, (ViewGroup) activity.findViewById(R.id.toast_root));
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
