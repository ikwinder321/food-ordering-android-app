package com.example.android.foodorderingapp;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;

import java.io.InputStream;

@SuppressLint("StaticFieldLeak")
class AsyncFetch extends AsyncTask<String, Void, Bitmap> {

    ImageView poster;
    String type;

    public AsyncFetch(ImageView poster, String type) {
        if (type.equals("valentine")) type = "breakfast";
        else if (type.equals("birthday")) type = "lunch";
        else type = "dinner";
        this.poster = poster;
        this.type = type;
    }

    protected Bitmap doInBackground(String... urls) {
        String imageUrls = urls[0];
        Bitmap moviePoster = null;
        try {
            InputStream in = new java.net.URL(imageUrls).openStream();
            moviePoster = BitmapFactory.decodeStream(in);
        } catch (Exception e) {
            int Min = 1;
            int Max = 6;
            int pos = Min + (int) (Math.random() * ((Max - Min) + 1));
            String imageUri = "@drawable/" + type + "_" + pos;  // where myresource (without the extension) is the file
            poster.setImageURI(Uri.parse(imageUri));
            Log.e("Error", imageUrls);
            e.printStackTrace();
        }
        return moviePoster;
    }

    @Override
    protected void onPostExecute(Bitmap result) {
        Handler handler = new Handler();
        handler.postDelayed(() -> poster.setImageBitmap(result), 200);
    }
}