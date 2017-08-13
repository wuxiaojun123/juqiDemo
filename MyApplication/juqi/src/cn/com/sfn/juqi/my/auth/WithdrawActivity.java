package cn.com.sfn.juqi.my.auth;

import cn.com.sfn.juqi.controller.UserController;
import cn.com.sfn.juqi.model.AuthModel;
import cn.com.sfn.juqi.widgets.CircleImageView;
import cn.com.wx.util.GlideUtils;
import cn.com.wx.util.LogUtils;

import com.example.juqi.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

/***
 * 我要提现
 */
public class WithdrawActivity extends Activity implements OnClickListener {
    private Intent mIntent;
    private TextView backMy, balance, income;
    private TextView authentication, name;
    private Button confirm;
    private UserController userController;
    private AuthModel authModel;
    private CircleImageView avatar;

    private Handler myhandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    initView();
                    break;
            }
        }
    };

    @SuppressLint("HandlerLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_withdraw);
        userController = new UserController();
        findViewById();
        initOperate();
    }

    protected void findViewById() {
        backMy = (TextView) findViewById(R.id.withdraw_back_to_my);
        authentication = (TextView) findViewById(R.id.is_authentication);
        confirm = (Button) findViewById(R.id.withdraw_confirm_btn);
        avatar = (CircleImageView) findViewById(R.id.withdraw_avatar);
        name = (TextView) findViewById(R.id.uname);
        balance = (TextView) findViewById(R.id.my_balance_txt);
        income = (TextView) findViewById(R.id.my_income_txt);
    }

    protected void initView() {
        backMy.setOnClickListener(this);
        GlideUtils.loadCircleImage(authModel.getAvatar(), avatar);

        name.setText(authModel.getName());
        balance.setText(authModel.getBalance());
        income.setText(authModel.getIncome());
        if (authModel.getStatus().equals("0")) {
            authentication.setOnClickListener(this);
            confirm.setTag("0");
        } else if (authModel.getStatus().equals("1")) {
            authentication.setText("已认证");
            authentication.setClickable(false);
            confirm.setTag("1");
//            confirm.setOnClickListener(this);
        } else if (authModel.getStatus().equals("2")) {
            authentication.setText("正在审核");
            authentication.setClickable(false);
            confirm.setClickable(false);
        } else {
            authentication.setText("未通过");
            authentication.setOnClickListener(this);
//            confirm.setOnClickListener(this);
        }
        confirm.setOnClickListener(this);
    }

    protected void initOperate() {
        new Thread() {
            public void run() {
                authModel = userController.authDetail();
                myhandler.sendEmptyMessage(1);
            }
        }.start();
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.withdraw_back_to_my:
                finish();
                break;
            case R.id.is_authentication:
                // 跳往身份认证界面
                mIntent = new Intent(WithdrawActivity.this,
                        AuthenticationActivity.class);
                startActivity(mIntent);
                break;
            case R.id.withdraw_confirm_btn:
                String tag = (String) confirm.getTag();
                LogUtils.e("认证点击:" + tag);
                if ("0".equals(tag)) {
                    mIntent = new Intent(WithdrawActivity.this,
                            AuthenticationActivity.class);
                    startActivity(mIntent);
                } else {
                    mIntent = new Intent(WithdrawActivity.this,
                            GoWithdrawActivity.class);
                    startActivity(mIntent);
                }

                break;
        }
    }
}