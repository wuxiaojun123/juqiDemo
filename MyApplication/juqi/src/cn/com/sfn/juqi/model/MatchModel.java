package cn.com.sfn.juqi.model;

import java.util.List;

import android.graphics.Bitmap;

/**
 * @author wangwb
 * @function 球局类
 */
public class MatchModel {
	private String id;
	private String title;
	private String u_id;
	private String u_mobile;
	private String attendance;
	private String num;
	private String create_time;
	private String start_time;
	private String modify_time;
	private String deadline;
	private String duration;
	private String type;
	private String spec;
	private String detail;
	private String s_id;
	private String s_name;
	private String location;
	private double longitude;
	private double latitude;
	private String fee;
	private String status;
	private String u_name;
	private String u_tel;
	private String status_title;
	private String type_title;
	private List<AttendModel> attendList;
	private Bitmap uImg;
	private String uAge;
	private int JoinEntire;
	private int JoinLevel;
	private int nowJoinNum;
	private int nextJoinNum;
	private int RelEntire;
	private int RelLevel;
	private int nowRelNum;
	private int nextRelNum;
	private String payNum;
	private String attendID;
	private String userAndmatch;
	private int page;

	public int getJoinEntire() {
		return JoinEntire;
	}

	public void setJoinEntire(int joinEntire) {
		JoinEntire = joinEntire;
	}

	public int getJoinLevel() {
		return JoinLevel;
	}

	public void setJoinLevel(int joinLevel) {
		JoinLevel = joinLevel;
	}

	public int getNowJoinNum() {
		return nowJoinNum;
	}

	public void setNowJoinNum(int nowJoinNum) {
		this.nowJoinNum = nowJoinNum;
	}

	public int getNextJoinNum() {
		return nextJoinNum;
	}

	public void setNextJoinNum(int nextJoinNum) {
		this.nextJoinNum = nextJoinNum;
	}

	public int getRelEntire() {
		return RelEntire;
	}

	public void setRelEntire(int relEntire) {
		RelEntire = relEntire;
	}

	public int getRelLevel() {
		return RelLevel;
	}

	public void setRelLevel(int relLevel) {
		RelLevel = relLevel;
	}

	public int getNowRelNum() {
		return nowRelNum;
	}

	public void setNowRelNum(int nowRelNum) {
		this.nowRelNum = nowRelNum;
	}

	public int getNextRelNum() {
		return nextRelNum;
	}

	public void setNextRelNum(int nextRelNum) {
		this.nextRelNum = nextRelNum;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getU_id() {
		return u_id;
	}

	public void setU_id(String u_id) {
		this.u_id = u_id;
	}

	public String getU_mobile() {
		return u_mobile;
	}

	public void setU_mobile(String u_mobile) {
		this.u_mobile = u_mobile;
	}

	public String getAttendance() {
		return attendance;
	}

	public void setAttendance(String attendance) {
		this.attendance = attendance;
	}

	public String getNum() {
		return num;
	}

	public void setNum(String num) {
		this.num = num;
	}

	public String getCreate_time() {
		return create_time;
	}

	public void setCreate_time(String create_time) {
		this.create_time = create_time;
	}

	public String getStart_time() {
		return start_time;
	}

	public void setStart_time(String start_time) {
		this.start_time = start_time;
	}

	public String getModify_time() {
		return modify_time;
	}

	public void setModify_time(String modify_time) {
		this.modify_time = modify_time;
	}

	public String getDeadline() {
		return deadline;
	}

	public void setDeadline(String deadline) {
		this.deadline = deadline;
	}

	public String getDuration() {
		return duration;
	}

	public void setDuration(String duration) {
		this.duration = duration;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getSpec() {
		return spec;
	}

	public void setSpec(String spec) {
		this.spec = spec;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

	public String getS_id() {
		return s_id;
	}

	public void setS_id(String s_id) {
		this.s_id = s_id;
	}

	public String getS_name() {
		return s_name;
	}

	public void setS_name(String s_name) {
		this.s_name = s_name;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public String getFee() {
		return fee;
	}

	public void setFee(String fee) {
		this.fee = fee;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getU_name() {
		return u_name;
	}

	public void setU_name(String u_name) {
		this.u_name = u_name;
	}

	public String getU_tel() {
		return u_tel;
	}

	public void setU_tel(String u_tel) {
		this.u_tel = u_tel;
	}

	public String getStatus_title() {
		return status_title;
	}

	public void setStatus_title(String status_title) {
		this.status_title = status_title;
	}

	public String getType_title() {
		return type_title;
	}

	public void setType_title(String type_title) {
		this.type_title = type_title;
	}

	public List<AttendModel> getAttendList() {
		return attendList;
	}

	public void setAttendList(List<AttendModel> attendList) {
		this.attendList = attendList;
	}

	public Bitmap getuImg() {
		return uImg;
	}

	public void setuImg(Bitmap uImg) {
		this.uImg = uImg;
	}

	public String getuAge() {
		return uAge;
	}

	public void setuAge(String uAge) {
		this.uAge = uAge;
	}

	public String getPayNum() {
		return payNum;
	}

	public void setPayNum(String payNum) {
		this.payNum = payNum;
	}

	public String getAttendID() {
		return attendID;
	}

	public void setAttendID(String attendID) {
		this.attendID = attendID;
	}

	public String getUserAndmatch() {
		return userAndmatch;
	}

	public void setUserAndmatch(String userAndmatch) {
		this.userAndmatch = userAndmatch;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}
}