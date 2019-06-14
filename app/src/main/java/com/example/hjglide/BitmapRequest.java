package com.example.hjglide;

import android.content.Context;
import android.widget.ImageView;

import java.lang.ref.SoftReference;

public class BitmapRequest {
    //上下文对象
    private Context context;
    //图片请求地址
    private String url;
    //图片标志
    private String urlMd5;
    //占位图
    private int resId;
    //图片加载的控件
    private SoftReference<ImageView> imageView;
    //回调对象
    private RequestListener listener;

    public BitmapRequest(Context context) {
        this.context = context;
    }

    public BitmapRequest load(String url) {
        this.url = url;
        this.urlMd5 = MD5Utils.getMD5(url);
        return this;
    }
    public BitmapRequest loading (int redId){
        this.resId = resId;
        return this;
    }
    public BitmapRequest listener (RequestListener listener){
        this.listener = listener;
        return this;
    }

    public void into(ImageView imageView){
        imageView.setTag(this.urlMd5);
        this.imageView = new SoftReference<>(imageView);
        //将图片请求添加到队列中。
        RequestManager.getInstance(context).addBitmapRequest(this);
    }


    public String getUrl() {
        return url;
    }

    public String getUrlMd5() {
        return urlMd5;
    }

    public int getResId() {
        return resId;
    }

    public ImageView getImageView() {
        return imageView.get();
    }

    public RequestListener getListener() {
        return listener;
    }
}
