package cn.com.sfn.juqi.my.message;

import java.util.List;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import cn.com.sfn.juqi.adapter.CommentsItemAdapter;
import cn.com.sfn.juqi.controller.MessageController;
import cn.com.sfn.juqi.model.CommentsModel;

import com.example.juqi.R;

public class MessageCommentFragment extends Fragment {
	private ListView commentListView;
	private ListAdapter listAdapter;
	private List<CommentsModel> comments;
	private Handler myhandler;
	private MessageController messageController;

	@SuppressLint("HandlerLeak")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		View commentView = inflater.inflate(R.layout.activity_tab_comments,
				container, false);
		messageController = new MessageController();
		commentListView = (ListView) commentView
				.findViewById(R.id.commentMessageList);
		initCommentMessage();
		myhandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case 1:
					listAdapter = new CommentsItemAdapter(getActivity(),
							comments, null);
					commentListView.setAdapter(listAdapter);
					break;
				default:
					break;
				}
			}
		};
		return commentView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	public void initCommentMessage() {
		new Thread() {
			public void run() {
				comments = messageController.getCommentsMessage();
				Message msg = new Message();
				msg.what = 1;
				myhandler.sendMessage(msg);
			}
		}.start();
	}
}
