
package cn.com.sfn.juqi.sign;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.com.sfn.juqi.adapter.MatchItemAdapter;
import cn.com.sfn.juqi.controller.MatchController;
import cn.com.sfn.juqi.model.MatchModel;
import cn.com.sfn.juqi.util.Config;
import cn.com.sfn.juqi.util.ToastUtil;
import cn.com.sfn.juqi.widgets.XListView;
import cn.com.sfn.juqi.widgets.XListView.IXListViewListener;
import cn.com.wx.util.LogUtils;

import com.example.juqi.R;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.TextView;

@SuppressLint("HandlerLeak")
public class SignFragment extends Fragment implements IXListViewListener {
    private View signView;
    private Intent mIntent;
    @BindView(R.id.matchList)
    XListView matchListView;
    @BindView(R.id.btn1)
    Button btn1;
    @BindView(R.id.btn2)
    Button btn2;
    @BindView(R.id.btn3)
    Button btn3;
    @BindView(R.id.btn4)
    Button btn4;
    @BindView(R.id.choose)
    TextView chooseBtn;

    private MatchController matchController;
    private static int page = 1;
    private SlidePopWin slidePop;
    private ListAdapter listAdapter;
    private List<MatchModel> matches;
    private Context mContext;

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    if (matches.size() != 0) {
                        Config.pages = matches.get(0).getPage();
                    }
                    listAdapter = new MatchItemAdapter(getActivity(), matches);
                    matchListView.setAdapter(listAdapter);
                    break;
                case 2:
                    ToastUtil.show(mContext, "已到最后一条");
                    onLoad();
                    break;
                case 3:
                    listAdapter = new MatchItemAdapter(getActivity(), matches);
                    matchListView.setAdapter(listAdapter);
                    onLoad();
                    break;
                case 4:
                    listAdapter = new MatchItemAdapter(getActivity(), matches);
                    matchListView.setAdapter(listAdapter);
                    onLoad();
                    break;
                case 5:
                    LogUtils.e("TYPE" + Config.choose_type);
                    ToastUtil.show(mContext, Config.choose_type);
                    break;
                case 10:
                    Bundle bd = msg.getData();
                    Config.choose_type = bd.getString("type");
                    new Thread() {
                        @Override
                        public void run() {
                            try {
                                page = 1;
                                if (!(Config.choose_type.equals("地理位置")
                                        || Config.choose_type.equals("费用高低")
                                        || Config.choose_type.equals("时间先后") || Config.choose_type
                                        .equals("综合排序"))) {
                                    matches = matchController
                                            .getMatchByCondition2(Config.choose_type, 1);
                                } else if (Config.choose_type.equals("费用高低")) {
                                    matches = matchController
                                            .getMatchByCondition("1", 1);
                                } else if (Config.choose_type.equals("时间先后")) {
                                    matches = matchController
                                            .getMatchByCondition("4", 1);
                                } else if (Config.choose_type.equals("地理位置")) {
                                    matches = matchController
                                            .getMatchByCondition1("5",
                                                    Config.lon, Config.lat, 1);
                                } else if (Config.choose_type.equals("综合排序")) {
                                    matches = matchController
                                            .getMatchByCondition("6", 1);
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            mHandler.sendEmptyMessage(1);
                        }
                    }.start();
                    break;
            }
        }
    };

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        mContext = getActivity();
        signView = inflater.inflate(R.layout.activity_sign, container, false);
        matchController = new MatchController();
        ButterKnife.bind(this, signView);

        initView();
        return signView;
    }


    private void initView() {
        matchListView.setPullLoadEnable(true);
        matchListView.setXListViewListener(this);
        matchListView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1,
                                    int position, long arg3) {
                MatchModel mm = (MatchModel) arg0.getItemAtPosition(position);
                mIntent = new Intent();
                mIntent.putExtra("matchId", mm.getId());
                mIntent.setClass(getActivity(), MatchDetailActivity.class);
                startActivity(mIntent);
            }
        });
    }

    public void initSign() {
        new Thread() {
            public void run() {
                try {
                    matches = matchController.getMatch(1);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Message msg = new Message();
                msg.what = 1;
                mHandler.sendMessage(msg);
            }
        }.start();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @OnClick({R.id.btn1, R.id.btn2, R.id.btn3, R.id.btn4, R.id.choose})
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.btn1:
                btn1Click();
                break;
            case R.id.btn2:
                btn2Click();

                break;
            case R.id.btn3:
                btn3Click();
                break;
            case R.id.btn4:
                btn4Click();
                break;
            case R.id.choose:
                showSlide(view);
                break;
        }
    }

    private void btn4Click() {
        initBottemBtn();
        btn4.setBackgroundResource(R.drawable.signrightpressed);
        new Thread() {
            @Override
            public void run() {
                try {
                    page = 1;
                    matches = matchController.getMatch(1);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Message msg = new Message();
                msg.what = 1;
                mHandler.sendMessage(msg);
            }
        }.start();
    }

    private void btn3Click() {
        initBottemBtn();
        btn3.setBackgroundColor(getResources().getColor(R.color.middle_btn_pressed));
        new Thread() {
            @Override
            public void run() {
                try {
                    page = 1;
                    matches = matchController.getMatchPlusOneDay(
                            "%2B7 Days", 1);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Message msg = new Message();
                msg.what = 1;
                mHandler.sendMessage(msg);
            }
        }.start();
    }

    private void btn2Click() {
        initBottemBtn();
        btn2.setBackgroundColor(getResources().getColor(R.color.middle_btn_pressed));
        new Thread() {
            @Override
            public void run() {
                try {
                    page = 1;
                    matches = matchController.getMatchPlusOneDay(
                            "%2B2 Days", 1);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Message msg = new Message();
                msg.what = 1;
                mHandler.sendMessage(msg);
            }
        }.start();
    }

    private void btn1Click() {
        initBottemBtn();
        btn1.setBackgroundResource(R.drawable.signleftpressed);
        new Thread() {
            @Override
            public void run() {
                try {
                    page = 1;
                    matches = matchController.getMatchPlusOneDay(
                            "%2B1 Days", 1);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Message msg = new Message();
                msg.what = 1;
                mHandler.sendMessage(msg);
            }
        }.start();
    }

    // 弹出选择窗
    public void showSlide(View view) {
        slidePop = new SlidePopWin(getActivity(), mHandler);
        slidePop.showAtLocation(signView.findViewById(R.id.poppop),
                Gravity.LEFT, 0, 0);
    }

    private void initBottemBtn() {
        btn1.setBackgroundResource(R.drawable.signleftbtn);
        btn2.setBackgroundResource(R.drawable.signmiddlebtn);
        btn3.setBackgroundResource(R.drawable.signmiddlebtn);
        btn4.setBackgroundResource(R.drawable.signrightbtn);
    }

    private void onLoad() {
        matchListView.stopRefresh();
        matchListView.stopLoadMore();
        matchListView.setRefreshTime("刚刚");
    }

    @Override
    public void onRefresh() {
        new Thread() {
            @Override
            public void run() {
                try {
                    sleep(2000);
                    Message msg = new Message();
                    msg.what = 3;
                    mHandler.sendMessage(msg);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    @Override
    public void onLoadMore() {
        new Thread() {
            @Override
            public void run() {
                try {
                    sleep(2000);
                    page += 1;
                    if (page <= Config.pages) {
                        for (int i = 0; i < matchController.getMatch(page)
                                .size(); i++) {
                            matches.add(matchController.getMatch(page).get(i));
                        }
                        Message msg = new Message();
                        msg.what = 4;
                        mHandler.sendMessage(msg);
                    } else {
                        Message msg = new Message();
                        msg.what = 2;
                        mHandler.sendMessage(msg);
                    }

                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
}
