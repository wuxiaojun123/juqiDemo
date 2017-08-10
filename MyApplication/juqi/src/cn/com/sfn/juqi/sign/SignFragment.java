
package cn.com.sfn.juqi.sign;

import java.util.List;

import cn.com.sfn.juqi.adapter.MatchItemAdapter;
import cn.com.sfn.juqi.controller.MatchController;
import cn.com.sfn.juqi.model.MatchModel;
import cn.com.sfn.juqi.util.Config;
import cn.com.sfn.juqi.widgets.XListView;
import cn.com.sfn.juqi.widgets.XListView.IXListViewListener;

import com.example.juqi.R;
import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("HandlerLeak")
public class SignFragment extends Fragment implements IXListViewListener {
    private View signView;
    private Intent mIntent;
    private XListView matchListView;
    private ListAdapter listAdapter;
    private List<MatchModel> matches;
    private Button btn1, btn2, btn3, btn4;
    private TextView chooseBtn;
    private Handler myhandler;
    private MatchController matchController;
    private static int page = 1;
    private SlidePopWin slidePop;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);
        signView = inflater.inflate(R.layout.activity_sign, container, false);
        matchController = new MatchController();
        matchListView = (XListView) signView.findViewById(R.id.matchList);
        btn1 = (Button) signView.findViewById(R.id.btn1);
        btn2 = (Button) signView.findViewById(R.id.btn2);
        btn3 = (Button) signView.findViewById(R.id.btn3);
        btn4 = (Button) signView.findViewById(R.id.btn4);
        chooseBtn = (TextView) signView.findViewById(R.id.choose);

        matchListView.setPullLoadEnable(true);
        matchListView.setXListViewListener(this);
        myhandler = new Handler() {
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
                        Toast.makeText(getActivity(), "已到最后一条", Toast.LENGTH_SHORT)
                                .show();
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
                        Log.e("TYPE", Config.choose_type);
                        Toast.makeText(getActivity(), Config.choose_type,
                                Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        break;
                }
            }
        };
        btn1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
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
                        myhandler.sendMessage(msg);
                    }
                }.start();
            }
        });
        btn2.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
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
                        myhandler.sendMessage(msg);
                    }
                }.start();
            }
        });
        btn3.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
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
                        myhandler.sendMessage(msg);
                    }
                }.start();
            }
        });
        btn4.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
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
                        myhandler.sendMessage(msg);
                    }
                }.start();
            }
        });
        chooseBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                showSlide(v);
            }
        });
        SlidePopWin.mHandler = new Handler() {
            public void handleMessage(Message mesg) {
                switch (mesg.what) {
                    case 1:
                        Bundle bd = mesg.getData();
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
                                Message msg = new Message();
                                msg.what = 1;
                                myhandler.sendMessage(msg);
                            }
                        }.start();
                        break;
                }
            }
        };
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
        return signView;
    }

    private void initBottemBtn() {
        btn1.setBackgroundResource(R.drawable.signleftbtn);
        btn2.setBackgroundResource(R.drawable.signmiddlebtn);
        btn3.setBackgroundResource(R.drawable.signmiddlebtn);
        btn4.setBackgroundResource(R.drawable.signrightbtn);
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
                myhandler.sendMessage(msg);
            }
        }.start();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    // 弹出选择窗
    public void showSlide(View view) {
        slidePop = new SlidePopWin(getActivity());
        slidePop.showAtLocation(signView.findViewById(R.id.poppop),
                Gravity.LEFT, 0, 0);
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
                    myhandler.sendMessage(msg);
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
                        myhandler.sendMessage(msg);
                    } else {
                        Message msg = new Message();
                        msg.what = 2;
                        myhandler.sendMessage(msg);
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
