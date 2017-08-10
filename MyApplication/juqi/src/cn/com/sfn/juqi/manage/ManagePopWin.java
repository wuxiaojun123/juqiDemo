package cn.com.sfn.juqi.manage;

import com.example.juqi.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

public class ManagePopWin extends PopupWindow {
	private View view;
	private LinearLayout wxshare, pyshare, wbshare, change, another, cancel,
			copyshare;
	private Button dis;

	public ManagePopWin(Context mContext, OnClickListener itemsOnClick) {
		this.view = LayoutInflater.from(mContext).inflate(
				R.layout.take_more_options, null);
		wxshare = (LinearLayout) view.findViewById(R.id.wx_share);
		pyshare = (LinearLayout) view.findViewById(R.id.py_share);
		wbshare = (LinearLayout) view.findViewById(R.id.wb_share);
		another = (LinearLayout) view.findViewById(R.id.another_btn);
		cancel = (LinearLayout) view.findViewById(R.id.cancel_btn);
		//change = (LinearLayout) view.findViewById(R.id.change_btn);
		copyshare = (LinearLayout) view.findViewById(R.id.copy_share);
		wxshare.setOnClickListener(itemsOnClick);
		pyshare.setOnClickListener(itemsOnClick);
		wbshare.setOnClickListener(itemsOnClick);
		copyshare.setOnClickListener(itemsOnClick);
		another.setOnClickListener(itemsOnClick);
		//change.setOnClickListener(itemsOnClick);
		cancel.setOnClickListener(itemsOnClick);
		dis = (Button) view.findViewById(R.id.manage_option_cancel);
		dis.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dismiss();
			}
		});
		// 设置外部可点击
		this.setOutsideTouchable(true);
		// mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
		this.view.setOnTouchListener(new View.OnTouchListener() {
			@SuppressLint("ClickableViewAccessibility")
			public boolean onTouch(View v, MotionEvent event) {

				int height = view.findViewById(R.id.manage_options_content)
						.getTop();

				int y = (int) event.getY();
				if (event.getAction() == MotionEvent.ACTION_UP) {
					if (y < height) {
						dismiss();
					}
				}
				return true;
			}
		});
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
