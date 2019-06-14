package com.example.hjglide.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ImageView;

import com.example.hjglide.util.libcore.ImageCache;

import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 图片下载类
 * Created by paul on 15/12/28.
 */
public class ImageLoader {
    private static final String TAG = ImageLoader.class.getSimpleName();

    private static ImageLoader sInstance;

    private static ImageCache mCache = null;

    private static int mLoadingImageId ;

    private static int mErrorImageId;

    private ExecutorService mExecutorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    private ImageLoader(Context context) {
        mCache = new DoubleCache(context);
    }

    //设置缓存模式，用户可以定制自己需要缓存模式
//    private void setImageCache(ImageCache cache){
//        mCache = cache;
//    }
//    public static ImageLoader getInstance(Context context) {
//        if (sInstance == null) {
//            synchronized (ImageLoader.class) {
//                sInstance = new ImageLoader(context);
//            }
//        }
//        return sInstance;
//    }

    public void displayImage(String url, ImageView imageView) {
        Bitmap bitmap = mCache.get(url);
        if (bitmap != null) {
            imageView.setImageBitmap(bitmap);
            mCache.put(url, bitmap);
            return;
        }
        submitLoadRequest(url, imageView);
    }

    private void submitLoadRequest(final String url, final ImageView imageView) {
        Log.i(TAG,"Download,url:"+url);
        imageView.setTag(url);
        mExecutorService.submit(new Runnable() {
            @Override
            public void run() {
                final Bitmap bitmap = downloadImage(url);
                if (imageView.getTag().equals(url)) {

                    imageView.post(new Runnable() {
                        @Override
                        public void run() {
                            imageView.setImageBitmap(bitmap);
                        }
                    });
                }
                mCache.put(url, bitmap);
            }
        });
    }
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {

        }
    };

    public Bitmap downloadImage(String url) {
        Bitmap bitmap = null;
        HttpURLConnection conn = null;
        try {
            URL url1 = new URL(url);
            conn = (HttpURLConnection) url1.openConnection();
            bitmap = BitmapFactory.decodeStream(conn.getInputStream());
            if (bitmap!=null){
                mCache.put(url, bitmap);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
        return bitmap;
    }


    private static void applyConfig(ImageLoader loader){
        loader.mCache = mCache;
    }
    public static class Builder{
        Context context;
        public Builder(Context context){
            this.context = context;
        }
        public Builder setImageCache(ImageCache cache){
            mCache = cache;
            return this;
        }

        public Builder setErrorImageId(int resId){
            mErrorImageId = resId;
            return this;
        }
        public Builder setLoadingImageId(int resId){
            mLoadingImageId = resId;
            return this;
        }

        public ImageLoader create(){
            ImageLoader loader = new ImageLoader(context);
            applyConfig(loader);
            return loader;
        }
    }
}
