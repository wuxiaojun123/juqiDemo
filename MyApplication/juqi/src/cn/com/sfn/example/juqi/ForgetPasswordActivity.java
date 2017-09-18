
package cn.com.sfn.example.juqi;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.com.sfn.juqi.controller.UserController;
import cn.com.sfn.juqi.util.Config;
import cn.com.sfn.juqi.util.ValidateUtil;
import cn.com.wx.util.BaseSubscriber;
import cn.smssdk.SMSSDK;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import com.example.juqi.R;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.StrictMode;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ForgetPasswordActivity extends Activity {
    private Button forgetButton;
    private Button codeButton;
    private EditText account;
    private EditText idCode;
    private EditText password;
    private TextView back;
    private CountDownTimer timer;
    private UserController userController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_forget_password);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);
        userController = new UserController();
        SMSSDK.initSDK(this, "115e0bb417ef0",
                "bacf7c360ee01e353503c6aad5be5ae7");
        forgetButton = (Button) findViewById(R.id.forget_btn);
        codeButton = (Button) findViewById(R.id.get_code_btn);
        account = (EditText) findViewById(R.id.reg_account);
        idCode = (EditText) findViewById(R.id.id_code);
        password = (EditText) findViewById(R.id.reg_pwd);
        back = (TextView) findViewById(R.id.back);
        back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        timer = new CountDownTimer(60000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                codeButton.setText((millisUntilFinished / 1000) + "秒后重新获取");
                codeButton.setTextColor(Color.parseColor("#5c626f"));
                codeButton.setEnabled(false);
            }

            @Override
            public void onFinish() {
                codeButton.setEnabled(true);
                codeButton.setTextColor(Color.parseColor("#14a9ce"));
                codeButton.setText("获取验证码");
            }
        };
        codeButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                sendCode();
            }
        });
        forgetButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String accountParams = account.getText().toString().trim();
                String codeParams = idCode.getText().toString().trim();
                String passwordParams = password.getText().toString().trim();

                if (accountParams.equals("")
                        || codeParams.equals("")
                        || passwordParams.equals("")) {
                    Toast.makeText(ForgetPasswordActivity.this, "请全部填写",
                            Toast.LENGTH_SHORT).show();
                } else {
                    if (ValidateUtil.isMobile(accountParams)) {
                        confirm(accountParams, passwordParams, codeParams);
                    } else {
                        Toast.makeText(ForgetPasswordActivity.this, "请输入手机号",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    /***
     * 提交到服务器
     */
    private void confirm(final String accountParams, final String passwordParams, final String codeParams) {
        Observable.create(new Observable.OnSubscribe<String>() {

            @Override
            public void call(Subscriber<? super String> subscriber) {
                int rs = userController.forget(accountParams, passwordParams, codeParams);
                subscriber.onNext(rs + "");
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<String>() {
                    @Override
                    public void onNext(String text) {
                        if (TextUtils.isEmpty(text)) {
                            Toast.makeText(ForgetPasswordActivity.this, "重置失败",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            int rs = Integer.parseInt(text);
                            if (rs == Config.ForgetPasswordSuccess) {
                                Toast.makeText(ForgetPasswordActivity.this, "重置成功",
                                        Toast.LENGTH_SHORT).show();
                                finish();
                            } else if (rs == -1) {
                                Toast.makeText(ForgetPasswordActivity.this, "网络异常",
                                        Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(ForgetPasswordActivity.this, "重置失败",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });


    }

    /***
     *
     */
    private void sendCode() {
        String accountParams = account.getText().toString().trim();
        if (TextUtils.isEmpty(accountParams)) {
            Toast.makeText(ForgetPasswordActivity.this, "不能为空",
                    Toast.LENGTH_SHORT).show();
        } else {
            if (ValidateUtil.isMobile(accountParams)) {
                Toast.makeText(ForgetPasswordActivity.this, "已发送，请稍等",
                        Toast.LENGTH_SHORT).show();
                SMSSDK.getVerificationCode("86", accountParams);
                timer.start();
            } else {
                Toast.makeText(ForgetPasswordActivity.this, "请输入手机号",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }


}
