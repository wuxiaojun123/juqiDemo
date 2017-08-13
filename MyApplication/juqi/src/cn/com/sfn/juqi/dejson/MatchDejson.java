/**
 * 球局列表
 */
package cn.com.sfn.juqi.dejson;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import cn.com.sfn.juqi.model.AttendModel;
import cn.com.sfn.juqi.model.CommentsModel;
import cn.com.sfn.juqi.model.MatchModel;
import cn.com.sfn.juqi.net.MyHttpClient;
import cn.com.sfn.juqi.util.Config;

public class MatchDejson {
	private List<MatchModel> matchList;
	private String matches;
	private JSONObject matchObject;
	private int page;
	public List<MatchModel> matchListDejson(String str) throws Exception {
		try {
			JSONObject jObject = new JSONObject(str);
			matchObject = jObject.getJSONObject("data");
			matches = matchObject.getString("game_list");
			page = matchObject.getInt("total_page");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		JSONArray jsonArray;
		try {
			matchList = new ArrayList<MatchModel>();
			jsonArray = new JSONArray(matches);
			for (int i = 0; i < jsonArray.length(); i++) {
				MatchModel matchModel = new MatchModel();
				JSONObject json = (JSONObject) jsonArray.get(i);
				matchModel.setId(json.getString("id"));
				matchModel.setTitle(json.getString("title"));
				matchModel.setU_id(json.getString("u_id"));
				matchModel.setU_mobile(json.getString("u_mobile"));
				matchModel.setAttendance(json.getString("attendance"));
				matchModel.setNum(json.getString("num"));
				matchModel.setDuration(json.getString("duration"));
				matchModel.setCreate_time(json.getString("create_time"));
				matchModel.setStart_time(json.getString("start_time"));
				matchModel.setSpec(json.getString("spec"));
				matchModel.setDetail(json.getString("detail"));
				matchModel.setS_name(json.getString("s_name"));
				matchModel.setLocation(json.getString("location"));
				matchModel.setLongitude(Double.valueOf(json
						.getString("longitude")));
				matchModel.setLatitude(Double.valueOf(json
						.getString("latitude")));
				matchModel.setFee(json.getString("fee"));
				matchModel.setuImg(Config.URL_BASE
						+ json.getString("u_img"));
				matchModel.setPage(page);
				matchList.add(matchModel);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return matchList;
	}

	private JSONObject gameObject, statusObject;
	private String attends, gStatus, jStatus;
	private List<AttendModel> attendList;
	private String userMatch;

	public MatchModel matchDetailDejson(String str) {
		MatchModel matchDetail = new MatchModel();
		try {
			JSONObject jObject = new JSONObject(str);
			if (jObject.getInt("status") == 0) {
				return null;
			} else {
				matchObject = jObject.getJSONObject("data");
				gameObject = matchObject.getJSONObject("game");
				attends = matchObject.getString("join");
				statusObject = matchObject.getJSONObject("status");
				gStatus = statusObject.getString("g_status");
				jStatus = statusObject.getString("j_status");
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}
		/**
		 * gStatus:0-招募中 1-人员满 2-球局开始 3-球局已结束 4-发起者取消
		 * jStatus:-1-未参与 0-未支付 1-已过期  2-已支付 3-已退出 4-已结束 5-扫描确认成功
		 * userMatch:0-可参与 1-需支付 2-可退出 3-满员 4-已结束  5-正在进行
		 */
		if (gStatus.equals("0")) {
			if (jStatus.equals("-1") || jStatus.equals("1")
					|| jStatus.equals("3")) {
				userMatch = "0";
			} else if (jStatus.equals("0")) {
				userMatch = "1";
			} else if (jStatus.equals("2") || jStatus.equals("5")) {
				userMatch = "2";
			} else {
				userMatch = "4";
			}
		} else if (gStatus.equals("1")) {
			if (jStatus.equals("-1") || jStatus.equals("1")
					|| jStatus.equals("3")) {
				userMatch = "3";
			} else if (jStatus.equals("0")) {
				userMatch = "1";
			} else if (jStatus.equals("2") || jStatus.equals("5")) {
				userMatch = "2";
			} else {
				userMatch = "4";
			}
		} else if (gStatus.equals("2")) {
			userMatch = "5";
		} else if (gStatus.equals("3")) {
			userMatch = "3";
		} else if (gStatus.equals("4")) {
			userMatch = "4";
		}
		JSONArray jsonArray;
		try {
			attendList = new ArrayList<AttendModel>();
			jsonArray = new JSONArray(attends);
			for (int i = 0; i < jsonArray.length(); i++) {
				AttendModel attendModel = new AttendModel();
				JSONObject json = (JSONObject) jsonArray.get(i);
				attendModel.setId(json.getString("u_id"));
				attendModel.setU_name(json.getString("u_name"));
				attendModel.setU_mobile(json.getString("u_mobile"));
				attendModel.setAvatar(Config.URL_BASE
								+ json.getString("u_img"));
				attendModel.setU_age(json.getString("u_age"));
				attendModel.setJoinLevel(json.getString("join_level"));
				attendModel.setStatus(json.getString("status_title"));
				attendModel.setFee(gameObject.getString("fee"));
				attendList.add(attendModel);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			matchDetail.setId(gameObject.getString("id"));
			matchDetail.setTitle(gameObject.getString("title"));
			matchDetail.setU_id(gameObject.getString("u_id"));
			matchDetail.setU_mobile(gameObject.getString("u_mobile"));
			matchDetail.setAttendance(gameObject.getString("attendance"));
			matchDetail.setNum(gameObject.getString("num"));
			matchDetail.setDuration(gameObject.getString("duration"));
			matchDetail.setCreate_time(gameObject.getString("create_time"));
			matchDetail.setStart_time(gameObject.getString("start_time"));
			matchDetail.setType(gameObject.getString("type"));
			matchDetail.setSpec(gameObject.getString("spec"));
			matchDetail.setDetail(gameObject.getString("detail"));
			matchDetail.setS_name(gameObject.getString("s_name"));
			matchDetail.setLocation(gameObject.getString("location"));
			matchDetail.setLongitude(Double.valueOf(gameObject
					.getString("longitude")));
			matchDetail.setLatitude(Double.valueOf(gameObject
					.getString("latitude")));
			matchDetail.setFee(gameObject.getString("fee"));
			matchDetail.setAttendList(attendList);
			matchDetail.setU_name(gameObject.getString("u_name"));
			matchDetail.setJoinEntire(gameObject.getInt("join_entire"));
			matchDetail.setJoinLevel(gameObject.getInt("join_level"));
			matchDetail.setNowJoinNum(gameObject.getInt("join_num"));
			matchDetail.setNextJoinNum(gameObject.getInt("next_join_num"));
			matchDetail.setRelEntire(gameObject.getInt("release_entire"));
			matchDetail.setRelLevel(gameObject.getInt("release_level"));
			matchDetail.setNowRelNum(gameObject.getInt("release_num"));
			matchDetail.setuAge(gameObject.getString("u_age"));
			matchDetail.setNextRelNum(gameObject.getInt("next_release_num"));
			matchDetail.setuImg(Config.URL_BASE
					+ gameObject.getString("u_img"));
			matchDetail.setUserAndmatch(userMatch);
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return matchDetail;
	}

	private JSONObject infoObject;
	private String manageAttends;
	private List<AttendModel> manageAttendList;

	public MatchModel manageJoinDejson(String str) {
		MatchModel matchDetail = new MatchModel();
		try {
			JSONObject jObject = new JSONObject(str);
			if (jObject.getInt("status") == 0) {
				return null;
			} else {
				infoObject = jObject.getJSONObject("info");
				manageAttends = jObject.getString("data");
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}
		JSONArray jsonArray;
		try {
			manageAttendList = new ArrayList<AttendModel>();
			jsonArray = new JSONArray(manageAttends);
			for (int i = 0; i < jsonArray.length(); i++) {
				AttendModel attendModel = new AttendModel();
				JSONObject json = (JSONObject) jsonArray.get(i);
				attendModel.setId(json.getString("u_id"));
				attendModel.setU_name(json.getString("u_name"));
				attendModel.setTime(json.getString("enroll_time"));
				attendModel.setU_mobile(json.getString("u_mobile"));
				attendModel.setAvatar(Config.URL_BASE
								+ json.getString("u_img"));
				attendModel.setStatus(json.getString("status"));
				attendModel.setFee(infoObject.getString("fee"));
				manageAttendList.add(attendModel);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			matchDetail.setId(infoObject.getString("g_id"));
			matchDetail.setFee(infoObject.getString("fee"));
			matchDetail.setAttendance(infoObject.getString("total"));
			matchDetail.setPayNum(infoObject.getString("payed"));
			matchDetail.setAttendList(manageAttendList);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return matchDetail;
	}

	private String tableString;
	private JSONObject table;
	private JSONObject commentTable;

	public String commentTable(String str) {
		try {
			JSONObject jObject = new JSONObject(str);
			commentTable = jObject.getJSONObject("data");
			table = commentTable.getJSONObject("info");
			tableString = table.getString("post_table");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return tableString;
	}

	private String result;
	private JSONObject rsSuccess;

	public String qrCon(String str) {
		try {
			JSONObject jObject = new JSONObject(str);
			if (jObject.getInt("status") == 0) {
				return "";
			} else {
				rsSuccess = jObject.getJSONObject("data");
				result = "id/" + rsSuccess.getInt("id") + "/faqizhe/"
						+ rsSuccess.getString("faqizhe") + "/token/"
						+ rsSuccess.getString("token");
				return result;
			}
		} catch (JSONException e) {
			e.printStackTrace();
			return "";
		}
	}

	private String outtrade;

	public String outTrade(String str) {
		try {
			JSONObject jObject = new JSONObject(str);
			
			if (jObject.getInt("status") == 0) {
				return "";
			} else {
				outtrade = jObject.getString("data");
				return outtrade;
			}
		} catch (JSONException e) {
			e.printStackTrace();
			return "";
		}
	}

	private String comments;
	private JSONObject commentObject;
	private List<CommentsModel> commentsList;

	public List<CommentsModel> commentListDejson(String str) {
		try {
			JSONObject jObject = new JSONObject(str);
			commentObject = jObject.getJSONObject("data");
			comments = commentObject.getString("comments");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		JSONArray jsonArray;
		try {
			commentsList = new ArrayList<CommentsModel>();
			jsonArray = new JSONArray(comments);
			for (int i = 0; i < jsonArray.length(); i++) {
				CommentsModel commentModel = new CommentsModel();
				JSONObject json = (JSONObject) jsonArray.get(i);
				commentModel.setUserId(json.getString("uid"));
				commentModel.setContent(json.getString("content"));
				commentModel.setUserName(json.getString("full_name"));
				commentModel.setPostId(json.getString("post_id"));
				commentModel.setToUid(json.getString("to_uid"));
				commentModel.setUserAvatar(Config.URL_BASE
								+ json.getString("u_img"));
				commentsList.add(commentModel);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return commentsList;
	}
}