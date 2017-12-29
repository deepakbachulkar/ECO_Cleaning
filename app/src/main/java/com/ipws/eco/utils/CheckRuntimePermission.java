package com.ipws.eco.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.ipws.eco.ui.activity.BaseActivity;

/**
 * Created by abhishek on 5/26/16.
 */
public class CheckRuntimePermission extends BaseActivity {
    int isAllowed = 0;
    RunTimePermissionRequestCode runTimePermissionRequestCode = new RunTimePermissionRequestCode();

    public static final String MyPREFERENCES = "MyPrefs" ;
    SharedPreferences sharedpreferences;

    public CheckRuntimePermission(SharedPreferences sharedpreferences) {
        this.sharedpreferences = sharedpreferences;
    }

    public int IsPermissionAllowed(final Activity activity, final Context context, final String[] permissions, final int requestCode, final String DonotShowAgainOption, final String InfoMessage){

        sharedpreferences = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();

        int permissionCount = permissions.length;
        int p = 0;
        for(int i = 0; i < permissionCount; i++){
            p += ContextCompat.checkSelfPermission(context, permissions[i]);
        }


        if(p != PackageManager.PERMISSION_GRANTED){

            boolean isAnyPermissionDenied = false;
            for (int i = 0; i < permissionCount; i++){
                isAnyPermissionDenied = isAnyPermissionDenied || ActivityCompat.shouldShowRequestPermissionRationale(activity, permissions[i]);
            }

            if(isAnyPermissionDenied){
                editor.putString(DonotShowAgainOption, "NotClicked");
                editor.commit();

                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
                alertBuilder.setTitle("Permission Required!");
                alertBuilder.setMessage(InfoMessage);

                alertBuilder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityCompat.requestPermissions(activity, permissions, requestCode );
                    }
                });

                alertBuilder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        isAllowed = runTimePermissionRequestCode.PERMISSION_DENIED_CODE;
                        dialog.dismiss();

//                        EventBusVO eventBusVO = new EventBusVO("NoClicked", null);
//                        EventBus.getDefault().post(eventBusVO);
                    }
                });

                final AlertDialog alertDialog = alertBuilder.create();
                Handler myHandler = new Handler();
                myHandler.post(new Runnable() {
                    @Override
                    public void run() {

                        if(!activity.isFinishing()){

                            if(!permissions[0].equalsIgnoreCase(PermissionsList.ACCESS_FINE_LOCATION) && !permissions[0].equalsIgnoreCase(PermissionsList.ACCESS_COARSE_LOCATION)){
                                alertDialog.show();
                            } else{
                                ActivityCompat.requestPermissions(activity, permissions, requestCode );
                            }

                        }
                    }
                });

            } else{
                sharedpreferences = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
                String spText = sharedpreferences.getString(DonotShowAgainOption, "");
                if(spText.equals("NotClicked")){
                    editor.putString(DonotShowAgainOption, "Clicked");
                    editor.commit();
                }

                sharedpreferences = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
                String spText2 = sharedpreferences.getString(DonotShowAgainOption, "");
                if(spText2.equals("Clicked")){
                    isAllowed = runTimePermissionRequestCode.DONOT_SHOW_PERMISSION_DIALOG_CODE;
                }

                ActivityCompat.requestPermissions(activity, permissions, requestCode );
            }

        } else{
            isAllowed = runTimePermissionRequestCode.PERMISSION_GRANTED_CODE;
        }

        return isAllowed;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
//        switch (requestCode){
//            case PermissionRequestCode.READ_CALENDAR_REQUEST_CODE:
        if(grantResults.length > 0){
            isAllowed = 1;
        } else{
            isAllowed = 0;
        }
        return;


//        }
    }
}
