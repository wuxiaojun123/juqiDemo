package cn.com.sfn.juqi.my.auth;

import cn.com.sfn.juqi.controller.UserController;
import cn.com.sfn.juqi.model.AuthModel;
import cn.com.sfn.juqi.util.Config;
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
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("HandlerLeak")
public class GoWithdrawActivity extends Activity implements OnClickListener {
	private TextView back, type, account, yue;
	private EditText money;
	private UserController userController;
	private AuthModel authModel;
	private CircleImageView avatar;
	private Handler myhandler;
	private Button confirm;
	private Intent mIntent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_go_withdraw);
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

	private void findViewById() {
		back = (TextView) findViewById(R.id.withdraw_back_to_my);
		money = (EditText) findViewById(R.id.withdrawmoney);
		avatar = (CircleImageView) findViewById(R.id.withdraw_avatar);
		type = (TextView) findViewById(R.id.type);
		account = (TextView) findViewById(R.id.account);
		yue = (TextView) findViewById(R.id.tixian);
		confirm = (Button) findViewById(R.id.withdraw_confirm_btn);
	}

	private void initView() {
		back.setOnClickListener(this);
		@SuppressWarnings("deprecation")
		Drawable drawable = new BitmapDrawable(authModel.getAvatar());// 转换成drawable
		avatar.setImageDrawable(drawable);
		if (authModel.getType().equals("支付宝")) {
			type.setText("支付宝账户");
		} else if(authModel.getType().equals("微信")){
			type.setText("微信账户");
		} else {
			type.setText("网络错误");
		}
		if (authModel.getAccount().equals("")) {
			account.setText("点我绑定");
		} else {
			account.setText(authModel.getAccount());
		}

		account.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mIntent = new Intent(GoWithdrawActivity.this,
						ChooseAccountActivity.class);
				startActivity(mIntent);
				finish();
			}
		});
		yue.setText("可提现余额:" + authModel.getResidula() + "元");
		if (type.getText().toString().equals("")) {
			confirm.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					mIntent = new Intent(GoWithdrawActivity.this,
							ChooseAccountActivity.class);
					startActivity(mIntent);
					finish();
				}
			});
		} else {
			confirm.setOnClickListener(this);
		}
	}

	protected void initOperate() {
		new Thread() {
			public void run() {
				authModel = userController.auth();
				Message msg = new Message();
				msg.what = 1;
				myhandler.sendMessage(msg);
			}
		}.start();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.withdraw_back_to_my:
			finish();
			break;
		case R.id.withdraw_confirm_btn:
			if (!TextUtils.isEmpty(money.getText().toString())) {
				int rs = userController.withDraw(account.getText().toString()
						.trim(), money.getText().toString(),
						authModel.getType());
				if (rs == Config.WithDrawSuccess) {
					Toast.makeText(GoWithdrawActivity.this, "24小时内将到提现到您的账户",
							Toast.LENGTH_LONG).show();
					finish();
				} else if (rs == -1) {
					Toast.makeText(GoWithdrawActivity.this, "网络异常",
							Toast.LENGTH_LONG).show();
				} else {
					Toast.makeText(GoWithdrawActivity.this, "提现失败",
							Toast.LENGTH_SHORT).show();
				}
			} else {
				Toast.makeText(GoWithdrawActivity.this, "请输入提现金额",
						Toast.LENGTH_LONG).show();
			}
			break;
		default:
			break;
		}
	}
}
