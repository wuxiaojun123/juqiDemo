package cn.com.sfn.juqi.sign;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import cn.com.sfn.alipay.util.PayResult;
import cn.com.sfn.alipay.util.SignUtils;
import cn.com.sfn.example.juqi.LoginActivity;
import cn.com.sfn.example.juqi.MainActivity;
import cn.com.sfn.juqi.adapter.CommentsItemAdapter;
import cn.com.sfn.juqi.adapter.JoinItemAdapter;
import cn.com.sfn.juqi.adapter.ListItemClickHelp;
import cn.com.sfn.juqi.controller.MatchController;
import cn.com.sfn.juqi.controller.UserController;
import cn.com.sfn.juqi.model.AttendModel;
import cn.com.sfn.juqi.model.CommentsModel;
import cn.com.sfn.juqi.model.MatchModel;
import cn.com.sfn.juqi.model.UserModel;
import cn.com.sfn.juqi.my.FriendDetailActivity;
import cn.com.sfn.juqi.util.Config;
import cn.com.sfn.juqi.util.Constants;
import cn.com.sfn.juqi.widgets.CircleImageView;
import cn.com.sfn.juqi.widgets.InnerListView;
import cn.com.sfn.juqi.widgets.XListView.IXListViewListener;
import cn.com.wx.util.DownLoadImage;
import cn.com.wx.util.WXUtil;
import cn.com.wx.util.DownLoadImage.BitmapCallBack;
import com.alipay.sdk.app.PayTask;
import com.amap.map3d.demo.route.RouteActivity;
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

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipboardManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class MatchDetailActivity extends Activity implements OnClickListener,
		IWeiboHandler.Response{
	// 支付宝
	// 商户PID
	public static final String PARTNER = "2088801141057672";
	// 商户收款账号
	public static final String SELLER = "bjyhhc@163.com";
	// 商户私钥，pkcs8格式
	public static final String RSA_PRIVATE = "MIICeAIBADANBgkqhkiG9w0BAQEFAASCAmIwggJeAgEAAoGBAMwYQvQKNmg7kSK+MyvIh4rrvR3vuFRHYskxDLZ8zXvqVqSnC/1MJTTgPBwEjzVMeLZts4bsh9YqdJFiIaCs7GeesPv7ECElqiWQ5Bj8/MHKqQ5fxWElvgBE5ldYyzeBh59+cXTqBZt3phR/yfRKwWot/gi/oH5vWucnxuvzCPMVAgMBAAECgYEAkM9WEhl86Q/6tpAFdrddlLAXCzqfqwzLhr0KrCB5G8b1mvX7h8mTTKUdgTOq+MI9lBLaIo2JA+gztUPPYFXRlZ+Rg0OPs2BqJ0L0MQmdnU8Bz1WpKpxLUKb0aNkJdy7/1TSIud8jd6aGZxvK7UxTOdQiutQMbOh9qTXk5qeEyAECQQDpAUA8wj/WoGEivYtqb7cgTd+4euNCEqiGz8+TTIbe7RRfkJ2g+vC8cSb8ZZO8zhz1QObQPnS3NvPKBSwU4HKVAkEA4Dycgs7MwxVDqhidCyuO8fmXkRzRUh5bGW1oAixk93rgGX6RWIPyLhqJer+bM65ttPWDaLQbWn2oKk2PncregQJBALL0+gBwdRWEAnbrO4vZF75g8UZAZBFYQUWhkF0itqe7UR2A9gAxG/qvsXDDF3A4ofcgDOa+QWiCdUWhKVUzee0CQDcPKu8DkEumgQLXIFiJzYOk4Y6EIPGk+oF3174Q4InT5grchRvS6jhf07oMKjO6dL2mOyoOb1j82bnpaYY5NgECQQDA+UBRXcNrmb08ozEm+RLIQFwB8JhyQ6K6a3RMjX84tR42nBvClGv0aIMLLqzjNibJQdJvtdxDvqkh9suEgWLG";
	// 支付宝公钥
	// public static final String RSA_PUBLIC =
	// "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCnxj/9qwVfgoUh/y2W89L6BkRAFljhNhgPdyPuBV64bfQNN1PjbCzkIM6qRdKBoLPXmKKMiFYnkd6rAoprih3/PrQEB/VsW8OoM8fxn67UDYuyBTqA23MML9q1+ilIZwBC2AQ2UBVOrFXfFl75p6/B5KsiNG9zpgmLCUYuLkxpLQIDAQAB";
	private static final int SDK_PAY_FLAG = 1;
	
	/*@SuppressLint("HandlerLeak")
	private Handler aliHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case SDK_PAY_FLAG: {
				PayResult payResult = new PayResult((String) msg.obj);
				*//**
				 * 同步返回的结果必须放置到服务端进行验证（验证的规则请看https://doc.open.alipay.com/doc2/
				 * detail.htm?spm=0.0.0.0.xdvAU6&treeId=59&articleId=103665&
				 * docType=1) 建议商户依赖异步通知
				 *//*
				String resultInfo = payResult.getResult();// 同步返回需要验证的信息
				Log.e("resultInfo", resultInfo);
				String resultStatus = payResult.getResultStatus();
				// 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
				if (TextUtils.equals(resultStatus, "9000")) {				
					Toast.makeText(MatchDetailActivity.this, "支付成功",
							Toast.LENGTH_SHORT).show();
					attendText.setText("退出球局");
					attendImg.setImageDrawable(getResources().getDrawable(
							R.drawable.tuichuqiuju));
					attendButton.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View arg0) {
							showQuit(arg0);
						}
					});
					
				} else {
					// 判断resultStatus 为非"9000"则代表可能支付失败
					// "8000"代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
					if (TextUtils.equals(resultStatus, "8000")) {
						Toast.makeText(MatchDetailActivity.this, "支付结果确认中",
								Toast.LENGTH_SHORT).show();
					} else {
						// 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
						Toast.makeText(MatchDetailActivity.this, "支付失败",
								Toast.LENGTH_SHORT).show();
					}
				}
				break;
			}
			default:
				break;
			}
		};
	};*/
			

	/** 微博、微信分享接口实例 */
	private IWeiboShareAPI WeiboShareAPI = null;
	private AuthInfo mWeiboAuth;
	// 自己微信应用的 appId
	public static final String WX_APP_ID = "wx6091081fcba9999a";
	// 自己微信应用的 appSecret
	public static final String WX_SECRET = "c9ed9adab65c9c9db3598d3f2d10b576";
	public static IWXAPI wxApi;
	private Intent mIntent;
	private ScrollView scrollView;
	private List<AttendModel> attends;
	private LinearLayout addressBtn;
	private ImageView shareButton;
	private List<CommentsModel> comments;
	private CircleImageView avatar;
	private ProgressBar fabu, canyu;
	private TextView matchTitle, matchGym, matchPlace, GymFormat, matchNum,
			fee, matchType,matchDetail;
	private TextView matchDuration, hostNumber, back,
			attendlistHint, commentlistHint, zjlev, cjlev, uage;
	private TextView hName;
	private LinearLayout attendButton, commentButton;
	private ListAdapter listAdapter, cListAdapter;
	private InnerListView attendListView, commentsListView;
	private double longitude, latitude;
	private Handler myhandler;
	private MatchController matchController;
	private MatchModel match = new MatchModel();
	private String id, table,resultStatus;
	private TextView attendText;
	private ImageView attendImg;
	private QuitPopWin quitPop;
	private String imageurl="http://www.juqilife.cn/index/game_info1/id/";
	private UserController userController=new UserController();
	private UserModel userModel = new UserModel();

	@SuppressLint("HandlerLeak")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_match_detail);
		// 创建微博分享接口实例
		WeiboShareAPI = WeiboShareSDK.createWeiboAPI(MatchDetailActivity.this,
				Constants.APP_KEY);
		WeiboShareAPI.registerApp();
		mWeiboAuth = new AuthInfo(this, Constants.APP_KEY,
				Constants.REDIRECT_URL, Constants.SCOPE);
		WeiboShareAPI.handleWeiboResponse(getIntent(), this);  
		
		//创建微信分享接口实例
		wxApi = WXAPIFactory.createWXAPI(this, WX_APP_ID, true);
		wxApi.registerApp(WX_APP_ID);
		
		//checkWX();
		matchController = new MatchController();
		mIntent = getIntent();
		id = mIntent.getStringExtra("matchId");//球赛的id
		if (TextUtils.isEmpty(id)) {
			Intent i_getvalue = getIntent();
			String action = i_getvalue.getAction();

			if (Intent.ACTION_VIEW.equals(action)) {
				Uri uri = i_getvalue.getData();
				if (uri != null) {
					id = uri.getQueryParameter("id");
				}
			}
		}
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
					Toast.makeText(MatchDetailActivity.this, "网络异常",
							Toast.LENGTH_SHORT).show();
					break;
				default:
					break;
				}
			}
		};
	}

	public ListItemClickHelp lich = new ListItemClickHelp() {
		@Override
		public void onClick(View item, View widget, int position, int which) {
			CommentsModel cm = (CommentsModel) comments.get(position);
			switch (which) {
			// 球局验票按钮，点击进入扫描二维码
			case R.id.comments_content:
				if (Config.is_login) {
					showComment(item, "0", id,
							"回复：@" + cm.getUserName() + ": ", table,
							cm.getUserId());
				} else {
					mIntent = new Intent(MatchDetailActivity.this,
							LoginActivity.class);
					startActivity(mIntent);
					finish();
				}
				break;
			case R.id.comments_avatar:
				if (Config.is_login) {
					mIntent = new Intent();
					mIntent.putExtra("id", cm.getUserId());
					mIntent.setClass(MatchDetailActivity.this,
							FriendDetailActivity.class);
					startActivity(mIntent);
					finish();
				} else {
					mIntent = new Intent(MatchDetailActivity.this,
							LoginActivity.class);
					startActivity(mIntent);
					finish();
				}
				break;
			default:
				break;
			}
		}
	};

	protected void init() {
		back.setOnClickListener(this);
		addressBtn.setOnClickListener(this);
		shareButton.setOnClickListener(this);
		commentButton.setOnClickListener(this);
		// 对将要展示的列表项进行初始化
		attends = match.getAttendList();
		if (attends.size() != 0) {
			attendlistHint.setVisibility(View.INVISIBLE);
		}
		listAdapter = new JoinItemAdapter(this, attends);
		attendListView.setAdapter(listAdapter);
		attendListView.setParentScrollView(scrollView);
		attendListView.setMaxHeight(600);
		attendListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				AttendModel fm = (AttendModel) arg0.getItemAtPosition(position);
				mIntent.putExtra("id", fm.getId());
				mIntent.setClass(MatchDetailActivity.this,
						FriendDetailActivity.class);
				startActivity(mIntent);
			}
		});
		if (comments.size() != 0) {
			commentlistHint.setVisibility(View.INVISIBLE);
		}
		cListAdapter = new CommentsItemAdapter(this, comments, lich);
		commentsListView.setAdapter(cListAdapter);
		commentsListView.setParentScrollView(scrollView);
		commentsListView.setMaxHeight(400);

		hName.setText(match.getU_name());
		uage.setText("球龄：" + match.getuAge() + "年");
		fabu.setMax(match.getNextRelNum() - match.getNowRelNum());
		fabu.setProgress(match.getRelEntire() - match.getNowRelNum());
		canyu.setMax(match.getNextJoinNum() - match.getNowJoinNum());
		canyu.setProgress(match.getJoinEntire() - match.getNowJoinNum());
		zjlev.setText("V" + match.getRelLevel());
		cjlev.setText("V" + match.getJoinLevel());
		@SuppressWarnings("deprecation")
		Drawable drawable = new BitmapDrawable(match.getuImg());// 转换成drawable
		avatar.setImageDrawable(drawable);
		avatar.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (Config.is_login) {
					mIntent = new Intent();
					mIntent.putExtra("id", match.getU_id());
					mIntent.setClass(MatchDetailActivity.this,
							FriendDetailActivity.class);
					startActivity(mIntent);
					//finish();
				} else {
					mIntent = new Intent(MatchDetailActivity.this,
							LoginActivity.class);
					startActivity(mIntent);
					finish();
				}
			}
		});
		fee.setText(match.getFee());		
		matchTitle.setText(match.getTitle());
		matchGym.setText(match.getS_name());
		matchPlace.setText(match.getLocation());
		if (match.getSpec().equals("1")) {
			GymFormat.setText("半场");
		} else {
			GymFormat.setText("全场");
		}
		if (match.getType().equals("0")) {
			matchType.setText("公开球局");
		} else {
			matchType.setText("私密球局");
		}
		int shownum = Integer.valueOf(match.getNum())
				- Integer.valueOf(match.getAttendance());
		matchNum.setText("共" + match.getNum() + "人,还差" + shownum + "人");
		matchDuration
				.setText(match.getStart_time().substring(0, 10)
						+ " "
						+ getWeek(match.getStart_time().substring(0, 10))
						+ " "
						+ match.getStart_time().substring(11, 16)
						+ "-"
						+ addDateMinut(match.getStart_time(),
								Integer.valueOf(match.getDuration()))
								.substring(11, 16));

		matchDetail.setMovementMethod(ScrollingMovementMethod.getInstance());
		matchDetail.setText(match.getDetail());

	
		longitude = Double.valueOf(match.getLongitude());
		latitude = Double.valueOf(match.getLatitude());
		scrollView.smoothScrollTo(0, 20);
		if (!match.getFee().equals("0")||match.getFee().equals("0")) {  //跟钱没关系，可去掉
			/*
			 * userMatch:0-可参与 1-需支付 2-可退出 3-满员 4-已结束 5-正在进行
			 */
			Log.e("match.getUserAndmatch()", match.getUserAndmatch());
			if (match.getUserAndmatch().equals("0")) {
				hostNumber.setText(match.getU_mobile().substring(0, 3) + "****"
						+ match.getU_mobile().substring(7, 11));
				attendText.setText("我要报名");
				attendImg.setImageDrawable(getResources().getDrawable(
						R.drawable.attend));
				attendButton.setOnClickListener(this);
			} else if (match.getUserAndmatch().equals("1")) {
				hostNumber.setText(match.getU_mobile().substring(0, 3) + "****"
						+ match.getU_mobile().substring(7, 11));
				attendText.setText("去支付");
				attendImg.setImageDrawable(getResources().getDrawable(
						R.drawable.gotopay));
				
				
				/*去支付！！！！！！*/
				
				attendButton.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						/*String rs = matchController.paygame(id, "AliPay");
						if (rs.equals("")) {
							Toast.makeText(MatchDetailActivity.this, "支付出错",
									Toast.LENGTH_SHORT).show();
						} else {
							pay(rs);
						}*/
						mIntent = new Intent();			
						mIntent.putExtra("matchid", id);
						mIntent.putExtra("matchfee", fee.getText().toString());
						mIntent.setClass(MatchDetailActivity.this,  ChoosePaymentActivity.class);
						startActivity(mIntent);
						finish();
					}
				});
			} else if (match.getUserAndmatch().equals("2")) {
				hostNumber.setText(match.getU_mobile());
				attendText.setText("退出球局");
				attendImg.setImageDrawable(getResources().getDrawable(
						R.drawable.tuichuqiuju));
				attendButton.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						showQuit(arg0);
					}
				});
				Log.e("match.getUserAndmatch()", match.getUserAndmatch());
			} else if (match.getUserAndmatch().equals("3")) {
				Log.e("match.getUserAndmatch", match.getUserAndmatch());
				hostNumber.setText(match.getU_mobile().substring(0, 3) + "****"
						+ match.getU_mobile().substring(7, 11));
				attendText.setText("人员已满");
				attendText.setTextColor(getResources()
						.getColor(R.color.halftxt));
				attendImg.setImageDrawable(getResources().getDrawable(
						R.drawable.manyuan));
				attendButton.setBackgroundColor(getResources().getColor(
						R.color.halfbtn));
				attendButton.setClickable(false);
			} else if (match.getUserAndmatch().equals("4")) {
				Log.e("match.getUserAndmatch", match.getUserAndmatch());
				hostNumber.setText(match.getU_mobile().substring(0, 3) + "****"
						+ match.getU_mobile().substring(7, 11));
				attendText.setText("已结束");
				attendText.setTextColor(getResources()
						.getColor(R.color.halftxt));
				attendImg.setImageDrawable(getResources().getDrawable(
						R.drawable.jieshu));
				attendButton.setBackgroundColor(getResources().getColor(
						R.color.halfbtn));
				attendButton.setClickable(false);
			} else if (match.getUserAndmatch().equals("5")) {
				hostNumber.setText(match.getU_mobile().substring(0, 3) + "****"
						+ match.getU_mobile().substring(7, 11));
				attendText.setText("进行中");
				attendText.setTextColor(getResources()
						.getColor(R.color.halftxt));
				attendImg.setImageDrawable(getResources().getDrawable(
						R.drawable.jinxingzhong));
				attendButton.setBackgroundColor(getResources().getColor(
						R.color.halfbtn));
				attendButton.setClickable(false);
			} 
			
			else {
				/*
				 * userMatch:0-可参与 1-需支付 2-可退出 3-满员 4-已结束 5-正在进行
				 */
				if (match.getUserAndmatch().equals("0")) {
					hostNumber.setText(match.getU_mobile().substring(0, 3)
							+ "****" + match.getU_mobile().substring(7, 11));
					attendText.setText("我要报名");
					attendImg.setImageDrawable(getResources().getDrawable(
							R.drawable.attend));
					attendButton.setOnClickListener(this);
				} else if (match.getUserAndmatch().equals("2")) {
					hostNumber.setText(match.getU_mobile());
					attendText.setText("退出球局");
					attendImg.setImageDrawable(getResources().getDrawable(
							R.drawable.tuichuqiuju));
					attendButton.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View arg0) {
							showQuit(arg0);
						}
					});
				} else if (match.getUserAndmatch().equals("3")) {
					hostNumber.setText(match.getU_mobile().substring(0, 3)
							+ "****" + match.getU_mobile().substring(7, 11));
					attendText.setText("人员已满");
					attendText.setTextColor(getResources().getColor(
							R.color.halftxt));
					attendImg.setImageDrawable(getResources().getDrawable(
							R.drawable.manyuan));
					attendButton.setBackgroundColor(getResources().getColor(
							R.color.halfbtn));
					attendButton.setClickable(false);
				} else if (match.getUserAndmatch().equals("4")) {
					hostNumber.setText(match.getU_mobile().substring(0, 3)
							+ "****" + match.getU_mobile().substring(7, 11));
					attendText.setText("已结束");
					attendText.setTextColor(getResources().getColor(
							R.color.halftxt));
					attendImg.setImageDrawable(getResources().getDrawable(
							R.drawable.jieshu));
					attendButton.setBackgroundColor(getResources().getColor(
							R.color.halfbtn));
					attendButton.setClickable(false);
				} else if (match.getUserAndmatch().equals("5")) {
					hostNumber.setText(match.getU_mobile().substring(0, 3)
							+ "****" + match.getU_mobile().substring(7, 11));
					attendText.setText("进行中");
					attendText.setTextColor(getResources().getColor(
							R.color.halftxt));
					attendImg.setImageDrawable(getResources().getDrawable(
							R.drawable.jinxingzhong));
					attendButton.setBackgroundColor(getResources().getColor(
							R.color.halfbtn));
					attendButton.setClickable(false);
				}
			}

		}

	}

	protected void initDetail() {
		new Thread() {
			public void run() {
				match = matchController.getinfo(id);
				if (match == null) {
					Message msg = new Message();
					msg.what = 2;
					myhandler.sendMessage(msg);
				} else {
					comments = matchController.getComment(id);
					table = matchController.getTable(id);
					Message msg = new Message();
					msg.what = 1;
					myhandler.sendMessage(msg);
				}
			}
		}.start();
	}

	protected void findViewById() {
		attendButton = (LinearLayout) findViewById(R.id.attend_btn);
		matchTitle = (TextView) findViewById(R.id.match_title);
		matchGym = (TextView) findViewById(R.id.gym_name);
		GymFormat = (TextView) findViewById(R.id.gym_format);
		matchType = (TextView) findViewById(R.id.type_detail);
		matchNum = (TextView) findViewById(R.id.match_number);
		matchDuration = (TextView) findViewById(R.id.match_time);
		hostNumber = (TextView) findViewById(R.id.tele_number);
		
		matchDetail = (TextView) findViewById(R.id.match_detail);
		
		shareButton = (ImageView) findViewById(R.id.share_btn);
		commentButton = (LinearLayout) findViewById(R.id.comment_btn);
		matchPlace = (TextView) findViewById(R.id.address_detail);
		back = (TextView) findViewById(R.id.match_detail_to_sign);
		fee = (TextView) findViewById(R.id.fee_detail);
		addressBtn = (LinearLayout) findViewById(R.id.address_btn);
		scrollView = (ScrollView) findViewById(R.id.layout_personal);
		attendlistHint = (TextView) findViewById(R.id.attendList_hint);
		commentlistHint = (TextView) findViewById(R.id.commentList_hint);
		attendListView = (InnerListView) findViewById(R.id.attendsList);
		commentsListView = (InnerListView) findViewById(R.id.commentsList);
		fabu = (ProgressBar) findViewById(R.id.fabu_bar);
		canyu = (ProgressBar) findViewById(R.id.canyu_bar);
		hName = (TextView) findViewById(R.id.host_name);
		zjlev = (TextView) findViewById(R.id.zj_level);
		cjlev = (TextView) findViewById(R.id.cj_level);
		avatar = (CircleImageView) findViewById(R.id.match_avatar);
		uage = (TextView) findViewById(R.id.ball_age);
		attendText = (TextView) findViewById(R.id.attend_txt);
		attendImg = (ImageView) findViewById(R.id.attend_img);
	}

	// 弹出分享窗
	public void showShare(View view) {
		SharePopWin sharePopWin = new SharePopWin(this, onClickListener);
		sharePopWin.showAtLocation(findViewById(R.id.layout_personal),
				Gravity.CENTER, 0, 0);
	}

	// 弹出报名球局窗
	public void showAttend(View view, String id, String fee) {
		AttendPopWin attendPopWin = new AttendPopWin(this, id, fee);
		attendPopWin.showAtLocation(findViewById(R.id.layout_personal),
				Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
	
	}

	// 弹出评论窗
	public void showComment(View view, String pid, String id, String str,
			String table, String toUid) {
		CommentPopWin commentPopWin = new CommentPopWin(this, pid, id, str,
				table, toUid);
		commentPopWin.showAtLocation(findViewById(R.id.layout_personal),
				Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
	}

	private OnClickListener onClickListener = new View.OnClickListener() {
		@SuppressWarnings("deprecation")
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.wx_share:
				//checkWX();
				if ( !wxApi.isWXAppInstalled() || !wxApi.isWXAppSupportAPI() ){
					Toast.makeText(MatchDetailActivity.this, "未安装微信客户端", Toast.LENGTH_SHORT).show();
				}
				wxShare(0);
				break;
			case R.id.py_share:
				wxShare(1);
				break;
			case R.id.wb_share:
				if (!WeiboShareAPI.isWeiboAppInstalled()) {
					Toast.makeText(MatchDetailActivity.this, "未安装微博客户端", Toast.LENGTH_SHORT).show();
				}
				sendMessage();
				Log.e("wbshareover", "wbshareover");
				break;
			case R.id.copy_share:
				ClipboardManager c = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
				c.setText("http://www.juqilife.cn/index/game_info1/id/" + id);
				Toast.makeText(MatchDetailActivity.this, "内容已复制到剪切板",
						Toast.LENGTH_SHORT).show();
				break;
			case R.id.quit_ok:
				int rs = matchController.quit(id);
				if (rs == Config.QuitSuccess) {
					Toast.makeText(MatchDetailActivity.this, "退出成功",
							Toast.LENGTH_LONG).show();
					finish();
				} else if (rs == -1) {
					Toast.makeText(MatchDetailActivity.this, "网络异常",
							Toast.LENGTH_LONG).show();
					quitPop.dismiss();
				} else {
					Toast.makeText(MatchDetailActivity.this, "已开始，退出失败",
							Toast.LENGTH_LONG).show();
					quitPop.dismiss();
				}
				break;
			default:
				break;
			}
		}
	};

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		// 详细地址哪一行的点击事件
		case R.id.address_btn:
			mIntent = new Intent();
			mIntent.putExtra("slon", Config.lon);
			mIntent.putExtra("slat", Config.lat);
			mIntent.putExtra("elon", longitude);
			mIntent.putExtra("elat", latitude);
			mIntent.setClass(MatchDetailActivity.this, RouteActivity.class);
			startActivity(mIntent);
			break;
		// 返回
		case R.id.match_detail_to_sign:
			finish();
			break;
		// 报名球局按钮
		case R.id.attend_btn:
			userModel = userController.getInfo(Config.login_userid);//每次点击都和服务器交互判断状态
			if (userModel == null) {//未登录或登录过期
				mIntent = new Intent(MatchDetailActivity.this,
						LoginActivity.class);
				startActivity(mIntent);
				finish();
				break;
			} else {		
				showAttend(v, id, match.getFee());//球赛的id
				break;
			}
			/*if (Config.is_login) {
				showAttend(v, id, match.getFee());//球赛的id			
				break;
			} else {
				mIntent = new Intent(MatchDetailActivity.this,
						LoginActivity.class);
				startActivity(mIntent);
				finish();
				break;
			}*/
			
			// 评论按钮
		case R.id.comment_btn:
			if (Config.is_login) {
				showComment(v, "0", id, "", table, match.getU_id());
				break;
			} else {
				mIntent = new Intent(MatchDetailActivity.this,
						LoginActivity.class);
				startActivity(mIntent);
				finish();
				break;
			}
		case R.id.share_btn:
			showShare(v);
			break;
		default:
			break;
		}
	}

	// 弹出分享窗
	public void showQuit(View view) {
		quitPop = new QuitPopWin(this, onClickListener);
		quitPop.showAtLocation(findViewById(R.id.layout_personal),
				Gravity.CENTER, 0, 0);
	}

	public static String addDateMinut(String day, int x) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = null;
		try {
			date = format.parse(day);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		if (date == null)
			return "";
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.MINUTE, x);// 24小时制
		date = cal.getTime();
		cal = null;
		return format.format(date);
	}

	private String getWeek(String pTime) {
		String Week = "周";
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Calendar c = Calendar.getInstance();
		try {
			c.setTime(format.parse(pTime));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		if (c.get(Calendar.DAY_OF_WEEK) == 1) {
			Week += "日";
		}
		if (c.get(Calendar.DAY_OF_WEEK) == 2) {
			Week += "一";
		}
		if (c.get(Calendar.DAY_OF_WEEK) == 3) {
			Week += "二";
		}
		if (c.get(Calendar.DAY_OF_WEEK) == 4) {
			Week += "三";
		}
		if (c.get(Calendar.DAY_OF_WEEK) == 5) {
			Week += "四";
		}
		if (c.get(Calendar.DAY_OF_WEEK) == 6) {
			Week += "五";
		}
		if (c.get(Calendar.DAY_OF_WEEK) == 7) {
			Week += "六";
		}
		return Week;
	}

	/**
	 * @see {@link Activity#onNewIntent}
	 */
	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		// 从当前应用唤起微博并进行分享后，返回到当前应用时，需要在此处调用该函数
		// 来接收微博客户端返回的数据；执行成功，返回 true，并调用
		// {@link IWeiboHandler.Response#onResponse}；失败返回 false，不调用上述回调
		WeiboShareAPI.handleWeiboResponse(intent, MatchDetailActivity.this);
	}

	/**
	 * 接收微博客户端请求的数据。 当微博客户端唤起当前应用并进行分享时，该方法被调用。
	 * 
	 * @param baseRequest
	 *            微博请求数据对象
	 * @see {@link IWeiboShareAPI#handleWeiboRequest}
	 */
	@Override
	public void onResponse(BaseResponse baseResp) {
		Log.e("onResponse","onResponse");
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

		if (WeiboShareAPI.isWeiboAppSupportAPI()) {
			Log.e("isWeiboAppSupportAPI","isWeiboAppSupportAPI");//ok
			int supportApi = WeiboShareAPI.getWeiboAppSupportAPI();
			if (supportApi >= 10351 /* ApiUtils.BUILD_INT_VER_2_2 */) {
				Log.e("supportApi >= 10351","supportApi >= 10351");//ok
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
		Log.e("getWebpageObj","getWebpageObj over");//ok
		// 2. 初始化从第三方到微博的消息请求
		SendMultiMessageToWeiboRequest request = new SendMultiMessageToWeiboRequest();
		// 用transaction唯一标识一个请求
		request.transaction = String.valueOf(System.currentTimeMillis());
		request.multiMessage = weiboMessage;
		Log.e("request.multiMessage","request.multiMessage");//ok
		// 3. 发送请求消息到微博，唤起微博分享界面
		WeiboShareAPI.sendRequest(MatchDetailActivity.this, request);
		Log.e("WeiboShareAPI.sendRequest","WeiboShareAPI.sendRequest");
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
		WeiboShareAPI.sendRequest(MatchDetailActivity.this, request);
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
		mediaObject.actionUrl = imageurl+ id;
		mediaObject.defaultText = "快来加入";
		return mediaObject;
	}

	private void wxShare(final int shareWhat) {
		checkWX();
		final SendMessageToWX.Req req = new SendMessageToWX.Req();
		WXWebpageObject webpage = new WXWebpageObject();
		// 要跳转的地址
		webpage.webpageUrl = imageurl + id;
		final WXMediaMessage msg = new WXMediaMessage(webpage);
		msg.title = "分享球局";
		msg.description = match.getTitle();
		// 网络图片地址 png 格式
		// eg：
		// http://h.hiphotos.baidu.com/image/w%3D310/sign=58272176271f95caa6f594b7f9177fc5/aa18972bd40735fa29e06ab19c510fb30f2408a1.png
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

	/**
	 * call alipay sdk pay. 调用SDK支付
	 */
	public void pay(String outTrade) {
		if (TextUtils.isEmpty(PARTNER) || TextUtils.isEmpty(RSA_PRIVATE)
				|| TextUtils.isEmpty(SELLER)) {
			new AlertDialog.Builder(this)
					.setTitle("警告")
					.setMessage("需要配置PARTNER | RSA_PRIVATE| SELLER")
					.setPositiveButton("确定",
							new DialogInterface.OnClickListener() {
								public void onClick(
										DialogInterface dialoginterface, int i) {
									finish();
								}
							}).show();
			return;
		}
		String orderInfo = getOrderInfo("局气约球报名费", "报名费", "0.01", outTrade);//fee.getText().toString()
		/**
		 * 特别注意，这里的签名逻辑需要放在服务端，切勿将私钥泄露在代码中！
		 */
		String sign = sign(orderInfo);
		try {
			/**
			 * 仅需对sign 做URL编码
			 */
			sign = URLEncoder.encode(sign, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		/**
		 * 完整的符合支付宝参数规范的订单信息
		 */
		final String payInfo = orderInfo + "&sign=\"" + sign + "\"&"
				+ getSignType();
		Runnable payRunnable = new Runnable() {
			@Override
			public void run() {
				// 构造PayTask 对象
				PayTask alipay = new PayTask(MatchDetailActivity.this);
				// 调用支付接口，获取支付结果
				String result = alipay.pay(payInfo, true);
				Message msg = new Message();
				msg.what = SDK_PAY_FLAG;
				msg.obj = result;
				//aliHandler.sendMessage(msg);
			}
		};
		// 必须异步调用
		Thread payThread = new Thread(payRunnable);
		payThread.start();
	}

	/**
	 * get the sdk version. 获取SDK版本号
	 */
	public void getSDKVersion() {
		PayTask payTask = new PayTask(this);
		String version = payTask.getVersion();
		Toast.makeText(this, version, Toast.LENGTH_SHORT).show();
	}

	/**
	 * create the order info. 创建订单信息
	 */
	private String getOrderInfo(String subject, String body, String price,
			String outTrade) {

		// 签约合作者身份ID
		String orderInfo = "partner=" + "\"" + PARTNER + "\"";

		// 签约卖家支付宝账号
		orderInfo += "&seller_id=" + "\"" + SELLER + "\"";

		// 商户网站唯一订单号
		orderInfo += "&out_trade_no=" + "\"" + outTrade + "\"";

		// 商品名称
		orderInfo += "&subject=" + "\"" + subject + "\"";

		// 商品详情
		orderInfo += "&body=" + "\"" + body + "\"";

		// 商品金额
		orderInfo += "&total_fee=" + "\"" + price + "\"";

		// 服务器异步通知页面路径
		orderInfo += "&notify_url=" + "\""
				+ "http://www.juqilife.cn/AliPayAPI/notify_url" + "\"";

		// 服务接口名称， 固定值
		orderInfo += "&service=\"mobile.securitypay.pay\"";

		// 支付类型， 固定值
		orderInfo += "&payment_type=\"1\"";

		// 参数编码， 固定值
		orderInfo += "&_input_charset=\"utf-8\"";

		// 设置未付款交易的超时时间
		// 默认30分钟，一旦超时，该笔交易就会自动被关闭。
		// 取值范围：1m～15d。
		// m-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭）。
		// 该参数数值不接受小数点，如1.5h，可转换为90m。
		orderInfo += "&it_b_pay=\"30m\"";

		// extern_token为经过快登授权获取到的alipay_open_id,带上此参数用户将使用授权的账户进行支付
		// orderInfo += "&extern_token=" + "\"" + extern_token + "\"";

		// 支付宝处理完请求后，当前页面跳转到商户指定页面的路径，可空
		orderInfo += "&return_url=\"m.alipay.com\"";

		// 调用银行卡支付，需配置此参数，参与签名， 固定值 （需要签约《无线银行卡快捷支付》才能使用）
		// orderInfo += "&paymethod=\"expressGateway\"";

		return orderInfo;
	}

	/**
	 * sign the order info. 对订单信息进行签名
	 * 
	 * @param content
	 *            待签名订单信息
	 */
	private String sign(String content) {
		return SignUtils.sign(content, RSA_PRIVATE);
	}

	/**
	 * get the sign type we use. 获取签名方式
	 */
	private String getSignType() {
		return "sign_type=\"RSA\"";
	}
	
	public void reload() {
	    Intent intent = getIntent();
	    overridePendingTransition(0, 0);
	    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
	    finish();
	    overridePendingTransition(0, 0);
	    startActivity(intent);
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