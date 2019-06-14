package com.example.hjglide.util.libcore;

import android.graphics.Bitmap;

/**
 * Created by paul on 15/12/28.
 */
public interface ImageCache {
    public Bitmap get(String url);
    public void put(String url, Bitmap bitmap);
}
