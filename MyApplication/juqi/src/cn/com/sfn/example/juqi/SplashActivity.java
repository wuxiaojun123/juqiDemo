
package cn.com.sfn.example.juqi;

import cn.com.sfn.juqi.util.Config;
import cn.com.wx.util.LogUtils;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.example.juqi.R;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.Window;
import android.widget.Toast;

/**
 * 功能：引导页
 *
 * @author wangwenbo
 */
@SuppressLint("HandlerLeak")
public class SplashActivity extends Activity implements AMapLocationListener {
    // 定位
    private LocationManagerProxy mLocationManagerProxy;
    private SharedPreferences setting;
    private Boolean is_login;
    // 自己微信应用的 appId
//    public static final String WX_APP_ID = "wx6091081fcba9999a";
    // 自己微信应用的 appSecret
//    public static final String WX_SECRET = "c9ed9adab65c9c9db3598d3f2d10b576";
//    public static IWXAPI wxApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_splash);
        setting = this.getSharedPreferences(Config.PREFS_NAME,
                Context.MODE_PRIVATE);
        is_login = setting.getBoolean("isLogin", false);
        LogUtils.e("是否登录" + is_login);
        Config.SessionID = setting.getString("sessionId", null);
        Config.login_type = setting.getString("type", null);
        Config.login_userid = setting.getString("userid", null);

        if (!TextUtils.isEmpty(Config.login_type))
            Log.e("TYTY", Config.login_type);
        if (!TextUtils.isEmpty(Config.SessionID))
            Log.e("IDDID", Config.SessionID);
//        wxApi = WXAPIFactory.createWXAPI(this, WX_APP_ID, true);
//        wxApi.registerApp(WX_APP_ID);
        mLocationManagerProxy = LocationManagerProxy.getInstance(this);
        // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
        // 注意设置合适的定位时间的间隔，并且在合适时间调用removeUpdates()方法来取消定位请求
        // 在定位结束后，在合适的生命周期调用destroy()方法
        // 其中如果间隔时间为-1，则定位只定一次
        mLocationManagerProxy.requestLocationData(
                LocationProviderProxy.AMapNetwork, -1, 15, this);
        mLocationManagerProxy.setGpsEnable(false);
        boolean mFirst = isFirstEnter(SplashActivity.this, SplashActivity.this
                .getClass().getName());
        if (mFirst)
            mHandler.sendEmptyMessageDelayed(SWITCH_GUIDACTIVITY, 1000);
        else
            mHandler.sendEmptyMessageDelayed(SWITCH_MAINACTIVITY, 1000);
    }

    private static final String SHAREDPREFERENCES_NAME = "my_pref";
    private static final String KEY_GUIDE_ACTIVITY = "guide_activity";

    @SuppressWarnings("deprecation")
    @SuppressLint("WorldReadableFiles")
    private boolean isFirstEnter(Context context, String className) {
        if (context == null || className == null
                || "".equalsIgnoreCase(className))
            return false;
        String mResultStr = context.getSharedPreferences(
                SHAREDPREFERENCES_NAME, Context.MODE_WORLD_READABLE).getString(
                KEY_GUIDE_ACTIVITY, "");
        if (mResultStr.equalsIgnoreCase("false"))
            return false;
        else
            return true;
    }

    @Override
    public void onLocationChanged(Location arg0) {
        LogUtils.e("经纬度是：" + arg0.getLatitude() + "------" + arg0.getLongitude());
    }

    @Override
    public void onProviderDisabled(String arg0) {
    }

    @Override
    public void onProviderEnabled(String arg0) {
    }

    @Override
    public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
    }

    @Override
    public void onLocationChanged(AMapLocation amapLocation) {
        LogUtils.e("返回的地图位置:" + amapLocation);
        if (amapLocation != null
                && amapLocation.getAMapException().getErrorCode() == 0) {
            // 获取位置信息
            Double geoLat = amapLocation.getLatitude();
            Double geoLng = amapLocation.getLongitude();
            Config.lat = geoLat;
            Config.lon = geoLng;
            LogUtils.e("定位的经度是:" + Config.lat + "------" + Config.lon);
//            Toast.makeText(SplashActivity.this, Config.lat + "+" + Config.lon, Toast.LENGTH_SHORT).show();
//            Log.e("onLocationChanged-geoLat", "geoLat");
        }
    }

    // 进入主界面
    private final static int SWITCH_MAINACTIVITY = 1000;
    // 进入引导界面
    private final static int SWITCH_GUIDACTIVITY = 1001;

    public Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SWITCH_MAINACTIVITY:
                    if (is_login) {
                        Config.is_login = true;
                    } else {
                        Config.is_login = false;
                    }
                    Intent mIntent = new Intent();
                    mIntent.setClass(SplashActivity.this, MainActivity.class);
                    SplashActivity.this.startActivity(mIntent);
                    SplashActivity.this.finish();
                    break;
                case SWITCH_GUIDACTIVITY:
                    mIntent = new Intent();
                    mIntent.setClass(SplashActivity.this, GuideActivity.class);
                    SplashActivity.this.startActivity(mIntent);
                    SplashActivity.this.finish();
                    break;
            }
            super.handleMessage(msg);
        }
    };
}
