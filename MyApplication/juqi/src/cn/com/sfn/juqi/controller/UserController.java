/**
 * 有关用户的后台操作
 *
 * @author wangwb
 */

package cn.com.sfn.juqi.controller;

import java.util.ArrayList;
import java.util.List;

import cn.com.sfn.juqi.dejson.BillDejson;
import cn.com.sfn.juqi.dejson.LoginDejson;
import cn.com.sfn.juqi.dejson.StandardDejson;
import cn.com.sfn.juqi.dejson.UserDejson;
import cn.com.sfn.juqi.model.AccountModel;
import cn.com.sfn.juqi.model.AuthModel;
import cn.com.sfn.juqi.model.BillModel;
import cn.com.sfn.juqi.model.FriendModel;
import cn.com.sfn.juqi.model.LoginModel;
import cn.com.sfn.juqi.model.StandardModel;
import cn.com.sfn.juqi.model.UserModel;
import cn.com.sfn.juqi.net.MyHttpClient;
import cn.com.sfn.juqi.util.Config;
import cn.com.wx.util.JsonUtils;
import cn.com.wx.util.LogUtils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.TextUtils;
import android.util.Log;

public class UserController {
    private SharedPreferences setting;
    private MyHttpClient httpClient;

    public UserController() {
        httpClient = new MyHttpClient();
    }

    /**
     * 调用网络层进行登录,如果登录成功将用户名保存到sharePerference
     *
     * @param username
     * @param password
     * @return 登录结果
     */
    public int login(String username, String password, Context context) {
        setting = context.getSharedPreferences(Config.PREFS_NAME,
                Context.MODE_PRIVATE);
        String action = "user/login/dologin";
        String param = "username=" + username + "&password=" + password;
        // 发送登陆请求 18810543162   123456
        String str = httpClient.doPost(context, action, param);
        if (!TextUtils.isEmpty(str)) {
            LogUtils.e("登录返回信息是:" + str);
//            LoginModel loginModel = (LoginModel) JsonUtils.toObject(str, LoginModel.class);
            LoginDejson logindejson = new LoginDejson();
            LoginModel loginModel = logindejson.dejson(str);
            // 判断是否登陆成功
            if (loginModel.getStatus() == 1) {
                // 记住登录状态
                Editor editor = setting.edit();
                // 存入数据
                editor.putString("userid", loginModel.getId());
                // 提交修改
                editor.commit();
                Config.login_userid = loginModel.getId();
                return Config.LoginSuccess;
            } else
                return Config.LoginFailed;
        }
        return -1;
        /*LoginDejson logindejson = new LoginDejson();
        LoginModel login = logindejson.dejson(str);
        if (login == null)
            return -1;
        // 判断是否登陆成功
        if (login.getStatus() == 1) {
            // 记住登录状态
            Editor editor = setting.edit();
            // 存入数据
            editor.putString("userid", login.getUserid());
            // 提交修改
            editor.commit();
            Config.login_userid = login.getUserid();
            return Config.LoginSuccess;
        } else
            return Config.LoginFailed;*/
    }

    // 修改密码
    public int changePassword(String old, String newpassword, String repass) {
        String action = "user/profile/password_post";
        String param = "old_password=" + old + "&password=" + newpassword
                + "&repassword=" + repass;
        String str = httpClient.doPost(action, param);
        StandardDejson changedejson = new StandardDejson();
        StandardModel changepass = changedejson.dejson(str);
        if (changepass == null)
            return -1;
        if (changepass.getStatus() == 1) {
            return Config.ChangePasswordSuccess;
        } else
            return Config.ChangePasswordFailed;
    }

    public int logoff() {
        String action = "user/index/logout";
        String str = httpClient.doGet(action);
        StandardDejson outdejson = new StandardDejson();
        StandardModel out = outdejson.dejson(str);
        if (out == null)
            return -1;
        if (out.getStatus() == 1) {
            return Config.LogoutSuccess;
        } else
            return Config.LogoutFailed;
    }

