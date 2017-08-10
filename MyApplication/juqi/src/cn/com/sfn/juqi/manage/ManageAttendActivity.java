/**
 * 付费球局的参与者页面
 */
package cn.com.sfn.juqi.manage;

import java.util.List;

import cn.com.sfn.juqi.adapter.AttendItemAdapter;
import cn.com.sfn.juqi.appoint.AppointActivity;
import cn.com.sfn.juqi.controller.MatchController;
import cn.com.sfn.juqi.model.AttendModel;
import cn.com.sfn.juqi.model.MatchModel;
import cn.com.sfn.juqi.my.FriendDetailActivity;
import cn.com.sfn.juqi.sign.MatchDetailActivity;
import cn.com.sfn.juqi.util.Config;
import cn.com.sfn.juqi.util.Constants;
import cn.com.wx.util.DownLoadImage;
import cn.com.wx.util.DownLoadImage.BitmapCallBack;
import cn.com.wx.util.WXUtil;

import com.example.juqi.R;
import com.sina.weibo.sdk.api.WebpageObject;
import com.sina.weibo.sdk.api.WeiboMessage;
import com.sina.weibo.sdk.api.WeiboMultiMessage;
import com.sina.weibo.sdk.api.share.BaseResponse;
import com.sina.weibo.sdk.api.share.IWeiboHandler;
import com.sina.weibo.sdk.api.share.IWeiboShareAPI;
import com.sina.weibo.sdk.api.share.SendMessageToWeiboRequest;
import com.sina.weibo.sdk.api.share.SendMultiMessageToWeiboRequest;
import com.sina.weibo.sdk.api.share.WeiboShareSDK;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.constant.WBConstants;
import com.sina.weibo.sdk.utils.Utility;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.zxing.activity.CaptureActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ClipboardManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

