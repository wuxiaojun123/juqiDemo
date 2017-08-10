package cn.com.sfn.juqi.sign;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.regex.Pattern;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Message;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import cn.com.sfn.alipay.util.SignUtils;
import cn.com.sfn.juqi.controller.MatchController;
import cn.com.sfn.juqi.model.MatchModel;
import cn.com.sfn.juqi.my.FriendActivity;
import cn.com.sfn.juqi.my.FriendDetailActivity;
import cn.com.sfn.juqi.util.Config;

import com.alipay.sdk.app.PayTask;
import com.example.juqi.R;

public class AttendPopWin extends PopupWindow {
	private TextView fee, check;
	private Button confirm;
	private View mMenuView; // PopupWindow上面装载的View
	private CheckBox checkBox;
	private EditText name, phone; // 签名输入框
	private MatchController matchController;
	private MatchModel match = new MatchModel();
	private MatchDetailActivity matchdetail=new MatchDetailActivity();
	private int result;
	private Intent mIntent;
	private Activity attendcontext;
	public static int index=0;
	
    

	public AttendPopWin(final Activity  context, final String matchId,     //final Activity context
			final String personFee) {                                    //球赛的id
		super(context);
		

		/* 将xml布局初始化为View,并初始化上面的控件 */
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mMenuView = inflater.inflate(R.layout.take_attend_pop, null);
		matchController = new MatchController();
		
		fee = (TextView) mMenuView.findViewById(R.id.attend_fee);
		check = (TextView) mMenuView.findViewById(R.id.check_url);
		check.setMovementMethod(LinkMovementMethod.getInstance());
		fee.setText(personFee);
		name = (EditText) mMenuView.findViewById(R.id.attend_name);
		phone = (EditText) mMenuView.findViewById(R.id.attend_phone);
		checkBox = (CheckBox) mMenuView.findViewById(R.id.check_attend);
		confirm = (Button) mMenuView.findViewById(R.id.attend_confirm);
		confirm.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (name.getText().toString().equals("")
						|| phone.getText().toString().equals("")) {
					Toast.makeText(context, "请全部填写", Toast.LENGTH_SHORT).show();
				} else {
					if (isPhone(phone.getText().toString())) {
						if (checkBox.isChecked()) {
							result = matchController.attend(matchId, name
									.getText().toString(), phone.getText()
									.toString());
							Log.e("matchController.attend-result",Integer.toString(result) );
							if (result == Config.AttendSuccess) {							
								
								
								Toast.makeText(context, "报名成功",
										Toast.LENGTH_LONG).show();			
								//context.finish();
								context.recreate();
								dismiss();
								index=1;
								
							} else if (result == -1) {
								Toast.makeText(context, "网络异常",
										Toast.LENGTH_LONG).show();
							} else {
								Toast.makeText(context, "报名失败",
										Toast.LENGTH_LONG).show();
							}
						} else {
							Toast.makeText(context, "请勾选", Toast.LENGTH_SHORT)
									.show();
						}
					} else {
						Toast.makeText(context, "请输入正确的手机号", Toast.LENGTH_SHORT)
								.show();
					}
				}
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

				int height = mMenuView.findViewById(R.id.attend_layout)
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

	private boolean isPhone(String text) {
		if (Pattern.compile("^(1)\\d{10}$|(\\+\\d{3,})").matcher(text).matches()) {
			return true;
		}
		return false;
	}
	
	
	
	
}