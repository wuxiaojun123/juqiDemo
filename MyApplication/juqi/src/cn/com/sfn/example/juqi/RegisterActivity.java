package cn.com.sfn.example.juqi;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.StrictMode;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import cn.com.sfn.juqi.controller.UserController;
import cn.com.sfn.juqi.util.Config;
import cn.com.sfn.juqi.util.ToastUtil;
import cn.com.sfn.juqi.util.ValidateUtil;
import cn.smssdk.SMSSDK;

import com.example.juqi.LoginActivity;
import com.example.juqi.R;

public class RegisterActivity extends Activity {
    private Button registerButton;
    private Button codeButton;
    private CheckBox confirmRegister;
    private EditText account;
    private EditText idCode;
    private EditText password;
    private TextView login;
    private Intent mIntent;
    private CountDownTimer timer;
    private UserController userController;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_register);
        mContext = this;
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);
        SMSSDK.initSDK(this, "115e0bb417ef0",
                "bacf7c360ee01e353503c6aad5be5ae7");
        userController = new UserController();
        registerButton = (Button) findViewById(R.id.reg_btn);
        codeButton = (Button) findViewById(R.id.get_code_btn);
        confirmRegister = (CheckBox) findViewById(R.id.check_pwd);
        account = (EditText) findViewById(R.id.reg_account);
        idCode = (EditText) findViewById(R.id.id_code);
        password = (EditText) findViewById(R.id.reg_pwd);
        login = (TextView) findViewById(R.id.login_btn);
        login.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mIntent = new Intent(RegisterActivity.this, LoginActivity.class);
                mIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(mIntent);
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
                String phoneStr = account.getText().toString().trim();
                if (TextUtils.isEmpty(phoneStr)) {
                    Toast.makeText(RegisterActivity.this, "不能为空",
                            Toast.LENGTH_SHORT).show();
                } else {
                    if (ValidateUtil.isMobile(phoneStr)) {
                        Toast.makeText(RegisterActivity.this, "已发送，请稍等",
                                Toast.LENGTH_SHORT).show();
                        SMSSDK.getVerificationCode("86", account.getText()
                                .toString().trim());
                        timer.start();
                    } else {
                        Toast.makeText(RegisterActivity.this, "请输入手机号",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        registerButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String accountStr = account.getText().toString().trim();
                String idCodeStr = idCode.getText().toString().trim();
                String passwordStr = password.getText().toString().trim();

                if (TextUtils.isEmpty(accountStr)
                        || TextUtils.isEmpty(idCodeStr)
                        || TextUtils.isEmpty(passwordStr)) {
                    Toast.makeText(RegisterActivity.this, "请全部填写",
                            Toast.LENGTH_SHORT).show();
                } else {
                    if (ValidateUtil.isMobile(accountStr)) {
                        if (confirmRegister.isChecked()) {
                            int rs = userController.register(account.getText()
                                            .toString(), password.getText().toString(),
                                    idCode.getText().toString());
                            if (rs == Config.RegisterSuccess) {
                                Toast.makeText(RegisterActivity.this, "注册成功",
                                        Toast.LENGTH_SHORT).show();
                                finish();
                            } else if (rs == -1) {
                                Toast.makeText(RegisterActivity.this, "网络异常",
                                        Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(RegisterActivity.this, "注册失败",
                                        Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(RegisterActivity.this, "请确认",
                                    Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        ToastUtil.show(mContext, "手机号格式不正确");
                    }
                }
            }
        });
    }

}
