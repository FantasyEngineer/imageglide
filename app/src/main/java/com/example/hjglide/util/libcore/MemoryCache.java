package com.example.hjglide.util.libcore;

import android.graphics.Bitmap;
import android.util.Log;
import android.util.LruCache;

/**
 * 内存缓存使用lru算法处理
 * Created by paul on 15/12/28.
 */
public class MemoryCache implements ImageCache {
    private static final String TAG = MemoryCache.class.getSimpleName();
    private LruCache<String,Bitmap> mMemoryCache;
    public MemoryCache(){
        init();
    }

    private void init(){
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory()/1024);
        final int cacheSize = maxMemory/4;
        mMemoryCache = new LruCache<String,Bitmap>(cacheSize){
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getRowBytes()*value.getHeight()/1024;
            }
        };
    }
    @Override
    public Bitmap get(String key) {
        Bitmap bitmap = mMemoryCache.get(key);
        if (bitmap!=null){
            Log.i(TAG,"File is exist in memory");
        }
        return mMemoryCache.get(key);
    }

    @Override
    public void put(String key, Bitmap bitmap) {
        if (get(key)==null) {
            mMemoryCache.put(key, bitmap);
        }
    }
}
