package cn.com.sfn.juqi.controller;

import java.util.ArrayList;
import java.util.List;

import cn.com.sfn.juqi.dejson.MessageDejson;
import cn.com.sfn.juqi.model.CommentsModel;
import cn.com.sfn.juqi.model.MessageModel;
import cn.com.sfn.juqi.net.MyHttpClient;

public class MessageController {
	private MyHttpClient httpClient;

	public MessageController() {
		httpClient = new MyHttpClient();
	}

	// 获取消息列表
	public List<MessageModel> getMessage(String type) {
		String action = "user/center/get_notice_detail";
		String params = "type=" + type;
		String str = httpClient.doPost(action, params);
		List<MessageModel> officials = new ArrayList<MessageModel>();
		if (str.equals("time out")) {
			return officials;
		} else {
			MessageDejson md = new MessageDejson();
			officials = md.officialListDejson(str, type);
			return officials;
		}
	}

	// 获取评论消息
	public List<CommentsModel> getCommentsMessage() {
		String action = "user/center/get_comment_detail";
		String str = httpClient.doGet(action);
		System.out.println(str);
		List<CommentsModel> comments = new ArrayList<CommentsModel>();
		if (str.equals("time out")) {
			return comments;
		} else {
			MessageDejson md = new MessageDejson();
			comments = md.commentsListDejson(str);
			return comments;
		}
	}
}
