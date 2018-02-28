package com.is_great.pro.pets.utils;

import android.content.Context;
import android.net.Uri;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by tripa on 2/18/2018.
 */

public class NetworkUtils {
    private static final String TAG = NetworkUtils.class.getSimpleName();

    private static final String SERVER_BASE_URL="http://192.168.0.6/pet/index.php";

    public static String getStringUrl(Context context){
        return SERVER_BASE_URL;
    }
    public static URL getUrl(Context context){
        return buildUrl();
    }

    private static URL buildUrl() {
        Uri serverQueryUri = Uri.parse(SERVER_BASE_URL).buildUpon()
                .build();

        try{
            URL serverQueryUrl = new URL(serverQueryUri.toString());
            return serverQueryUrl;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
