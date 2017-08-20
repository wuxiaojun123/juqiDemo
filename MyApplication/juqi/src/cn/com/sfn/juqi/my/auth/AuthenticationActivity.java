package cn.com.sfn.juqi.my.auth;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import cn.com.sfn.example.juqi.LoginActivity;
import cn.com.sfn.example.juqi.MainActivity;
import cn.com.sfn.juqi.controller.UserController;
import cn.com.sfn.juqi.model.AuthModel;
import cn.com.sfn.juqi.net.MyHttpClient;
import cn.com.sfn.juqi.util.ChooseCameraPopuUtils;
import cn.com.sfn.juqi.util.Config;
import cn.com.sfn.juqi.util.ToastUtil;
import cn.com.wx.util.BaseSubscriber;
import cn.com.wx.util.GlideUtils;
import cn.com.wx.util.LogUtils;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import com.example.juqi.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class AuthenticationActivity extends Activity implements OnClickListener {
    // 认证按钮
    private Button authConfirm;
    private EditText real;
    private TextView back;
    private TextView mytvreason;
    private ImageView image1, image2;
    private String front, backImage;
    private UserController userController;
    private Context mContext;
    private ChooseCameraPopuUtils chooseCameraPopuUtilsFront;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_authentication);
        mContext = this;
        userController = new UserController();
        findViewById();
        initView();
        initUpload();
    }

    private void initUpload() {
        chooseCameraPopuUtilsFront = new ChooseCameraPopuUtils(this, Config.URL_UPLOAD_IDENTITY);
        chooseCameraPopuUtilsFront.setOnUploadImageListener(new ChooseCameraPopuUtils.OnUploadImageListener() {
            @Override
            public void onLoadError() {
                ToastUtil.show(mContext, "选择相片出错");
            }

            @Override
            public void onLoadSucced(int flag, String url) {
                LogUtils.e("头像修改的路径是：" + url);
                if(!TextUtils.isEmpty(url)){
                    if (flag == 1) {
                        front = url;
                    } else if (flag == 2) {
                        backImage = url;
                    }
                    ToastUtil.show(mContext, "上传成功");
                }else{
                    ToastUtil.show(mContext, "上传失败");
                }
            }
        });
    }

    protected void findViewById() {
        image1 = (ImageView) findViewById(R.id.image1);
        image2 = (ImageView) findViewById(R.id.image2);
        authConfirm = (Button) findViewById(R.id.auth_confirm_btn);
        back = (TextView) findViewById(R.id.auth_back_to_withdraw);
        mytvreason = (TextView) findViewById(R.id.reason);
        real = (EditText) findViewById(R.id.real_name);
    }

    protected void initView() {
        image1.setOnClickListener(this);
        image2.setOnClickListener(this);
        authConfirm.setOnClickListener(this);
        back.setOnClickListener(this);

        Observable.create(new Observable.OnSubscribe<AuthModel>() {
            @Override
            public void call(Subscriber<? super AuthModel> subscriber) {
                AuthModel authModel = userController.authDetail();
                subscriber.onNext(authModel);
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<AuthModel>() {
                    @Override
                    public void onNext(AuthModel authModel) {
                        if (authModel.getStatus().equals("3")) {
                            mytvreason.setText("审核未通过原因：请检查照片是否清晰");
                            Toast.makeText(AuthenticationActivity.this, "当前审核没有通过",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    public void onClick(View v) {
        switch (v.getId()) {
            // 返回上一页
            case R.id.auth_back_to_withdraw:
                finish();
                break;
            case R.id.image1:
                chooseCameraPopuUtilsFront.flag = 1;
                chooseCameraPopuUtilsFront.showPopupWindow();

                break;
            case R.id.image2:
                chooseCameraPopuUtilsFront.flag = 2;
                chooseCameraPopuUtilsFront.showPopupWindow();

                break;
            // 确认认证提交按钮
            case R.id.auth_confirm_btn:
                confirmServer();
                break;
        }
    }

    private void confirmServer() {
        final String realName = real.getText().toString().trim();
        if (TextUtils.isEmpty(realName)) {
            ToastUtil.show(mContext, "请输入身份证上的真实姓名");
        } else {
            if (!TextUtils.isEmpty(front) && !TextUtils.isEmpty(backImage)) {
                Observable.create(new Observable.OnSubscribe<String>() {

                    @Override
                    public void call(Subscriber<? super String> subscriber) {
                        int rs = userController.UploadAuth(realName, front, backImage);
                        subscriber.onNext(rs + "");
                    }
                })
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new BaseSubscriber<String>() {
                            @Override
                            public void onNext(String s) {
                                if (!TextUtils.isEmpty(s)) {
                                    int rs = Integer.parseInt(s);
                                    if (rs == Config.AuthUploadSuccess) {
                                        ToastUtil.show(mContext, "提交成功，审核将在三日内完成！");
                                        finish();
                                    } else if (rs == -1) {
                                        ToastUtil.show(mContext, "网络异常");
                                    } else {
                                        ToastUtil.show(mContext, "提交失败");
                                    }
                                } else {
                                    ToastUtil.show(mContext, "提交失败");
                                }
                            }
                        });

            } else {
                ToastUtil.show(mContext, "请上传全部图片");
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (chooseCameraPopuUtilsFront != null) {
            chooseCameraPopuUtilsFront.onActivityResult(requestCode, resultCode, data);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

}