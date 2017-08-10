package cn.com.sfn.juqi.dejson;

import org.json.JSONException;
import org.json.JSONObject;

import cn.com.sfn.juqi.model.StandardModel;
import cn.com.sfn.juqi.util.Config;

public class AppointDejson {
	public StandardModel dejson(String str) {
		StandardModel appointModel = new StandardModel();
		if (str.startsWith("\ufeff")) {
			str = str.substring(1);
		} else {
		}
		try {
			JSONObject jObject = new JSONObject(str);
			appointModel.setStatus(jObject.getInt("status"));
			appointModel.setInfo(jObject.getString("info"));
			Config.AppointFailedHint = jObject.getString("info");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return appointModel;
	}
}
