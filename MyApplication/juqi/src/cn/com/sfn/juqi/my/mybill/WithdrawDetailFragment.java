package cn.com.sfn.juqi.my.mybill;

import java.util.List;

import cn.com.sfn.juqi.adapter.WithdrawDetailItemAdapter;
import cn.com.sfn.juqi.controller.UserController;
import cn.com.sfn.juqi.model.BillModel;

import com.example.juqi.R;

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

@SuppressLint("HandlerLeak")
public class WithdrawDetailFragment extends Fragment {
	private ListView withdrawDetailListView;
	private ListAdapter listAdapter;
	private List<BillModel> withdrawDetails;
	private Handler myhandler;
	private UserController userController;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		View withdrawDetailView = inflater.inflate(
				R.layout.activity_tab_withdrawdetail, container, false);
		userController = new UserController();
		withdrawDetailListView = (ListView) withdrawDetailView
				.findViewById(R.id.withdrawDetailList);
		initWithdrawDetail();
		myhandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case 1:
					listAdapter = new WithdrawDetailItemAdapter(getActivity(),
							withdrawDetails);
					withdrawDetailListView.setAdapter(listAdapter);
					break;
				default:
					break;
				}
			}
		};
		return withdrawDetailView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}
	public void initWithdrawDetail() {
		new Thread() {
			public void run() {
				withdrawDetails = userController.getWithdrawDetailList();
				Message msg = new Message();
				msg.what = 1;
				myhandler.sendMessage(msg);
			}
		}.start();
	}
}