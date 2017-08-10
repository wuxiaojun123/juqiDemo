package cn.com.sfn.juqi.my.mybill;

import java.util.List;

import cn.com.sfn.juqi.adapter.BillDetailAdapter;
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

public class BillDetailFragment extends Fragment {
	private ListView billDetailListView;
	private ListAdapter listAdapter;
	private List<BillModel> billDetails;
	private Handler myhandler;
	private UserController userController;
	@SuppressLint("HandlerLeak")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		View billDetailView = inflater.inflate(
				R.layout.activity_tab_billdetail, container, false);
		userController = new UserController();
		billDetailListView = (ListView) billDetailView
				.findViewById(R.id.billDetailList);
		initBillDetail();
		myhandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case 1:
					listAdapter = new BillDetailAdapter(getActivity(), billDetails);
					billDetailListView.setAdapter(listAdapter);
					break;
				default:
					break;
				}
			}
		};
		return billDetailView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}
	public void initBillDetail() {
		new Thread() {
			public void run() {
				billDetails = userController.getBillDetailList();
				Message msg = new Message();
				msg.what = 1;
				myhandler.sendMessage(msg);
			}
		}.start();
	}
}