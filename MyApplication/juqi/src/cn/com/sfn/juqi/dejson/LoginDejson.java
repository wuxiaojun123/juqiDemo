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
            int status = jObject.getInt("status");
            loginModel.setStatus(status);
            if (status == 0) {
                loginModel.setInfo(jObject.getString("info"));
            } else {
                loginModel.setUserid(jObject.getString("id"));
                loginModel.setState(jObject.getString("state"));
                loginModel.setSession_id(jObject.getString("session_id"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return loginModel;
    }

}
