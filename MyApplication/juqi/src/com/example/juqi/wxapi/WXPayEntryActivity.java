package com.example.juqi.wxapi;


import net.sourceforge.simcpux.Constants;

import cn.com.wx.util.LogUtils;

import com.example.juqi.R;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {

    private static final String TAG = "MicroMsg.SDKSample.WXPayEntryActivity";

    private IWXAPI api;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pay_result);

        api = WXAPIFactory.createWXAPI(this, Constants.APP_ID);
        api.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq req) {
    }

    @Override
    public void onResp(BaseResp resp) {
        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            LogUtils.e("微信支付结果:" + resp.errStr + "======" + resp.errCode);
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.app_tip);//提示
            if (resp.errCode == 0) {
                builder.setMessage(getString(R.string.pay_result_callback_msg) + "支付成功");
                builder.show();
            } else {
                if (resp.errCode == -1) {
                    builder.setMessage(getString(R.string.pay_result_callback_msg) + "支付失败");
                    builder.show();
                } else {
                    builder.setMessage(getString(R.string.pay_result_callback_msg) + "用户取消");
                    builder.show();
                }
            }
        }
    }

}