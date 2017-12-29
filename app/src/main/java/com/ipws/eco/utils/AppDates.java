package com.ipws.eco.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.StringTokenizer;

/**
 * Created by ziffi on 10/8/17.
 */

public class AppDates
{
    private  static AppDates instance;
    public static String    APP_SHOW_DATE_FORMAT ="dd-MM-yyyy";
    public static String APP_SEND_DATE_FORMAT ="yyyy-dd-MM";
    public static String APP_SHOW_DATE_FORMAT_2 ="dd-MMM-yyyy";


    private AppDates()
    {
    }

    public static AppDates with(){
        if(instance== null)
          instance = new  AppDates();
        return instance;
    }

//"MMM MM dd, yyyy h:mm a"
    public String currentDateTime(String format)
    {
        String strDate=null;
        try{
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat mdformat = new SimpleDateFormat(format);
            strDate = mdformat.format(calendar.getTime());
            Logs.d("Date current:"+strDate);
        }catch (Exception e){
            e.printStackTrace();
        }
        return strDate;
    }

    public String currentNextDateTime(String format)
    {
        String strDate=null;
        try{
            Calendar calendar = Calendar.getInstance();
            int d = calendar.get(Calendar.DATE);
            calendar.set(Calendar.DATE, d+1);
            SimpleDateFormat mdformat = new SimpleDateFormat(format);
            strDate = mdformat.format(calendar.getTime());
            Logs.d("Date current:"+strDate);
        }catch (Exception e){
            e.printStackTrace();
        }
        return strDate;
    }

    public long stringToMillSec(String date, String format)
    {
        long longMill=0;
//        String givenDateString = "Tue Apr 23 16:08:28 GMT+05:30 2013";
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        try {
            Date mDate = sdf.parse(date);
            long timeInMilliseconds = mDate.getTime();
            return timeInMilliseconds;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return longMill;
    }

    public String stringToString(String date, String format, String format2)
    {
//        String givenDateString = "Tue Apr 23 16:08:28 GMT+05:30 2013";
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        try {
            Date mDate = sdf.parse(date);
            long timeInMilliseconds = mDate.getTime();
            SimpleDateFormat mdformat = new SimpleDateFormat(format2);
            return  mdformat.format(timeInMilliseconds);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

    public String timeCanvert(String startTime)
    {
        StringTokenizer tk = new StringTokenizer(startTime);
//        String date = tk.nextToken();
        String time = tk.nextToken();

        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss");
        SimpleDateFormat sdfs = new SimpleDateFormat("hh:mm a");
        Date dt;
        try {
            dt = sdf.parse(startTime);
            return sdfs.format(dt); // <-- I got result here
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }
}
