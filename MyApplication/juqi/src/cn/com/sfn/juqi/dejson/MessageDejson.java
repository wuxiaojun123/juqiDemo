package cn.com.sfn.juqi.dejson;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cn.com.sfn.juqi.model.CommentsModel;
import cn.com.sfn.juqi.model.MessageModel;
import cn.com.sfn.juqi.net.MyHttpClient;
import cn.com.sfn.juqi.util.Config;

public class MessageDejson {
	private List<MessageModel> officialList;
	private String officials;

	public List<MessageModel> officialListDejson(String str, String type) {
		officialList = new ArrayList<MessageModel>();
		String typename = null;
		if (type == "2") {
			typename = "系统消息";
		} else if (type == "3") {
			typename = "活动提醒";
		} else {
			typename = "官方通知";
		}
		try {
			JSONObject jObject = new JSONObject(str);
			if (jObject.getInt("status") == 0) {
				return officialList;
			} else {
				officials = jObject.getString("data");
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}
		JSONArray jsonArray;
		try {
			jsonArray = new JSONArray(officials);
			for (int i = 0; i < jsonArray.length(); i++) {
				MessageModel messageModel = new MessageModel();
				JSONObject json = (JSONObject) jsonArray.get(i);
				messageModel.setContent(json.getString("content"));
				messageModel.setTime(json.getString("create_time").substring(0,
						16));
				messageModel.setType(typename);
				officialList.add(messageModel);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return officialList;
	}

	private List<CommentsModel> commentList;
	private String comments;

	public List<CommentsModel> commentsListDejson(String str) {
		commentList = new ArrayList<CommentsModel>();
		try {
			JSONObject jObject = new JSONObject(str);
			if (jObject.getInt("status") == 0) {
				return commentList;
			} else {
				comments = jObject.getString("data");
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}
		JSONArray jsonArray;
		try {
			jsonArray = new JSONArray(comments);
			for (int i = 0; i < jsonArray.length(); i++) {
				CommentsModel commentsModel = new CommentsModel();
				JSONObject json = (JSONObject) jsonArray.get(i);
				commentsModel.setUserName(json.getString("full_name"));
				commentsModel.setContent(json.getString("content"));
				commentsModel.setUserAvatar(Config.URL_BASE
								+ json.getString("u_img"));
				commentList.add(commentsModel);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return commentList;
	}
}
