/**
 * 
 */
package cn.com.sfn.juqi.util;

import android.text.TextUtils;

/**
 * 校验器
 * Copyright (c) 2014 Sinocall All right reserved.
 * 
 */
public class ValidateUtil {

	/**
	 * 校验字符串是否为null或者空字符串
	 * 如果字符串中包含空字符（含空格）返回false
	 * @param str
	 * @return
	 */
	public static boolean isNull(String str){
		if(null == str){
			return true;
		}
		if("".equals(str.trim())){
			return true;
		}
		return false;
	}
	
	/**
	 * 校验字符串是否为null或者空字符串
	 * 如果字符串中包含空字符（含空格）返回true
	 * @param str
	 */
	public static boolean isNotNull(String str){
		return !isNull(str);
	}
	
	/**
	 * 校验手机号格式
	 * 手机号格式校验正确则返回true,否则返回false
	 * @param mob 需要校验的手机号
	 * @return
	 */
	public static boolean isMobile(String mob){
		String reg = "^((13[0-9])|(15[^4,\\D])|(18[0-9]))\\d{8}$";
		if(isNull(mob)){
			return false;
		}
		if(mob.matches(reg)){
			return true;
		}
		return false;
	}
	
	/**
     * 判断格式为邮箱
     * @param email
     * @return
     */
    public static boolean isEmail(String email){
    	if(isNull(email)){
    		return false;
    	}else{
    		String reg = "^[A-Za-z0-9][\\w\\._]*[a-zA-Z0-9]+@[A-Za-z0-9-_]+\\.([A-Za-z]{2,4})";
    		if(email.matches(reg)){
    			return true;
    		}else{
    			return false;
    		}
    	}
    }
    
    /***
     * 判断格式是否为身份证
     * @param sfz
     * @return
     */
    public static boolean isShenFenZheng(String sfz){
    	if(TextUtils.isEmpty(sfz)){
    		return false;
    	}else{
    		String mySfz = "(^[1-9]\\d{7}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}$)|(^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}([0-9]|X|x)$)";
    		if(sfz.matches(mySfz)){
    			return true;
    		}else{
    			return false;
    		}
    	}
    }
    
    /***
     * 验证密码的长度是否为8-12位
     * @param pwd
     * @return
     */
    public static boolean validatePwd(String pwd){
    	if(TextUtils.isEmpty(pwd)){
    		return false;
    	}else{
    		String mySfz = "[a-zA-Z][a-zA-Z\\d]{7,11}";
    		if(pwd.matches(mySfz)){
    			return true;
    		}else{
    			return false;
    		}
    	}
    }
    
}
