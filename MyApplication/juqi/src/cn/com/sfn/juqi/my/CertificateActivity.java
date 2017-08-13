/**
 * @author wangwb
 * @function 我的凭证界面显示及功能
 */
package cn.com.sfn.juqi.my;

import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import cn.com.sfn.juqi.adapter.CertificateItemAdapter;
import cn.com.sfn.juqi.adapter.ListItemClickHelp;
import cn.com.sfn.juqi.controller.MatchController;
import cn.com.sfn.juqi.model.MatchModel;
import cn.com.sfn.juqi.sign.MatchDetailActivity;
import cn.com.sfn.juqi.util.Config;

import com.example.juqi.R;

/****
 * 球局凭证
 */
@SuppressLint("HandlerLeak")
public class CertificateActivity extends Activity {
    private TextView back;
    private Intent mIntent;
    private ListView matchListView;
    private ListAdapter listAdapter;
    private List<MatchModel> matches;
    private MatchController matchController;
    private LinearLayout not;

    private Handler myhandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    if (matches.size() != 0) {
                        not.setVisibility(View.INVISIBLE);
                    }
                    // listview内部按钮功能实现
                    listAdapter = new CertificateItemAdapter(
                            CertificateActivity.this, matches, lich);
                    matchListView.setAdapter(listAdapter);
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_certificate);
        matchController = new MatchController();
        matchListView = (ListView) findViewById(R.id.certificateList);
        not = (LinearLayout) findViewById(R.id.rl);
        // 返回按钮
        back = (TextView) findViewById(R.id.back_to_my);
        back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        initList();

    }

    public void initList() {
        new Thread() {
            public void run() {
                matches = matchController
                        .getCertificateList(Config.login_userid);
                myhandler.sendEmptyMessage(1);
            }
        }.start();
    }

    public void showQRCode(String attendId) {
        QRCodePopWin QRPopWin = new QRCodePopWin(this, attendId);
        QRPopWin.showAtLocation(findViewById(R.id.layout_certificate),
                Gravity.CENTER, 0, 0);
    }

    public ListItemClickHelp lich = new ListItemClickHelp() {
        @Override
        public void onClick(View item, View widget, int position, int which) {
            MatchModel mm = matches.get(position);
            switch (which) {
                case R.id.qr_btn:
                    showQRCode(mm.getAttendID());
                    break;
                case R.id.certificate_match_btn:
                    mIntent = new Intent();
                    mIntent.putExtra("matchId", mm.getId());
                    mIntent.setClass(CertificateActivity.this,
                            MatchDetailActivity.class);
                    startActivity(mIntent);
                    break;
                default:
                    break;
            }
        }
    };

}