/**
 * @author wangwb
 * @function 约球界面
 */
package cn.com.sfn.juqi.appoint;

import java.util.Calendar;

import cn.com.sfn.example.juqi.MainActivity;
import cn.com.sfn.example.juqi.ValidateMobileActivity;
import cn.com.sfn.juqi.controller.MatchController;
import cn.com.sfn.juqi.model.MatchModel;
import cn.com.sfn.juqi.util.Config;
import cn.com.sfn.juqi.util.ToastUtil;
import cn.com.sfn.juqi.util.ValidateUtil;
import cn.com.sfn.juqi.widgets.ArrayWheelAdapter;
import cn.com.sfn.juqi.widgets.DuringWheelAdapter;
import cn.com.sfn.juqi.widgets.NumericWheelAdapter;
import cn.com.sfn.juqi.widgets.OnWheelScrollListener;
import cn.com.sfn.juqi.widgets.WheelView;
import cn.com.wx.util.BaseSubscriber;
import cn.com.wx.util.LogUtils;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import com.amap.map3d.poisearch.PoiAroundSearchActivity;
import com.example.juqi.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.PopupWindow.OnDismissListener;

public class AppointActivity extends Activity implements OnClickListener {
    /**
     * 写的很乱，但是基本都是页面上的点击和显示控件
     */
    // 在页面上添加其他layout
    private LayoutInflater inflater = null;
    // 确认发布按钮
    private Button confirmButton;
    private Intent mIntent;
    // 滚动view呈现在PopupWindow中
    private PopupWindow menuWindow;
    private MatchController matchController;
    // 所在场馆按钮，点击后可以进入地图
    private RelativeLayout placeMap;
    // 球局标题，球局人数，发起人手机，球局详情
    private EditText title, number, phone, detail, fee, place;
    // 滚动view的对象
    private WheelView year, month, day, hour, mins, wDuring, mForamt, mType;
    // 开始时间，报名截止，球局时长，场地规格，球局类型按钮，跳出相应的popupwin
    private RelativeLayout startDate, endDate, duringTime, formatBtn, typeBtn;
    // 开始时间，报名截止，球局时长，所在场馆，详细地址，场地规格，球局类型
    private TextView startTime, endTime, during, placeDetail,
            matchFormat, type;
    // 选择的球场的经纬度
    private double latitude, longtitude;
    private MatchModel match = new MatchModel();
    private String id = "";
    private String district = "";
    private static final int DECIMAL_DIGITS = 2;

    private Context mContext;

