package cn.com.sfn.juqi.my.auth;

import com.example.juqi.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ChooseAccountActivity extends Activity implements OnClickListener{
	private RelativeLayout ali, wx;
	private TextView back;
	private Intent mIntent;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_choose_account);
		findViewById();
		initView();
	}
	protected void findViewById() {
		ali = (RelativeLayout) findViewById(R.id.ali_btn);
		wx = (RelativeLayout) findViewById(R.id.wx_btn);
		back = (TextView) findViewById(R.id.back_to_setting);
	}
	protected void initView() {
		ali.setOnClickListener(this);
		wx.setOnClickListener(this);
		back.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back_to_setting:
			finish();
			break;
		case R.id.ali_btn:
			mIntent = new Intent();
			mIntent.putExtra("type", "支付宝");
			mIntent.setClass(ChooseAccountActivity.this, SetAccountActivity.class);
			startActivity(mIntent);
			finish();
			break;
		case R.id.wx_btn:
			Toast.makeText(ChooseAccountActivity.this, "暂不支持微信提现哦~",
					Toast.LENGTH_SHORT).show();
			break;
		default:
			break;
		}
	}
}
