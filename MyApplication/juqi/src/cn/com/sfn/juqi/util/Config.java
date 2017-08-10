package cn.com.sfn.juqi.util;

import android.graphics.Bitmap;

public class Config {
	// 高地地图所需的经纬度
	public static double lat;// 纬度
	public static double lon;// 经度
	public static String place;
	public static double t_lat;// 纬度
	public static double t_lon;// 经度
	public static String choose_type;
	// 微信相关的
	public static String wxsex;
	public static Bitmap wxavatar;
	// 别的
	public static String login_userid; // 登陆用户的id
	public static boolean is_login;// 判断是否登录
	public static String login_avatar;// 登陆用户的头像
	public static String login_nickname;// 登陆用户的昵称
	public static int pages;
	public static String login_type;
	public static String Toggle;

	// 有关用户的均以1开头
	public static final int LOGIN_CODE = 100;

	public static final int LoginSuccess = 1001;
	public static final int LoginFailed = 1002;

	public static final int RegisterSuccess = 1003;
	public static final int RegisterFailed = 1004;

	public static final int EditSuccess = 1005;
	public static final int EditFailed = 1006;

	public static final int ChangePasswordSuccess = 1007;
	public static final int ChangePasswordFailed = 1008;

	public static final int AddFriendSuccess = 1009;
	public static final int AddFriendFailed = 1010;

	public static final int ForgetPasswordSuccess = 1011;
	public static final int ForgetPasswordFailed = 1012;

	public static final int ValidateSuccess = 1013;
	public static final int ValidateFailed = 1014;

	public static final int AuthUploadSuccess = 1015;
	public static final int AuthUploadFailed = 1016;

	public static final int SetAccountSuccess = 1017;
	public static final int setAccountFailed = 1018;

	public static final int WithDrawSuccess = 1019;
	public static final int WithDrawFailed = 1020;
	
	public static final int LogoutSuccess = 1021;
	public static final int LogoutFailed = 1022;

	// 有关球局的均以2开头
	public static final int AppointSuccess = 2001;
	public static final int AppointFailed = 2002;

	public static final int AttendSuccess = 2003;
	public static final int AttendFailed = 2004;

	public static final int CommentSuccess = 2005;
	public static final int CommentFailed = 2006;

	public static final int ChangeMatchSuccsee = 2007;
	public static final int ChangeMatchFailed = 2008;

	public static final int DeleteMatchSuccess = 2009;
	public static final int DeleteMatchFailed = 2010;

	public static final int ScanQrSuccess = 2011;
	public static final int ScanQrFailed = 2012;

	public static final int QuitSuccess = 2013;
	public static final int QuitFailed = 2014;
	
	public static final int CancelFriendSuccess = 2015;
	public static final int CancelFriendFailed = 2016;
	
	public static String AppointFailedHint;

	public static final String PREFS_NAME = "MyPrefsFile";
	public static String SessionID;
}