package com.hcare.homeopathy.hcare.JvFiles;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.hcare.homeopathy.hcare.R;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Objects;

import uk.co.senab.photoview.PhotoViewAttacher;

public class ImageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        ImageView fullScreenImageView = (ImageView)
                findViewById(R.id.fullScreenImageView);
        Intent callingActivityIntent = getIntent();
        if(callingActivityIntent != null) {
            Uri imageUri = callingActivityIntent.getData();
            if(imageUri != null && fullScreenImageView != null) {
                Glide.with(this)
                        .load(imageUri)
                        .into(fullScreenImageView);
            }
            PhotoViewAttacher photoAttacher;
            photoAttacher= new PhotoViewAttacher
                    (Objects.requireNonNull(fullScreenImageView));
            photoAttacher.update();
        }


    }

}
