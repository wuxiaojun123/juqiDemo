package cn.com.sfn.juqi.my.mybill;

import java.util.List;

import cn.com.sfn.juqi.adapter.BillOverviewAdapter;
import cn.com.sfn.juqi.controller.UserController;
import cn.com.sfn.juqi.model.AccountModel;
import cn.com.sfn.juqi.model.BillModel;
import cn.com.sfn.juqi.util.Config;
import cn.com.sfn.juqi.util.ToastUtil;
import cn.com.sfn.juqi.widgets.CircleImageView;
import cn.com.wx.util.GlideUtils;

import com.example.juqi.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

@SuppressLint("HandlerLeak")
public class BillOverViewFragment extends Fragment {
    private ListView billOverviewListView;
    private ListAdapter listAdapter;
    private List<BillModel> billOverviews;
    private AccountModel accountModel;
    private UserController userController;
    private CircleImageView avatar;
    private TextView balance, income;
    private View billOverView;
    private Context mContext;

    Handler myhandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    initView();
                    break;
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        billOverView = inflater.inflate(R.layout.activity_tab_billoverview,
                container, false);
        mContext = getActivity();
        userController = new UserController();
        findViewById();
        initBillOverView();

        return billOverView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public void initBillOverView() {
        new Thread() {
            public void run() {
                accountModel = userController.billOverView();
                myhandler.sendEmptyMessage(1);
            }
        }.start();
    }

    private void findViewById() {
        avatar = (CircleImageView) billOverView.findViewById(R.id.bill_avatar);
        balance = (TextView) billOverView.findViewById(R.id.balance);
        income = (TextView) billOverView.findViewById(R.id.income);
        billOverviewListView = (ListView) billOverView
                .findViewById(R.id.billOverviewList);
    }

    private void initView() {
        if (Config.mUserModel != null) {
            GlideUtils.loadCircleImage(Config.mUserModel.getUserAvatar(), avatar);
        }
        if (accountModel != null) {
            balance.setText(accountModel.getBalance());
            income.setText(accountModel.getIncome());
            billOverviews = accountModel.getBillModel();
            listAdapter = new BillOverviewAdapter(getActivity(), billOverviews);
            billOverviewListView.setAdapter(listAdapter);
        } else {
            ToastUtil.show(mContext, "无账单明细");
        }
    }

}