
package cn.com.sfn.juqi.my;

import cn.com.sfn.example.juqi.LoginActivity;
import cn.com.sfn.example.juqi.RegisterActivity;
import cn.com.sfn.juqi.controller.UserController;
import cn.com.sfn.juqi.model.UserModel;
import cn.com.sfn.juqi.my.auth.WithdrawActivity;
import cn.com.sfn.juqi.my.message.MessageActivity;
import cn.com.sfn.juqi.my.mybill.MyBillActivity;
import cn.com.sfn.juqi.util.Config;
import cn.com.sfn.juqi.widgets.CircleImageView;
import cn.com.sfn.juqi.widgets.RoundProgressBar;

import com.example.juqi.R;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MyFragment extends Fragment implements OnClickListener {
    @SuppressLint("SdCardPath")
    private static String path = "/sdcard/myHead/";
    private View myView;
    private TextView username, attend, release, friendnum, role, ageTv, sexTv,
            signTv;
    private TextView mLoginButton, mRegister, chengwei;
    private RelativeLayout infoDetail;
    private Intent mIntent = null;
    private CircleImageView avatar_btn;
    // sd路径

    private RelativeLayout mSettingsButton, mWithdrawButton, mFriendButton;
    private RelativeLayout mCertificateButton, mBillButton, mMessageButton;
    private RoundProgressBar defenseBar, offenseBar, zongheBar;
    private UserController userController;
    private Handler myhandler;
    private UserModel userModel = new UserModel();

    @SuppressLint("HandlerLeak")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        myView = inflater.inflate(R.layout.activity_my, container, false);
        userController = new UserController();
        Log.e("TEST", Config.SessionID + "idididi" + Config.login_userid + Config.login_type);
        findViewById();
        initView();
        initData();
        myhandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 1:
                        initShow();
                        if (userModel != null)
                            Log.e("weishea", userModel.getNickName());
                        break;
                    case 2:
                        Toast.makeText(getActivity(), "未登录", Toast.LENGTH_SHORT)
                                .show();
                        break;
                }
            }
        };
        return myView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    protected void findViewById() {
        offenseBar = (RoundProgressBar) myView.findViewById(R.id.offense_bar);
        defenseBar = (RoundProgressBar) myView.findViewById(R.id.defense_bar);
        zongheBar = (RoundProgressBar) myView.findViewById(R.id.zonghe_bar);
        username = (TextView) myView.findViewById(R.id.username);
        infoDetail = (RelativeLayout) myView.findViewById(R.id.info_detail);
        avatar_btn = (CircleImageView) myView.findViewById(R.id.avatar);
        mRegister = (TextView) myView.findViewById(R.id.my_register_button);
        mLoginButton = (TextView) myView.findViewById(R.id.my_login_button);
        mBillButton = (RelativeLayout) myView.findViewById(R.id.my_bill_btn);
        mFriendButton = (RelativeLayout) myView
                .findViewById(R.id.my_friend_btn);
        mMessageButton = (RelativeLayout) myView
                .findViewById(R.id.my_message_btn);
        mSettingsButton = (RelativeLayout) myView
                .findViewById(R.id.my_settings_btn);
        mWithdrawButton = (RelativeLayout) myView
                .findViewById(R.id.my_withdraw_btn);
        mCertificateButton = (RelativeLayout) myView
                .findViewById(R.id.my_certificate_btn);
        attend = (TextView) myView.findViewById(R.id.attend_achieve);
        release = (TextView) myView.findViewById(R.id.org_achieve);
        friendnum = (TextView) myView.findViewById(R.id.friend_num);
        role = (TextView) myView.findViewById(R.id.role);
        ageTv = (TextView) myView.findViewById(R.id.age);
        sexTv = (TextView) myView.findViewById(R.id.sex);
        signTv = (TextView) myView.findViewById(R.id.show_state);
        chengwei = (TextView) myView.findViewById(R.id.chengwei);
    }

    protected void initView() {
        infoDetail.setOnClickListener(this);
        mBillButton.setOnClickListener(this);
        mLoginButton.setOnClickListener(this);
        mRegister.setOnClickListener(this);
        mFriendButton.setOnClickListener(this);
        mMessageButton.setOnClickListener(this);
        mSettingsButton.setOnClickListener(this);
        mWithdrawButton.setOnClickListener(this);
        mCertificateButton.setOnClickListener(this);
        if (!Config.is_login) {
            mLoginButton.setVisibility(View.VISIBLE);
            mRegister.setVisibility(View.VISIBLE);
        } else {
            mLoginButton.setVisibility(View.GONE);
            mRegister.setVisibility(View.GONE);
        }
    }

    private void initShow() {
        if (userModel == null || userModel.getJoinEntire() == null) {
            return;
        }
        attend.setText("参与成就:" + userModel.getJoinEntire().toString());
        release.setText("组织成就:" + userModel.getReleaseEntire());
        friendnum.setText("关注球友:" + userModel.getFriendNum());
        username.setText(userModel.getNickName());
        signTv.setText(userModel.getSignature());
        chengwei.setText(userModel.getStandard());
        if (userModel.getPosition().equals("")) {
            role.setText("无");
        } else {
            role.setText(userModel.getPosition());
        }
        if (userModel.getAge().equals("0")) {
            ageTv.setText("无");
        } else {
            ageTv.setText(userModel.getAge());
        }
        if (userModel.getUserSex().equals("")) {
            sexTv.setText("?");
        } else if (userModel.getUserSex().equals("1")) {
            sexTv.setText(R.string.male);
        } else {
            sexTv.setText(R.string.female);
        }
        offenseBar.setProgress(Integer.valueOf(userModel.getOffense()));
        defenseBar.setProgress(Integer.valueOf(userModel.getDefense()));
        zongheBar.setProgress(Integer.valueOf(userModel.getComprehensive()));
        @SuppressWarnings("deprecation")
        Drawable drawable = new BitmapDrawable(userModel.getUserAvatar());// 转换成drawable
        avatar_btn.setImageDrawable(drawable);
    }

    protected void initData() {
        new Thread() {
            public void run() {
                userModel = userController.getInfo(Config.login_userid);
                if (userModel == null) {
                    Message msg = new Message();
                    msg.what = 2;
                    myhandler.sendMessage(msg);
                } else {
                    Message msg = new Message();
                    msg.what = 1;
                    myhandler.sendMessage(msg);
                }
            }
        }.start();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.my_login_button:
                mIntent = new Intent(getActivity(), LoginActivity.class);
                startActivity(mIntent);
                break;
            case R.id.my_register_button:
                mIntent = new Intent(getActivity(), RegisterActivity.class);
                startActivity(mIntent);
                break;
            case R.id.info_detail:
                if (!Config.is_login) {
                    mIntent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(mIntent);
                    break;
                } else {
                    mIntent = new Intent(getActivity(), DetailInfoActivity.class);
                    startActivity(mIntent);
                    break;
                }
            case R.id.my_certificate_btn:
                if (!Config.is_login) {
                    mIntent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(mIntent);
                    break;
                } else {
                    mIntent = new Intent(getActivity(), CertificateActivity.class);
                    startActivity(mIntent);
                    break;
                }
            case R.id.my_friend_btn:
                if (!Config.is_login) {
                    mIntent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(mIntent);
                    break;
                } else {
                    mIntent = new Intent(getActivity(), FriendActivity.class);
                    startActivity(mIntent);
                    break;
                }
            case R.id.my_message_btn:
                if (!Config.is_login) {
                    mIntent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(mIntent);
                    break;
                } else {
                    mIntent = new Intent(getActivity(), MessageActivity.class);
                    startActivity(mIntent);
                    break;
                }
            case R.id.my_bill_btn:
                if (!Config.is_login) {
                    mIntent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(mIntent);
                    break;
                } else {
                    mIntent = new Intent(getActivity(), MyBillActivity.class);
                    startActivity(mIntent);
                    break;
                }
            case R.id.my_withdraw_btn:
                if (!Config.is_login) {
                    mIntent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(mIntent);
                    break;
                } else {
                    mIntent = new Intent(getActivity(), WithdrawActivity.class);
                    startActivity(mIntent);
                    break;
                }
            case R.id.my_settings_btn:
                if (!Config.is_login) {
                    mIntent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(mIntent);
                    break;
                } else {
                    mIntent = new Intent(getActivity(), SettingsActivity.class);
                    startActivity(mIntent);
                    break;
                }
            default:
                break;
        }
    }
}
