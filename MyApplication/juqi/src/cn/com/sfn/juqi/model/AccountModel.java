package cn.com.sfn.juqi.model;

import java.util.List;

import android.graphics.Bitmap;

public class AccountModel {
	private String id;
	private Bitmap avatar;
	private String balance;
	private String income;
	private List<BillModel> billModel;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Bitmap getAvatar() {
		return avatar;
	}
	public void setAvatar(Bitmap avatar) {
		this.avatar = avatar;
	}
	public String getBalance() {
		return balance;
	}
	public void setBalance(String balance) {
		this.balance = balance;
	}
	public String getIncome() {
		return income;
	}
	public void setIncome(String income) {
		this.income = income;
	}
	public List<BillModel> getBillModel() {
		return billModel;
	}
	public void setBillModel(List<BillModel> billModel) {
		this.billModel = billModel;
	}
	
}
