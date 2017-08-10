package cn.com.sfn.juqi.dejson;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cn.com.sfn.juqi.model.AccountModel;
import cn.com.sfn.juqi.model.BillModel;
import cn.com.sfn.juqi.net.MyHttpClient;

public class BillDejson {
	private JSONObject infoObject, userObject;
	private String finance;
	private List<BillModel> billList;

	public AccountModel accountDejson(String str) {
		AccountModel accountModel = new AccountModel();
		try {
			JSONObject jObject = new JSONObject(str);
			if (jObject.getInt("status") == 0) {
				return null;
			} else {
				infoObject = jObject.getJSONObject("data");
				userObject = infoObject.getJSONObject("user");
				finance = infoObject.getString("finance");
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		JSONArray jsonArray;
		try {
			billList = new ArrayList<BillModel>();
			jsonArray = new JSONArray(finance);
			for (int i = 0; i < jsonArray.length(); i++) {
				BillModel billModel = new BillModel();
				JSONObject json = (JSONObject) jsonArray.get(i);
				billModel.setDate(json.getString("date").substring(5, 10));
				if (json.getString("num").substring(0, 1).equals("-")) {
					billModel.setMoney(json.getString("num"));
				} else {
					billModel.setMoney("+" + json.getString("num"));
				}
				billModel.setOperate(json.getString("g_title"));
				billList.add(billModel);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			accountModel.setId(userObject.getString("id"));
			accountModel.setAvatar(MyHttpClient.getImage("http://192.168.3.2"
					+ userObject.getString("u_img")));
			accountModel.setBalance(userObject.getString("coin"));
			accountModel.setIncome(userObject.getString("add_up"));
			accountModel.setBillModel(billList);
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return accountModel;
	}

	private String billDetail;
	private List<BillModel> billDetailList;

	public List<BillModel> getBillDetail(String str) {
		try {
			JSONObject jObject = new JSONObject(str);
			billDetail = jObject.getString("data");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		JSONArray jsonArray;
		try {
			billDetailList = new ArrayList<BillModel>();
			jsonArray = new JSONArray(billDetail);
			for (int i = 0; i < jsonArray.length(); i++) {
				BillModel billModel = new BillModel();
				JSONObject json = (JSONObject) jsonArray.get(i);
				if (!json.getString("g_id").equals("0")) {
					billModel.setName(json.getString("canyuzhe"));
					billModel.setOperate(json.getString("g_title"));
				} else {
					billModel.setName(json.getString("g_title"));
					billModel.setOperate("");
				}
				billModel.setDate(json.getString("date").substring(5, 10));
				if (json.getString("num").substring(0, 1).equals("-")) {
					billModel.setMoney(json.getString("num"));
				} else {
					billModel.setMoney("+" + json.getString("num"));
				}
				
				billDetailList.add(billModel);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return billDetailList;
	}
	
	private String withdrawDetail;
	private List<BillModel> withdrawList;

	public List<BillModel> getWithdrawDetail(String str) {
		try {
			JSONObject jObject = new JSONObject(str);
			withdrawDetail = jObject.getString("data");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		JSONArray jsonArray;
		try {
			withdrawList = new ArrayList<BillModel>();
			jsonArray = new JSONArray(withdrawDetail);
			for (int i = 0; i < jsonArray.length(); i++) {
				BillModel billModel = new BillModel();
				JSONObject json = (JSONObject) jsonArray.get(i);
				billModel.setDate(json.getString("date").substring(5, 10));
				billModel.setState(json.getString("status_title"));
				if (json.getString("num").substring(0, 1).equals("-")) {
					billModel.setMoney(json.getString("num"));
				} else {
					billModel.setMoney("+" + json.getString("num"));
				}
				withdrawList.add(billModel);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return withdrawList;
	}
}
