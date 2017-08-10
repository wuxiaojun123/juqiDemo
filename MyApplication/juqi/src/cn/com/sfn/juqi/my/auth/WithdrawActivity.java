package cn.com.sfn.juqi.my.auth;

import cn.com.sfn.juqi.controller.UserController;
import cn.com.sfn.juqi.model.AuthModel;
import cn.com.sfn.juqi.widgets.CircleImageView;

import com.example.juqi.R;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class WithdrawActivity extends Activity implements OnClickListener {
	private Intent mIntent;
	private TextView backMy, balance, income;
	private TextView authentication, name;
	private Button confirm;
	private Handler myhandler;
	private UserController userController;
	private AuthModel authModel;
	private CircleImageView avatar;

	@SuppressLint("HandlerLeak")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_withdraw);
		userController = new UserController();
		findViewById();
		initOperate();
		myhandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case 1:
					initView();
					break;
				default:
					break;
				}
			}
		};
	}

	protected void findViewById() {
		backMy = (TextView) findViewById(R.id.withdraw_back_to_my);
		authentication = (TextView) findViewById(R.id.is_authentication);
		confirm = (Button) findViewById(R.id.withdraw_confirm_btn);
		avatar = (CircleImageView) findViewById(R.id.withdraw_avatar);
		name = (TextView) findViewById(R.id.uname);
		balance = (TextView) findViewById(R.id.my_balance_txt);
		income = (TextView) findViewById(R.id.my_income_txt);
	}

	protected void initView() {
		backMy.setOnClickListener(this);
		@SuppressWarnings("deprecation")
		Drawable drawable = new BitmapDrawable(authModel.getAvatar());// 转换成drawable
		avatar.setImageDrawable(drawable);
		name.setText(authModel.getName());
		balance.setText(authModel.getBalance());
		income.setText(authModel.getIncome());
		if (authModel.getStatus().equals("0")) {
			authentication.setOnClickListener(this);
			confirm.setOnClickListener(this);
		} else if (authModel.getStatus().equals("1")) {
			authentication.setText("已认证");
			authentication.setClickable(false);
			confirm.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					mIntent = new Intent(WithdrawActivity.this,
							GoWithdrawActivity.class);
					startActivity(mIntent);
				}
			});
		} else if (authModel.getStatus().equals("2")) {
			authentication.setText("正在审核");
			authentication.setClickable(false);
			confirm.setClickable(false);
		} else {
			authentication.setText("未通过");
			authentication.setOnClickListener(this);
			confirm.setOnClickListener(this);
		}
	}

	protected void initOperate() {
		new Thread() {
			public void run() {
				authModel = userController.authDetail();
				Message msg = new Message();
				msg.what = 1;
				myhandler.sendMessage(msg);
			}
		}.start();
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.withdraw_back_to_my:
			finish();
			break;
		case R.id.is_authentication:
			mIntent = new Intent(WithdrawActivity.this,
					AuthenticationActivity.class);
			startActivity(mIntent);
			finish();
			break;
		case R.id.withdraw_confirm_btn:
			mIntent = new Intent(WithdrawActivity.this,
					AuthenticationActivity.class);
			startActivity(mIntent);
			finish();
			break;
		default:
			break;
		}
	}
}