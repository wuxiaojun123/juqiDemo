package cn.com.sfn.juqi.model;

import android.graphics.Bitmap;

public class AttendModel {
	private String id;
	private String u_name;
	private String u_mobile;
	private String u_age;
	private String avatar;
	private String time;
	private String joinLevel;
	private String status;
	private String fee;
	private Bitmap qr;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getU_name() {
		return u_name;
	}

	public void setU_name(String u_name) {
		this.u_name = u_name;
	}

	public String getU_mobile() {
		return u_mobile;
	}

	public void setU_mobile(String u_mobile) {
		this.u_mobile = u_mobile;
	}

	public String getU_age() {
		return u_age;
	}

	public void setU_age(String u_age) {
		this.u_age = u_age;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public String getJoinLevel() {
		return joinLevel;
	}

	public void setJoinLevel(String joinLevel) {
		this.joinLevel = joinLevel;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getFee() {
		return fee;
	}

	public void setFee(String fee) {
		this.fee = fee;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public Bitmap getQr() {
		return qr;
	}

	public void setQr(Bitmap qr) {
		this.qr = qr;
	}
}
