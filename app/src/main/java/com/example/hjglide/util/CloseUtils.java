package com.example.hjglide.util;

import java.io.Closeable;
import java.io.IOException;

/**
 * Created by paul on 15/12/28.
 */
public final class CloseUtils {
    private CloseUtils(){

    }
    public static void closeQuietly(Closeable closeable){
        if (null!=closeable){
            try {
                closeable.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
