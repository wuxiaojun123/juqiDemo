package cn.com.sfn.juqi.my;

import cn.com.sfn.example.juqi.MainActivity;
import cn.com.sfn.juqi.controller.UserController;
import cn.com.sfn.juqi.util.Config;
import cn.com.sfn.juqi.widgets.SwitchButton;

import com.example.juqi.R;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class SettingsActivity extends Activity implements OnClickListener {
    private Button logoff;
    private Intent mIntent;
    private TextView backMy;
    private SwitchButton notification;
    private SharedPreferences settings;
    private RelativeLayout change;
    private RelativeLayout rl_about;
    private RelativeLayout rl_help;
    private UserController userController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_settings);
        userController = new UserController();
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);
        settings = getSharedPreferences(Config.PREFS_NAME, 0);
        findViewById();
        initView();
        if (TextUtils.isEmpty(Config.Toggle)) {
            notification.setChecked(false);
        } else if (Config.Toggle.equals("1")) {
            notification.setChecked(true);
        } else {
            notification.setChecked(false);
        }
        notification.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
                if (arg1) {
                    Config.Toggle = "1";
                } else {
                    Config.Toggle = "0";
                }
            }
        });
    }

    protected void findViewById() {
        logoff = (Button) findViewById(R.id.logout_btn);
        backMy = (TextView) findViewById(R.id.settings_back_to_my);
        notification = (SwitchButton) findViewById(R.id.notification_btn);
        change = (RelativeLayout) findViewById(R.id.password_change);
        rl_about = (RelativeLayout) findViewById(R.id.rl_about);
        rl_help = (RelativeLayout) findViewById(R.id.rl_help);
    }

    protected void initView() {
        backMy.setOnClickListener(this);
        logoff.setOnClickListener(this);
        change.setOnClickListener(this);
        rl_about.setOnClickListener(this);
        rl_help.setOnClickListener(this);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.settings_back_to_my:
                finish();
                break;
            case R.id.logout_btn:
                int rs = userController.logoff();
                if (rs == Config.LogoutSuccess) {
                    // 记住登录状态
                    Editor editor = settings.edit();
                    // 存入数据
                    editor.putBoolean("isLogin", false);
                    editor.putString("userid", null);
                    editor.putString("sessionId", null);
                    // 提交修改
                    editor.commit();
                    Config.is_login = false;
                    Config.login_userid = null;
                    Config.SessionID = null;
                    mIntent = new Intent(SettingsActivity.this, MainActivity.class);
                    mIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(mIntent);
                    finish();
                } else if (rs == -1) {
                    Toast.makeText(SettingsActivity.this, "网络异常", Toast.LENGTH_LONG)
                            .show();
                } else {
                    Toast.makeText(SettingsActivity.this, "登出失败", Toast.LENGTH_LONG)
                            .show();
                }
                break;
            case R.id.password_change:
                mIntent = new Intent(SettingsActivity.this,
                        ChangePasswordActivity.class);
                startActivity(mIntent);
                break;
            case R.id.rl_about:
                // 关于我们
                startActivity(new Intent(SettingsActivity.this, AboutActivity.class));

                break;
            case R.id.rl_help:
                // 帮助中心
                startActivity(new Intent(SettingsActivity.this, HelpCenterActivity.class));

                break;
        }
    }
}