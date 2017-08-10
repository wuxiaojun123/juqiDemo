package cn.com.sfn.juqi.model;

public class BillModel {
	private String name;
	private String Date;
	private String money;
	private String state;
	private String operate;

	public String getOperate() {
		return operate;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getDate() {
		return Date;
	}

	public void setDate(String date) {
		Date = date;
	}

	public void setOperate(String operate) {
		this.operate = operate;
	}

	public String getMoney() {
		return money;
	}

	public void setMoney(String money) {
		this.money = money;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}