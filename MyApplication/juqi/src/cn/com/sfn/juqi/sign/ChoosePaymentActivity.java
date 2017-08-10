package cn.com.sfn.juqi.sign;


import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import net.sourceforge.simcpux.Constants;
import net.sourceforge.simcpux.MD5;
import net.sourceforge.simcpux.Util;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import cn.com.sfn.alipay.util.PayResult;
import cn.com.sfn.alipay.util.SignUtils;
import cn.com.sfn.juqi.controller.MatchController;
import cn.com.sfn.juqi.util.Config;

import com.alipay.sdk.app.PayTask;
import com.example.juqi.R;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ChoosePaymentActivity extends Activity implements OnClickListener{
	private RelativeLayout ali, wx;
	private TextView back;
	private Intent mIntent;
	private String id,rs,fee;
	private MatchController matchController=new MatchController();
	private PayReq req= new PayReq();
	private StringBuffer sb=new StringBuffer();
	
	public static IWXAPI wxApi;
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
	
	@SuppressLint("HandlerLeak")
	private Handler aliHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case SDK_PAY_FLAG: {
				PayResult payResult = new PayResult((String) msg.obj);
				/**
				 * 同步返回的结果必须放置到服务端进行验证（验证的规则请看https://doc.open.alipay.com/doc2/
				 * detail.htm?spm=0.0.0.0.xdvAU6&treeId=59&articleId=103665&
				 * docType=1) 建议商户依赖异步通知
				 */
				String resultInfo = payResult.getResult();// 同步返回需要验证的信息
				Log.e("resultInfo", resultInfo);
				String resultStatus = payResult.getResultStatus();
				// 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
				if (TextUtils.equals(resultStatus, "9000")) {				
					Toast.makeText(ChoosePaymentActivity.this, "支付成功",
							Toast.LENGTH_SHORT).show();				
					mIntent = new Intent();			
					mIntent.putExtra("matchId", id);			
					mIntent.setClass(ChoosePaymentActivity.this,  MatchDetailActivity.class);
					startActivity(mIntent);
					finish();
				} else {
					// 判断resultStatus 为非"9000"则代表可能支付失败
					// "8000"代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
					if (TextUtils.equals(resultStatus, "8000")) {
						Toast.makeText(ChoosePaymentActivity.this, "支付结果确认中",
								Toast.LENGTH_SHORT).show();
					} else {
						// 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
						Toast.makeText(ChoosePaymentActivity.this, "支付失败",
								Toast.LENGTH_SHORT).show();
					}
				}
				break;
			}
			default:
				break;
			}
		};
	};
		
		
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_choose_payment);
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
		mIntent = getIntent();
		id = mIntent.getStringExtra("matchid");//球局id
		fee = mIntent.getStringExtra("matchfee");
		//创建微信接口实例
		wxApi = WXAPIFactory.createWXAPI(this, Constants.APP_ID, true);
		wxApi.registerApp(Constants.APP_ID);
		
				
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.back_to_setting:
			finish();
			break;
		case R.id.ali_btn:
			String rs = matchController.paygame(id, "AliPay");
			if (rs.equals("")) {
				Toast.makeText(ChoosePaymentActivity.this, "支付出错",
						Toast.LENGTH_SHORT).show();
			} else {
				pay(rs);
			}
			
			
			break;
		case R.id.wx_btn:
			if(!wxApi.isWXAppInstalled())
            {
				Toast.makeText(ChoosePaymentActivity.this, "微信未安装",
						Toast.LENGTH_SHORT).show();
                return;
            }           
            if(!wxApi.isWXAppSupportAPI())
            {
            	Toast.makeText(ChoosePaymentActivity.this, "微信版本不支持",
						Toast.LENGTH_SHORT).show();
                return;
            }    
            
            
            
			/*rs = matchController.weixinpay(Config.login_userid,id,fee);
			if (rs.equals("")) {
				Toast.makeText(ChoosePaymentActivity.this, "支付出错",
						Toast.LENGTH_SHORT).show();
			} else {
				genPayReq();
				sendPayReq();	
			}*/
			
			String weixinrs =matchController.weixinpay(Config.login_userid,id,fee);//服务器返回的都是string类型数据   这里要改回fee！！！！！
			Log.e("fee", fee);
			try {
			JSONObject json;
			JSONObject jObject = new JSONObject(weixinrs);
			if(jObject.getInt("status") == 1){
				json = jObject.getJSONObject("data");				
				Log.e("get server pay params:",weixinrs);
				
				PayReq req = new PayReq();
				req.appId			= json.getString("appid");
				req.partnerId		= json.getString("partnerid");
				req.prepayId		= json.getString("prepayid");
				req.nonceStr		= json.getString("noncestr");
				req.timeStamp		= json.getString("timestamp");
				req.packageValue	= json.getString("package");
				req.sign			= json.getString("sign");
				//Log.e("get server pay sign:",exChange(json.getString("sign")));
				//req.extData			= "app data"; // optional
				Toast.makeText(ChoosePaymentActivity.this, "正常调起支付", Toast.LENGTH_SHORT).show();
				// 在支付之前，如果应用没有注册到微信，应该先调用IWXMsg.registerApp将应用注册到微信
				if(wxApi.sendReq(req)==true){
					Log.e("wxApi.sendReq", "wxApi.sendReqsuccess");
				}else{
					Log.e("wxApi.sendReq", "wxApi.sendReqfail");
				}
				
			}else{			
				Toast.makeText(ChoosePaymentActivity.this, jObject.getString("info"), Toast.LENGTH_SHORT).show();
			}
			}catch (JSONException e) {
				e.printStackTrace();
			}
			finish();
			
		}
	}
	//把一个字符串中的大写转为小写，小写转换为大写：思路1  
	public static String exChange(String str){  
	    StringBuffer sb = new StringBuffer();  
	    if(str!=null){  
	        for(int i=0;i<str.length();i++){  
	            char c = str.charAt(i);  
	            if(Character.isUpperCase(c)){  
	                sb.append(Character.toLowerCase(c));  
	            }else if(Character.isLowerCase(c)){  
	                sb.append(Character.toUpperCase(c));   
	            }  
	        }  
	    }  
	      
	    return sb.toString();  
	}  

	private void genPayReq() {

		req.appId = Constants.APP_ID;
		req.partnerId = Constants.MCH_ID;
		req.prepayId = rs;
		req.packageValue = "prepay_id="+rs;//扩展字段 ，暂填Sign=WXPay
		req.nonceStr = genNonceStr();
		req.timeStamp = String.valueOf(genTimeStamp());


		List<NameValuePair> signParams = new LinkedList<NameValuePair>();
		signParams.add(new BasicNameValuePair("appid", req.appId));
		signParams.add(new BasicNameValuePair("noncestr", req.nonceStr));
		signParams.add(new BasicNameValuePair("package", req.packageValue));
		signParams.add(new BasicNameValuePair("partnerid", req.partnerId));
		signParams.add(new BasicNameValuePair("prepayid", req.prepayId));
		signParams.add(new BasicNameValuePair("timestamp", req.timeStamp));

		req.sign = genAppSign(signParams);

		sb.append("sign\n"+req.sign+"\n\n");

		//show.setText(sb.toString());

		Log.e("orion", "----"+signParams.toString());

	}
	private void sendPayReq() {
		wxApi.registerApp(Constants.APP_ID);
		wxApi.sendReq(req);
	}
	
	private String genPackageSign(List<NameValuePair> params) {
		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < params.size(); i++) {
			sb.append(params.get(i).getName());
			sb.append('=');
			sb.append(params.get(i).getValue());
			sb.append('&');
		}
		sb.append("key=");
		sb.append(Constants.API_KEY);


		String packageSign = MD5.getMessageDigest(sb.toString().getBytes()).toUpperCase();
		Log.e("orion","----"+packageSign);
		return packageSign;
	}
	private String genAppSign(List<NameValuePair> params) {
		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < params.size(); i++) {
			sb.append(params.get(i).getName());
			sb.append('=');
			sb.append(params.get(i).getValue());
			sb.append('&');
		}
		sb.append("key=");
		sb.append(Constants.API_KEY);

		this.sb.append("sign str\n"+sb.toString()+"\n\n");
		String appSign = MD5.getMessageDigest(sb.toString().getBytes());
		Log.e("orion","----"+appSign);
		return appSign;
	}
	private String genNonceStr() {
		Random random = new Random();
		return MD5.getMessageDigest(String.valueOf(random.nextInt(10000)).getBytes());
	}

	private long genTimeStamp() {
		return System.currentTimeMillis() / 1000;
	}
	private String genOutTradNo() {
		Random random = new Random();
//		return "COATBAE810"; //璁㈠崟鍙峰啓姝荤殑璇濆彧鑳芥敮浠樹竴娆★紝绗簩娆′笉鑳界敓鎴愯鍗�
		return MD5.getMessageDigest(String.valueOf(random.nextInt(10000)).getBytes());
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
		String orderInfo = getOrderInfo("局气约球报名费", "报名费", fee       //fee 这里要改回！！！！！
				, outTrade);
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
				PayTask alipay = new PayTask(ChoosePaymentActivity.this);
				// 调用支付接口，获取支付结果
				String result = alipay.pay(payInfo, true);
				Message msg = new Message();
				msg.what = SDK_PAY_FLAG;
				msg.obj = result;
				aliHandler.sendMessage(msg);
				
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
}
