package com.example.hjglide.util;


import com.example.hjglide.util.libcore.ImageCache;
import com.example.hjglide.util.libcore.MemoryCache;

/**
 * Created by paul on 15/12/29.
 */
public class ImageLoaderConfig {
    private ImageCache mCache = new MemoryCache();


    public class Builder{
        ImageCache cache = new MemoryCache();
        public Builder setCache(ImageCache cache){
            mCache = cache;
            return this;
        }
    }


}
