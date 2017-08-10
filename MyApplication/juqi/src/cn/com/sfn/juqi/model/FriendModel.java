package cn.com.sfn.juqi.model;

import java.util.List;

import android.graphics.Bitmap;

public class FriendModel {
	private String userId;
	private String nickName;
	private Bitmap userAvatar;
	private String userSex;
	private String age;
	private String signature;
	private String offense;
	private String defense;
	private String comprehensive;
	private String joinEntire;
	private String releaseEntire;
	private String standard;
	private String position;
	private String friendNum;
	private List<MatchModel> releaseList;
	private String friendship;
	
	
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public Bitmap getUserAvatar() {
		return userAvatar;
	}

	public void setUserAvatar(Bitmap userAvatar) {
		this.userAvatar = userAvatar;
	}

	public String getUserSex() {
		return userSex;
	}

	public void setUserSex(String userSex) {
		this.userSex = userSex;
	}

	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
	}

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

	public String getOffense() {
		return offense;
	}

	public void setOffense(String offense) {
		this.offense = offense;
	}

	public String getDefense() {
		return defense;
	}

	public void setDefense(String defense) {
		this.defense = defense;
	}

	public String getComprehensive() {
		return comprehensive;
	}

	public void setComprehensive(String comprehensive) {
		this.comprehensive = comprehensive;
	}

	public String getJoinEntire() {
		return joinEntire;
	}

	public void setJoinEntire(String joinEntire) {
		this.joinEntire = joinEntire;
	}

	public String getReleaseEntire() {
		return releaseEntire;
	}

	public void setReleaseEntire(String releaseEntire) {
		this.releaseEntire = releaseEntire;
	}

	public String getStandard() {
		return standard;
	}

	public void setStandard(String standard) {
		this.standard = standard;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public String getFriendNum() {
		return friendNum;
	}

	public void setFriendNum(String friendNum) {
		this.friendNum = friendNum;
	}

	public List<MatchModel> getReleaseList() {
		return releaseList;
	}

	public void setReleaseList(List<MatchModel> releaseList) {
		this.releaseList = releaseList;
	}

	public String getFriendship() {
		return friendship;
	}

	public void setFriendship(String friendship) {
		this.friendship = friendship;
	}

}
