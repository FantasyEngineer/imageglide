package com.example.hjglide;

import android.app.Application;
import android.content.Context;

import com.example.hjglide.util.DoubleCache;

import java.util.concurrent.LinkedBlockingQueue;

public class RequestManager {
    private DoubleCache cache;

    //创建单例
    private static RequestManager requestManager;
    //创建队列
    private LinkedBlockingQueue<BitmapRequest> requestQueue = new LinkedBlockingQueue();

    private RequestManager(Context context1) {
        start(context1);
    }

    public static RequestManager getInstance(Context context1) {
        if (requestManager==null){
            requestManager = new RequestManager(context1);
        }

        return requestManager;
    }

    //    创建线程数组
    private BitmapDispatcher[] bitmapDispatchers;

    private void start(Context context1) {
        stop();
        cache = new DoubleCache(context1);
        startAllDispatcher();
    }

    private void stop() {
        if (bitmapDispatchers != null && bitmapDispatchers.length > 0) {
            for (BitmapDispatcher bitmapDispatcher : bitmapDispatchers) {
                if (!bitmapDispatcher.isInterrupted()) {
                    bitmapDispatcher.interrupt();
                }
            }

        }
    }

    //    开始开启，让每一个线程开始上班
    private void startAllDispatcher() {
        //获取手机单应用的最大线程数
        int threadCount = Runtime.getRuntime().availableProcessors();
        bitmapDispatchers = new BitmapDispatcher[threadCount];
        for (int i = 0; i < threadCount; i++) {
            BitmapDispatcher bitmapDispatcher = new BitmapDispatcher(requestQueue, cache);
            bitmapDispatcher.start();
//           将每一个dispatcher中放到数组中。统一管理，停止所有线程的时候使用
            bitmapDispatchers[i] = bitmapDispatcher;
        }
    }


    //将图片请求添加到队列
    public void addBitmapRequest(BitmapRequest bitmapRequest) {
        if (bitmapRequest == null) {
            return;
        }
        if (!requestQueue.contains(bitmapRequest)) {
            requestQueue.add(bitmapRequest);
        }
    }


}