    Handler myhandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    initAnother();
                    break;
                case 2:
                    Toast.makeText(AppointActivity.this, "网络异常",
                            Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    @SuppressLint("HandlerLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_appoint);
        mContext = this;

        mIntent = getIntent();
        id = mIntent.getStringExtra("matchId");
        findViewById();
        if (TextUtils.isEmpty(id)) {//true
            initView();
        } else {
            initDetail();
        }
    }

    protected void initDetail() {
        new Thread() {
            public void run() {
                match = matchController.getinfo(id);
                if (match == null) {
                    myhandler.sendEmptyMessage(2);
                } else {
                    myhandler.sendEmptyMessage(1);
                }
            }
        }.start();
    }

    /**
     * 封装好的findId方法
     */
    protected void findViewById() {
        matchController = new MatchController();
        fee = (EditText) findViewById(R.id.fee_content);
        fee.setFilters(new InputFilter[]{lengthfilter});
        endTime = (TextView) findViewById(R.id.date_end);
        during = (TextView) findViewById(R.id.set_during);
        type = (TextView) findViewById(R.id.appoint_type);
        number = (EditText) findViewById(R.id.num_content);
        startTime = (TextView) findViewById(R.id.date_start);
        title = (EditText) findViewById(R.id.title_content);
        phone = (EditText) findViewById(R.id.phone_content);
        detail = (EditText) findViewById(R.id.match_detail); // 球局详情
        endDate = (RelativeLayout) findViewById(R.id.end_date);
        placeMap = (RelativeLayout) findViewById(R.id.place_map);
        startDate = (RelativeLayout) findViewById(R.id.start_date);
        place = (EditText) findViewById(R.id.appoint_place_detail);
        matchFormat = (TextView) findViewById(R.id.appoint_format);
        duringTime = (RelativeLayout) findViewById(R.id.get_during);
        typeBtn = (RelativeLayout) findViewById(R.id.appoint_type_txt);
        confirmButton = (Button) findViewById(R.id.appoint_confirm_btn);
        placeDetail = (TextView) findViewById(R.id.appoint_detail_place);
        formatBtn = (RelativeLayout) findViewById(R.id.appoint_format_txt);
    }

    /**
     * 封装好的初始化方法
     */
    protected void initAnother() {
        title.setText(match.getTitle());
        startTime.setText(match.getStart_time());
        endTime.setText(match.getStart_time());
        double time = Double.valueOf(match.getDuration()) / 60;
        if (time - (int) time == 0) {
            during.setText((int) time + "");
        } else {
            during.setText(time + "");
        }
        if (match.getSpec().equals("1")) {
            matchFormat.setText("半场");
        } else {
            matchFormat.setText("全场");
        }
        number.setText(match.getNum());
        if (match.getType().equals("0")) {
            type.setText("公开球局");
        } else {
            type.setText("私密球局");
        }
        place.setText(match.getS_name());
        placeDetail.setText(match.getLocation());
        phone.setText(match.getU_mobile());
        fee.setText(match.getFee());
        detail.setText(match.getDetail());
        longtitude = match.getLongitude();
        latitude = match.getLatitude();

        typeBtn.setOnClickListener(this);
        placeMap.setOnClickListener(this);
        startDate.setOnClickListener(this);
        formatBtn.setOnClickListener(this);
        duringTime.setOnClickListener(this);
        confirmButton.setOnClickListener(this);
        inflater = (LayoutInflater) this
                .getSystemService(LAYOUT_INFLATER_SERVICE);
    }

    /**
     * 封装好的初始化方法
     */
    protected void initView() {
        typeBtn.setOnClickListener(this);
        endDate.setOnClickListener(this);
        placeMap.setOnClickListener(this);
        startDate.setOnClickListener(this);
        formatBtn.setOnClickListener(this);
        duringTime.setOnClickListener(this);
        confirmButton.setOnClickListener(this);
        inflater = (LayoutInflater) this
                .getSystemService(LAYOUT_INFLATER_SERVICE);
    }

    // 点击事件
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.place_map:
                LogUtils.e("经度和纬度是：" + Config.lat + "--" + Config.lon);
                if (Config.lat == 0.0 || Config.lon == 0.0) {
                    Toast.makeText(AppointActivity.this, "wait", Toast.LENGTH_SHORT)
                            .show();
                } else {
                    Intent mapIntent = new Intent(AppointActivity.this,
                            PoiAroundSearchActivity.class);
                    mapIntent.putExtra("lon", Config.lon);
                    mapIntent.putExtra("lat", Config.lat);
                    startActivityForResult(mapIntent, 0);
                }

                break;
            // 点击确认发布按钮
            case R.id.appoint_confirm_btn:
                confirmRelease();
                break;

            // 点击开始时间行，弹出日期选择框
            case R.id.start_date:
                hideInput(v);
                showPopwindow(getDataPick(startTime));
                break;
            case R.id.end_date:
                Toast.makeText(AppointActivity.this, "与开始时间相同", Toast.LENGTH_LONG)
                        .show();
                break;
            // 点击球局时长行，弹出时间选择框
            case R.id.get_during:
                hideInput(v);
                showPopwindow(getDuringPick(during));
                break;
            // 点击球局规格行， 弹出规格选择框
            case R.id.appoint_format_txt:
                hideInput(v);
                showPopwindow(getFormatPick(matchFormat));
                break;
            // 点击球局类型行，弹出类型选择框
            case R.id.appoint_type_txt:
                hideInput(v);
                showPopwindow(getTypePick(type));
                break;
            default:
                break;
        }
    }

    /***
     * 确认发布
     */
    private void confirmRelease() {
        int duration = (int) (Double.valueOf(during.getText().toString()) * 60);
        int iFormat;
        if (matchFormat.getText().toString().equals("半场")) {
            iFormat = 1;
        } else {
            iFormat = 0;
        }
        int iType;
        if (type.getText().toString().equals("私密球局")) {
            iType = 1;
        } else {
            iType = 0;
        }
        String titleParams = title.getText().toString().trim();
        String startTimeParams = startTime.getText().toString().trim();
        String numberParams = number.getText().toString().trim();
        String feeParams = fee.getText().toString().trim();
        String detailParams = detail.getText().toString().trim();
        String placeParams = place.getText().toString().trim();
        String placeDetailParams = placeDetail.getText().toString().trim();
        String phoneParams = phone.getText().toString().trim();

        if (TextUtils.isEmpty(titleParams)) {
            ToastUtil.show(mContext, "请输入球局标题");
            return;
        }
        if (TextUtils.isEmpty(startTimeParams)) {
            ToastUtil.show(mContext, "请选择开始时间");
            return;
        }
        if (TextUtils.isEmpty(numberParams)) {
            ToastUtil.show(mContext, "请输入球局人数");
            return;
        }
        if (TextUtils.isEmpty(placeParams)) {
            ToastUtil.show(mContext, "请输入所在场馆");
            return;
        }
        if (TextUtils.isEmpty(placeDetailParams)) {
            ToastUtil.show(mContext, "请输入详细地址");
            return;
        }
        if (TextUtils.isEmpty(phoneParams)) {
            ToastUtil.show(mContext, "请输入手机号码");
            return;
        }
        if (!ValidateUtil.isMobile(phoneParams)) {
            ToastUtil.show(mContext, "手机号格式不正确");
            return;
        }
        if (TextUtils.isEmpty(feeParams)) {
            ToastUtil.show(mContext, "请输入球局费用");
            return;
        }
        if (TextUtils.isEmpty(detailParams)) {
            ToastUtil.show(mContext, "请输入球局详情");
            return;
        }

        confirmNet(duration, iFormat, iType, titleParams, startTimeParams, numberParams, feeParams, detailParams, placeParams, placeDetailParams, phoneParams);

    }

    /***
     * 确认提交
     * @param duration
     * @param iFormat
     * @param iType
     * @param titleParams
     * @param startTimeParams
     * @param numberParams
     * @param feeParams
     * @param detailParams
     * @param placeParams
     * @param placeDetailParams
     * @param phoneParams
     */
    private void confirmNet(final int duration, final int iFormat, final int iType, final String titleParams, final String startTimeParams, final String numberParams, final String feeParams, final String detailParams, final String placeParams, final String placeDetailParams, final String phoneParams) {
        Observable.create(new Observable.OnSubscribe<String>() {

            @Override
            public void call(Subscriber<? super String> subscriber) {
                int rs = matchController.release(titleParams,
                        Config.login_userid, startTimeParams,
                        duration, numberParams, iType, iFormat,
                        feeParams, detailParams, placeParams
                        , placeDetailParams, latitude, longtitude,
                        phoneParams, district);
                subscriber.onNext(rs+"");
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<String>() {
                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(String s) {
                        int rs = Integer.parseInt(s);
                        if (rs == Config.AppointSuccess) {
                            ToastUtil.show(mContext, "发布成功");
                            finish();
                        } else if (rs == -1) {
                            ToastUtil.show(mContext, "网络异常");
                        } else {
                            if (Config.AppointFailedHint == null) {
                                ToastUtil.show(mContext, "网络异常");
                            } else {
                                if (Config.AppointFailedHint
                                        .equals("\u53d1\u5e03\u7403\u5c40\u524d\u8bf7\u5148\u7ed1\u5b9a\u624b\u673a\u53f7\uff01")) {
                                    mIntent = new Intent(AppointActivity.this,
                                            ValidateMobileActivity.class);
                                    startActivity(mIntent);
                                } else {
                                    ToastUtil.show(mContext, Config.AppointFailedHint);
                                }
                            }
                        }
                    }
                });
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

    /**
     * 初始化format pick
     *
     * @param tv
     * @return view
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    private View getFormatPick(final TextView tv) {
        final View view = inflater.inflate(R.layout.formatpick, null);
        mForamt = (WheelView) view.findViewById(R.id.format_choose);
        String format[] = {"全场", "半场"};
        mForamt.setAdapter(new ArrayWheelAdapter(format));
        Button bt = (Button) view.findViewById(R.id.f_set);
        bt.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                int id = mForamt.getCurrentItem();
                String f = mForamt.getAdapter().getItem(id);
                matchFormat.setText(f);
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

    /**
     * 初始化type pick
     *
     * @param tv
     * @return view
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    private View getTypePick(final TextView tv) {
        final View view = inflater.inflate(R.layout.formatpick, null);
        mType = (WheelView) view.findViewById(R.id.format_choose);
        String format[] = {"公共球局", "私密球局"};
        mType.setAdapter(new ArrayWheelAdapter(format));
        Button bt = (Button) view.findViewById(R.id.f_set);
        bt.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                int id = mType.getCurrentItem();
                String f = mType.getAdapter().getItem(id);
                type.setText(f);
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

    /**
     * 初始化 球局时长
     *
     * @param tv
     * @return view
     */
    private View getDuringPick(final TextView tv) {
        final View view = inflater.inflate(R.layout.duringpick, null);
        wDuring = (WheelView) view.findViewById(R.id.during);
        wDuring.setAdapter(new DuringWheelAdapter(0.5, 8));
        wDuring.setLabel("小时");
        wDuring.setCyclic(true);
        Button bt = (Button) view.findViewById(R.id.d_set);
        bt.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                double str = ((double) wDuring.getCurrentItem()) / 2 + 0.5;
                if (str - (int) str == 0) {
                    during.setText((int) str + "");
                } else {
                    during.setText(str + "");
                }
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

    /**
     * 初始化日期
     *
     * @param tv
     * @return view
     */
    private View getDataPick(final TextView tv) {
        Calendar c = Calendar.getInstance();
        int curYear = c.get(Calendar.YEAR);
        int curMonth = c.get(Calendar.MONTH) + 1;// 通过Calendar算出的月数要+1
        int curDate = c.get(Calendar.DATE);
        int curHour = c.get(Calendar.HOUR_OF_DAY);
        int curMinute = c.get(Calendar.MINUTE);
        final View view = inflater.inflate(R.layout.datapick, null);

        year = (WheelView) view.findViewById(R.id.year);
        year.setAdapter(new NumericWheelAdapter(2000, 2030));
        year.setLabel("年");
        year.setCyclic(true);
        year.addScrollingListener(scrollListener);

        month = (WheelView) view.findViewById(R.id.month);
        month.setAdapter(new NumericWheelAdapter(1, 12));
        month.setLabel("月");
        month.setCyclic(true);
        month.addScrollingListener(scrollListener);

        day = (WheelView) view.findViewById(R.id.day);
        initDay(curYear, curMonth);
        day.setLabel("日");
        day.setCyclic(true);

        year.setCurrentItem(curYear - 2000);
        month.setCurrentItem(curMonth - 1);
        day.setCurrentItem(curDate - 1);
        hour = (WheelView) view.findViewById(R.id.hour);
        hour.setAdapter(new NumericWheelAdapter(0, 23));
        hour.setLabel("时");
        hour.setCyclic(true);
        mins = (WheelView) view.findViewById(R.id.mins);
        mins.setAdapter(new NumericWheelAdapter(0, 59));
        mins.setLabel("分");
        mins.setCyclic(true);

        hour.setCurrentItem(curHour);
        mins.setCurrentItem(curMinute);

        Button bt = (Button) view.findViewById(R.id.set);
        bt.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String str = (year.getCurrentItem() + 2000) + "-"
                        + (month.getCurrentItem() + 1) + "-"
                        + (day.getCurrentItem() + 1) + " "
                        + hour.getCurrentItem() + ":" + mins.getCurrentItem()
                        + ":00";
                tv.setText(str);
                endTime.setText(str);
                menuWindow.dismiss();
            }
        });
        Button cancel = (Button) view.findViewById(R.id.cancel);
        cancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                menuWindow.dismiss();
            }
        });
        return view;
    }

    /**
     * 年月日在wheelView里的循环
     *
     * @param year
     * @param month
     * @return
     */
    OnWheelScrollListener scrollListener = new OnWheelScrollListener() {

        @Override
        public void onScrollingStarted(WheelView wheel) {

        }

        @Override
        public void onScrollingFinished(WheelView wheel) {
            int n_year = year.getCurrentItem() + 1950;
            int n_month = month.getCurrentItem() + 1;
            initDay(n_year, n_month);
        }
    };

    /**
     * 判断每个月的日数，闰年
     *
     * @param year
     * @param month
     * @return day
     */
    private int getDay(int year, int month) {
        int day = 30;
        boolean flag = false;
        if ((year + 50) % 4 == 0)
            flag = true;
        switch (month) {
            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 12:
                day = 31;
                break;
            case 2:
                if (flag) {
                    day = 29;
                    break;
                } else {
                    day = 28;
                    break;
                }
            default:
                day = 30;
                break;
        }
        return day;
    }

    // 初始化日
    private void initDay(int arg1, int arg2) {
        day.setAdapter(new NumericWheelAdapter(1, getDay(arg1, arg2), "%02d"));
    }

    /**
     * 点击地图的回调方法
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 处理扫描结果（在界面上显示）
        if (resultCode == Activity.RESULT_OK) {
            Bundle bundle = data.getExtras();
            String placeDetailResult = bundle.getString("detailPlace");
            latitude = bundle.getDouble("latitude");
            longtitude = bundle.getDouble("longtitude");
            district = bundle.getString("district");
            placeDetail.setText(placeDetailResult);
        }
    }

    /**
     * 设置小数位数控制
     */
    InputFilter lengthfilter = new InputFilter() {
        @Override
        public CharSequence filter(CharSequence source, int start, int end,
                                   Spanned dest, int dstart, int dend) {
            // 删除等特殊字符，直接返回   
            if ("".equals(source.toString())) {
                return null;
            }
            String dValue = dest.toString();
            String[] splitArray = dValue.split("//.");
            if (splitArray.length > 1) {
                String dotValue = splitArray[1];
                int diff = dotValue.length() + 1 - DECIMAL_DIGITS;
                if (diff > 0) {
                    return source.subSequence(start, end - diff);
                }
            }
            return null;
        }
    };
}