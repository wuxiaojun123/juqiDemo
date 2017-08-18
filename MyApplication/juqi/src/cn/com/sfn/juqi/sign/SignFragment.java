
package cn.com.sfn.juqi.sign;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.example.juqi.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.com.sfn.juqi.adapter.MatchItemAdapter;
import cn.com.sfn.juqi.controller.MatchController;
import cn.com.sfn.juqi.model.MatchModel;
import cn.com.sfn.juqi.rxbus.RxBus;
import cn.com.sfn.juqi.rxbus.rxtype.ChangeMatchRxbusType;
import cn.com.sfn.juqi.util.Config;
import cn.com.sfn.juqi.util.ToastUtil;
import cn.com.sfn.juqi.widgets.XListView;
import cn.com.sfn.juqi.widgets.XListView.IXListViewListener;
import cn.com.wx.util.BaseSubscriber;
import cn.com.wx.util.LogUtils;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

@SuppressLint("HandlerLeak")
public class SignFragment extends Fragment implements IXListViewListener {

    private final static String LIMIT0 = "%2B1 Days";
    private final static String LIMIT1 = "%2B2 Days";
    private final static String LIMIT2 = "%2B7 Days";
    private final static String LIMIT3 = null;

    @BindView(R.id.id_drawerlayout)
    DrawerLayout mDrawerlayout;
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

    @BindView(R.id.id_chaoyang)
    TextView id_chaoyang;
    @BindView(R.id.id_haidian)
    TextView id_haidian;
    @BindView(R.id.id_dongcheng)
    TextView id_dongcheng;
    @BindView(R.id.id_xicheng)
    TextView id_xicheng;
    @BindView(R.id.id_fentai)
    TextView id_fentai;
    @BindView(R.id.id_daxing)
    TextView id_daxing;
    @BindView(R.id.id_tongzhou)
    TextView id_tongzhou;
    @BindView(R.id.id_changping)
    TextView id_changping;

    @BindView(R.id.id_area)
    TextView id_area;
    @BindView(R.id.id_fee)
    TextView id_fee;
    @BindView(R.id.id_time)
    TextView id_time;
    @BindView(R.id.id_total)
    TextView id_total;

    @BindView(R.id.id_region)
    TextView id_region; // 活动地点
    @BindView(R.id.id_sort)
    TextView id_sort; // 排序方式

    private MatchController matchController = new MatchController();
    private int page = 1; // 当前页
    private int pages; // 总页数
    private int flag = -1;
    private String chooseRegion; // 活动地点
    private String chooseSort; // 排序方式

    private SlidePopWin slidePop;
    private MatchItemAdapter listAdapter;
    private Context mContext;


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        mContext = getActivity();
        View signView = inflater.inflate(R.layout.activity_sign, container, false);
        ButterKnife.bind(this, signView);

        initView();
        drawerListener();
        btn1.performClick();

