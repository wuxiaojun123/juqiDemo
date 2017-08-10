/**
 * @author wangwb
 * 该页面是验票后的跳转页面
 */
package cn.com.sfn.juqi.manage;

import cn.com.sfn.juqi.controller.MatchController;
import cn.com.sfn.juqi.util.Config;

import com.example.juqi.R;
import com.zxing.activity.CaptureActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

@SuppressLint("HandlerLeak")
public class InspectActivity extends Activity {
	private Intent mIntent;
	private TextView resultOk, failedInfo, failedTime, back;
	private LinearLayout resultFaild;
	private Button continueInspect;
	private String result;
	private MatchController matchController;
	private int rs;
	private Handler myhandler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_inspect);
		matchController = new MatchController();
		back = (TextView) findViewById(R.id.inspect_back_to_manage);
		resultOk = (TextView) findViewById(R.id.inspect_ok_result);
		resultFaild = (LinearLayout) findViewById(R.id.inspect_failed_result);
		failedInfo = (TextView) findViewById(R.id.inspect_failed_info);
		failedTime = (TextView) findViewById(R.id.inspect_failed_time);
		continueInspect = (Button) findViewById(R.id.continue_inspect);
		mIntent = getIntent();
		result = mIntent.getStringExtra("result");//扫码类传过来的结果
		initResult();
		myhandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case 1:
					if (rs == Config.ScanQrFailed) {
						resultOk.setVisibility(View.INVISIBLE);
						failedInfo.setText("此凭证已签到");
						failedTime.setText("签到时间：2016-02-12 13:55:22");
					} else {
						resultFaild.setVisibility(View.INVISIBLE);
					}
					continueInspect.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							mIntent = new Intent();
							mIntent.setClass(InspectActivity.this,
									CaptureActivity.class);
							startActivity(mIntent);
							finish();
						}
					});
					break;
				default:
					break;
				}
			}
		};
		back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

	public void initResult() {
		new Thread() {
			public void run() {
				rs = matchController.getQRresult(result);
				Message msg = new Message();
				msg.what = 1;
				myhandler.sendMessage(msg);
			}
		}.start();
	}
}
