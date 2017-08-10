
package cn.com.sfn.example.juqi;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import cn.com.sfn.example.juqi.accomplishments.MyAccomplishmentsBean;
import cn.com.sfn.example.juqi.accomplishments.MyAccomplishmentsCanYuAdapter;
import cn.com.sfn.example.juqi.accomplishments.MyAccomplishmentsZuZhiAdapter;
import cn.com.sfn.example.juqi.accomplishments.MyAccoumplishmentsControl;
import cn.com.sfn.juqi.util.ToastUtil;

import com.example.juqi.R;

import java.util.ArrayList;
import java.util.List;

/***
 * 我的成就
 */
public class MyAccomplishmentsActivity extends Activity implements OnClickListener {

    private TextView id_tv_back;
    private Button btn_not_obtain;// 未获得
    private Button btn_obtain;// 已获得
    private Button canyu_btn;// 参与成就
    private Button zuzhi_btn;// 组织成就
    private ListView id_listview;
    // 选中和为选中的颜色
    private int selectorColor;
    private int defaultColor;
    // 获取网络数据业务类
    private MyAccoumplishmentsControl control;
    // 当前是否为未获得状态
    private boolean isNotObtain = true;
    // 未获得参与成就
    private List<MyAccomplishmentsBean> notObtainCanYuList;
    // 未获得组织成就
    private List<MyAccomplishmentsBean> notObtainZuZhiList;
    // 已获得参与成就
    private List<MyAccomplishmentsBean> obtainCanYuList;
    // 已获得组织成就
    private List<MyAccomplishmentsBean> obtainZuZhiList;
    // 判断是否请求过网络
    private boolean notObtainCanYuFlag;
    private boolean notObtainZuZhiFlag;
    private boolean obtainCanYuFlag;
    private boolean obtainZuZhiFlag;
    // 未获得参与成就
    private MyAccomplishmentsCanYuAdapter mNotObtainCanYuAdapter;
    // 已获得参与成就
    private MyAccomplishmentsCanYuAdapter mObtainCanYuAdapter;
    // 未获得组织成就
    private MyAccomplishmentsZuZhiAdapter mNotObtainZuZhiAdapter;
    // 已获得组织成就
    private MyAccomplishmentsZuZhiAdapter mObtainZuZhiAdapter;

    private MyHandler mHandler;
    private Context mContext;

