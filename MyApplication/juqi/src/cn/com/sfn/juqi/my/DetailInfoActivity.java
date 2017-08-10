package cn.com.sfn.juqi.my;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import cn.com.sfn.example.juqi.MainActivity;
import cn.com.sfn.juqi.controller.UserController;
import cn.com.sfn.juqi.model.UserModel;
import cn.com.sfn.juqi.net.MyHttpClient;
import cn.com.sfn.juqi.util.Config;
import cn.com.sfn.juqi.widgets.ArrayWheelAdapter;
import cn.com.sfn.juqi.widgets.CircleImageView;
import cn.com.sfn.juqi.widgets.NumberWheelAdaper;
import cn.com.sfn.juqi.widgets.WheelView;

import com.example.juqi.R;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.PopupWindow.OnDismissListener;

@SuppressLint("HandlerLeak")
public class DetailInfoActivity extends Activity implements OnClickListener {
	@SuppressLint("SdCardPath")
	private static String path = "/sdcard/myHead/";
	private MyHttpClient uploadClient;
	private Bitmap head;
	// 在页面上添加其他layout
	private LayoutInflater inflater = null;
	private TextView back;
	private CircleImageView avatar;
	private TextView sex, offense, defense, zonghe, shizhan, weizhi;
	private RelativeLayout sexBtn, offBtn, defBtn, zonBtn, roleBtn, levBtn,
			avatar_btn;
	private EditText nickName, age, gameAge, height, weight, signature, zhanji;
	private UserController userController;
	private Handler myhandler;
	private UserModel userModel = new UserModel();
	// 滚动view呈现在PopupWindow中
	private PopupWindow menuWindow;
	// 滚动view的对象
	private WheelView wArray, wPercent;
	private String sexFormat[] = { "男", "女" };
	private String role[] = { "SF", "PF", "C", "PG", "SG" };
	private String level[] = { "初级菜鸟", "一般水平", "业余高手" };
	private Button confirm;
	private String sexStr;
	private Intent mIntent;
	private String postUrl = "http://www.juqilife.cn/user/profile/avatar_upload"; // 处理POST请求的页面

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_detail_info);
		userController = new UserController();
		findViewById();
		initView();
		initData();
		myhandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case 1:
					initShow();
					break;
				case 2:
					Toast.makeText(DetailInfoActivity.this, "未登录",
							Toast.LENGTH_SHORT).show();
					break;
				default:
					break;
				}
			}
		};
	}

	private void initShow() {
		if (userModel.getNickName().equals("")) {
			nickName.setHint(R.string.hint11);
		} else {
			nickName.setText(userModel.getNickName());
		}
		if (userModel.getUserSex().equals("1")) {
			sex.setText("男");
		} else {
			sex.setText("女");
		}
		age.setText(userModel.getAge());
		@SuppressWarnings("deprecation")
		Drawable drawable = new BitmapDrawable(userModel.getUserAvatar());// 转换成drawable
		avatar.setImageDrawable(drawable);
		gameAge.setText(userModel.getUage());
		height.setText(userModel.getHeight());
		weight.setText(userModel.getWeight());
		offense.setText(userModel.getOffense());
		defense.setText(userModel.getDefense());
		zonghe.setText(userModel.getComprehensive());
		shizhan.setText(userModel.getStandard());
		weizhi.setText(userModel.getPosition());
		if (userModel.getSignature().equals("")) {
			signature.setHint(R.string.gexing_hint);
		} else {
			signature.setText(userModel.getSignature());
		}
		if (userModel.getGrade().equals("")) {
			zhanji.setHint(R.string.zhanji_hint);
		} else {
			zhanji.setText(userModel.getGrade());
		}
	}

	protected void findViewById() {
		back = (TextView) findViewById(R.id.back_to_my);
		sexBtn = (RelativeLayout) findViewById(R.id.sex_btn);
		sex = (TextView) findViewById(R.id.sex);
		offBtn = (RelativeLayout) findViewById(R.id.offense_btn);
		offense = (TextView) findViewById(R.id.detail_offense);
		defBtn = (RelativeLayout) findViewById(R.id.defense_btn);
		defense = (TextView) findViewById(R.id.detail_defense);
		zonBtn = (RelativeLayout) findViewById(R.id.zonghe_btn);
		zonghe = (TextView) findViewById(R.id.detail_zonghe);
		roleBtn = (RelativeLayout) findViewById(R.id.role_btn);
		weizhi = (TextView) findViewById(R.id.detail_role);
		levBtn = (RelativeLayout) findViewById(R.id.level_btn);
		shizhan = (TextView) findViewById(R.id.detail_level);
		avatar = (CircleImageView) findViewById(R.id.info_avatar);
		avatar_btn = (RelativeLayout) findViewById(R.id.avatar_btn);
		nickName = (EditText) findViewById(R.id.name_detail);
		age = (EditText) findViewById(R.id.age_detail);
		gameAge = (EditText) findViewById(R.id.game_age_detail);
		height = (EditText) findViewById(R.id.height_detail);
		weight = (EditText) findViewById(R.id.weight_detail);
		signature = (EditText) findViewById(R.id.gexing_detail);
		zhanji = (EditText) findViewById(R.id.zhanji_detail);
		confirm = (Button) findViewById(R.id.info_confirm_btn);
		inflater = (LayoutInflater) this
				.getSystemService(LAYOUT_INFLATER_SERVICE);
	}

	protected void initView() {
		back.setOnClickListener(this);
		sexBtn.setOnClickListener(this);
		offBtn.setOnClickListener(this);
		defBtn.setOnClickListener(this);
		zonBtn.setOnClickListener(this);
		roleBtn.setOnClickListener(this);
		levBtn.setOnClickListener(this);
		avatar_btn.setOnClickListener(this);
		confirm.setOnClickListener(this);
	}

	protected void initData() {
		new Thread() {
			public void run() {
				userModel = userController.getInfo(Config.login_userid);
				if (userModel == null) {
					Message msg = new Message();
					msg.what = 2;
					myhandler.sendMessage(msg);
				} else {
					Message msg = new Message();
					msg.what = 1;
					myhandler.sendMessage(msg);
				}
			}
		}.start();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back_to_my:
			finish();
			break;
		case R.id.sex_btn:
			hideInput(v);
			showPopwindow(getArrayPick(sex, sexFormat));
			break;
		case R.id.offense_btn:
			hideInput(v);
			showPopwindow(getPercentPick(offense));
			break;
		case R.id.defense_btn:
			hideInput(v);
			showPopwindow(getPercentPick(defense));
			break;
		case R.id.zonghe_btn:
			hideInput(v);
			showPopwindow(getPercentPick(zonghe));
			break;
		case R.id.level_btn:
			hideInput(v);
			showPopwindow(getArrayPick(shizhan, level));
			break;
		case R.id.role_btn:
			hideInput(v);
			showPopwindow(getArrayPick(weizhi, role));
			break;
		case R.id.avatar_btn:
			Intent avaIntent = new Intent(Intent.ACTION_PICK, null);
			avaIntent.setDataAndType(
					MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
			startActivityForResult(avaIntent, 1);
			break;
		case R.id.info_confirm_btn:
			if (sex.getText().toString().equals("男")) {
				sexStr = "1";
			} else {
				sexStr = "2";
			}
			int rs = userController
					.editInfo(nickName.getText().toString(), sexStr, age
							.getText().toString(),
							gameAge.getText().toString(), height.getText()
									.toString(), weight.getText().toString(),
							offense.getText().toString(), defense.getText()
									.toString(), zonghe.getText().toString(),
							shizhan.getText().toString(), weizhi.getText()
									.toString(),
							signature.getText().toString(), zhanji.getText()
									.toString());
			if (rs == Config.EditSuccess) {
				Toast.makeText(DetailInfoActivity.this, "修改成功",
						Toast.LENGTH_LONG).show();
				mIntent = new Intent();
				mIntent.putExtra("flag", 5);
				mIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				mIntent.setClass(DetailInfoActivity.this, MainActivity.class);
				startActivity(mIntent);
				finish();
				finish();
				break;
			} else if (rs == -1) {
				Toast.makeText(DetailInfoActivity.this, "网络异常",
						Toast.LENGTH_LONG).show();
				break;
			} else {
				Toast.makeText(DetailInfoActivity.this, "网络异常",
						Toast.LENGTH_LONG).show();
				break;
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
		case 3:
			if (data != null) {
				Bundle extras = data.getExtras();
				head = extras.getParcelable("data");
				if (head != null) {
					setPicToView(head);// 保存在SD卡中
					avatar.setImageBitmap(head);// 用ImageView显示出来
					/**
					 * 上传服务器代码
					 */
					uploadClient = new MyHttpClient();
					String sstr = uploadClient.uploadFile(postUrl, "head.jpg", path
							+ "head.jpg");
					if (!sstr.equals(""))
						Log.i("upload", "success");
					else
						Log.i("upload", "fail");
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
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		// outputX outputY 是裁剪图片宽高
		intent.putExtra("outputX", 150);
		intent.putExtra("outputY", 150);
		intent.putExtra("return-data", true);
		startActivityForResult(intent, type);
	}

	private void setPicToView(Bitmap mBitmap) {
		String sdStatus = Environment.getExternalStorageState();
		if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用
			return;
		}
		FileOutputStream b = null;
		File file = new File(path);
		file.mkdirs();// 创建文件夹
		String fileName = path + "head.jpg";// 图片名字
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

	/**
	 * 隐藏输入法
	 */
	private void hideInput(View v) {
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
	}

	/**
	 * 初始化popupWindow
	 * 
	 * @param view
	 */
	@SuppressWarnings("deprecation")
	private void showPopwindow(View view) {
		menuWindow = new PopupWindow(view, LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT);
		menuWindow.setFocusable(true);
		menuWindow.setBackgroundDrawable(new BitmapDrawable());
		menuWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);
		menuWindow.setOnDismissListener(new OnDismissListener() {
			@Override
			public void onDismiss() {
				menuWindow = null;
			}
		});
	}

	private View getPercentPick(final TextView tv) {
		final View view = inflater.inflate(R.layout.duringpick, null);
		wPercent = (WheelView) view.findViewById(R.id.during);
		wPercent.setAdapter(new NumberWheelAdaper(0, 100));
		wPercent.setLabel("%");
		wPercent.setCyclic(true);
		Button bt = (Button) view.findViewById(R.id.d_set);
		bt.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				int str = wPercent.getCurrentItem() * 10;
				tv.setText(str + "");
				menuWindow.dismiss();
			}
		});
		Button cancel = (Button) view.findViewById(R.id.d_cancel);
		cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				menuWindow.dismiss();
			}
		});
		return view;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private View getArrayPick(final TextView tv, String format[]) {
		final View view = inflater.inflate(R.layout.formatpick, null);
		wArray = (WheelView) view.findViewById(R.id.format_choose);
		wArray.setAdapter(new ArrayWheelAdapter(format));
		Button bt = (Button) view.findViewById(R.id.f_set);
		bt.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				int id = wArray.getCurrentItem();
				String f = wArray.getAdapter().getItem(id);
				tv.setText(f);
				menuWindow.dismiss();
			}
		});
		Button cancel = (Button) view.findViewById(R.id.f_cancel);
		cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				menuWindow.dismiss();
			}
		});
		return view;
	}
}