
package cn.com.sfn.example.juqi;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.com.sfn.juqi.controller.UserController;
import cn.com.sfn.juqi.util.Config;
import cn.smssdk.SMSSDK;

import com.example.juqi.R;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.StrictMode;
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
                if (account.getText().toString().trim().equals("")) {
                    Toast.makeText(ForgetPasswordActivity.this, "不能为空",
                            Toast.LENGTH_SHORT).show();
                } else {
                    if (isPhone(account.getText().toString().trim())) {
                        Toast.makeText(ForgetPasswordActivity.this, "已发送，请稍等",
                                Toast.LENGTH_SHORT).show();
                        SMSSDK.getVerificationCode("86", account.getText()
                                .toString().trim());
                        timer.start();
                    } else {
                        Toast.makeText(ForgetPasswordActivity.this, "请输入手机号",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        forgetButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (account.getText().toString().equals("")
                        || idCode.getText().toString().equals("")
                        || password.getText().toString().equals("")) {
                    Toast.makeText(ForgetPasswordActivity.this, "请全部填写",
                            Toast.LENGTH_SHORT).show();
                } else {
                    if (isPhone(account.getText().toString())) {
                        int rs = userController.forget(account.getText()
                                .toString(), password.getText().toString(),
                                idCode.getText().toString());
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

                    } else {
                        Toast.makeText(ForgetPasswordActivity.this, "请输入手机号",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private boolean isPhone(String inputText) {
        Pattern p = Pattern
                .compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
        Matcher m = p.matcher(inputText);
        return m.matches();
    }
}
