package com.example.rabbi.values;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class MyCacheManager {
    public static void setDefaults(String key, String value, Context context)
    {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.commit();
    }
    public static String getDefaults(String key, Context context)
    {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(key, "");
    }
    public static String getURI(Context context){
        String ip = MyCacheManager.getDefaults(Constants.ipTitle,context);
        String port = MyCacheManager.getDefaults(Constants.portTitle,context);
        String route = MyCacheManager.getDefaults(Constants.routeTitle,context);

        String URI = "http://"+ip+":"+port+"/"+route;

        if(URI.length()<15)
            return Fields.getServerUrl();
        else return URI;
    }
}