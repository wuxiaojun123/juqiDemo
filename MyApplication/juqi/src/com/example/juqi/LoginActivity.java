
package com.example.juqi;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.exception.WeiboException;
import com.tencent.connect.UserInfo;
import com.tencent.connect.auth.QQToken;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.com.sfn.example.juqi.ForgetPasswordActivity;
import cn.com.sfn.example.juqi.RegisterActivity;
import cn.com.sfn.juqi.controller.UserController;
import cn.com.sfn.juqi.rxbus.RxBus;
import cn.com.sfn.juqi.rxbus.rxtype.LoginRxbusType;
import cn.com.sfn.juqi.util.AccessTokenKeeper;
import cn.com.sfn.juqi.util.Config;
import cn.com.sfn.juqi.util.Constants;
import cn.com.sfn.juqi.util.ToastUtil;
import cn.com.wx.util.BaseSubscriber;
import cn.com.wx.util.LogUtils;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class LoginActivity extends Activity implements OnClickListener {

    @BindView(R.id.accout)
    EditText accountEdit;
    @BindView(R.id.pwd)
    EditText passwordEdit;
    @BindView(R.id.register_btn)
    TextView register;
    @BindView(R.id.free_see)
    TextView see;
    @BindView(R.id.wechat_login)
    TextView wxBtn;
    @BindView(R.id.qq_login)
    TextView qqBtn;
    @BindView(R.id.weibo_login)
    TextView weiboBtn;
    @BindView(R.id.forget_btn)
    TextView forgetBtn;
    @BindView(R.id.login_btn)
    Button loginButton;

    private Intent mIntent;
    private SharedPreferences settings;
    private UserController userController;
    private String account, password;
    // QQ登录
    private Tencent mTencent;
    //    public static QQAuth mQQAuth;
    public static String openidString;
    public static String nicknameString;
    public static String TAG = "MainActivity";
    Bitmap bitmap = null;
    // 微博登录
    private AuthInfo mAuthInfo;
    /**
     * 封装了 "access_token"，"expires_in"，"refresh_token"，并提供了他们的管理功能
     */
    private Oauth2AccessToken mAccessToken;
    /**
     * 注意：SsoHandler 仅当 SDK 支持 SSO 时有效
     */
    private SsoHandler mSsoHandler;
    // 微信登录
    public IWXAPI wxApi;
    private Context mContext;

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                JSONObject response = (JSONObject) msg.obj;
                if (response.has("nickname")) {
                    try {
                        ToastUtil.show(mContext, "登录成功");
                        nicknameString = response.getString("nickname");
                        // 记住登录状态
                        Editor editor = settings.edit();
                        // 存入数据
                        editor.putBoolean("isLogin", true);
                        editor.putString("type", "qq");
                        editor.putString("userid", Config.login_userid);
                        // 提交修改
                        editor.commit();
                        Config.is_login = true;
                        Config.login_type = "qq";

                        finishLoginActivity();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        mContext = this;

        initWeixinLogin();
        initweibologin();
        initEvent();
        initData();
        initBus();
    }

    private void initBus() {
        RxBus.getDefault().toObservable(LoginRxbusType.class).subscribe(new Action1<LoginRxbusType>() {
            @Override
            public void call(LoginRxbusType loginRxbusType) {
                if (loginRxbusType.flag == 1) { // 微信登录成功
                    finishLoginActivity();
                }
            }
        });
    }


    private void initData() {
        userController = new UserController();
        settings = getSharedPreferences(Config.PREFS_NAME, 0);
        accountEdit.setText(settings.getString("username", ""));
        passwordEdit.setText(settings.getString("password", ""));
    }


    private void initWeixinLogin() {
        wxApi = WXAPIFactory.createWXAPI(this, net.sourceforge.simcpux.Constants.APP_ID, true);
        wxApi.registerApp(net.sourceforge.simcpux.Constants.APP_ID);
    }

    /**
     * 进行微博授权初始化操作
     */
    private void initweibologin() {
        // 初始化授权类对象，将应用的信息保存
        mAuthInfo = new AuthInfo(this, Constants.APP_KEY,
                Constants.REDIRECT_URL, Constants.SCOPE);
        mSsoHandler = new SsoHandler(LoginActivity.this, mAuthInfo);

        mAccessToken = AccessTokenKeeper.readAccessToken(this);
        if (mAccessToken.isSessionValid()) {
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.qq_login:
                LoginQQ();

                break;
            case R.id.wechat_login:
                SendAuth.Req req = new SendAuth.Req();
                req.scope = "snsapi_userinfo";
                req.state = String.valueOf(System.currentTimeMillis());
                wxApi.sendReq(req);

                break;
            case R.id.weibo_login:// SSO 授权, ALL IN ONE
                // 如果手机安装了微博客户端则使用客户端授权,没有则进行网页授权
                mSsoHandler.authorize(new AuthListener());

                break;
            case R.id.forget_btn:
                mIntent = new Intent(LoginActivity.this,
                        ForgetPasswordActivity.class);
                startActivity(mIntent);

                break;
            case R.id.free_see:
                finishLoginActivity();

                break;
            case R.id.register_btn:
                mIntent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(mIntent);

                break;
            case R.id.login_btn:
                account = accountEdit.getText().toString().trim();
                password = passwordEdit.getText().toString().trim();
                login(account, password);

                break;
        }
    }

    /***
     * 登录方法
     */
    private void login(final String account, final String password) {
        if (TextUtils.isEmpty(account)) {
            ToastUtil.show(mContext, "用户名不能为空");
        } else if (TextUtils.isEmpty(password)) {
            ToastUtil.show(mContext, "密码不能为空");
        } else {
            Observable.create(new Observable.OnSubscribe<String>() {
                @Override
                public void call(Subscriber<? super String> subscriber) {
                    int rs = userController.login(account, password,
                            LoginActivity.this);
                    subscriber.onNext(rs + "");
                }
            }).subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new BaseSubscriber<String>() {
                        @Override
                        public void onNext(String text) {
                            if (!TextUtils.isEmpty(text)) {
                                int rs = Integer.parseInt(text);
                                if (rs == Config.LoginSuccess) {
                                    ToastUtil.show(mContext, "登录成功");
                                    // 记住登录状态
                                    Editor editor = settings.edit();
                                    // 存入数据
                                    editor.putString("username", account);
                                    editor.putString("password", password);
                                    editor.putBoolean("isLogin", true);
                                    editor.putString("type", "putong");
                                    // 提交修改
                                    editor.commit();
                                    Config.is_login = true;
                                    Config.login_type = "putong";

                                    finishLoginActivity();
                                } else if (rs == -1) {
                                    ToastUtil.show(mContext, "网络异常");
                                }
                            } else {
                                ToastUtil.show(mContext, "用户名或密码错误");
                            }
                        }
                    });


        }
    }

    public void LoginQQ() {
        // 这里的APP_ID请换成你应用申请的APP_ID，我这里使用的是DEMO中官方提供的测试APP_ID 222222
        // 第一个参数就是上面所说的申请的APPID，第二个是全局的Context上下文，这句话实现了调用QQ登录
        mTencent = Tencent.createInstance(net.sourceforge.simcpux.Constants.QQ_LOGIN_APP_ID, getApplicationContext());
        /**
         * 通过这句代码，SDK实现了QQ的登录，这个方法有三个参数，第一个参数是context上下文，第二个参数SCOPO
         * 是一个String类型的字符串，表示一些权限 官方文档中的说明：应用需要获得哪些API的权限，由“，”分隔。例如：SCOPE =
         * “get_user_info,add_t”；所有权限用“all”
         * 第三个参数，是一个事件监听器，IUiListener接口的实例，这里用的是该接口的实现类
         */
        if (!mTencent.isSessionValid()) {
            mTencent.login(LoginActivity.this, "all", new BaseUiListener());
        }
    }

    /**
     * 当自定义的监听器实现IUiListener接口后，必须要实现接口的三个方法， onComplete onCancel onError
     * 分别表示第三方登录成功，取消 ，错误。
     */
    private class BaseUiListener implements IUiListener {
        public void onCancel() {
            ToastUtil.show(mContext, "取消登录");
        }

        public void onComplete(Object response) {
            try {
                // 获得的数据是JSON格式的，获得你想获得的内容
                // 如果你不知道你能获得什么，看一下下面的LOG
                LogUtils.e("-------------" + response.toString());

                openidString = ((JSONObject) response).getString("openid");
                final String str = "access_token="
                        + ((JSONObject) response).getString("access_token")
                        + "%26expires_in="
                        + ((JSONObject) response).getString("expires_in")
                        + "%26open_id=" + openidString;

                confirmQQ(str);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        public void onError(UiError arg0) {
            ToastUtil.show(mContext, "错误");
        }

    }

    /**
     * qq登录
     *
     * @param str
     */
    private void confirmQQ(final String str) {
        Observable.create(new Observable.OnSubscribe<String>() {

            @Override
            public void call(Subscriber<? super String> subscriber) {
                String result = userController.oauthRegister("Qq", str, LoginActivity.this);
                subscriber.onNext(result);
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<String>() {
                    @Override
                    public void onNext(String text) {
                        if (TextUtils.isEmpty(text)) {
                            ToastUtil.show(mContext, "登录超时，请稍后重试");
                        } else {
                            Config.login_userid = text;
                            /**
                             * 到此已经获得OpneID以及其他你想获得的内容了
                             * QQ登录成功了，我们还想获取一些QQ的基本信息，比如昵称，头像什么的，这个时候怎么办？
                             * sdk给我们提供了一个类UserInfo，这个类中封装了QQ用户的一些信息，我么可以通过这个类拿到这些信息
                             * 如何得到这个UserInfo类呢？
                             */
                            QQToken qqToken = mTencent.getQQToken();
                            UserInfo info = new UserInfo(getApplicationContext(), qqToken);
                            // 这样我们就拿到这个类了，之后的操作就跟上面的一样了，同样是解析JSON
                            info.getUserInfo(new IUiListener() {
                                public void onComplete(final Object response) {
                                    Log.e(TAG, "---------------111111");
                                    Message msg = new Message();
                                    msg.obj = response;
                                    msg.what = 0;
                                    mHandler.sendMessage(msg);
                                    LogUtils.e("qq登录返回结果" + response.toString());

                                    /**
                                     * 由于图片需要下载所以这里使用了线程，如果是想获得其他文字信息直接 在mHandler里进行操作
                                     */
            /*new Thread() {
                @Override
                public void run() {
                    JSONObject json = (JSONObject) response;
                    try {
                        bitmap = Util.getbitmap(json
                                .getString("figureurl_qq_2"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Message msg = new Message();
                    msg.obj = bitmap;
                    msg.what = 1;
                    mHandler.sendMessage(msg);
                }
            }.start();*/
                                }

                                public void onCancel() {
                                    LogUtils.e("qq登录取消");
                                }

                                public void onError(UiError arg0) {
                                    LogUtils.e("qq登录报错" + arg0.errorDetail + "--" + arg0.errorMessage + "--" + arg0.errorCode);
                                }
                            });
                        }
                    }
                });
    }

    /**
     * 微博认证授权回调类。 1. SSO 授权时，需要在 {@link #onActivityResult} 中调用
     * {@link SsoHandler#authorizeCallBack} 后， 该回调才会被执行。 2. 非 SSO
     * 授权时，当授权结束后，该回调就会被执行。 当授权成功后，请保存该 access_token、expires_in、uid 等信息到
     * SharedPreferences 中。
     */
    class AuthListener implements WeiboAuthListener {
        @Override
        public void onCancel() {
            ToastUtil.show(mContext, "取消授权");
        }

        @Override
        public void onComplete(Bundle values) {
            // 从 Bundle 中解析 Token
            mAccessToken = Oauth2AccessToken.parseAccessToken(values);
            if (mAccessToken.isSessionValid()) {
                final String json = "{" + "\"access_token\":\""
                        + values.getString("access_token") + "\","
                        + "\"uid\":\"" + values.getString("uid") + "\","
                        + "\"expires_in\":\"" + values.getString("expires_in")
                        + "\"," + "\"remind_in\":\""
                        + values.getString("expires_in") + "\"}";

                confirmWeiboLogin(json);

            } else {
                // 以下几种情况，您会收到 Code：
                // 1. 当您未在平台上注册的应用程序的包名与签名时；
                // 2. 当您注册的应用程序包名与签名不正确时；
                // 3. 当您在平台上注册的包名和签名与您当前测试的应用的包名和签名不匹配时。
                String code = values.getString("code");
                String message = "授权失败";
                if (!TextUtils.isEmpty(code)) {
                    message = message + "\nObtained the code: " + code;
                }
                ToastUtil.show(mContext, message);
            }
        }

        @Override
        public void onWeiboException(WeiboException e) {
            e.printStackTrace();
            ToastUtil.show(mContext, "Auth exception : " + e.getMessage());
        }
    }


    private void confirmWeiboLogin(final String json) {
        Observable.create(new Observable.OnSubscribe<String>() {

            @Override
            public void call(Subscriber<? super String> subscriber) {
                String result = userController.oauthRegister("Sina", json, LoginActivity.this);
                subscriber.onNext(result);
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<String>() {
                    @Override
                    public void onNext(String text) {
                        if (TextUtils.isEmpty(text)) {
                            ToastUtil.show(mContext, "登录超时，请稍后重试");
                        } else {
                            Config.login_userid = text;
                            // 记住登录状态
                            Editor editor = settings.edit();
                            // 存入数据
                            editor.putBoolean("isLogin", true);
                            editor.putString("type", "weibo");
                            editor.putString("userid", Config.login_userid);
                            // 提交修改
                            editor.commit();
                            Config.is_login = true;
                            Config.login_type = "weibo";
                            // 保存 Token 到 SharedPreferences
                            AccessTokenKeeper.writeAccessToken(LoginActivity.this,
                                    mAccessToken);

                            ToastUtil.show(mContext, "授权成功");
                            finishLoginActivity();
                        }
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (mTencent != null) {
            mTencent.onActivityResult(requestCode, resultCode, data);
        }
        // SSO 授权回调
        // 重要：发起 SSO 登陆的 Activity 必须重写 onActivityResults
        if (mSsoHandler != null) {
            mSsoHandler.authorizeCallBack(requestCode, resultCode, data);
        }
    }

    private void initEvent() {
        weiboBtn.setOnClickListener(this);
        forgetBtn.setOnClickListener(this);
        wxBtn.setOnClickListener(this);
        see.setOnClickListener(this);
        register.setOnClickListener(this);
        loginButton.setOnClickListener(this);
        qqBtn.setOnClickListener(this);
    }

    private void finishLoginActivity() {
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public void onBackPressed() {
        finishLoginActivity();
    }

}
