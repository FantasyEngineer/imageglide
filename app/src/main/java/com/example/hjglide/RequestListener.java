package com.example.hjglide;

import android.graphics.Bitmap;

public interface RequestListener {
    //失败
    void onFail();

    //成功
    boolean onSuccess(Bitmap bitmap);
}
