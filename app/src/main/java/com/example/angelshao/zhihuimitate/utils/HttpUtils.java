package com.example.angelshao.zhihuimitate.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.ResponseHandlerInterface;

/**
 * AsyncHttpClient静态实例化的封装,通过HttpUtils类来调用网络
 */
public class HttpUtils {

    private static AsyncHttpClient client = new AsyncHttpClient();

    public static void get(String url, ResponseHandlerInterface responseHandler) {
        client.get(Constant.BASEURL + url, responseHandler);
    }

    public static void getImage(String url, ResponseHandlerInterface responseHandler) {
        client.get(Constant.BASEURL + url, responseHandler);
    }

    public static boolean isNetworkConnected(Context context) {
        if (context!=null) {
            ConnectivityManager manager= (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo info = manager.getActiveNetworkInfo();
            if (info!=null) {
                return info.isAvailable();
            }
        }
        return false;
    }

}