    public class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            int arg1 = msg.arg1;
            if (arg1 == 0) {
                // 成功
                String json = (String) msg.obj;
                List<MyAccomplishmentsBean> list = control.gsonParseJson(json);
                int flag = msg.arg2;
                if (flag == 1) {
                    // 未获得组织成就
                    if (list != null && !list.isEmpty()) {
                        notObtainZuZhiList = list;
                    } else {
                        // 没有数据,需要刷新适配器
                        notObtainZuZhiList = new ArrayList<MyAccomplishmentsBean>();
                    }
                    mNotObtainZuZhiAdapter = new MyAccomplishmentsZuZhiAdapter(mContext, notObtainZuZhiList);
                    id_listview.setAdapter(mNotObtainZuZhiAdapter);
                } else if (flag == 2) {
                    // 未获得参与成就
                    if (list != null && !list.isEmpty()) {
                        notObtainCanYuList = list;
                    } else {
                        notObtainCanYuList = new ArrayList<MyAccomplishmentsBean>();
                    }
                    mNotObtainCanYuAdapter = new MyAccomplishmentsCanYuAdapter(mContext, notObtainCanYuList);
                    id_listview.setAdapter(mNotObtainCanYuAdapter);
                } else if (flag == 3) {
                    // 已获得组织成就
                    if (list != null && !list.isEmpty()) {
                        obtainZuZhiList = list;
                    } else {
                        obtainZuZhiList = new ArrayList<MyAccomplishmentsBean>();
                    }
                    mObtainZuZhiAdapter = new MyAccomplishmentsZuZhiAdapter(mContext, obtainZuZhiList);
                    id_listview.setAdapter(mObtainZuZhiAdapter);
                } else if (flag == 4) {
                    // 已获得参与成就
                    if (list != null && !list.isEmpty()) {
                        obtainCanYuList = list;
                    } else {
                        obtainCanYuList = new ArrayList<MyAccomplishmentsBean>();
                    }
                    mObtainCanYuAdapter = new MyAccomplishmentsCanYuAdapter(mContext, obtainCanYuList);
                    id_listview.setAdapter(mObtainCanYuAdapter);
                }
            } else if (arg1 == -1) {
                // 超时
                ToastUtil.show(getApplicationContext(), "连接超时");
            } else if (arg1 == -2) {
                // 解析json出错
                ToastUtil.show(getApplicationContext(), "数据异常");
            }

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_my_accomplishments);
        mContext = getApplicationContext();
        initView();
        initEvent();
        initData();
    }

    private void initData() {
        mHandler = new MyHandler();
        control = new MyAccoumplishmentsControl(mHandler);
        btn_obtain.performClick();
    }

    private void initEvent() {
        id_tv_back.setOnClickListener(this);
        btn_not_obtain.setOnClickListener(this);
        btn_obtain.setOnClickListener(this);
        canyu_btn.setOnClickListener(this);
        zuzhi_btn.setOnClickListener(this);

    }

    private void initView() {
        id_tv_back = (TextView) findViewById(R.id.id_tv_back);
        btn_not_obtain = (Button) findViewById(R.id.btn_not_obtain);
        btn_obtain = (Button) findViewById(R.id.btn_obtain);
        canyu_btn = (Button) findViewById(R.id.canyu_btn);
        zuzhi_btn = (Button) findViewById(R.id.zuzhi_btn);
        id_listview = (ListView) findViewById(R.id.id_listview);

        selectorColor = getResources().getColor(R.color.middle_btn_pressed);
        defaultColor = getResources().getColor(R.color.middle_btn_unpressed);
    }

    @Override
    public void onClick(View arg0) {
        int id = arg0.getId();
        switch (id) {
            case R.id.id_tv_back:
                finish();
                break;
            case R.id.btn_not_obtain:
                // 未获得
                changeBtnBackground(id);
                if (notObtainCanYuList != null) {
                    id_listview.setAdapter(mNotObtainCanYuAdapter);
                } else {
                    if (!notObtainCanYuFlag) {
                        control.getJsonStr(MyAccoumplishmentsControl.NOT_OBTAIN_CANYU_PATH);
                        notObtainCanYuFlag = true;
                    }
                }
                break;
            case R.id.btn_obtain:
                // 已获得
                changeBtnBackground(id);
                if (obtainCanYuList != null) {
                    System.out.println("进入已获得按钮");
                    id_listview.setAdapter(mObtainCanYuAdapter);
                } else {
                    if (!obtainCanYuFlag) {
                        control.getJsonStr(MyAccoumplishmentsControl.OBTAIN_CANYU_PATH);
                        obtainCanYuFlag = true;
                    }
                }
                break;
            case R.id.canyu_btn:
                changeChengJiuBackground(id);
                if (isNotObtain) {
                    // 未获得参与成就
                    if (notObtainCanYuList != null) {
                        id_listview.setAdapter(mNotObtainCanYuAdapter);
                    } else if (!notObtainCanYuFlag) {
                        control.getJsonStr(MyAccoumplishmentsControl.NOT_OBTAIN_CANYU_PATH);
                        notObtainCanYuFlag = true;
                    }
                } else {
                    // 已获得参与成就
                    if (obtainCanYuList != null) {
                        id_listview.setAdapter(mObtainCanYuAdapter);
                    } else if (!obtainCanYuFlag) {
                        control.getJsonStr(MyAccoumplishmentsControl.OBTAIN_CANYU_PATH);
                        obtainCanYuFlag = true;
                    }
                }

                break;
            case R.id.zuzhi_btn:
                changeChengJiuBackground(id);
                if (isNotObtain) {
                    // 未获得组织成就
                    if (notObtainZuZhiList != null) {
                        id_listview.setAdapter(mNotObtainZuZhiAdapter);
                    } else if (!notObtainZuZhiFlag) {
                        control.getJsonStr(MyAccoumplishmentsControl.NOT_OBTAIN_ZUZHI_PATH);
                        notObtainZuZhiFlag = true;
                    }
                } else {
                    // 已获得组织成就
                    if (obtainZuZhiList != null) {
                        id_listview.setAdapter(mObtainZuZhiAdapter);
                    } else if (!obtainZuZhiFlag) {
                        control.getJsonStr(MyAccoumplishmentsControl.OBTAIN_ZUZHI_PATH);
                        obtainZuZhiFlag = true;
                    }
                }

                break;
        }
    }

    /***
     * 改变参与成就和组织成就的背景
     * 
     * @param id
     */
    private void changeChengJiuBackground(int id) {
        if (id == R.id.canyu_btn) {
            canyu_btn.setBackgroundResource(R.drawable.img_bg_chengjiu_btn);
            zuzhi_btn.setBackgroundResource(R.drawable.img_bg_not_obtain);
        } else {
            canyu_btn.setBackgroundResource(R.drawable.img_bg_not_obtain);
            zuzhi_btn.setBackgroundResource(R.drawable.img_bg_chengjiu_btn);
        }
    }

    /***
     * 改变已获得和未获得的背景
     * 
     * @param id
     */
    private void changeBtnBackground(int id) {
        if (id == R.id.btn_not_obtain) {
            btn_not_obtain.setBackgroundColor(selectorColor);
            btn_obtain.setBackgroundColor(defaultColor);
            isNotObtain = true;
        } else if (id == R.id.btn_obtain) {
            btn_obtain.setBackgroundColor(selectorColor);
            btn_not_obtain.setBackgroundColor(defaultColor);
            isNotObtain = false;
        }
        changeChengJiuBackground(R.id.canyu_btn);
    }

    @Override
    protected void onDestroy() {
        if (control != null) {
            control.shutDown();
        }
        super.onDestroy();
    }

}
