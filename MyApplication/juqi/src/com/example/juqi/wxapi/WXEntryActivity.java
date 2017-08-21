package com.example.juqi.wxapi;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import net.sourceforge.simcpux.Constants;

import cn.com.wx.util.LogUtils;

/**
 * Created by wuxiaojun on 17-5-25.
 */

public class WXEntryActivity extends Activity implements IWXAPIEventHandler {

    private IWXAPI api;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            LogUtils.e("执行WXEntryActivity");
            mContext = getApplicationContext();
            api = WXAPIFactory.createWXAPI(this, Constants.APP_ID, true);
            api.registerApp(Constants.APP_ID); // 将应用注册到微信
            api.handleIntent(getIntent(), this);

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
     * ?act=connect_wx&op=index
     * "connect_wx", "index",
     *
     * @param code
     */
    private void getAccessToken(String code) {


    }


    /***
     * ?act=connect_wx&op=login
     * "connect_wx", "login",
     *
     * @param access_token
     * @param openid
     * @param platformClient
     */
    private void login(String access_token, String openid, String platformClient) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(api != null){
            api.unregisterApp();
        }
    }

}
