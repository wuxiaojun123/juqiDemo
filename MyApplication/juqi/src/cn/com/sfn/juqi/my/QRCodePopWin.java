package cn.com.sfn.juqi.my;

import cn.com.sfn.juqi.controller.MatchController;
import cn.com.wx.util.DisplayUtil;
import cn.com.wx.util.LogUtils;
import rx.Observable;
import rx.Single;
import rx.SingleSubscriber;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

import com.example.juqi.R;
import com.google.zxing.WriterException;
import com.zxing.encoding.EncodingHandler;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class QRCodePopWin extends PopupWindow {
    private View view;
    private TextView hint;
    private ImageView image;
    private Button close;
    private MatchController matchController;


    public QRCodePopWin(Context mContext, final String attendId) {
        this.view = LayoutInflater.from(mContext).inflate(R.layout.take_qr_pop, null);
        matchController = new MatchController();
        image = (ImageView) view.findViewById(R.id.qr);
        hint = (TextView) view.findViewById(R.id.tv1);
        close = (Button) view.findViewById(R.id.qr_close);

        Observable
                .create(new Observable.OnSubscribe<String>() {
                    @Override
                    public void call(Subscriber<? super String> subscriber) {
                        String content = matchController.getQR(attendId);
                        subscriber.onNext(content);
                    }
                }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String content) {
                        LogUtils.e("获取到的二维码内容是:" + content);
                        if (TextUtils.isEmpty(content)) {
                            hint.setText("已扫描或其他原因，不能查看凭证");
                        } else {
                            try {
                                // 根据字符串生成二维码图片并显示在界面上，第二个参数为图片的大小（600*600）
                                Bitmap qrCodeBitmap = EncodingHandler
                                        .createQRCode(content, 450);
                                image.setImageBitmap(qrCodeBitmap);
                            } catch (WriterException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });

        close.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        /* 设置弹出窗口特征 */
        // 设置视图
        this.setContentView(this.view);
        // 设置弹出窗体的宽和高
        this.setHeight(DisplayUtil.dip2px(mContext, 270));
        int[] devicesSize = DisplayUtil.getDeviceWidthHeight(mContext);
        int width = devicesSize[0] - DisplayUtil.dip2px(mContext, 60);
        this.setWidth(width);
        // 设置弹出窗体可点击
        this.setFocusable(true);
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        // 设置弹出窗体的背景
        this.setBackgroundDrawable(dw);
        // 设置弹出窗体显示时的动画，从底部向上弹出
        this.setAnimationStyle(R.style.take_share_anim);
    }
}
