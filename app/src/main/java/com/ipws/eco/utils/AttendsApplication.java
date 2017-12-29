package com.ipws.eco.utils;

import android.app.Application;
import android.content.Context;

/**
 * Created by ziffi on 9/23/17.
 */

public class AttendsApplication extends Application
{

    private static Context mContext=null;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
    }



    public static Context getAttendsApplicationContext() {
        return mContext;
    }
}
