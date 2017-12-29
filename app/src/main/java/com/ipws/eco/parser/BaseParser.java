package com.ipws.eco.parser;


import com.ipws.eco.model.BaseVO;
import com.ipws.eco.utils.AppUtils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by arun on 22/04/15.
 */

public abstract class BaseParser {
    protected String mJsonString;

    private BaseParser() {
    }

    public BaseParser(String jsonString) {
        try {
            mJsonString = jsonString;
            if (AppUtils.isStringDataValid(mJsonString)) {
                JSONObject jObj = null;
                try {
                    jObj = new JSONObject(mJsonString);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public abstract BaseVO parse();
}
