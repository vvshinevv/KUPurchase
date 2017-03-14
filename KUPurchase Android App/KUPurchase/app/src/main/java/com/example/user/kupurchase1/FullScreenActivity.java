package com.example.user.kupurchase1;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import uk.co.senab.photoview.PhotoViewAttacher;

public class FullScreenActivity extends AppCompatActivity {

    public ImageView full_image_view;
    public String imagePath;
    public PhotoViewAttacher photoViewAttacher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen);

        Intent intent= getIntent();
        imagePath = intent.getStringExtra("productImageURL");

        full_image_view = (ImageView) findViewById(R.id.full_image_view);

        Picasso.with(this).load(imagePath).into(full_image_view);
        photoViewAttacher = new PhotoViewAttacher(full_image_view);
    }
}
