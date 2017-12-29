package com.ipws.eco.ui.controls;

import android.app.TimePickerDialog;
import android.content.Context;
import android.widget.TimePicker;

import java.util.Calendar;

/**
 * Created by ziffi on 10/28/17.
 */
public class AppDialogs
{
    private  Context mContext;
    private static AppDialogs dialogs;

    private AppDialogs(Context context){
        mContext = context;
    }

    public static AppDialogs with(Context context){
        if(dialogs==null)
            dialogs = new AppDialogs(context);
        return dialogs;
    }

    public void showTimePicker(String dialogTitle, TimePickerDialog.OnTimeSetListener listener){
        Calendar mCurrentTime = Calendar.getInstance();
        int hour = mCurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mCurrentTime.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(mContext, listener, hour, minute, true);//Yes 24 hour time
        mTimePicker.setTitle(dialogTitle);
        mTimePicker.show();
    }
}