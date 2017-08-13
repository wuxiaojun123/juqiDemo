/**
 * @author wangwb
 * 成就列表页面
 */

package cn.com.sfn.juqi.achieve;

import java.util.List;

import cn.com.sfn.example.juqi.MyAccomplishmentsActivity;
import cn.com.sfn.juqi.adapter.AchieveItemAdapter;
import cn.com.sfn.juqi.controller.AchieveController;
import cn.com.sfn.juqi.model.AchieveModel;

import com.example.juqi.R;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class AchieveFragment extends Fragment implements OnClickListener {

    private ListView achieveListView;
    private AchieveItemAdapter achieveItemAdapter;
    private List<AchieveModel> achieves;
    private Button canyu, zuzhi, myself_btn;
    private TextView title;
    private View achieveView;
    private AchieveController achieveController;

    private Handler myhandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    title.setText("参与成就");
                    achieveItemAdapter = new AchieveItemAdapter(getActivity(),
                            achieves);
                    achieveListView.setAdapter(achieveItemAdapter);
                    break;
                case 2:
                    title.setText("组织成就");
                    achieveItemAdapter = new AchieveItemAdapter(getActivity(),
                            achieves);
                    achieveListView.setAdapter(achieveItemAdapter);
                    break;
                default:
                    break;
            }
        }
    };

    @SuppressLint("HandlerLeak")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        achieveView = inflater.inflate(R.layout.activity_achieve, container,
                false);
        achieveController = new AchieveController();
        findViewById();
        initAchieve();
        zuzhi.setOnClickListener(this);
        canyu.setOnClickListener(this);
        myself_btn.setOnClickListener(this);

        return achieveView;
    }

    private void findViewById() {
        achieveListView = (ListView) achieveView.findViewById(R.id.achieveList);
        canyu = (Button) achieveView.findViewById(R.id.canyu_btn);
        zuzhi = (Button) achieveView.findViewById(R.id.zuzhi_btn);
        myself_btn = (Button) achieveView.findViewById(R.id.myself_btn);
        title = (TextView) achieveView.findViewById(R.id.ac_title);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public void initAchieve() {
        new Thread() {
            public void run() {
                try {
                    achieves = achieveController.getJoinAchieveList("0");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                myhandler.sendEmptyMessage(1);
            }
        }.start();
    }

    public void releaseAchieve() {
        new Thread() {
            public void run() {
                try {
                    achieves = achieveController.getReleaseAchieveList("1");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                myhandler.sendEmptyMessage(2);
            }
        }.start();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.zuzhi_btn:
                releaseAchieve();
                break;
            case R.id.canyu_btn:
                initAchieve();
                break;
            case R.id.myself_btn:
                startActivity(new Intent(getActivity(), MyAccomplishmentsActivity.class));
                break;
            default:
                break;
        }
    }
}
