package com.example.android.foodorderingapp;

import android.app.Activity;
import android.content.res.Resources;
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
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

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

        ImageView imageView = listItem.findViewById(R.id.home_icon);
        //imageView.setImageResource(currentFood.getImageResourceId());
        Glide.with(getContext()).load(R.drawable.giff).into(imageView);
        //String url = "https://blog.hubspot.com/hubfs/image8-2.jpg";
        String url = "";
        String fType = currentFood.getCategory();
        if (fType.equals("valentine")) url = lunchUrls[position % 6];
        else if (fType.equals("birthday")) url = breakfastUrls[position % 6];
        else  url = dinnerUrls[position % 6];
        new AsyncFetch(imageView,fType).execute(url);


        TextView foodName = listItem.findViewById(R.id.home_food_name);
        foodName.setText(currentFood.getName());

        TextView foodCost = listItem.findViewById(R.id.home_food_cost);
        foodCost.setText("$" + currentFood.getCost());

        String finalUrl = url;
        imageView.setOnClickListener(v -> {
            CustomAlertDialog customAlertDialog = new CustomAlertDialog(getContext(), finalUrl, currentFood);
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