        initRxbus();

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
                Intent mIntent = new Intent();
                mIntent.putExtra("matchId", mm.getId());
                mIntent.setClass(getActivity(), MatchDetailActivity.class);
                startActivity(mIntent);
            }
        });
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private void initRxbus(){
        RxBus.getDefault().toObservable(ChangeMatchRxbusType.class).subscribe(new Action1<ChangeMatchRxbusType>() {
            @Override
            public void call(ChangeMatchRxbusType changeMatchRxbusType) {
                // 刷新界面
                if(changeMatchRxbusType.isRefresh){
                    btnClick();
                }
            }
        });
    }


    @OnClick({R.id.btn1, R.id.btn2, R.id.btn3, R.id.btn4, R.id.choose,
            R.id.id_chaoyang, R.id.id_haidian, R.id.id_dongcheng, R.id.id_xicheng,
            R.id.id_fentai, R.id.id_daxing, R.id.id_tongzhou, R.id.id_changping,
            R.id.id_area, R.id.id_fee, R.id.id_time, R.id.id_total})
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
                // 点击不限，需要清空筛选条件
                id_region.setText(null);
                id_sort.setText(null);
                btn4Click();

                break;
            case R.id.choose:
                //showSlide(view);
                mDrawerlayout.openDrawer(Gravity.LEFT);
                break;
            case R.id.id_chaoyang:
                id_region.setText("朝阳区");
                break;
            case R.id.id_haidian:
                id_region.setText("海淀区");
                break;
            case R.id.id_dongcheng:
                id_region.setText("东城区");
                break;
            case R.id.id_xicheng:
                id_region.setText("西城区");
                break;
            case R.id.id_fentai:
                id_region.setText("丰台区");
                break;
            case R.id.id_daxing:
                id_region.setText("大兴区");
                break;
            case R.id.id_tongzhou:
                id_region.setText("通州区");
                break;
            case R.id.id_changping:
                id_region.setText("昌平区");
                break;
            case R.id.id_area:
                id_sort.setText("地理位置");
                break;
            case R.id.id_fee:
                id_sort.setText("费用高低");
                break;
            case R.id.id_time:
                id_sort.setText("时间先后");
                break;
            case R.id.id_total:
                id_sort.setText("综合排序");
                break;
        }
    }


    private void drawerListener() {
        mDrawerlayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(View drawerView) {

            }

            @Override
            public void onDrawerClosed(View drawerView) {
                // 关闭了
                String regionStr = id_region.getText().toString();
                String sortStr = id_sort.getText().toString();
                if (!regionStr.equals(chooseRegion) || !sortStr.equals(sortStr)) {
                    condition();
                }
                chooseRegion = regionStr;
                chooseSort = sortStr;
                LogUtils.e("选中的是：" + chooseRegion + "----" + chooseSort);
            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });
    }

    private void condition() {
        Observable.create(new Observable.OnSubscribe<List<MatchModel>>() {

            @Override
            public void call(Subscriber<? super List<MatchModel>> subscriber) {
                try {
                    page = 1;
                    String limit = judgeLimitByFlag(flag);

                    List<MatchModel> matches = null;
                    if (!(chooseSort.equals("地理位置")
                            || chooseSort.equals("费用高低")
                            || chooseSort.equals("时间先后") || chooseSort.equals("综合排序"))) {
                        matches = matchController.getMatchByCondition2(limit, chooseRegion, 1);

                    } else if (chooseSort.equals("费用高低")) {
                        matches = matchController.getMatchByCondition(limit, "1", 1);
                    } else if (chooseSort.equals("时间先后")) {
                        matches = matchController.getMatchByCondition(limit, "4", 1);
                    } else if (chooseSort.equals("地理位置")) {
                        matches = matchController.getMatchByCondition1(limit, "5", Config.lon, Config.lat, 1);
                    } else if (chooseSort.equals("综合排序")) {
                        matches = matchController.getMatchByCondition(limit, "6", 1);
                    }
                    subscriber.onNext(matches);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<List<MatchModel>>() {
                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        onLoadFinish();
                    }

                    @Override
                    public void onNext(List<MatchModel> list) {
                        if (list.size() != 0) {
                            pages = list.get(0).getPage();
                            if (page < pages) {
                                page += 1;
                            }
                        }
                        if (listAdapter == null) {
                            listAdapter = new MatchItemAdapter(getActivity(), list);
                            matchListView.setAdapter(listAdapter);
                        } else {
                            listAdapter.setMatches(list);
                            listAdapter.notifyDataSetChanged();
                        }
                        onLoadFinish();
                    }
                });

    }

    private void btn4Click() {
        if (flag == 3) {
            return;
        }
        flag = 3;
        initBottemBtn();
        btn4.setBackgroundResource(R.drawable.signrightpressed);
        getData(LIMIT3, false);
    }

    private void btn3Click() {
        if (flag == 2) {
            return;
        }
        flag = 2;
        initBottemBtn();
        btn3.setBackgroundColor(getResources().getColor(R.color.middle_btn_pressed));
        getData(LIMIT2, false);
    }

    private void btn2Click() {
        if (flag == 1) {
            return;
        }
        flag = 1;
        initBottemBtn();
        btn2.setBackgroundColor(getResources().getColor(R.color.middle_btn_pressed));
        getData(LIMIT1, false);
    }

    private void btn1Click() {
        if (flag == 0) {
            return;
        }
        flag = 0;
        initBottemBtn();
        btn1.setBackgroundResource(R.drawable.signleftpressed);
        getData(LIMIT0, false);
    }

    private void btnClick(){
        switch (flag){
            case 0:
                btn1Click();
                break;
            case 1:
                btn2Click();
                break;
            case 2:
                btn3Click();
                break;
            case 3:
                btn4Click();
                break;
        }
    }

    /***
     * 请求获取首页数据
     *
     * @param limit
     */
    private void getData(final String limit, final boolean isLoadmore) {
        Observable.create(new Observable.OnSubscribe<List<MatchModel>>() {

            @Override
            public void call(Subscriber<? super List<MatchModel>> subscriber) {
                try {
                    if (!isLoadmore) { // 如果不是加载更多
                        page = 1;
                    }
                    List<MatchModel> matchModelList = null;
                    if (TextUtils.isEmpty(limit)) {
                        matchModelList = matchController.getMatch(page);
                    } else {
                        matchModelList = matchController.getMatchPlusOneDay(limit, page);
                    }
                    subscriber.onNext(matchModelList);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<List<MatchModel>>() {
                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        onLoadFinish();
                    }

                    @Override
                    public void onNext(List<MatchModel> list) {
                        if (list.size() != 0) {
                            pages = list.get(0).getPage();
                            if (page < pages) {
                                page += 1;
                            }
                        }
                        if (isLoadmore) {
                            if (listAdapter != null)
                                listAdapter.addMatcher(list);
                        } else {
                            listAdapter = new MatchItemAdapter(getActivity(), list);
                            matchListView.setAdapter(listAdapter);
                        }
                        onLoadFinish();
                    }
                });
    }

    private void initBottemBtn() {
        btn1.setBackgroundResource(R.drawable.signleftbtn);
        btn2.setBackgroundResource(R.drawable.signmiddlebtn);
        btn3.setBackgroundResource(R.drawable.signmiddlebtn);
        btn4.setBackgroundResource(R.drawable.signrightbtn);
    }

    private void onLoadFinish() {
        matchListView.stopRefresh();
        matchListView.stopLoadMore();
        matchListView.setRefreshTime("刚刚");
    }

    @Override
    public void onRefresh() {
        // 下拉刷新数据
        switch (flag) {
            case 0:
                getData(LIMIT0, false);
                break;
            case 1:
                getData(LIMIT1, false);
                break;
            case 2:
                getData(LIMIT2, false);
                break;
            case 3:
                getData(LIMIT3, false);
                break;
        }
    }

    /**
     * 根据flag判断limit
     *
     * @param flag
     * @return
     */
    private String judgeLimitByFlag(int flag) {
        String limit = null;
        switch (flag) {
            case 0:
                limit = LIMIT0;
                break;
            case 1:
                limit = LIMIT1;
                break;
            case 2:
                limit = LIMIT2;
                break;
            case 3:
                limit = LIMIT3;
                break;
        }
        return limit;
    }

    @Override
    public void onLoadMore() {
        if (page <= pages) {
            ToastUtil.show(mContext, "已到最后一条");
            onLoadFinish();
            return;
        }
        switch (flag) {
            case 0:
                getData(LIMIT0, true);
                break;
            case 1:
                getData(LIMIT1, true);
                break;
            case 2:
                getData(LIMIT2, true);
                break;
            case 3:
                getData(LIMIT3, true);
                break;
        }
        /*page += 1;
        if (page <= pages) {
            for (int i = 0; i < matchController.getMatch(page).size(); i++) {
                matches.add(matchController.getMatch(page).get(i));
            }
            mHandler.sendEmptyMessage(4);
        } else {
            mHandler.sendEmptyMessage(2);
        }*/
    }

    public void closeDrawer() {
        if (mDrawerlayout.isDrawerOpen(Gravity.RIGHT)) {
            mDrawerlayout.closeDrawer(Gravity.RIGHT);
        }
    }


}
