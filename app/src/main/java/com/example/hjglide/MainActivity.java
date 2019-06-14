package com.example.hjglide;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class MainActivity extends AppCompatActivity {

    private static String[] PERMISSIONS_STORAGE = {
            "android.permission.WRITE_EXTERNAL_STORAGE",
            "android.permission.READ_EXTERNAL_STORAGE"
    };
    private LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        linearLayout = findViewById(R.id.ll);
        verfystorgePermissions(this);

    }

    private void verfystorgePermissions(Activity activity) {
        int permission = ActivityCompat.checkSelfPermission(activity, "android.permission.WRITE_EXTERNAL_STORAGE");
        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE, 1);
        }
    }

    public void loadone(View view) {
        ImageView imageView = new ImageView(this);
        linearLayout.addView(imageView);
        Glide.with(this).load("http://img2.ph.126.net/9zADa8NFFR0mqj8WnTEOQw==/2858378388597099017.jpg")
                .loading(R.mipmap.ic_launcher).listener(new RequestListener() {
            @Override
            public void onFail() {

            }

            @Override
            public boolean onSuccess(Bitmap bitmap) {
                Log.d("MainActivity", "下载成功");
                return false;
            }
        }).into(imageView);
    }

    public void loadmore(View view) {
        loadone(view);
        loadone(view);
        loadone(view);
        loadone(view);
        loadone(view);
        loadone(view);
        loadone(view);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.d("MainActivity", "requestCode:" + requestCode);
    }
}