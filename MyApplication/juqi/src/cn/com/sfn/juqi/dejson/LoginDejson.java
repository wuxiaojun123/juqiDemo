/**
 * 处理登录的返回结果
 */
package cn.com.sfn.juqi.dejson;

import org.json.JSONException;
import org.json.JSONObject;

import cn.com.sfn.juqi.model.LoginModel;

public class LoginDejson {
    
	public LoginModel dejson(String str) {
		LoginModel loginModel = new LoginModel();
		if (str.startsWith("\ufeff")) {
			str = str.substring(1);
		} else {
		}
		try {
			JSONObject jObject = new JSONObject(str);
			loginModel.setStatus(jObject.getInt("status"));
			loginModel.setUserid(jObject.getString("id"));
			loginModel.setState(jObject.getString("state"));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return loginModel;
	}
	
}
