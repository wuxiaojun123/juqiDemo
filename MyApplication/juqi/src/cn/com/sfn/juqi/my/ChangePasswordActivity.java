package cn.com.sfn.juqi.my;

import cn.com.sfn.juqi.controller.UserController;
import cn.com.sfn.juqi.util.Config;

import com.example.juqi.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ChangePasswordActivity extends Activity implements OnClickListener {
	private TextView back;
	private EditText old, newpass, repass;
	private Button confirm;
	private UserController userController;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_change_password);
		userController = new UserController();
		findViewById();
		initView();
	}

	private void findViewById() {
		back = (TextView) findViewById(R.id.back_to_setting);
		old = (EditText) findViewById(R.id.old_password);
		newpass = (EditText) findViewById(R.id.new_password);
		repass = (EditText) findViewById(R.id.confirm_new_password);
		confirm = (Button) findViewById(R.id.confirm_change_password);
	}

	private void initView() {
		back.setOnClickListener(this);
		confirm.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back_to_setting:
			finish();
			break;
		case R.id.confirm_change_password:
			int rs = userController.changePassword(old.getText().toString(),
					newpass.getText().toString(), repass.getText().toString());
			if (rs == Config.ChangePasswordSuccess) {
				Toast.makeText(ChangePasswordActivity.this, "修改成功",
						Toast.LENGTH_SHORT).show();
				finish();
			} else if (rs == -1) {
				Toast.makeText(ChangePasswordActivity.this, "网络异常",
						Toast.LENGTH_LONG).show();
			} else {
				Toast.makeText(ChangePasswordActivity.this, "修改失败",
						Toast.LENGTH_SHORT).show();
			}
			break;
		default:
			break;
		}
	}
}
