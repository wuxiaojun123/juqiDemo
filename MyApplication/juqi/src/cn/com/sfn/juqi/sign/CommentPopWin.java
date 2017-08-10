package cn.com.sfn.juqi.sign;

import cn.com.sfn.juqi.controller.MatchController;
import cn.com.sfn.juqi.util.Config;

import com.example.juqi.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.Toast;

public class CommentPopWin extends PopupWindow {
	private Button send;
	private View mMenuView; // PopupWindow上面装载的View
	private EditText comments; // 签名输入框
	private int result;
	private MatchController matchController;

	public CommentPopWin(final Activity context, final String parentId,
			final String id, final String str, final String table,
			final String toUid) {
		super(context);
		/* 将xml布局初始化为View,并初始化上面的控件 */
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mMenuView = inflater.inflate(R.layout.take_comments_pop, null);
		matchController = new MatchController();
		comments = (EditText) mMenuView.findViewById(R.id.comments);
		comments.setText(str);//初始空
		send = (Button) mMenuView.findViewById(R.id.send_confirm);
		send.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				result = matchController.comments(comments.getText().toString()
						.trim(), parentId, id, table, toUid);
				if (result == Config.CommentSuccess) {
					Toast.makeText(context, "评论成功,审核后才能显示！", Toast.LENGTH_LONG)
							.show();
					dismiss();
				} else if (result == -1) {
					Toast.makeText(context, "网络异常", Toast.LENGTH_LONG).show();
				} else {
					Toast.makeText(context, "评论失败", Toast.LENGTH_LONG).show();
				}
				dismiss();
			}
		});
		// 设置SignPopupWindow的View
		this.setContentView(mMenuView);
		// 设置SignPopupWindow弹出窗体的宽
		this.setWidth(LayoutParams.MATCH_PARENT);
		// 设置SignPopupWindow弹出窗体的高
		this.setHeight(LayoutParams.WRAP_CONTENT);
		// 设置SignPopupWindow弹出窗体可点击
		this.setFocusable(true);
		// 设置SignPopupWindow弹出窗体动画效果
		this.setAnimationStyle(R.style.take_share_anim);
		// 实例化一个ColorDrawable颜色为半透明
		ColorDrawable dw = new ColorDrawable(0x80000000);
		// 设置SignPopupWindow弹出窗体的背景
		this.setBackgroundDrawable(dw);
		// 防止虚拟软键盘被弹出菜单遮住
		this.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

		// mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
		mMenuView.setOnTouchListener(new OnTouchListener() {
			@SuppressLint("ClickableViewAccessibility")
			public boolean onTouch(View v, MotionEvent event) {
				int height = mMenuView.findViewById(R.id.comments_layout)
						.getTop();
				int y = (int) event.getY();
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					if (y < height) {
						dismiss();
					}
				}
				return true;
			}
		});
	}
}