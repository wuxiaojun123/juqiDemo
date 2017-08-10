/**
 * 管理页面
 *
 * @author wangwb
 */
package cn.com.sfn.juqi.manage;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;

import cn.com.sfn.juqi.adapter.ListItemClickHelp;
import cn.com.sfn.juqi.adapter.ManageItemAdapter;
import cn.com.sfn.juqi.controller.MatchController;
import cn.com.sfn.juqi.model.MatchModel;
import cn.com.sfn.juqi.sign.MatchDetailActivity;
import cn.com.sfn.juqi.util.Config;

import com.example.juqi.R;
import com.zxing.activity.CaptureActivity;

@SuppressLint("HandlerLeak")
public class ManageFragment extends Fragment {
    private Intent mIntent;
    private ListView matchListView;
    private ManageItemAdapter listAdapter;
    private List<MatchModel> matches;
    private LinearLayout emptyLayout;
    private MatchController matchController;

    private Handler myhandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    if (matches.size() != 0) {
                        emptyLayout.setVisibility(View.INVISIBLE);
                    }
                    listAdapter.addList(matches);
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View manageView = inflater.inflate(R.layout.activity_manage, container,
                false);
        matchController = new MatchController();
        matchListView = (ListView) manageView.findViewById(R.id.manageList);
        emptyLayout = (LinearLayout) manageView.findViewById(R.id.rl);

        // listview内部按钮功能实现
        matches = new ArrayList<>();
        listAdapter = new ManageItemAdapter(getActivity(), matches, lich);//lich: ListItemClickHelp callback,传一个点击回调，在本activity中设置事件
        matchListView.setAdapter(listAdapter);
        if (Config.login_userid != null) {
            initList();
        }

        return manageView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public void initList() {
        new Thread() {
            public void run() {
                matches = matchController.getManageMatchList(Config.login_userid);
                Message msg = new Message();
                msg.what = 1;
                myhandler.sendMessage(msg);
            }
        }.start();
    }

    public ListItemClickHelp lich = new ListItemClickHelp() {
        @Override
        public void onClick(View item, View widget, int position, int which) {
            MatchModel mm = matches.get(position);
            switch (which) {
                // 球局验票按钮，点击进入扫描二维码
                case R.id.manage_inspect_btn:
                    mIntent = new Intent(getActivity(), CaptureActivity.class);
                    startActivity(mIntent);
                    break;
                // 查看报名按钮，点击进入参与人列表
                case R.id.manage_attend_btn:
                    mIntent = new Intent();
                    mIntent.putExtra("matchId", mm.getId());
                    mIntent.setClass(getActivity(), ManageAttendActivity.class);
                    startActivity(mIntent);
                    break;
                // 进入球局详情页
                case R.id.manage_detail:
                    mIntent = new Intent();
                    mIntent.putExtra("matchId", mm.getId());
                    mIntent.setClass(getActivity(), MatchDetailActivity.class);
                    startActivity(mIntent);
                    break;
                // 修改球局
                case R.id.change_btn:
                    mIntent = new Intent();
                    mIntent.putExtra("matchId", mm.getId());
                    mIntent.setClass(getActivity(), ChangeMatchActivity.class);
                    startActivity(mIntent);
                    break;
                default:
                    break;
            }
        }
    };

}
