package cn.com.sfn.juqi.manage;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.example.juqi.R;

public class ConfirmPopWin extends PopupWindow {
	private View view;
	private Button ok;

	public ConfirmPopWin(Context mContext, OnClickListener itemsOnClick) {
		this.view = LayoutInflater.from(mContext).inflate(
				R.layout.confirm_delete_pop, null);
		ok = (Button) view.findViewById(R.id.delete_ok);
		ok.setOnClickListener(itemsOnClick);
		// 设置外部可点击
		this.setOutsideTouchable(true);
		/* 设置弹出窗口特征 */
		// 设置视图
		this.setContentView(this.view);
		// 设置弹出窗体的宽和高
		this.setHeight(RelativeLayout.LayoutParams.MATCH_PARENT);
		this.setWidth(RelativeLayout.LayoutParams.MATCH_PARENT);
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
