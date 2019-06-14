package com.example.hjglide.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import com.example.hjglide.util.libcore.DiskCache;
import com.example.hjglide.util.libcore.ImageCache;
import com.example.hjglide.util.libcore.MemoryCache;


/**
 * 双缓存操作类
 * Created by paul on 15/12/28.
 */
public class DoubleCache implements ImageCache {
    private static final String TAG = DoubleCache.class.getSimpleName();
    private MemoryCache mMemoryCache = null;
    private DiskCache mDiskCache = null;

    public DoubleCache(Context context){
        mMemoryCache = new MemoryCache();
        mDiskCache = new DiskCache(context);
    }
    @Override
    public Bitmap get(String url) {
        String key = url2Key(url);
        Bitmap bitmap = mMemoryCache.get(key);
        if(bitmap==null){
            Log.d(TAG, "从磁盘取");
            bitmap = mDiskCache.get(key);
            //取出来之后放入到内存中
            mMemoryCache.put(key, bitmap);
        }else {
            Log.d(TAG, "从内存取");
        }
        return bitmap;
     }

    @Override
    public void put(String url, Bitmap bitmap) {
        String key = url2Key(url);
        mMemoryCache.put(key,bitmap);
        mDiskCache.put(key,bitmap);
    }

    //url转key
    private String url2Key(String url){
        String key = MD5.hashKeyForDisk(url)+".jpg";
        return key;
    }
}
