package com.ipws.eco.utils;

/**
 * Created by abhishek on 5/26/16.
 */
public class RunTimePermissionRequestCode {
    public final static int CONTACT_REQUEST_CODE = 111;
    public final static int LOCATION_REQUEST_CODE = 112;
    public final static int SMS_REQUEST_CODE = 113;
    public final static int STORAGE_REQUEST_CODE = 114;
    public final static int MICROPHONE_REQUEST_CODE = 115;

    public final static String DONOT_SHOW_CONTACT_REQUEST = "donot_show_contact_request";
    public final static String DONOT_SHOW_LOCATION_REQUEST = "donot_show_location_request";
    public final static String DONOT_SHOW_SMS_REQUEST = "donot_show_sms_request";
    public final static String DONOT_SHOW_STORAGE_REQUEST = "donnot_show_storage_request";
    public final static String DONOT_SHOW_MICROPHONE_REQUEST = "donnot_show_microphone_request";

    public final static int PERMISSION_GRANTED_CODE = 1;
    public final static int PERMISSION_DENIED_CODE = 0;
    public final static int DONOT_SHOW_PERMISSION_DIALOG_CODE = -1;

    public final static String infoMessageCONTACT = "This Service requires permission to read CONTACT for Google Cloud Messaging. Do you want to proceed?";
    public final static String infoMessageLOCATION = "This Service requires permission to access Location. Do you want to proceed?";
    public final static String infoMessageSMS = "We would need permissions to read your SMS to auto verify your number. Do you wish to grant permissions?";
    public final static String infoMessageSTORAGE = "This Service requires permission to access photo, media and files on your device. Do you want to proceed?";
    public final static String infoMessageMICROPHONE = "This Service requires permission to access microphone of your device. Do you want to proceed?";

    public final static String toastMessageCONTACT = "It seems permission for Contact is denied. You need to grant this app permission for CONTACT from the App Settings";
    public final static String toastMessageLOCATION = "It seems permission for Location is denied. You need to grant permission for LOCATION from the App Settings";
    public final static String toastMessageSMS = "It seems permission for sms is denied. You need to grant this app permission for SMS from the App Settings";
    public final static String toastMessageSTORAGE = "It seems permission for storage is denied. You need to grant this app permission for STORAGE from the App Settings";
    public final static String toastMessageMICROPHONE = "It seems permission for microphone is denied. You need to grant this app permission for MICROPHONE from the App Settings";
    public final static String toastMessageMICROPHONEorSTORAGE = "It seems permission for microphone or storage is denied. You need to grant this app permission for MICROPHONE and STORAGE from the App Settings";

}
