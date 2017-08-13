package cn.com.sfn.juqi.my;

import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import cn.com.sfn.juqi.adapter.FriendItemAdapter;
import cn.com.sfn.juqi.controller.MatchController;
import cn.com.sfn.juqi.controller.UserController;
import cn.com.sfn.juqi.model.UserModel;
import cn.com.sfn.juqi.util.Config;

import com.example.juqi.R;


/***
 * 我的局友
 */
@SuppressLint("HandlerLeak")
public class FriendActivity extends Activity {

    private ListView friendListView;
    private ListAdapter listAdapter;
    private List<UserModel> friends;
    private Intent mIntent;
    private TextView back;
    private TextView search;
    private Button cancel_ok;
    private Button cancel;
    private LinearLayout not;
    private UserController userController;
    private MatchController matchController;
    private PopupWindow cancelPop;
    private Context context;
    public static FriendActivity instance = null;

    Handler myhandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    if (friends != null) {
                        not.setVisibility(View.INVISIBLE);
                    }
                    // listview内部按钮功能实现
                    listAdapter = new FriendItemAdapter(FriendActivity.this,
                            friends);
                    friendListView.setAdapter(listAdapter);
                    friendListView.setOnItemLongClickListener(new OnItemLongClickListener() {
                        @Override
                        public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int position,
                                                       long arg3) {

                            UserModel fm = (UserModel) arg0
                                    .getItemAtPosition(position);
                            openDialog(fm.getUserId());

                            return true;
                        }

                    });
                    friendListView
                            .setOnItemClickListener(new OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> arg0,
                                                        View arg1, int position, long arg3) {
                                    UserModel fm = (UserModel) arg0
                                            .getItemAtPosition(position);
                                    mIntent = new Intent();
                                    mIntent.putExtra("id", fm.getUserId());
                                    mIntent.setClass(FriendActivity.this,
                                            FriendDetailActivity.class);
                                    startActivity(mIntent);
                                }
                            });
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
        setContentView(R.layout.activity_friend);
        userController = new UserController();
        matchController = new MatchController();
        friendListView = (ListView) findViewById(R.id.friendList);
        back = (TextView) findViewById(R.id.back_to_my);
        search = (TextView) findViewById(R.id.searchfriends);
        not = (LinearLayout) findViewById(R.id.rl);
        instance = this;
        initList();

        back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        search.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mIntent = new Intent(FriendActivity.this,
                        SearchFriendActivity.class);
                startActivity(mIntent);
            }
        });
    }

    public void initList() {
        new Thread() {
            public void run() {
                try {
                    friends = userController.getFriendList();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Message msg = new Message();
                msg.what = 1;
                myhandler.sendMessage(msg);
            }
        }.start();
    }

    public void reload() {
        Intent intent = getIntent();
        overridePendingTransition(0, 0);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        finish();
        overridePendingTransition(0, 0);
        startActivity(intent);
    }

    //在此方法中创建dialog
    private void openDialog(final String userid) {
        AlertDialog b = new AlertDialog.Builder(this, AlertDialog.THEME_HOLO_DARK)//THEME_HOLO_LIGHT
                .setTitle("提示")
                .setMessage("确定要取消关注该局友吗?")
                .setPositiveButton("确认",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // TODO Auto-generated method stub
                                int rs = matchController.cancelFriend(userid);

                                if (rs == Config.CancelFriendSuccess) {
                                    Toast.makeText(FriendActivity.this, "取消关注成功",
                                            Toast.LENGTH_LONG).show();
                                    reload();

                                } else if (rs == Config.CancelFriendFailed) {
                                    Toast.makeText(FriendActivity.this, "取消关注失败",
                                            Toast.LENGTH_LONG).show();

                                }
                            }
                        })
                .setNegativeButton("取消",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //如果用户点击取消退出，则对话框消失
                                dialog.dismiss();
                            }
                        })
                .show();
        //b.getWindow().setBackgroundDrawableResource(R.drawable.tips_box);//设置dialog背景,要在show()之后否则会报错
        b.getWindow().setLayout(600, 350);//设置Dialog大小

    }


}