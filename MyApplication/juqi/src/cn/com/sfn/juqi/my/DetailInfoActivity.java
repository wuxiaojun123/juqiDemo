package cn.com.sfn.juqi.my;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.juqi.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import cn.com.sfn.juqi.controller.UserController;
import cn.com.sfn.juqi.model.UserModel;
import cn.com.sfn.juqi.rxbus.RxBus;
import cn.com.sfn.juqi.rxbus.rxtype.RxtypeUpdateBean;
import cn.com.sfn.juqi.util.ChooseCameraPopuUtils;
import cn.com.sfn.juqi.util.Config;
import cn.com.sfn.juqi.util.ToastUtil;
import cn.com.sfn.juqi.widgets.ArrayWheelAdapter;
import cn.com.sfn.juqi.widgets.CircleImageView;
import cn.com.sfn.juqi.widgets.NumberWheelAdaper;
import cn.com.sfn.juqi.widgets.WheelView;
import cn.com.wx.util.BaseSubscriber;
import cn.com.wx.util.GlideUtils;
import cn.com.wx.util.LogUtils;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

@SuppressLint("HandlerLeak")
public class DetailInfoActivity extends Activity implements OnClickListener {
    private static String path = "/sdcard/myHead/";
    // 在页面上添加其他layout
    private LayoutInflater inflater = null;
    private TextView back;
    private CircleImageView avatar;
    private TextView sex, offense, defense, zonghe, shizhan, weizhi;
    private RelativeLayout sexBtn, offBtn, defBtn, zonBtn, roleBtn, levBtn,
            avatar_btn;
    private EditText nickName, age, gameAge, height, weight, signature, zhanji;
    private UserController userController;
    private UserModel userModel = new UserModel();
    // 滚动view呈现在PopupWindow中
    private PopupWindow menuWindow;
    // 滚动view的对象
    private WheelView wArray, wPercent;
    private String sexFormat[] = {"男", "女"};
    private String role[] = {"SF", "PF", "C", "PG", "SG"};
    private String level[] = {"初级菜鸟", "一般水平", "业余高手"};
    private Button confirm;
    private String sexStr;
    private Context mContext;
    private ChooseCameraPopuUtils chooseCameraPopuUtils;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_detail_info);
        mContext = this;
        userController = new UserController();
        findViewById();
        initView();
        initData();
    }

    private void initShow() {
        if (userModel == null) {
            return;
        }
        if (userModel.getNickName().equals("")) {
            nickName.setHint(R.string.hint11);
        } else {
            nickName.setText(userModel.getNickName());
        }
        if (userModel.getUserSex().equals("1")) {
            sex.setText("男");
        } else {
            sex.setText("女");
        }
        String ageParams = userModel.getAge().trim();
        if (!TextUtils.isEmpty(ageParams) && !"0".equals(ageParams)) {
            age.setText(ageParams);
        }
        String uageParams = userModel.getUage().trim();
        if (!TextUtils.isEmpty(uageParams) && !"0".equals(uageParams)) {
            gameAge.setText(uageParams);
        }
        String heightParams = userModel.getHeight().trim();
        if (!TextUtils.isEmpty(heightParams) && !"0".equals(heightParams)) {
            height.setText(heightParams);
        }
        String weightParams = userModel.getWeight().trim();
        if (!TextUtils.isEmpty(weightParams) && !"0".equals(weightParams)) {
            weight.setText(weightParams);
        }
        offense.setText(userModel.getOffense());
        defense.setText(userModel.getDefense());
        zonghe.setText(userModel.getComprehensive());
        shizhan.setText(userModel.getStandard());
        weizhi.setText(userModel.getPosition());
        if (userModel.getSignature().equals("")) {
            signature.setHint(R.string.gexing_hint);
        } else {
            signature.setText(userModel.getSignature());
        }
        if (userModel.getGrade().equals("")) {
            zhanji.setHint(R.string.zhanji_hint);
        } else {
            zhanji.setText(userModel.getGrade());
        }
        GlideUtils.loadCircleImage(userModel.getUserAvatar(), avatar);
    }

    protected void findViewById() {
        back = (TextView) findViewById(R.id.back_to_my);
        sexBtn = (RelativeLayout) findViewById(R.id.sex_btn);
        sex = (TextView) findViewById(R.id.sex);
        offBtn = (RelativeLayout) findViewById(R.id.offense_btn);
        offense = (TextView) findViewById(R.id.detail_offense);
        defBtn = (RelativeLayout) findViewById(R.id.defense_btn);
        defense = (TextView) findViewById(R.id.detail_defense);
        zonBtn = (RelativeLayout) findViewById(R.id.zonghe_btn);
        zonghe = (TextView) findViewById(R.id.detail_zonghe);
        roleBtn = (RelativeLayout) findViewById(R.id.role_btn);
        weizhi = (TextView) findViewById(R.id.detail_role);
        levBtn = (RelativeLayout) findViewById(R.id.level_btn);
        shizhan = (TextView) findViewById(R.id.detail_level);
        avatar = (CircleImageView) findViewById(R.id.info_avatar);
        avatar_btn = (RelativeLayout) findViewById(R.id.avatar_btn);
        nickName = (EditText) findViewById(R.id.name_detail);
        age = (EditText) findViewById(R.id.age_detail);
        gameAge = (EditText) findViewById(R.id.game_age_detail);
        height = (EditText) findViewById(R.id.height_detail);
        weight = (EditText) findViewById(R.id.weight_detail);
        signature = (EditText) findViewById(R.id.gexing_detail);
        zhanji = (EditText) findViewById(R.id.zhanji_detail);
        confirm = (Button) findViewById(R.id.info_confirm_btn);
        inflater = (LayoutInflater) this
                .getSystemService(LAYOUT_INFLATER_SERVICE);
    }

    protected void initView() {
        back.setOnClickListener(this);
        sexBtn.setOnClickListener(this);
        offBtn.setOnClickListener(this);
        defBtn.setOnClickListener(this);
        zonBtn.setOnClickListener(this);
        roleBtn.setOnClickListener(this);
        levBtn.setOnClickListener(this);
        avatar_btn.setOnClickListener(this);
        confirm.setOnClickListener(this);
    }

    protected void initData() {
        userModel = Config.mUserModel;
        initShow();
        chooseCameraPopuUtils = new ChooseCameraPopuUtils(this, Config.URL_UPLOAD_HEADER);
        chooseCameraPopuUtils.setOnUploadImageListener(new ChooseCameraPopuUtils.OnUploadImageListener() {
            @Override
            public void onLoadError() {
                ToastUtil.show(mContext, "选择相片出错");
            }

            @Override
            public void onLoadSucced(int flag, String url) {
                LogUtils.e("头像修改的路径是：" + url);
                if (!TextUtils.isEmpty(url)) {
                    Config.mUserModel.setUserAvatar(Config.URL_IMAGE_BASE + url);
                    GlideUtils.loadCircleImage(Config.mUserModel.getUserAvatar(), avatar);
                    sendRxtypeUpdateFlag(1);
                    ToastUtil.show(mContext, "上传成功");
                } else {
                    ToastUtil.show(mContext, "上传失败");
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_to_my:
                finish();
                break;
            case R.id.sex_btn:
                hideInput(v);
                showPopwindow(getArrayPick(sex, sexFormat));
                break;
            case R.id.offense_btn:
                hideInput(v);
                showPopwindow(getPercentPick(offense));
                break;
            case R.id.defense_btn:
                hideInput(v);
                showPopwindow(getPercentPick(defense));
                break;
            case R.id.zonghe_btn:
                hideInput(v);
                showPopwindow(getPercentPick(zonghe));
                break;
            case R.id.level_btn:
                hideInput(v);
                showPopwindow(getArrayPick(shizhan, level));
                break;
            case R.id.role_btn:
                hideInput(v);
                showPopwindow(getArrayPick(weizhi, role));
                break;
            case R.id.avatar_btn:
                /*Intent avaIntent = new Intent(Intent.ACTION_PICK, null);
                avaIntent.setDataAndType(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image*//*");
                startActivityForResult(avaIntent, 1);*/
                chooseCameraPopuUtils.showPopupWindow();
                break;
            case R.id.info_confirm_btn:
                String sexParams = sex.getText().toString().trim();
                String nichNameParams = nickName.getText().toString().trim();
                String ageParams = age.getText().toString().trim();
                String gameAgeParams = gameAge.getText().toString().trim();
                String heightParams = height.getText().toString().trim();
                String weightParams = weight.getText().toString().trim();
                String offenseParams = offense.getText().toString().trim();
                String defenseParams = defense.getText().toString().trim();
                String zongheParams = zonghe.getText().toString().trim();
                String shizhanParams = shizhan.getText().toString().trim();
                String weizhiParams = weizhi.getText().toString().trim();
                String signatureParams = signature.getText().toString().trim();
                String zhanjiParams = zhanji.getText().toString().trim();

                if (sexParams.equals("男")) {
                    sexStr = "1";
                } else {
                    sexStr = "2";
                }
                confirmServer(nichNameParams, ageParams, gameAgeParams, heightParams, weightParams, offenseParams, defenseParams, zongheParams, shizhanParams, weizhiParams, signatureParams, zhanjiParams);
                break;
            default:
                break;
        }
    }

    private void confirmServer(final String nichNameParams, final String ageParams, final String gameAgeParams, final String heightParams,
                               final String weightParams, final String offenseParams, final String defenseParams, final String zongheParams,
                               final String shizhanParams, final String weizhiParams, final String signatureParams, final String zhanjiParams) {

        Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                int rs = userController
                        .editInfo(nichNameParams, sexStr, ageParams, gameAgeParams
                                , heightParams, weightParams, offenseParams
                                , defenseParams, zongheParams, shizhanParams
                                , weizhiParams, signatureParams
                                , zhanjiParams);
                subscriber.onNext(rs + "");
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<String>() {
                    @Override
                    public void onNext(String text) {
                        if (TextUtils.isEmpty(text)) {
                            ToastUtil.show(mContext, "网络异常");
                        } else {
                            int rs = Integer.parseInt(text);
                            if (rs == Config.EditSuccess) {
                                ToastUtil.show(mContext, "修改成功");
                                UserModel mUserModel = Config.mUserModel;
                                if (mUserModel != null) {
                                    mUserModel.setNickName(nichNameParams);
                                    mUserModel.setUserSex(sexStr);
                                    mUserModel.setAge(ageParams);
                                    mUserModel.setUage(gameAgeParams);
                                    mUserModel.setHeight(heightParams);
                                    mUserModel.setWeight(weightParams);
                                    mUserModel.setOffense(offenseParams);
                                    mUserModel.setDefense(defenseParams);
                                    mUserModel.setComprehensive(zongheParams);
                                    mUserModel.setStandard(shizhanParams);
                                    mUserModel.setPosition(weizhiParams);
                                    mUserModel.setSignature(signatureParams);
                                    mUserModel.setGrade(zhanjiParams);
                                    Config.mUserModel = mUserModel;
                                }
                                sendRxtypeUpdateFlag(2);
                            } else if (rs == -1) {
                                ToastUtil.show(mContext, "网络异常");
                            } else {
                                ToastUtil.show(mContext, "网络异常");
                            }
                        }
                    }
                });
    }

    private void sendRxtypeUpdateFlag(int updateFlag) {
        RxBus.getDefault().post(new RxtypeUpdateBean(updateFlag));
        if (updateFlag == 2) {
            finish();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (chooseCameraPopuUtils != null)
            chooseCameraPopuUtils.onActivityResult(requestCode, resultCode, data);

        /*switch (requestCode) {
            case 1:
                if (resultCode == Activity.RESULT_OK) {
                    cropPhoto(data.getData(), 3);// 裁剪图片
                }
                break;
            case 3:
                if (data != null) {
                    Bundle extras = data.getExtras();
                    Bitmap headBitmap = extras.getParcelable("data");
                    if (headBitmap != null) {
                        setPicToView(headBitmap);// 保存在SD卡中
                        *//**
         * 上传服务器代码
         *//*
                        uploadClient = new MyHttpClient();
                        String sstr = uploadClient.uploadFile(Config.URL_UPLOAD, "head.jpg", path
                                + "head.jpg");
                        if (!TextUtils.isEmpty(sstr)) {
                            LogUtils.e("上传success:" + sstr);
                            Config.mUserModel.setUserAvatar(Config.URL_IMAGE_BASE + sstr);
                            sendRxtypeUpdateFlag(1);
                            ToastUtil.show(mContext, "上传成功");
                        } else {
                            ToastUtil.show(mContext, "上传失败");
                        }
                        if (headBitmap != null) {
                            headBitmap.recycle();
                        }
                    }
                }
                break;
            default:
                break;
        }*/
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 调用系统的裁剪
     *
     * @param uri
     */
    public void cropPhoto(Uri uri, int type) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 150);
        intent.putExtra("outputY", 150);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, type);
    }

    private void setPicToView(Bitmap mBitmap) {
        String sdStatus = Environment.getExternalStorageState();
        if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用
            return;
        }
        FileOutputStream b = null;
        File file = new File(path);
        file.mkdirs();// 创建文件夹
        String fileName = path + "head.jpg";// 图片名字
        try {
            b = new FileOutputStream(fileName);
            mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, b);// 把数据写入文件

            GlideUtils.loadCircleImage(fileName, avatar);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                b.flush();
                b.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 隐藏输入法
     */
    private void hideInput(View v) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    /**
     * 初始化popupWindow
     *
     * @param view
     */
    @SuppressWarnings("deprecation")
    private void showPopwindow(View view) {
        menuWindow = new PopupWindow(view, LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT);
        menuWindow.setFocusable(true);
        menuWindow.setBackgroundDrawable(new BitmapDrawable());
        menuWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);
        menuWindow.setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss() {
                menuWindow = null;
            }
        });
    }

    private View getPercentPick(final TextView tv) {
        final View view = inflater.inflate(R.layout.duringpick, null);
        wPercent = (WheelView) view.findViewById(R.id.during);
        wPercent.setAdapter(new NumberWheelAdaper(0, 100));
        wPercent.setLabel("%");
        wPercent.setCyclic(true);
        Button bt = (Button) view.findViewById(R.id.d_set);
        bt.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                int str = wPercent.getCurrentItem() * 10;
                tv.setText(str + "");
                menuWindow.dismiss();
            }
        });
        Button cancel = (Button) view.findViewById(R.id.d_cancel);
        cancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                menuWindow.dismiss();
            }
        });
        return view;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private View getArrayPick(final TextView tv, String format[]) {
        final View view = inflater.inflate(R.layout.formatpick, null);
        wArray = (WheelView) view.findViewById(R.id.format_choose);
        wArray.setAdapter(new ArrayWheelAdapter(format));
        Button bt = (Button) view.findViewById(R.id.f_set);
        bt.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                int id = wArray.getCurrentItem();
                String f = wArray.getAdapter().getItem(id);
                tv.setText(f);
                menuWindow.dismiss();
            }
        });
        Button cancel = (Button) view.findViewById(R.id.f_cancel);
        cancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                menuWindow.dismiss();
            }
        });
        return view;
    }
}