package cn.com.sfn.juqi.dejson;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import cn.com.sfn.juqi.model.StandardModel;

public class StandardDejson {
    public StandardModel dejson(String str) {
        StandardModel registerModel = new StandardModel();
        if (str.startsWith("\ufeff")) {
            str = str.substring(1);
        } else {
        }
        try {
            JSONObject jObject = new JSONObject(str);
            registerModel.setStatus(jObject.getInt("status"));
            registerModel.setInfo(jObject.getString("info"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return registerModel;
    }
}
