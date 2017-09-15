package com.example.juqi.wxapi;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;

import com.example.juqi.LoginActivity;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import net.sourceforge.simcpux.Constants;

import org.json.JSONObject;

import cn.com.sfn.juqi.controller.UserController;
import cn.com.sfn.juqi.rxbus.RxBus;
import cn.com.sfn.juqi.rxbus.rxtype.LoginRxbusType;
import cn.com.sfn.juqi.util.AccessTokenKeeper;
import cn.com.sfn.juqi.util.Config;
import cn.com.sfn.juqi.util.ToastUtil;
import cn.com.wx.util.BaseSubscriber;
import cn.com.wx.util.LogUtils;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by wuxiaojun on 17-5-25.
 */

public class WXEntryActivity extends Activity implements IWXAPIEventHandler {

    private IWXAPI api;
    private UserController userController;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            LogUtils.e("执行WXEntryActivity");
            mContext = getApplicationContext();

            api = WXAPIFactory.createWXAPI(this, Constants.APP_ID);
//            api = WXAPIFactory.createWXAPI(this, Constants.APP_ID, true);
            api.registerApp(Constants.APP_ID); // 将应用注册到微信
            api.handleIntent(getIntent(), this);

            userController = new UserController();

        } catch (Exception e) {
            e.printStackTrace();
            finish();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        LogUtils.e("执行onNewIntent");
        api.handleIntent(intent, this);
    }


    @Override
    public void onReq(BaseReq baseReq) {
        LogUtils.e("当前的req是" + baseReq.getType());
    }

    @Override
    public void onResp(BaseResp resp) {
        LogUtils.e("onResp返回数据是" + resp.errStr + "----" + resp.errCode);
        int code = resp.errCode;
        switch (code) {
            case BaseResp.ErrCode.ERR_OK:
                SendAuth.Resp sendResp = (SendAuth.Resp) resp;
                if (sendResp != null) {
                    LogUtils.e("code是：" + sendResp.code);
                    // 拿到code 再去获取access_token
                    getAccessToken(sendResp.code);
                }

                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                // 用户取消
                LogUtils.e("点击取消");
//                WXEntryActivity.this.setResult(RESULT_CANCELED);
                WXEntryActivity.this.finish();

                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
                // 发送被拒绝
                LogUtils.e("点击拒绝");
//                WXEntryActivity.this.setResult(RESULT_CANCELED);
                WXEntryActivity.this.finish();

                break;
            default:
//                WXEntryActivity.this.setResult(RESULT_CANCELED);
                WXEntryActivity.this.finish();

                break;
        }

    }

    /***
     * 获取token
     * /api/oauth/getAccessToken/type/weixin/code
     *
     * @param code
     */
    private void getAccessToken(final String code) {
        Observable.create(new Observable.OnSubscribe<String>() {

            @Override
            public void call(Subscriber<? super String> subscriber) {
                String result = userController.getAccessToken(code);
                subscriber.onNext(result);
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<String>() {
                    @Override
                    public void onNext(String text) {
                        if (!TextUtils.isEmpty(text)) {
                            try {
                                JSONObject jsonObject = new JSONObject(text);
                                if (jsonObject.has("errcode")) {
                                    ToastUtil.show(mContext, "获取token失败");
                                    finish();
                                } else {
                                    String accessToken = jsonObject.getString("access_token");
                                    String expires_in = jsonObject.getString("expires_in");
                                    String open_id = jsonObject.getString("openid");
                                    final String params = "access_token="
                                            + accessToken
                                            + "%26expires_in="
                                            + expires_in
                                            + "%26openid=" + open_id;
                                    login(params);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                ToastUtil.show(mContext, "获取数据失败");
                                finish();
                            }

                        } else {
                            ToastUtil.show(mContext, "获取token失败");
                            finish();
                        }
                    }
                });
    }


    /***
     * ?act=connect_wx&op=login
     * "connect_wx", "login",
     *
     * @param params
     */
    private void login(final String params) {
        Observable.create(new Observable.OnSubscribe<String>() {

            @Override
            public void call(Subscriber<? super String> subscriber) {
                String result = userController.oauthRegister("weixin", params, mContext);
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
                            SharedPreferences settings = getSharedPreferences(Config.PREFS_NAME, 0);
                            // 记住登录状态
                            SharedPreferences.Editor editor = settings.edit();
                            // 存入数据
                            editor.putBoolean("isLogin", true);
                            editor.putString("type", "weixin");
                            editor.putString("userid", Config.login_userid);
                            // 提交修改
                            editor.commit();
                            Config.is_login = true;
                            Config.login_type = "weixin";
                            // 关闭登录页面
                            RxBus.getDefault().post(new LoginRxbusType(1));

                            ToastUtil.show(mContext, "授权成功");
                        }
                        finish();
                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (api != null) {
            api.unregisterApp();
        }
    }

}
