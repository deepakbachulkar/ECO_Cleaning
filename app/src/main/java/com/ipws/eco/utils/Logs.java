package com.ipws.eco.utils;

import android.util.Log;
/**
 * Created by Deepak on 10/7/17.
 */

public class Logs
{
    private static boolean isDebug=true;
    private static String TAG="ECO";

    public static void d(String log)
    {
        if(isDebug){
            Log.d(TAG, log);
        }
    }

    public static void d(String tag, String log)
    {
        if(isDebug){
            Log.d(tag, log);
        }
    }

    public static void e(String log)
    {
        if(isDebug){
            Log.e(TAG, log);
        }
    }

    public static void e(String tag, String log)
    {
        if(isDebug){
            Log.e(tag, log);
        }
    }
    public static void i(String log)
    {
        if(isDebug){
            Log.i(TAG, log);
        }
    }

    public static void i(String tag, String log)
    {
        if(isDebug){
            Log.i(tag, log);
        }
    }
}