@SuppressLint("HandlerLeak")
public class ManageAttendActivity extends Activity implements
		IWeiboHandler.Response {
	private TextView back, cost, attend, pay;
	private LinearLayout not;
	private ImageView more;
	private Intent mIntent;
	private ListView attendListView;
	private ListAdapter listAdapter;
	private ManagePopWin managePopWin;
	private CancelPopWin cancelPopWin;
	private ConfirmPopWin confirmPopWin;
	private String id;
	private MatchModel match = new MatchModel();
	private List<AttendModel> attends;
	private Handler myhandler;
	@SuppressWarnings("unused")
	private static final String TAG = "WBShareActivity";
	/** 微博微信分享接口实例 */
	private IWeiboShareAPI mWeiboShareAPI = null;
	// 自己微信应用的 appId
	public static final String WX_APP_ID = "wx6091081fcba9999a";
	// 自己微信应用的 appSecret
	public static final String WX_SECRET = "c9ed9adab65c9c9db3598d3f2d10b576";
	public static IWXAPI wxApi;

	private MatchController matchController;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_manage_attend);
		// 创建微博分享接口实例
		mWeiboShareAPI = WeiboShareSDK.createWeiboAPI(
				ManageAttendActivity.this, Constants.APP_KEY);
		mWeiboShareAPI.registerApp();
		if (savedInstanceState != null) {
            mWeiboShareAPI.handleWeiboResponse(getIntent(), this);
        }
		
		wxApi = WXAPIFactory.createWXAPI(this, WX_APP_ID, true);
		wxApi.registerApp(WX_APP_ID);
		matchController = new MatchController();
		mIntent = getIntent();
		id = mIntent.getStringExtra("matchId");
		findViewById();
		initDetail();
		myhandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case 1:
					init();
					break;
				case 2:
					Toast.makeText(ManageAttendActivity.this, "网络异常",
							Toast.LENGTH_SHORT).show();
					break;
				default:
					break;
				}
			}
		};
	}

	protected void findViewById() {
		attendListView = (ListView) findViewById(R.id.attendList);
		back = (TextView) findViewById(R.id.attend_back_to_manage);
		cost = (TextView) findViewById(R.id.cost_num);
		more = (ImageView) findViewById(R.id.more_option);
		attend = (TextView) findViewById(R.id.attend_num);
		not = (LinearLayout) findViewById(R.id.rl);
		pay = (TextView) findViewById(R.id.num_of_pay);
	}

	protected void initDetail() {
		new Thread() {
			public void run() {
				match = matchController.getJoinDetail(id);
				if (match == null) {
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

	protected void init() {
		more.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showOptions(v);
				/*mIntent = new Intent(ManageAttendActivity.this, CaptureActivity.class);
				startActivity(mIntent);*/
			}
		});
		cost.setText(match.getFee());
		attend.setText(match.getAttendance());
		pay.setText(match.getPayNum());
		attends = match.getAttendList();
		if (attends.size() != 0) {
			not.setVisibility(View.INVISIBLE);
		}
		listAdapter = new AttendItemAdapter(this, attends);
		attendListView.setAdapter(listAdapter);
		// 返回
		back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		// 点击每一项，进入好友详情页
		attendListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				AttendModel fm = (AttendModel) arg0.getItemAtPosition(position);
				mIntent = new Intent();
				mIntent.putExtra("id", fm.getId());
				mIntent.setClass(ManageAttendActivity.this,
						FriendDetailActivity.class);
				startActivity(mIntent);
			}
		});
		back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

	// 弹出分享窗
	public void showOptions(View view) {
		managePopWin = new ManagePopWin(this, onClickListener);
		managePopWin.showAtLocation(findViewById(R.id.options_loc),
				Gravity.BOTTOM, 0, 0);
	}

	public void showCofirmDelete(View view) {
		confirmPopWin = new ConfirmPopWin(this, onClickListener);
		confirmPopWin.showAtLocation(findViewById(R.id.options_loc),
				Gravity.CENTER, 0, 0);
	}

	public void showCancel(View view) {
		cancelPopWin = new CancelPopWin(this, onClickListener);
		cancelPopWin.showAtLocation(findViewById(R.id.options_loc),
				Gravity.CENTER, 0, 0);
	}

	private OnClickListener onClickListener = new View.OnClickListener() {
		@SuppressWarnings("deprecation")
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.wx_share:
				wxShare(0);
				break;
			case R.id.py_share:
				wxShare(1);
				break;
			case R.id.wb_share:
				if (!mWeiboShareAPI.isWeiboAppInstalled()) {
					Toast.makeText(ManageAttendActivity.this, "未安装微博客户端", Toast.LENGTH_SHORT).show();
				}
				sendMessage();
				Log.e("wbshareover", "wbshareover");//ok
				break;
			/*case R.id.change_btn:
				mIntent = new Intent();
				mIntent.putExtra("matchId", id);
				mIntent.setClass(ManageAttendActivity.this,
						ChangeMatchActivity.class);
				startActivity(mIntent);
				break;*/
			case R.id.another_btn:
				mIntent = new Intent();
				mIntent.putExtra("matchId", id);
				mIntent.setClass(ManageAttendActivity.this,
						AppointActivity.class);
				startActivity(mIntent);
				break;
			case R.id.cancel_btn:
				managePopWin.dismiss();
				showCancel(v);
				break;
			case R.id.cancel_ok:
				cancelPopWin.dismiss();
				showCofirmDelete(v);
				break;
			case R.id.delete_ok:
				int rs = matchController.deleteMatch(id);
				if (rs == Config.DeleteMatchSuccess) {
					Toast.makeText(ManageAttendActivity.this, "球局删除成功",
							Toast.LENGTH_LONG).show();
					confirmPopWin.dismiss();
					finish();
					break;
				} else if (rs == -1) {
					Toast.makeText(ManageAttendActivity.this, "网络异常",
							Toast.LENGTH_LONG).show();
					break;
				} else {
					Toast.makeText(ManageAttendActivity.this, "已有人付款，球局删除失败",
							Toast.LENGTH_LONG).show();
					confirmPopWin.dismiss();
					break;
				}
			case R.id.copy_share:
				ClipboardManager c = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
				c.setText("http://www.juqilife.cn/index/game_info1/id/" + id);
				Toast.makeText(ManageAttendActivity.this, "内容已复制到剪切板",
						Toast.LENGTH_SHORT).show();
				break;
			default:
				break;
			}
		}
	};

	/**
	 * @see {@link Activity#onNewIntent}
	 */
	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		// 从当前应用唤起微博并进行分享后，返回到当前应用时，需要在此处调用该函数
		// 来接收微博客户端返回的数据；执行成功，返回 true，并调用
		// {@link IWeiboHandler.Response#onResponse}；失败返回 false，不调用上述回调
		mWeiboShareAPI.handleWeiboResponse(intent, this);
	}

	/**
	 * 接收微客户端博请求的数据。 当微博客户端唤起当前应用并进行分享时，该方法被调用。
	 * 
	 * @param baseRequest
	 *            微博请求数据对象
	 * @see {@link IWeiboShareAPI#handleWeiboRequest}
	 */
	@Override
	public void onResponse(BaseResponse baseResp) {
		Log.e("onResponse","接收微客户端博请求的数据成功");
		if (baseResp != null) {
			switch (baseResp.errCode) {
			case WBConstants.ErrorCode.ERR_OK:
				Toast.makeText(this,
						R.string.weibosdk_demo_toast_share_success,
						Toast.LENGTH_LONG).show();
				break;
			case WBConstants.ErrorCode.ERR_CANCEL:
				Toast.makeText(this,
						R.string.weibosdk_demo_toast_share_canceled,
						Toast.LENGTH_LONG).show();
				break;
			case WBConstants.ErrorCode.ERR_FAIL:
				Toast.makeText(
						this,
						getString(R.string.weibosdk_demo_toast_share_failed)
								+ "Error Message: " + baseResp.errMsg,
						Toast.LENGTH_LONG).show();
				break;
			}
		}
	}

	/**
	 * 第三方应用发送请求消息到微博，唤起微博分享界面。
	 * 
	 * @see {@link #sendMultiMessage} 或者 {@link #sendSingleMessage}
	 */
	private void sendMessage() {
		if (mWeiboShareAPI.isWeiboAppSupportAPI()) {
			int supportApi = mWeiboShareAPI.getWeiboAppSupportAPI();
			if (supportApi >= 10351 /* ApiUtils.BUILD_INT_VER_2_2 */) {
				Log.e("sendMultiMessage","sendMultiMessageover");
				sendMultiMessage();
			} else {
				sendSingleMessage();
			}
		} else {
			Toast.makeText(this, R.string.weibosdk_demo_not_support_api_hint,
					Toast.LENGTH_SHORT).show();
		}
	}

	/**
	 * 第三方应用发送请求消息到微博，唤起微博分享界面。 注意：当
	 * {@link IWeiboShareAPI#getWeiboAppSupportAPI()} >= 10351 时，支持同时分享多条消息，
	 * 同时可以分享文本、图片以及网页。
	 */
	private void sendMultiMessage() {
		// 1. 初始化微博的分享消息
		WeiboMultiMessage weiboMessage = new WeiboMultiMessage();
		weiboMessage.mediaObject = getWebpageObj();
		Log.e("getWebpageObj","getWebpageObj over");
		// 2. 初始化从第三方到微博的消息请求
		SendMultiMessageToWeiboRequest request = new SendMultiMessageToWeiboRequest();
		// 用transaction唯一标识一个请求
		request.transaction = String.valueOf(System.currentTimeMillis());
		request.multiMessage = weiboMessage;
		// 3. 发送请求消息到微博，唤起微博分享界面
		mWeiboShareAPI.sendRequest(ManageAttendActivity.this, request);
		Log.e("smWeiboShareAPI.sendRequest","mWeiboShareAPI.sendRequest");
	}

	/**
	 * 第三方应用发送请求消息到微博，唤起微博分享界面。 当{@link IWeiboShareAPI#getWeiboAppSupportAPI()}
	 * < 10351 时，只支持分享单条消息，即 文本、图片、网页
	 */
	private void sendSingleMessage() {
		// 1. 初始化微博的分享消息
		// 用户可以分享文本、图片、网页、音乐、视频中的一种
		WeiboMessage weiboMessage = new WeiboMessage();
		weiboMessage.mediaObject = getWebpageObj();
		// 2. 初始化从第三方到微博的消息请求
		SendMessageToWeiboRequest request = new SendMessageToWeiboRequest();
		// 用transaction唯一标识一个请求
		request.transaction = String.valueOf(System.currentTimeMillis());
		request.message = weiboMessage;
		// 3. 发送请求消息到微博，唤起微博分享界面
		mWeiboShareAPI.sendRequest(ManageAttendActivity.this, request);
	}

	/**
	 * 创建多媒体（网页）消息对象。
	 * 
	 * @return 多媒体（网页）消息对象。
	 */

	private WebpageObject getWebpageObj() {
		WebpageObject mediaObject = new WebpageObject();
		mediaObject.identify = Utility.generateGUID();
		mediaObject.title = "局气";
		mediaObject.description = match.getTitle();
		Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
				R.drawable.share_test);
		// 设置 Bitmap 类型的图片到视频对象里 设置缩略图。 注意：最终压缩过的缩略图大小不得超过 32kb。
		mediaObject.setThumbImage(bitmap);
		mediaObject.actionUrl = "http://www.juqilife.cn/index/game_info1/id/"+ id;
		mediaObject.defaultText = "快来加入";
		return mediaObject;
	}
	private void wxShare(final int shareWhat) {
		checkWX(); 
		final SendMessageToWX.Req req = new SendMessageToWX.Req();
		WXWebpageObject webpage = new WXWebpageObject();
		// 要跳转的地址
		webpage.webpageUrl = "http://www.juqilife.cn/index/game_info1/id/" + id;
		final WXMediaMessage msg = new WXMediaMessage(webpage);
		msg.title = "分享球局";
		msg.description = match.getTitle();
		String imageUrl = "";
		// 0:发送到朋友 1:发送到朋友圈 2:收藏
		if (imageUrl.length() == 0) {
			// 分享的图片不能超过32k 否则弹不出微信分享框
			Bitmap bmp = BitmapFactory.decodeResource(getResources(),
					R.drawable.share_test);
			msg.thumbData = WXUtil.bmpToByteArray(bmp, true);
			req.transaction = buildTransaction("webpage");
			req.message = msg;
			req.scene = shareWhat;
			wxApi.sendReq(req);
		} else {
			// 主线程不能访问网络，开启线程下载图片
			new DownLoadImage(imageUrl).loadBitmap(new BitmapCallBack() {
				@Override
				public void getBitmap(Bitmap bitmap) {
					// 分享的图片不能超过32k 压缩图片
					Bitmap thumbBmp = Bitmap.createScaledBitmap(bitmap, 100,
							100, true);
					bitmap.recycle();
					msg.thumbData = WXUtil.bmpToByteArray(thumbBmp, true);
					req.transaction = buildTransaction("webpage");
					req.message = msg;
					req.scene = shareWhat;
					wxApi.sendReq(req);
				}
			});
		}
	}

	private String buildTransaction(final String type) {
		return (type == null) ? String.valueOf(System.currentTimeMillis())
				: type + System.currentTimeMillis();
	}
	// 检查微信是否安装
	private boolean checkWX() {
		boolean IsWXAppInstalledAndSupported = wxApi.isWXAppInstalled()
                && wxApi.isWXAppSupportAPI();
		if(IsWXAppInstalledAndSupported==false){
			Toast.makeText(this, "微信未安装", Toast.LENGTH_LONG).show();
		}
        return IsWXAppInstalledAndSupported;	
	}

}