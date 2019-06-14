package com.example.hjglide.util.libcore;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

import com.example.hjglide.util.CloseUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

/**
 * 磁盘缓存
 * Created by paul on 15/12/28.
 */
public class DiskCache implements ImageCache {
    private static final String TAG = DiskCache.class.getSimpleName();
    static String mPath ;
    public DiskCache(Context context){
        init(context);
    }
    private void init(Context context){
        // 获取图片缓存路径
        mPath = getDiskCachePath(context,"bitmap");
        File cacheDir = new File(mPath);
        if (!cacheDir.exists()) {
            cacheDir.mkdirs();
        }
    }
    @Override
    public Bitmap get(String key) {
        File file = new File(mPath+key);
        if (file.exists()){
            return BitmapFactory.decodeFile(mPath+key);
        }
        return null;
    }

    @Override
    public void put(String key, Bitmap bitmap) {
        FileOutputStream fileOutputStream = null;
        try {
            File file = new File(mPath+key);
            if (file.exists()){
                Log.i(TAG,"File is exist on disk");
            }
            fileOutputStream = new FileOutputStream(mPath+key);
            bitmap.compress(Bitmap.CompressFormat.PNG,100,fileOutputStream);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }finally {
            CloseUtils.closeQuietly(fileOutputStream);
        }
    }

    /**
     * 根据传入的dir获得路径
     * @param context
     * @param dir
     * @return
     */
    public String getDiskCachePath(Context context, String dir) {
        String cachePath;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            cachePath = context.getExternalCacheDir().getPath();
        } else {
            cachePath = context.getCacheDir().getPath();
        }
        return cachePath + File.separator + dir;
    }

}
