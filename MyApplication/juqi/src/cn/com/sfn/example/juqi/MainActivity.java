
package cn.com.sfn.example.juqi;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.juqi.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.com.sfn.juqi.achieve.AchieveFragment;
import cn.com.sfn.juqi.appoint.AppointActivity;
import cn.com.sfn.juqi.manage.ManageFragment;
import cn.com.sfn.juqi.my.MyFragment;
import cn.com.sfn.juqi.sign.SignFragment;
import cn.com.sfn.juqi.util.Config;
import cn.com.wx.util.LogUtils;

@SuppressLint("NewApi")
public class MainActivity extends Activity {

    @BindView(R.id.MyBottemSignBtn)
    protected LinearLayout signBtn;
    @BindView(R.id.MyBottemAppointBtn)
    protected LinearLayout appointBtn;
    @BindView(R.id.MyBottemAchieveBtn)
    protected LinearLayout achieveBtn;
    @BindView(R.id.MyBottemManageBtn)
    protected LinearLayout manageBtn;
    @BindView(R.id.MyBottemMyBtn)
    protected LinearLayout myBtn;

    @BindView(R.id.MyBottemSignImg)
    protected ImageView signImg;
    @BindView(R.id.MyBottemAppointImg)
    protected ImageView appointImg;
    @BindView(R.id.MyBottemAchieveImg)
    protected ImageView achieveImg;
    @BindView(R.id.MyBottemManageImg)
    protected ImageView manageImg;
    @BindView(R.id.MyBottemMyImg)
    protected ImageView myImg;

    @BindView(R.id.MyBottemSignTxt)
    protected TextView signTxt;
    @BindView(R.id.MyBottemAppointTxt)
    protected TextView appointTxt;
    @BindView(R.id.MyBottemManageTxt)
    protected TextView manageTxt;
    @BindView(R.id.MyBottemMyTxt)
    protected TextView myTxt;

    private FragmentManager fm;
    private long mExitTime = 0;
    private int flag = 0;

    private SignFragment signFragment; // 报名
    private AchieveFragment achieveFragment; // 成就
    private ManageFragment manageFragment; // 管理
    private MyFragment myFragment; // 我的
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mContext = this;
        // 进入系统默认为BillOverViewFragment
        fm = getFragmentManager();
        flag = getIntent().getIntExtra("flag", 0);
        if (flag == 0) {
            showFragment(R.id.MyBottemSignBtn);
        } else if (flag == 5) {
            showFragment(R.id.MyBottemMyBtn);
        }
    }

    @OnClick({R.id.MyBottemSignBtn, R.id.MyBottemAppointBtn, R.id.MyBottemAchieveBtn,
            R.id.MyBottemManageBtn, R.id.MyBottemMyBtn})
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.MyBottemAppointBtn:
                if (TextUtils.isEmpty(Config.login_userid)) {// 未登录或登录过期
                    startLoginActivity();
                } else {
                    /*initBottemBtn();
                    appointImg.setImageResource(R.drawable.appoint_pressed);
                    appointTxt.setTextColor(Color.parseColor("#7be5ff"));*/
                    Intent mIntent = new Intent(MainActivity.this, AppointActivity.class);
                    startActivity(mIntent);
                }
                break;
            default:
                showFragment(id);
                break;
        }
    }

    private void showFragment(int id) {
        FragmentTransaction mFragmentTransaction = fm.beginTransaction();
        hideFragment(mFragmentTransaction);
        switch (id) {
            case R.id.MyBottemSignBtn:

                initBottemBtn();
                signImg.setImageResource(R.drawable.sign_pressed);
                signTxt.setTextColor(Color.parseColor("#7be5ff"));

                if (signFragment == null) {
                    signFragment = new SignFragment();
                    mFragmentTransaction.add(R.id.fragment_content, signFragment);
                } else {
                    mFragmentTransaction.show(signFragment);
                }

                break;
            case R.id.MyBottemAchieveBtn:
                if (TextUtils.isEmpty(Config.login_userid)) {// 未登录或登录过期
                    startLoginActivity();
                } else {
                    initBottemBtn();
                    achieveImg.setImageResource(R.drawable.achieve_pressed);

                    if (achieveFragment == null) {
                        achieveFragment = new AchieveFragment();
                        mFragmentTransaction.add(R.id.fragment_content, achieveFragment);
                    } else {
                        mFragmentTransaction.show(achieveFragment);
                    }
                }

                break;
            case R.id.MyBottemManageBtn:
                if (TextUtils.isEmpty(Config.login_userid)) {// 未登录或登录过期
                    startLoginActivity();
                } else {
                    initBottemBtn();
                    manageImg.setImageResource(R.drawable.manage_pressed);
                    manageTxt.setTextColor(Color.parseColor("#7be5ff"));

                    if (manageFragment == null) {
                        manageFragment = new ManageFragment();
                        mFragmentTransaction.add(R.id.fragment_content, manageFragment);
                    } else {
                        mFragmentTransaction.show(manageFragment);
                    }
                }

                break;
            case R.id.MyBottemMyBtn:
                if (TextUtils.isEmpty(Config.login_userid)) {// 未登录或登录过期
                    startLoginActivity();
                } else {
                    initBottemBtn();
                    myImg.setImageResource(R.drawable.my_pressed);
                    myTxt.setTextColor(Color.parseColor("#7be5ff"));

                    if (myFragment == null) {
                        myFragment = new MyFragment();
                        mFragmentTransaction.add(R.id.fragment_content, myFragment);
                    } else {
                        mFragmentTransaction.show(myFragment);
                    }
                }
                break;
        }
        mFragmentTransaction.commitAllowingStateLoss();
    }

    private void hideFragment(FragmentTransaction mFragmentTransaction) {
        if (signFragment != null && !signFragment.isHidden()) {
            mFragmentTransaction.hide(signFragment);
        }
        if (achieveFragment != null && !achieveFragment.isHidden()) {
            mFragmentTransaction.hide(achieveFragment);
        }
        if (manageFragment != null && !manageFragment.isHidden()) {
            mFragmentTransaction.hide(manageFragment);
        }
        if (myFragment != null && !myFragment.isHidden()) {
            mFragmentTransaction.hide(myFragment);
        }
    }

    /**
     * 初始化控件样式
     */
    private void initBottemBtn() {
        signImg.setImageResource(R.drawable.bottem_sign);
        appointImg.setImageResource(R.drawable.bottem_appoint);
        achieveImg.setImageResource(R.drawable.bottem_achieve);
        manageImg.setImageResource(R.drawable.bottem_manage);
        myImg.setImageResource(R.drawable.bottem_my);

        int resSelectedColor = ContextCompat.getColor(mContext, R.color.search_bottem_textcolor);
        signTxt.setTextColor(resSelectedColor);
        appointTxt.setTextColor(resSelectedColor);
        manageTxt.setTextColor(resSelectedColor);
        myTxt.setTextColor(resSelectedColor);
    }

    /***
     * 跳到登录界面
     */
    private void startLoginActivity() {
        Intent mIntent = new Intent(MainActivity.this, LoginActivity.class);
        MainActivity.this.startActivityForResult(mIntent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
        LogUtils.e("请求码和返回码" + requestCode + "---" + resultCode);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            showFragment(R.id.MyBottemSignBtn);
        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - mExitTime) > 2000) {
                Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                mExitTime = System.currentTimeMillis();
            } else {
                // 将设置还原
//                userController = null;
                Config.login_userid = null;
                Config.SessionID = null;
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