    public int register(String mobile, String password, String verify) {
        String action = "user/register/doregister";
        String param = "mobile=" + mobile + "&password=" + password
                + "&verify=" + verify;
        // 发送登陆请求
        String str = httpClient.doPost(action, param);
        StandardDejson registerdejson = new StandardDejson();
        StandardModel register = registerdejson.dejson(str);
        if (register == null)
            return -1;
        // 判断是否注册成功
        if (register.getStatus() == 1) {
            return Config.RegisterSuccess;
        } else
            return Config.RegisterFailed;
    }

    public int forget(String mobile, String password, String verify) {
        String action = "user/login/doforgot_password";
        String param = "mobile=" + mobile + "&password=" + password
                + "&verify=" + verify;
        // 发送登陆请求
        String str = httpClient.doPost(action, param);
        StandardDejson forgetdejson = new StandardDejson();
        StandardModel forget = forgetdejson.dejson(str);
        if (forget == null)
            return -1;
        // 判断是否注册成功
        if (forget.getStatus() == 1) {
            return Config.ForgetPasswordSuccess;
        } else
            return Config.ForgetPasswordFailed;
    }

    public int validate(String mobile, String verify) {
        String action = "user/center/verify_mobile";
        String param = "mobile=" + mobile + "&verify=" + verify;
        // 发送登陆请求
        String str = httpClient.doPost(action, param);
        StandardDejson validatedejson = new StandardDejson();
        StandardModel validate = validatedejson.dejson(str);
        if (validate == null)
            return -1;
        // 判断是否注册成功
        if (validate.getStatus() == 1) {
            return Config.ValidateSuccess;
        } else
            return Config.ValidateFailed;
    }

    public UserModel getInfo(String id) {
        UserModel user = new UserModel();
        String action = "user/center/index";
        String param = "id=" + id;
        System.out.println("用户" + param);
        String str = httpClient.doPost(action, param);
        LogUtils.e("用户信息是:" + str);
        if (str.equals("time out")) {
            return null;
        } else {
            UserDejson userDejson = new UserDejson();
            user = userDejson.userInfoDejson(str);
            if (user == null) {
                return null;
            } else {
                return user;
            }
        }
    }

    public int editInfo(String nickname, String sex, String age, String uage,
                        String height, String weight, String offense, String defense,
                        String comprehensive, String standard, String position,
                        String signature, String grade) {
        String action = "user/profile/edit_post";
        String param = "user_nicename=" + nickname + "&sex=" + sex + "&age="
                + age + "&u_age=" + uage + "&height=" + height + "&weight="
                + weight + "&offense=" + offense + "&defense=" + defense
                + "&comprehensive=" + comprehensive + "&standard=" + standard
                + "&position=" + position + "&signature=" + signature
                + "&grade=" + grade;
        // 发送登陆请求
        String str = httpClient.doPost(action, param);
        StandardDejson editdejson = new StandardDejson();
        StandardModel edit = editdejson.dejson(str);
        if (edit == null)
            return -1;
        // 判断是否注册成功
        if (edit.getStatus() == 1) {
            return Config.EditSuccess;
        } else
            return Config.EditFailed;
    }

    public int addFriend(String id) {
        String action = "user/center/add_friends";
        String param = "id=" + id;
        // 发送加好友请求
        String str = httpClient.doPost(action, param);
        StandardDejson editdejson = new StandardDejson();
        StandardModel add = editdejson.dejson(str);
        if (add == null)
            return -1;
        // 判断是否添加成功
        if (add.getStatus() == 1) {
            return Config.AddFriendSuccess;
        } else
            return Config.AddFriendFailed;
    }

    public FriendModel friendDetail(String id) {
        FriendModel friend = new FriendModel();
        String action = "user/index/index";
        String param = "id=" + id;
        String str = httpClient.doPost(action, param);
        Log.e("ceshi", str);
        if (str.equals("time out")) {
            return null;
        } else {
            UserDejson friendDejson = new UserDejson();
            friend = friendDejson.friendInfoDejson(str);
            if (friend == null) {
                return null;
            } else {
                return friend;
            }
        }
    }

    public List<UserModel> getFriendList() throws Exception {
        String action = "user/center/my_friends";
        String str = httpClient.doGet(action);
        if (str.equals("time out")) {
            return null;
        } else {
            List<UserModel> friends = new ArrayList<UserModel>();
            UserDejson md = new UserDejson();
            friends = md.getFriendList(str);
            return friends;
        }
    }

