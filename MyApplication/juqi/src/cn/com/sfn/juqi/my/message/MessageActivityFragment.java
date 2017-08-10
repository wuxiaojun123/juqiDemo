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
import cn.com.sfn.juqi.adapter.MessageItemAdapter;
import cn.com.sfn.juqi.controller.MessageController;
import cn.com.sfn.juqi.model.MessageModel;

import com.example.juqi.R;

@SuppressLint("HandlerLeak")
public class MessageActivityFragment extends Fragment {
	private ListView activityListView;
	private ListAdapter listAdapter;
	private List<MessageModel> activities;
	private Handler myhandler;
	private MessageController messageController;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		View activityView = inflater.inflate(R.layout.activity_tab_activity,
				container, false);
		messageController = new MessageController();
		activityListView = (ListView) activityView
				.findViewById(R.id.activityList);
		initActivityMessage();
		myhandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case 1:
					listAdapter = new MessageItemAdapter(getActivity(),
							activities);
					activityListView.setAdapter(listAdapter);
					break;
				default:
					break;
				}
			}
		};
		return activityView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	public void initActivityMessage() {
		new Thread() {
			public void run() {
				activities = messageController.getMessage("3");
				Message msg = new Message();
				msg.what = 1;
				myhandler.sendMessage(msg);
			}
		}.start();
	}
}
