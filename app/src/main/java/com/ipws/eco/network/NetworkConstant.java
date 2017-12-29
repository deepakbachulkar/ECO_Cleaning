package com.ipws.eco.network;

/**
 * Created by ziffi on 10/7/17.
 */

public interface NetworkConstant
{
//    String dev =      "http://54.252.232.149/cleaningMobileApidev/API/PrepaidAPI.aspx?";
//    String test      ="http://54.252.232.149/cleaningMobileApi/API/PrepaidAPI.aspx?";
//    String BASE_URL = "http://54.252.182.129/cleaningMobileApi/API/PrepaidAPI.aspx?";

    String dev = "http://mycleaning.online/cleaningMobileApidev/API/PrepaidAPI.aspx?";
    String test="http://mycleaning.online/cleaningMobileApi/API/PrepaidAPI.aspx?";

    String BASE_URL = dev;

    String KEY = "IPWEBSOFT@2@9#2017";
    String LOGIN="PrepaidAPI.aspx?";
    String STAFF_IN_OUT="PrepaidAPI.aspx?";
    public static String user ="patil@ipwebsoft.com", password="Ipws@123";

//    http://54.252.182.129/cleaningMobileApi/API/PrepaidAPI.aspx?TxnType=PLOGIN&LOGINNAME1=9595036832&Msg=MACHINEIP:10.10.10.10|TDTSM:2017-08-19%2019*02*10.093|GUID:7987989-98789-98798|LOGINNAME2:9595036832|PASS:Ipws@123



//    [Base URL]?TxnType=PLOGIN&LOGINNAME1=9595036832&Msg=MACHINEIP:10.10.10.10|TDTSM:2017-08-19 19:02:10.093|GUID=7987989-98789-98798|LOGINNAME2:9595036832|PASS:Ipws@123
//
//    http://54.252.182.129/cleaningMobileApi/API/PrepaidAPI.aspx?

//    string LOGIN = ***WebAPIUrl + "?TxnType=&USERID=&Msg=" +              AESCrypt.Encrypt("MACHINEIP=10.10.0.58|TDTSM=2017-08-19 19:02:18.093|GUID=8ABB2315-0A67-4C3F-BDFB-64DDD198A2B3|USERID=ashish.ghorpade@gmail.com|PWD=Abc@123", AESCrypt.KEY).Replace("/", "_");

//        url="http://54.252.182.129/cleaningMobileApi/API/PrepaidAPI.aspx?TxnType=PLOGIN&LOGINNAME1=9595036832&Msg=LOGINNAME2:9595036832|PASS:Ipws@123|ROLE:C|MACHINEIP:10.10.0.57|DOB:14-10-1990|TDTSM:2017-06-20%2012:30:38.287";

}