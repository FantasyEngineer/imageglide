package com.example.hjglide;

import android.app.Application;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.widget.ImageView;

import com.example.hjglide.util.CloseUtils;
import com.example.hjglide.util.DoubleCache;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.LinkedBlockingQueue;

public class BitmapDispatcher extends Thread {
    //使用主线程
    private MyHandler handler = new MyHandler();
    private DoubleCache cache;

    //防止内存泄漏
    private static class MyHandler extends Handler {
        public MyHandler() {
            super(Looper.getMainLooper());
        }
    }

    //创建一个阻塞队列
    private LinkedBlockingQueue<BitmapRequest> requestQueue;

    public BitmapDispatcher(LinkedBlockingQueue requestQueue, DoubleCache cache) {
        this.requestQueue = requestQueue;
        this.cache = cache;
    }


    @Override
    public void run() {
        super.run();
        //判断是否被中断，如果中断了，则线程终止
        while (!isInterrupted()) {
            try {
                //从队列中获取图片请求
                BitmapRequest br = requestQueue.take();
                //设置占位图片
                showLoadingImage(br);
                //下载图片
                Bitmap bitmap = findBitmap(br);
                //将图片显示到imageViews
                showImageView(bitmap, br);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 展示图片(要判断md5)
     *
     * @param bitmap
     */
    private void showImageView(final Bitmap bitmap, final BitmapRequest br) {
        if (bitmap != null && br.getImageView() != null &&
                br.getUrlMd5().equals(br.getImageView().getTag())) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    br.getImageView().setImageBitmap(bitmap);
                    if (br.getListener() != null) {
                        RequestListener listener = br.getListener();
                        listener.onSuccess(bitmap);
                    }
                }
            });
        }


    }

    private Bitmap findBitmap(BitmapRequest br) {
        Bitmap bitmap = cache.get(br.getUrl());
        if (bitmap != null) {
            return bitmap;
        } else {
            bitmap = downloadBitmap(br.getUrl());
            return bitmap;
        }

    }

    /**
     * 下载图片
     *
     * @param uri
     */
    private Bitmap downloadBitmap(String uri) {
        FileOutputStream fos = null;
        InputStream is = null;
        Bitmap bitmap = null;
        try {
            URL url = new URL(uri);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            is = conn.getInputStream();
            bitmap = BitmapFactory.decodeStream(is);
            if (bitmap != null) {
                cache.put(uri, bitmap);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            CloseUtils.closeQuietly(is);
            CloseUtils.closeQuietly(fos);
        }
        return bitmap;
    }

    /**
     * 展示占位图片
     *
     * @param br
     */
    private void showLoadingImage(BitmapRequest br) {
        if (br.getResId() > 0 && br.getImageView() != null) {
            final int resId = br.getResId();
            final ImageView imageView = br.getImageView();
            handler.post(new Runnable() {
                @Override
                public void run() {
                    imageView.setImageResource(resId);

                }
            });
        }
    }
}
