package com.ipws.eco.network;

import android.app.ProgressDialog;
import android.util.Base64;
import android.util.Log;
import android.util.Xml;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.ipws.eco.utils.Logs;

import java.lang.reflect.Array;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.Security;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;


import static java.security.spec.MGF1ParameterSpec.SHA256;

/**
 * Created by ziffi on 10/2/17.
 */

public class ServerRequest
{

        private void login(){

        }




//    private void serverRequest(String name, String password)
//    {
//
//        try{
//
//            String url ="http://54.252.182.129/cleaningMobileApi/API/PrepaidAPI.aspx?";
//            String parameter = "TxnType=PLOGIN&LOGINNAME1="+name+"&Msg=" +
//                    "LOGINNAME2:9595036832|PASS:"+password+"|ROLE:C|MACHINEIP:10.10.0.57|DOB:14-10-1990|TDTSM:2017-06-20%2012:30:38.287";
//            parameter ="TxnType=LOGIN&LOGINNAME1=9595036832&Msg=";
//            String encyParameter="LOGINNAME2:9595036832|PASS:Abc@123|ROLE:C|MACHINEIP:10.10.0.57|DOB:14-10-1990|TDTSM:2017-06-20%2012:30:38.287";
//            parameter+= encrypt(parameter, NetworkConstant.KEY).replace("/","_");
//
//            url = NetworkConstant.BASE_URL + NetworkConstant.LOGIN+parameter;
//            Logs.d("Server parameter:"+url);
//
////        url="http://54.252.182.129/cleaningMobileApi/API/PrepaidAPI.aspx?TxnType=PLOGIN&LOGINNAME1=9595036832&Msg=LOGINNAME2:9595036832|PASS:Ipws@123|ROLE:C|MACHINEIP:10.10.0.57|DOB:14-10-1990|TDTSM:2017-06-20%2012:30:38.287";
//
//            final ProgressDialog pDialog = new ProgressDialog(getActivity());
//            pDialog.setMessage("Loading...");
//            pDialog.show();
//            StringRequest strReq = new StringRequest(Request.Method.GET,
//                    url, new Response.Listener<String>() {
//                @Override
//                public void onResponse(String response) {
//                    Log.d(TAG, response.toString());
//                    parse(response);
//                    pDialog.hide();
//                }
//            }, new Response.ErrorListener() {
//                @Override
//                public void onErrorResponse(VolleyError error) {
//                    VolleyLog.d(TAG, "Error: " + error.getMessage());
//                    error.printStackTrace();
//                    pDialog.hide();
//                    Toast.makeText(getActivity(), "Unable to connect server.", Toast.LENGTH_SHORT).show();
//                }
//            });
//            RequestQueue mRequestQueue = Volley.newRequestQueue(getActivity());
//            mRequestQueue.add(strReq);
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//    }
//
//
//














    public static String Decrypt(String text, String key) throws Exception {
        Cipher cipher = Cipher.getInstance
                ("AES/CBC/PKCS5Padding"); //this parameters should not be changed
        byte[] keyBytes = new byte[16];
        byte[] b = key.getBytes("UTF-8");
        int len = b.length;
        if (len > keyBytes.length)
            len = keyBytes.length;
        System.arraycopy(b, 0, keyBytes, 0, len);
        SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");
        IvParameterSpec ivSpec = new IvParameterSpec(keyBytes);
        cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);
        byte[] results = new byte[text.length()];
//        BASE64Decoder decoder = new BASE64Decoder();
        try {
//            results = cipher.doFinal(decoder.decodeBuffer(text));
            results = cipher.doFinal(Base64.decode(results, Base64.DEFAULT));
        } catch (Exception e) {
            Log.i("Erron in Decryption", e.toString());
        }
        Log.i("Data", new String(results, "UTF-8"));
        return new String(results, "UTF-8"); // it returns the result as a String
    }

    public static String Encrypt(String text, String key) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        byte[] keyBytes = new byte[16];
        byte[] b = key.getBytes("UTF-8");
        int len = b.length;
        if (len > keyBytes.length)
            len = keyBytes.length;
        System.arraycopy(b, 0, keyBytes, 0, len);
        SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");
        IvParameterSpec ivSpec = new IvParameterSpec(keyBytes);
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);

        byte[] results = cipher.doFinal(text.getBytes("UTF-8"));
//        BASE64Encoder encoder = new BASE64Encoder();
//        return BASE64.encode(results); // it returns the result as a String
        return Base64.encodeToString(results, Base64.DEFAULT);
    }

    public static void main() throws Exception
    {

//            Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());

            byte[] input = "www.javacodegeeks.com".getBytes();

            byte[] keyBytes = new byte[]{0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09,

                    0x0a, 0x0b, 0x0c, 0x0d, 0x0e, 0x0f, 0x10, 0x11, 0x12, 0x13, 0x14, 0x15, 0x16, 0x17};

            SecretKeySpec key = new SecretKeySpec(keyBytes, "AES");

            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS7Padding", "BC");

            System.out.println(new String(input));

            // encryption pass

            cipher.init(Cipher.ENCRYPT_MODE, key);

            byte[] cipherText = new byte[cipher.getOutputSize(input.length)];

            int ctLength = cipher.update(input, 0, input.length, cipherText, 0);

            ctLength += cipher.doFinal(cipherText, ctLength);

            System.out.println(new String(cipherText).getBytes("UTF-8").toString());

            System.out.println(ctLength);

            // decryption pass

            cipher.init(Cipher.DECRYPT_MODE, key);

            byte[] plainText = new byte[cipher.getOutputSize(ctLength)];

            int ptLength = cipher.update(cipherText, 0, ctLength, plainText, 0);

            ptLength += cipher.doFinal(plainText, ptLength);

            System.out.println(new String(plainText));

            System.out.println(ptLength);
        }



}
