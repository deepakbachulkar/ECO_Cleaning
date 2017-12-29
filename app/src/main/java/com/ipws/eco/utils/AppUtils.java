package com.ipws.eco.utils;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.UrlQuerySanitizer;
import android.os.Bundle;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Shruthi on 4/24/15.
 */
public class AppUtils {

    private static  boolean isDebug= true;
    public static boolean isValidEmail(String email) {
//        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
//                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
//        String EMAIL_PATTERN = "([^ ])+@" + "([^ ]+)" + "(\\.[^ \\.]+)";
//        String EMAIL_PATTERN = "^[\\\\w-\\\\+]+(\\\\.[\\\\w]+)*@[\\\\w-]+(\\\\.[\\\\w]+)*(\\\\.[a-z]{2,})$";
//                ".+@" + "[A-Za-z0-9-]+(\\.[.]+)*(\\.[.])$";
        final Pattern pattern = Patterns.EMAIL_ADDRESS;
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public static boolean isValidPassword(String pass) {
        if (pass == null) {
            return false;
        } else if (pass.length() == 0) {
            return false;
        } else if (pass.equals("")) {
            return false;
        } else if (pass.length() < 6) {
            return false;
        }
        return true;
    }

    public static boolean isValidMobileNumber(String str) {
        try {
            long number = Long.parseLong(str);
        } catch (Exception e) {
            return false;
        }
        if (str == null)
            return false;
        else if ("".equals(str))
            return false;
        else if (str.length() != 10)
            return false;
        else return true;
    }

    public static boolean isEmptyField(String value) {
        return value != null && value.trim().length() > 0;
    }

//    public static BitmapDrawable writeTextOnDrawable(int drawableId, String text, float textSize, float xposition, float yposition) {
//
//        String[] texts = text.split("#");
//
//        Bitmap bm = BitmapFactory.decodeResource(ZiffiApplication.getZiffiApplicationContext().getResources(), drawableId)
//                .copy(Bitmap.Config.ARGB_8888, true);
//
//        Typeface tf = Typeface.create("Helvetica", Typeface.BOLD);
//
//        Paint paint = new Paint();
//        paint.setStyle(Paint.Style.FILL);
//        paint.setColor(Color.WHITE);
//        paint.setTypeface(tf);
//        paint.setTextAlign(Paint.Align.CENTER);
//        paint.setTextSize(convertToPixels(ZiffiApplication.getZiffiApplicationContext(), textSize));
//
//        Rect textRect = new Rect();
//        paint.getTextBounds(text, 0, text.length(), textRect);
//
//        Canvas canvas = new Canvas(bm);
//
//        //If the text is bigger than the canvas , reduce the font size
//        //if (textRect.width() >= (canvas.getWidth() - 4))     //the padding on either sides is considered as 4, so as to appropriately fit in the text
//        //paint.setTextSize(convertToPixels(ZiffiApplication.getZiffiApplicationContext(), textSize/2));        //Scaling needs to be used for different dpi's
//
//        while ((textRect.width() >= (canvas.getWidth() - 4))) {
//            textSize = textSize / 2;
//            paint.setTextSize(convertToPixels(ZiffiApplication.getZiffiApplicationContext(), textSize));
//            paint.getTextBounds(text, 0, text.length(), textRect);
//        }
//
//        //Calculate the positions
//        float xPos = (canvas.getWidth() / 2) + xposition;//xhdpi 45 //mdpi 10     //-2 is for regulating the x position offset
//
//        //"- ((paint.descent() + paint.ascent()) / 2)" is the distance from the baseline to the center.
//        //int yPos = (int) ((canvas.getHeight() / 2) - ((paint.descent() + paint.ascent())));
//        float addy = yposition;//xhdpi 40 //mdpi 10
//        for (String str : texts) {
//            canvas.drawText(str, xPos, paint.descent() + addy, paint);
//            addy += yposition;//50
//        }
//
//        return new BitmapDrawable(ZiffiApplication.getZiffiApplicationContext().getResources(), bm);
//    }


    public static float convertToPixels(Context context, float nDP) {
        //return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
        final float conversionScale = context.getResources().getDisplayMetrics().density;
        return ((nDP * conversionScale) + 0.5f);

    }

    public static boolean isStringDataValid(String data) {
        if (data == null)
            return false;
        else if (data.equals("null"))
            return false;
        else if (data.equals("false"))
            return false;
        else if (data.equals(""))
            return false;
        else
            return true;
    }

    public static void saveDataToFile(String fileName, String data, Context context) {
        FileOutputStream fos = null;
        try {
            fos = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            fos.write(data.getBytes());
            fos.close();
        } catch (Exception e) {
            if (fos != null)
                try {
                    fos.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
        }
    }


    public static String getDataFromFile(String fileName, Context context) {
        FileInputStream fis = null;
        StringBuilder builder = new StringBuilder();
        try {
            fis = context.openFileInput(fileName);
            int c;
            while ((c = fis.read()) != -1) {
                builder.append((char) c);
            }
        } catch (Exception e) {
            try {
                if (fis != null)
                    fis.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            return null;
        }
        return builder.toString();
    }

    public static int deleteFile(String fileName, Context context) {
        int deletedItemCount = 0;
        String jsonStr = getDataFromFile(fileName, context);
        if (jsonStr != null) {
            try {
                JSONArray jsonArray = new JSONArray(jsonStr);
                if (jsonArray != null) {
                    deletedItemCount = jsonArray.length();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        context.deleteFile(fileName);
        return deletedItemCount;
    }

    /*public static void logOutUserAndShowLogin(){
        User.getInstance().setId(0);
        User.getInstance().setEmail(null);
        User.getInstance().setErrorMessage("Please login to continue.");
        User.getInstance().setNewInstance();
        ZiffiApplication.setBooleanZiffiPreference(ZiffiApplication.PREFERENCE_KEY_LAST_LOGGED_IN_TYPE, false);
    }*/

    public static boolean isNetworkConnected(Context context) {

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        //should check null because in air plan mode it will be null
        return (netInfo != null && netInfo.isConnected());

    }

    public static double returnStringDataAsDouble(String data) {
        try {
            data = data.trim();
            return Double.parseDouble(data);
        } catch (NumberFormatException e) {
            return 0;
        } catch (NullPointerException e) {
            return 0;
        }
    }

    public static int returnStringDataAsInteger(String data) {
        try {
            data = data.trim();
            return Integer.parseInt(data);
        } catch (NumberFormatException e) {
            return 0;
        } catch (NullPointerException e) {
            return 0;
        }
    }

    public static long returnStringDataAsLong(String data) {
        try {
            data = data.trim();
            return Long.parseLong(data);
        } catch (NumberFormatException e) {
            return 0;
        } catch (NullPointerException e) {
            return 0;
        }
    }

    public static Bitmap LoadImageFromWebOperations(String url) {
        try {
            InputStream is = (InputStream) new URL(url).getContent();
            return BitmapFactory.decodeStream(is);
        } catch (Exception e) {
            return null;
        }
    }


    public static boolean compareDate(String date) {
        try {
            Calendar today = Calendar.getInstance();
            int day = Integer.parseInt(date.substring(0, 2));
            int month = Integer.parseInt(date.substring(3, 5));
            int year = Integer.parseInt(date.substring(6, 10));
            int hour = Integer.parseInt(date.substring(11, 13));
            int minute = Integer.parseInt(date.substring(14, 16));
            int seconds = Integer.parseInt(date.substring(17));
            Calendar Day = Calendar.getInstance();
            Day.set(Calendar.DAY_OF_MONTH, day);
            Day.set(Calendar.MONTH, month - 1);
            Day.set(Calendar.YEAR, year);
            Day.set(Calendar.HOUR, hour);
            Day.set(Calendar.MINUTE, minute);
            Day.set(Calendar.SECOND, seconds);

            long diff = today.getTimeInMillis() - Day.getTimeInMillis();
            if (diff < 0)
                return true;
            else
                return false;
        } catch (Exception e) {
            return false;
        }
    }

    public static String getDateRepresentation(Calendar calendar) {
        if (calendar != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            return sdf.format(new Date(calendar.getTimeInMillis()));
        } else {
            return "";
        }

    }

//    public static HashMap getDataFromBundle(Bundle arguments) {
//        HashMap map = new HashMap();
//        if (arguments != null) {
//            for (String key : arguments.keySet()) {
//                Object value = arguments.get(key);
//                try {
//                    if (!(value instanceof BaseVO))
//                        map.put(key, value.toString());
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//        return map;
//    }

    public static Bundle getBundleFromMap(HashMap<String, String> map) {
        Bundle bundle = new Bundle();
        if (map != null) {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                bundle.putString(key, value);
            }
        }
        return bundle;
    }

    public static String getStandardDateFormat(int day, int month, int year) {
        String monthText = "";
        switch (month) {
            case 1:
                monthText = "January";
                break;
            case 2:
                monthText = "February";
                break;
            case 3:
                monthText = "March";
                break;
            case 4:
                monthText = "April";
                break;
            case 5:
                monthText = "May";
                break;
            case 6:
                monthText = "June";
                break;
            case 7:
                monthText = "July";
                break;
            case 8:
                monthText = "August";
                break;
            case 9:
                monthText = "September";
                break;
            case 10:
                monthText = "October";
                break;
            case 11:
                monthText = "November";
                break;
            case 12:
                monthText = "December";
                break;
            default:
                monthText = "";
        }

        String substring = "";
        switch (day) {
            case 1:
            case 21:
            case 31:
                substring = "st";
                break;
            case 2:
            case 22:
                substring = "nd";
                break;
            case 3:
            case 23:
                substring = "rd";
                break;
            default:
                substring = "th";
        }
        String standardDate = day + "" + substring + " " + monthText + ", " + year;
        return standardDate;
    }

    public static Drawable getAssetImage(Context context, String filename) throws IOException {
        AssetManager assets = context.getResources().getAssets();
        InputStream buffer = new BufferedInputStream((assets.open("drawable/" + filename + ".png")));
        Bitmap bitmap = BitmapFactory.decodeStream(buffer);
        return new BitmapDrawable(context.getResources(), bitmap);
    }


    public static String mapToString(HashMap<String, Integer> map) {
        if (!map.isEmpty()) {
            String others = "";
            for (Map.Entry entry : map.entrySet()) {
                String key = (String) entry.getKey();
                if (others.equals("")) {
                    others = key;
                } else {
                    others += "," + key;
                }
            }
            return others;
        }
        return null;
    }


    public static class MySpannable extends ClickableSpan {

        private boolean isUnderline = true;

        /**
         * Constructor
         */
        public MySpannable(boolean isUnderline) {
            this.isUnderline = isUnderline;
        }

        @Override
        public void updateDrawState(TextPaint ds) {

            ds.setUnderlineText(isUnderline);
            ds.setColor(Color.parseColor("#f31258"));

        }

        @Override
        public void onClick(View widget) {

        }
    }

    public static String hashMapTOOrderedString(HashMap<String, Integer> map) {

        if (!map.isEmpty()) {
            Set<String> keySet = map.keySet();
            ArrayList<String> arrayList = new ArrayList<>(keySet);
            Collections.sort(arrayList);
            return implodeArrayList(arrayList, ",");
            //String[] array = keySet.toArray();
            //Arrays.sort(array);
            //return implodeArray(array);
        }

        return null;
    }

    public static String implodeArray(String[] inputArray, String glueString) {
        String output = "";

        if (inputArray.length > 0) {
            StringBuilder sb = new StringBuilder();
            sb.append(inputArray[0]);

            for (int i = 1; i < inputArray.length; i++) {
                sb.append(glueString);
                sb.append(inputArray[i]);
            }

            output = sb.toString();
        }

        return output;
    }

    public static String implodeArrayList(ArrayList<String> inputArray, String glueString) {
        String output = "";

        if (inputArray.size() > 0) {
            StringBuilder sb = new StringBuilder();
            sb.append(inputArray.get(0));

            for (int i = 1; i < inputArray.size(); i++) {
                sb.append(glueString);
                sb.append(inputArray.get(i));
            }

            output = sb.toString();
        }

        return output;
    }

    public static HashMap<String, Object> jsonToMap(JSONObject json) throws JSONException {
        HashMap<String, Object> retMap = new HashMap<String, Object>();

        if (json != JSONObject.NULL) {
            retMap = toMap(json);
        }
        return retMap;
    }

    public static HashMap<String, Object> toMap(JSONObject object) throws JSONException {
        HashMap<String, Object> map = new HashMap<String, Object>();

        Iterator<String> keysItr = object.keys();
        while (keysItr.hasNext()) {
            String key = keysItr.next();
            Object value = object.get(key);

            if (value instanceof JSONArray) {
                value = toList((JSONArray) value);
            } else if (value instanceof JSONObject) {
                value = toMap((JSONObject) value);
            }
            map.put(key, value);
        }
        return map;
    }

    public static List<Object> toList(JSONArray array) throws JSONException {
        List<Object> list = new ArrayList<Object>();
        for (int i = 0; i < array.length(); i++) {
            Object value = array.get(i);
            if (value instanceof JSONArray) {
                value = toList((JSONArray) value);
            } else if (value instanceof JSONObject) {
                value = toMap((JSONObject) value);
            }
            list.add(value);
        }
        return list;
    }

    public static HashMap<String, String> decodeUrlToParams(String uri) {
        HashMap<String, String> dataMap = new HashMap<>();

        try {
            UrlQuerySanitizer urlQuerySanitizer = new UrlQuerySanitizer();
            urlQuerySanitizer.setAllowUnregisteredParamaters(true);
            urlQuerySanitizer.setUnregisteredParameterValueSanitizer(UrlQuerySanitizer.getAllButNulLegal());
            urlQuerySanitizer.parseUrl(uri);
            for (String paramName : urlQuerySanitizer.getParameterSet()) {
                dataMap.put(paramName, URLDecoder.decode(urlQuerySanitizer.getValue(paramName), "UTF-8"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dataMap;
    }

    public static String toCSV(String[] array) {
        String result = "";
        if (array.length > 0) {
            StringBuilder sb = new StringBuilder();
            for (String s : array) {
                sb.append(s).append(",");
            }
            result = sb.deleteCharAt(sb.length() - 1).toString();
        }
        return result;
    }

    public static void showSoftKeyboard(EditText et){
        if(et != null){
            InputMethodManager imm =  (InputMethodManager) AttendsApplication.getAttendsApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(et, InputMethodManager.SHOW_IMPLICIT);
        }
    }

    public static void hideSoftKeyboard(EditText et){
        if(et != null){
            InputMethodManager imm = (InputMethodManager) AttendsApplication.getAttendsApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(et.getWindowToken(), 0);
        }

    }



}
