package cn.com.sfn.juqi.my.auth;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import cn.com.sfn.example.juqi.LoginActivity;
import cn.com.sfn.example.juqi.MainActivity;
import cn.com.sfn.juqi.controller.UserController;
import cn.com.sfn.juqi.model.AuthModel;
import cn.com.sfn.juqi.net.MyHttpClient;
import cn.com.sfn.juqi.util.Config;

import com.example.juqi.R;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("SdCardPath")
public class AuthenticationActivity extends Activity implements OnClickListener {
	private Bitmap auth;
	private Intent mIntent;
	// 认证按钮
	private Button authConfirm;
	private EditText real;
	private static String path = "/sdcard/myAuth/";
	private TextView back;
	private TextView mytvreason;
	private ImageView image1, image2;
	private String postUrl = "http://www.juqilife.cn/user/profile/identity_upload"; // 处理POST请求的页面
	private MyHttpClient uploadClient;
	private String front, backImage;
	private UserController userController;
	private AuthModel authModel;

	public boolean onKeyDown(int keyCode, KeyEvent event) {//将按键事件绑定到这个类中
		if ((keyCode == KeyEvent.KEYCODE_BACK)) {						
			Intent intent = new Intent(AuthenticationActivity.this,
					WithdrawActivity.class);
			startActivity(intent);
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_authentication);
		uploadClient = new MyHttpClient();
		userController = new UserController();
		findViewById();
		initView();
	}

	protected void findViewById() {
		image1 = (ImageView) findViewById(R.id.image1);
		image2 = (ImageView) findViewById(R.id.image2);
		authConfirm = (Button) findViewById(R.id.auth_confirm_btn);
		back = (TextView) findViewById(R.id.auth_back_to_withdraw);
		mytvreason = (TextView) findViewById(R.id.reason);
		real = (EditText) findViewById(R.id.real_name);
	}

	protected void initView() {
		image1.setOnClickListener(this);
		image2.setOnClickListener(this);
		authConfirm.setOnClickListener(this);
		back.setOnClickListener(this);
		authModel = userController.authDetail();
		if (authModel.getStatus().equals("3")) {
			mytvreason.setText("审核未通过原因：请检查照片是否清晰");
			Toast.makeText(AuthenticationActivity.this, "当前审核没有通过",
					Toast.LENGTH_SHORT).show();
		}
		
	}

	public void onClick(View v) {
		switch (v.getId()) {
		// 返回上一页
		case R.id.auth_back_to_withdraw:
			mIntent = new Intent(AuthenticationActivity.this, WithdrawActivity.class);
			startActivity(mIntent);
			finish();
			break;
		case R.id.image1:
			Intent image1Intent = new Intent(Intent.ACTION_PICK, null);
			image1Intent.setDataAndType(
					MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
			startActivityForResult(image1Intent, 1);
			break;
		case R.id.image2:
			Intent image2Intent = new Intent(Intent.ACTION_PICK, null);
			image2Intent.setDataAndType(
					MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
			startActivityForResult(image2Intent, 2);
			break;
		// 确认认证提交按钮
		case R.id.auth_confirm_btn:
			if (TextUtils.isEmpty(real.getText().toString().trim())) {
				Toast.makeText(AuthenticationActivity.this, "请输入身份证上的真实姓名",
						Toast.LENGTH_SHORT).show();
				break;			
			} else {
				front = uploadClient.uploadFile(postUrl, "image1.jpg", path
						+ "image1.jpg");
				backImage = uploadClient.uploadFile(postUrl, "image2.jpg", path
						+ "image2.jpg");
				if (!TextUtils.isEmpty(front) && !TextUtils.isEmpty(backImage)) {
					int rs = userController.UploadAuth(real.getText().toString().trim(), front, backImage);
					if (rs == Config.AuthUploadSuccess) {
						Toast.makeText(AuthenticationActivity.this,
								"提交成功，审核将在三日内完成！", Toast.LENGTH_SHORT).show();
						mIntent = new Intent(AuthenticationActivity.this,
								WithdrawActivity.class);
						startActivity(mIntent);
						finish();
						break;
					} else if (rs == -1) {
						Toast.makeText(AuthenticationActivity.this, "网络异常",
								Toast.LENGTH_LONG).show();
						break;
					} else {
						Toast.makeText(AuthenticationActivity.this, "提交失败",
								Toast.LENGTH_SHORT).show();
						break;					
					}
				} else {
					Toast.makeText(AuthenticationActivity.this, "请上传全部图片",
							Toast.LENGTH_LONG).show();
					break;
				}
			}
		default:
			break;
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case 1:
			if (resultCode == Activity.RESULT_OK) {
				cropPhoto(data.getData(), 3);// 裁剪图片
			}
			break;
		case 2:
			if (resultCode == Activity.RESULT_OK) {
				cropPhoto(data.getData(), 4);// 裁剪图片
			}
			break;
		case 3:
			if (data != null) {
				Bundle extras = data.getExtras();
				auth = extras.getParcelable("data");
				if (auth != null) {
					setPicToView(auth, "image1.jpg");// 保存在SD卡中
					image1.setImageBitmap(auth);// 用ImageView显示出来
				}
			}
			break;
		case 4:
			if (data != null) {
				Bundle extras = data.getExtras();
				auth = extras.getParcelable("data");
				if (auth != null) {
					setPicToView(auth, "image2.jpg");// 保存在SD卡中
					image2.setImageBitmap(auth);// 用ImageView显示出来
				}
			}
			break;
		default:
			break;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	/**
	 * 调用系统的裁剪
	 * 
	 * @param uri
	 */
	public void cropPhoto(Uri uri, int type) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		intent.putExtra("crop", "true");
		// aspectX aspectY 是宽高的比例
		intent.putExtra("aspectX", 16);
		intent.putExtra("aspectY", 9);
		// outputX outputY 是裁剪图片宽高
		intent.putExtra("outputX", 117);
		intent.putExtra("outputY", 66);
		intent.putExtra("return-data", true);
		startActivityForResult(intent, type);
	}

	private void setPicToView(Bitmap mBitmap, String name) {
		String sdStatus = Environment.getExternalStorageState();
		if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用
			return;
		}
		FileOutputStream b = null;
		File file = new File(path);
		file.mkdirs();// 创建文件夹
		String fileName = path + name;// 图片名字
		try {
			b = new FileOutputStream(fileName);
			mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, b);// 把数据写入文件
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			try {
				b.flush();
				b.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}