    public String oauthRegister(String type, String data, Context context) {
        String action = "api/oauth/register/type/" + type;
        String params = "data=" + data;
        String str = httpClient.doPost(context, action, params);
        Log.e("sadasdad", str);
        String rs = "";
        if (str.equals("time out")) {
            return "";
        } else {
            UserDejson md = new UserDejson();
            rs = md.getThirdId(str);
        }
        return rs;
    }

    public AccountModel billOverView() {
        AccountModel account = new AccountModel();
        String action = "user/center/finance";
        String str = httpClient.doGet(action);
        Log.e("bov", str);
        if (str.equals("time out")) {
            return null;
        } else {
            BillDejson billDejson = new BillDejson();
            account = billDejson.accountDejson(str);
            if (account == null) {
                return null;
            } else {
                return account;
            }
        }
    }

    public List<BillModel> getBillDetailList() {
        String action = "user/center/finance_detail";
        String str = httpClient.doGet(action);
        if (str.equals("time out")) {
            return null;
        } else {
            List<BillModel> bills = new ArrayList<BillModel>();
            BillDejson md = new BillDejson();
            bills = md.getBillDetail(str);
            return bills;
        }
    }

    public List<BillModel> getWithdrawDetailList() {
        String action = "user/center/finance_detail";
        String str = httpClient.doGet(action);
        Log.e("bdl", str);
        if (str.equals("time out")) {
            return null;
        } else {
            List<BillModel> bills = new ArrayList<BillModel>();
            BillDejson md = new BillDejson();
            bills = md.getWithdrawDetail(str);
            return bills;
        }
    }

    public AuthModel authDetail() {
        AuthModel auth = new AuthModel();
        String action = "user/center/verify";
        String str = httpClient.doGet(action);
        if (str.equals("time out")) {
            return null;
        } else {
            UserDejson authDejson = new UserDejson();
            auth = authDejson.authDejson(str);
            if (auth == null) {
                return null;
            } else {
                return auth;
            }
        }
    }

    public AuthModel auth() {
        AuthModel auth = new AuthModel();
        String action = "user/center/withdrawals";
        String str = httpClient.doGet(action);
        Log.e("acc", str);
        if (str.equals("time out")) {
            return null;
        } else {
            UserDejson authDejson = new UserDejson();
            auth = authDejson.authDejson1(str);
            if (auth == null) {
                return null;
            } else {
                return auth;
            }
        }
    }

    public int UploadAuth(String name, String front, String back) {
        String action = "user/center/verify_post";
        String param = "name=" + name + "&front=" + front + "&back=" + back;
        ;
        // 发送加好友请求
        String str = httpClient.doPost(action, param);
        StandardDejson updejson = new StandardDejson();
        StandardModel up = updejson.dejson(str);
        if (up == null)
            return -1;
        // 判断是否添加成功
        if (up.getStatus() == 1) {
            return Config.AuthUploadSuccess;
        } else
            return Config.AuthUploadFailed;
    }

    public int setAccount(String account, String type) {
        String action = "user/center/set_account_post";
        String param = "account_name=" + account + "&type=" + type;
        // 发送加好友请求
        String str = httpClient.doPost(action, param);
        StandardDejson sadejson = new StandardDejson();
        StandardModel up = sadejson.dejson(str);
        if (up == null)
            return -1;
        // 判断是否添加成功
        if (up.getStatus() == 1) {
            return Config.SetAccountSuccess;
        } else
            return Config.setAccountFailed;
    }

    public int withDraw(String account, String num, String type) {
        String action = "user/center/withdrawals_post";
        String param = "account_name=" + account + "&num=" + num + "&type="
                + type;
        // 发送加好友请求
        String str = httpClient.doPost(action, param);
        Log.e("srrrr", str);
        StandardDejson sadejson = new StandardDejson();
        StandardModel up = sadejson.dejson(str);
        if (up == null)
            return -1;
        // 判断是否添加成功
        if (up.getStatus() == 1) {
            return Config.WithDrawSuccess;
        } else
            return Config.WithDrawFailed;
    }

}
