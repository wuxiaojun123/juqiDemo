package cn.com.sfn.juqi.my.auth;

import cn.com.sfn.juqi.controller.UserController;
import cn.com.sfn.juqi.util.Config;

import com.example.juqi.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SetAccountActivity extends Activity implements OnClickListener {
	private EditText account;
	private Button confirm;
	private UserController userController;
	private Intent mIntent;
	private String type;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_set_account);
		userController = new UserController();
		mIntent = getIntent();
		type = mIntent.getStringExtra("type");
		findViewById();
		initView();
	}

	protected void findViewById() {
		account = (EditText) findViewById(R.id.account);
		confirm = (Button) findViewById(R.id.confirm_account);
	}

	protected void initView() {
		confirm.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.confirm_account:
			int rs = userController.setAccount(account.getText().toString()
					.trim(), type);
			if (rs == Config.SetAccountSuccess) {
				Toast.makeText(SetAccountActivity.this, "绑定成功",
						Toast.LENGTH_SHORT).show();
				finish();
			} else if (rs == -1) {
				Toast.makeText(SetAccountActivity.this, "网络异常",
						Toast.LENGTH_LONG).show();
			} else {
				Toast.makeText(SetAccountActivity.this, "绑定失败",
						Toast.LENGTH_SHORT).show();
			}
			break;
		default:
			break;
		}
	}
}
