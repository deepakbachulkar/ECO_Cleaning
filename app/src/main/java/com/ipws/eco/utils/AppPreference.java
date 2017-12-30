package com.ipws.eco.utils;

import android.content.Context;
import android.content.SharedPreferences;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by ziffi on 9/23/17.
 */

public class AppPreference {

    private static AppPreference INSTANCE;
    private SharedPreferences preferences;
    private String PREFS_NAME = "eco";
    private String LOGIN = "login";
    private String LOGIN_NAME = "login_name";
    private String LOGIN_PASSWORD = "password";
    private String USER_ID = "user_id";
    private String LAST_LOGOUT = "last_logout";
    private String CLOCK_IN_OUT = "clock_in_out";
    private String DASH_BROAD_SCHEDULE ="dash_broad_schedule";
    private String DASH_BROAD_HOURS ="dash_broad_hours";
    private String DASH_BROAD_DATA ="dash_broad_data";

    private AppPreference(Context context){
        preferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public static AppPreference getInstance(Context context){
        if (INSTANCE == null) {
            INSTANCE = new AppPreference(context);
        }
        return INSTANCE;
    }

    public void setLogin(String data){
        preferences.edit().putString(LOGIN, data).commit();
    }


    public String getLogin(){
        String str="";
        if(preferences.getString(LOGIN,"")!=null)
            str = preferences.getString(LOGIN,"");
        return str;
    }

    public String getUserId(){
        try {
            if(preferences.getString(LOGIN,"")!=null) {
                String data = preferences.getString(LOGIN, "");
                JSONArray jsonArray = new JSONArray(data);
                JSONObject object = jsonArray.getJSONObject(0);
                String userId = object.getString("id");
                return userId;
            }
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
        return null;
    }

    public String getUserRoleId(){
        try {
            if(preferences.getString(LOGIN,"")!=null) {
                String data = preferences.getString(LOGIN, "");
                JSONArray jsonArray = new JSONArray(data);
                JSONObject object = jsonArray.getJSONObject(0);
                String userId = object.getString("role_id");
                return userId;
            }
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
        return null;
    }


    public String getUserName(){
        try {
            if(preferences.getString(LOGIN,"")!=null) {
                String data = preferences.getString(LOGIN, "");
                JSONArray jsonArray = new JSONArray(data);
                JSONObject object = jsonArray.getJSONObject(0);
                String userId = object.getString("user_name");
                return userId;
            }
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
        return null;
    }


    public void setLoginName(String data){
        preferences.edit().putString(LOGIN_NAME, data).commit();
    }




    public String getLoginName(){
        String str="";
        if(preferences.getString(LOGIN_NAME,"")!=null)
            str = preferences.getString(LOGIN_NAME,"");
        return str;
    }

    public void setPassword(String data){
        preferences.edit().putString(LOGIN_PASSWORD, data).commit();
    }

    public String getPassword(){
        String str="";
        if(preferences.getString(LOGIN_PASSWORD,"")!=null)
            str = preferences.getString(LOGIN_PASSWORD,"");
        return str;
    }

    public void setUserId(String data){
        preferences.edit().putString(USER_ID, data).commit();
    }

    public void setLastLogout(String data){
        preferences.edit().putString(LAST_LOGOUT, data).commit();
    }


    public String getLastLogout(){
        String str="";
        if(preferences.getString(LAST_LOGOUT,"")!=null)
            str = preferences.getString(LAST_LOGOUT,"");
        return str;
    }

    public void setClockInOut(boolean data){
        preferences.edit().putBoolean(CLOCK_IN_OUT, data).commit();
    }

    public boolean isLockOut(){
        boolean str=false;
            str = preferences.getBoolean(CLOCK_IN_OUT, false);
        return str;
    }

    public void setDashBroadSchedule(String data){
        preferences.edit().putString(DASH_BROAD_SCHEDULE, data).commit();
    }

    public String getDashBroadSchedule(){
        String str="";
        if(preferences.getString(DASH_BROAD_SCHEDULE,"")!=null)
            str = preferences.getString(DASH_BROAD_SCHEDULE,"");
        return str;
    }
    public void setDashBroadHours(String data){
        preferences.edit().putString(DASH_BROAD_HOURS, data).commit();
    }

    public void setDashBroadData(String data){
        preferences.edit().putString(DASH_BROAD_DATA, data).commit();
    }
    public String getDashBroadData(){
        String str="";
        if(preferences.getString(DASH_BROAD_DATA,"")!=null)
            str = preferences.getString(DASH_BROAD_DATA,"");
        return str;
    }

    public String getDashBroadHoursWeekly(){
        String str="";
        if(preferences.getString(DASH_BROAD_HOURS,"")!=null)
            str = preferences.getString(DASH_BROAD_HOURS,"");
        try{
            JSONArray objectArray = new JSONArray(str);
            JSONObject object = objectArray.getJSONObject(0);
            if(object!=null && object.has("WeekHrs")){
                return object.getString("WeekHrs");
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            return "0";
        }
    }


    public String getDashBroadHoursMonthly(){
        String str="";
        if(preferences.getString(DASH_BROAD_HOURS,"")!=null)
            str = preferences.getString(DASH_BROAD_HOURS,"");
        try{
            JSONArray objectArray = new JSONArray(str);
            JSONObject object = objectArray.getJSONObject(0);
            if(object!=null && object.has("MonthHrs")){
                return object.getString("MonthHrs");
            }
        }catch (Exception e){e.printStackTrace();}
        finally {
            return "0";
        }
    }
}
