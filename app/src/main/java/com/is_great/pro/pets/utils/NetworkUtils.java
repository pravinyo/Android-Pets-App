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

    private static final String SERVER_BASE_URL="";
    private static final String appid="";

    public static URL getUrl(Context context){
        return buildUrl(appid);
    }

    private static URL buildUrl(String appid) {
        Uri serverQueryUri = Uri.parse(SERVER_BASE_URL).buildUpon()
                .appendQueryParameter("appid",appid)
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
