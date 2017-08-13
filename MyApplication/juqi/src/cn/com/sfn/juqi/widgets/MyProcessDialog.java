package cn.com.sfn.juqi.widgets;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.juqi.R;

/**
 * Created by wuxiaojun on 17-3-2.
 */

public class MyProcessDialog {


    public static Dialog loadingDialog = null;

    /**
     * 显示一个等待框
     *
     * @param context 上下文环境
     * @param text 显示内容
     * @param isCancel 是否能用返回取消
     * @param isRight true 文字在右边false在下面
     */
    public static void showDialog(Context context, String text, boolean isCancel, boolean isRight) {
        try {
            creatDialog(context, text, isCancel, isRight);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 显示一个等待框
     *
     * @param context 上下文环境
     * @param text 显示内容
     */
    public static void showDialog(Context context, String text) {
        try {
            creatDialog(context, text, false, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 显示一个等待框
     *
     * @param context 上下文环境
     */
    public static void showDialog(Context context) {
        try {
            creatDialog(context, "加载中", true, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static TextView tipTextView;

    /**
     * 显示一个等待框
     *
     * @param context 上下文环境
     * @param msg 等待框的文字
     * @param isCancel 是否能用返回取消
     * @param isRight true 文字在右边false在下面
     */
    private static void creatDialog(Context context, String msg, boolean isCancel, boolean isRight) {
        LinearLayout.LayoutParams wrap_content = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        LinearLayout.LayoutParams wrap_content0 = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        LinearLayout main = new LinearLayout(context);
        main.setBackgroundColor(context.getResources().getColor(android.R.color.transparent));
        if (isRight) {
            main.setOrientation(LinearLayout.HORIZONTAL);
            wrap_content.setMargins(10, 0, 35, 0);
            wrap_content0.setMargins(35, 25, 0, 25);
        } else {
            main.setOrientation(LinearLayout.VERTICAL);
            wrap_content.setMargins(10, 5, 10, 15);
            wrap_content0.setMargins(35, 25, 35, 0);
        }
        main.setGravity(Gravity.CENTER);
        ImageView spaceshipImage = new ImageView(context);
        spaceshipImage.setImageResource(R.drawable.publicloading);
        tipTextView = new TextView(context);
        tipTextView.setTextColor(Color.WHITE);
        tipTextView.setText("请稍候...");
        // 加载旋转动画
        Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(context,
                R.anim.loading_animation);
        // 使用ImageView显示动画
        spaceshipImage.startAnimation(hyperspaceJumpAnimation);
        if (!TextUtils.isEmpty(msg))
            tipTextView.setText(msg);// 设置加载信息,否则加载默认值
        if (loadingDialog != null) {
            loadingDialog.show();
            return;
        }
        loadingDialog = new Dialog(context, R.style.loading_dialog);// 创建自定义样式dialog
        loadingDialog.setCancelable(true);// 是否可以用返回键取消
        loadingDialog.setCanceledOnTouchOutside(isCancel);// 是否可以点击其他地方取消
        main.addView(spaceshipImage, wrap_content0);
        main.addView(tipTextView, wrap_content);
        LinearLayout.LayoutParams fill_parent = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        loadingDialog.setContentView(main, fill_parent);// 设置布局
        loadingDialog.show();
    }

    /***
     * 设置dialog信息
     *
     * @param msg
     */
    public static void setDialogMsg(String msg) {
        if (loadingDialog != null && loadingDialog.isShowing() && tipTextView != null) {
            tipTextView.setText(msg);
        }
    }

    /**
     * 关闭对话框
     */
    public static void closeDialog() {
        if (loadingDialog != null) {
            loadingDialog.dismiss();
            loadingDialog = null;
            tipTextView = null;
            System.gc();
        }
    }

}
