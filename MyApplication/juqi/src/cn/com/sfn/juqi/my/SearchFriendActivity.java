package cn.com.sfn.juqi.my;

import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import cn.com.sfn.juqi.adapter.FriendItemAdapter;
import cn.com.sfn.juqi.controller.MatchController;
import cn.com.sfn.juqi.controller.UserController;
import cn.com.sfn.juqi.model.UserModel;
import cn.com.sfn.juqi.sign.MatchDetailActivity;
import cn.com.sfn.juqi.util.Config;

import com.example.juqi.R;

@SuppressLint("HandlerLeak")
public class SearchFriendActivity extends Activity {

	private ListView friendListView;
	private ListAdapter listAdapter;
	private List<UserModel> friends;
	private Intent mIntent;
	private TextView back;
	private TextView search;
	private EditText searchcontent;
	private LinearLayout not;
	private Handler myhandler;
	private UserController userController;
	private MatchController matchController;
	private String keyword;
	public static SearchFriendActivity instance = null; 
	
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK)) {								
			finish();
			FriendActivity.instance.finish();//结束FriendActivity
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_searchfriend);
		userController = new UserController();
		matchController=new MatchController();
		friendListView = (ListView) findViewById(R.id.friendList);		
		search = (TextView) findViewById(R.id.search);	
		not = (LinearLayout) findViewById(R.id.rl);
		back = (TextView) findViewById(R.id.back_to_my);
		searchcontent=(EditText) findViewById(R.id.searchcontent);
		
		myhandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case 1:
					if (friends != null) {
						not.setVisibility(View.INVISIBLE);
					}
					// listview内部按钮功能实现
					listAdapter = new FriendItemAdapter(SearchFriendActivity.this,
							friends);
					friendListView.setAdapter(listAdapter);
					friendListView
					.setOnItemClickListener(new OnItemClickListener() {
						@Override
						public void onItemClick(AdapterView<?> arg0,
								View arg1, int position, long arg3) {
							UserModel fm = (UserModel) arg0
									.getItemAtPosition(position);
							mIntent = new Intent();
							mIntent.putExtra("id", fm.getUserId());
							mIntent.setClass(SearchFriendActivity.this,
									FriendDetailActivity.class);
							mIntent.putExtra("ACTIVITY_NAME_KEY","SearchFriendActivity");
							startActivity(mIntent);
						}
					});
					break;
				}
			}
		};
		
		
		search.setOnClickListener(new OnClickListener() {	
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				new Thread() {
					public void run() {
						try {
							keyword=searchcontent.getText().toString().trim(); 
							Log.e("keyword",keyword);
							friends = matchController.searchFriend(keyword);//根据关键字获取UserModel类型的friends
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
		back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
				FriendActivity.instance.finish();//结束FriendActivity
			}
		});
		
		

	
		
	}

	
	
}