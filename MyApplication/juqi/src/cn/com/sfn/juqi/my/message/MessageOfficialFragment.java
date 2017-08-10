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

public class MessageOfficialFragment extends Fragment {
	private ListView officialListView;
	private ListAdapter listAdapter;
	private List<MessageModel> messages;
	private Handler myhandler;
	private MessageController messageController;

	@SuppressLint("HandlerLeak")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		View officialView = inflater.inflate(R.layout.activity_tab_official,
				container, false);
		officialListView = (ListView) officialView
				.findViewById(R.id.officialList);
		messageController = new MessageController();
		initOfficialMessage();
		myhandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case 1:
					listAdapter = new MessageItemAdapter(getActivity(),
							messages);
					officialListView.setAdapter(listAdapter);
					break;
				default:
					break;
				}
			}
		};
		return officialView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	public void initOfficialMessage() {
		new Thread() {
			public void run() {
				messages = messageController.getMessage("4");
				Message msg = new Message();
				msg.what = 1;
				myhandler.sendMessage(msg);
			}
		}.start();
	}
}
