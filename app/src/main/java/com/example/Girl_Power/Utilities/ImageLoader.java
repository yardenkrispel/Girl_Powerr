package com.example.Girl_Power.Utilities;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.Girl_Power.R;

public class ImageLoader {
    private Context context;

    public ImageLoader(Context context) {
        this.context = context;
    }

    public void loadImg (int imgId, ImageView imageView){
        Glide.
                with(context)
                .load(imgId)
                .placeholder(R.drawable.ic_launcher_background)
                .into(imageView);
    }

}
