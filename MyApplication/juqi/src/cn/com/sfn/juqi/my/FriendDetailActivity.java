package cn.com.sfn.juqi.my;

import java.util.List;

import cn.com.sfn.juqi.adapter.MatchItemAdapter;
import cn.com.sfn.juqi.controller.MatchController;
import cn.com.sfn.juqi.controller.UserController;
import cn.com.sfn.juqi.model.FriendModel;
import cn.com.sfn.juqi.model.MatchModel;
import cn.com.sfn.juqi.sign.MatchDetailActivity;
import cn.com.sfn.juqi.util.Config;
import cn.com.sfn.juqi.widgets.CircleImageView;
import cn.com.sfn.juqi.widgets.RoundProgressBar;

import com.example.juqi.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

@SuppressLint("HandlerLeak")
public class FriendDetailActivity extends Activity {
	private Intent mIntent;
	private Button addButton;
	private TextView username, username2, back, chengwei;
	private TextView attend, release, friendnum, role, ageTv, sexTv, signTv;
	private ListView matchListView;
	private ListAdapter listAdapter;
	private String id;
	private String activityname;
	private Handler myhandler;
	private UserController userController;
	private FriendModel friend;
	private List<MatchModel> matches;
	private RoundProgressBar defenseBar, offenseBar, zongheBar;
	private CircleImageView avatar;
	private MatchController matchController=new MatchController();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_friend_detail);
		
		userController = new UserController();
		mIntent = getIntent();
		id = mIntent.getStringExtra("id");//userid
		//activityname = mIntent.getStringExtra("ACTIVITY_NAME_KEY");
		
		findViewById();
		initDetail();
		myhandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case 1:
					initView();
					break;
				case 2:
					Toast.makeText(FriendDetailActivity.this, "网络异常",
							Toast.LENGTH_SHORT).show();
					break;
				default:
					break;
				}
			}
		};

	}

	private void initView() {
		matches = friend.getReleaseList();
		listAdapter = new MatchItemAdapter(this, matches);
		matchListView.setAdapter(listAdapter);
		@SuppressWarnings("deprecation")
		Drawable drawable = new BitmapDrawable(friend.getUserAvatar());// 转换成drawable
		avatar.setImageDrawable(drawable);
		username.setText(friend.getNickName());
		username2.setText(friend.getNickName());
		chengwei.setText(friend.getStandard());
		offenseBar.setProgress(Integer.valueOf(friend.getOffense()));
		defenseBar.setProgress(Integer.valueOf(friend.getDefense()));
		zongheBar.setProgress(Integer.valueOf(friend.getComprehensive()));
		attend.setText("参与成就:" + friend.getJoinEntire());
		release.setText("组织成就:" + friend.getReleaseEntire());
		friendnum.setText("关注球友:" + friend.getFriendNum());
		signTv.setText(friend.getSignature());
		if (friend.getPosition().equals("")) {
			role.setText("无");
		} else {
			role.setText(friend.getPosition());
		}
		if (friend.getAge().equals("0")) {
			ageTv.setText("无");
		} else {
			ageTv.setText(friend.getAge());
		}
		if (friend.getUserSex().equals("")) {
			sexTv.setText("?");
		} else if (friend.getUserSex().equals("1")) {
			sexTv.setText(R.string.male);
		} else {
			sexTv.setText(R.string.female);
		}
		if (friend.getFriendship().equals("0")) {
			addButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					int rs = userController.addFriend(id);
					if (rs == Config.AddFriendSuccess) {
						Toast.makeText(FriendDetailActivity.this, "添加成功",
								Toast.LENGTH_SHORT).show();
						finish();
					/*	if(mIntent.getStringExtra("ACTIVITY_NAME_KEY")!=null){
							SearchFriendActivity.instance.finish();//要不要？？？
							reload();
						}else{
							reload();
						}*/
						
						
					} else if (rs == -1) {
						Toast.makeText(FriendDetailActivity.this, "网络异常",
								Toast.LENGTH_LONG).show();
					} else {
						Toast.makeText(FriendDetailActivity.this, "添加失败",
								Toast.LENGTH_SHORT).show();
					}
				

				}
			});
		} else {			
			addButton.setText("取消关注");
			addButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					int rs = matchController.cancelFriend(id);
	            	   
	  				if (rs == Config.CancelFriendSuccess) {
	  					Toast.makeText(FriendDetailActivity.this, "取消关注成功",
	  							Toast.LENGTH_LONG).show();
	  					finish();				
	  					//FriendActivity.instance.finish();
	  				/*	if(mIntent.getStringExtra("ACTIVITY_NAME_KEY")!=null){
							SearchFriendActivity.instance.finish();//要不要？？？
							reload();
						}else{
							reload();
						}*/
						
	  					
	  					
	  				} else if (rs == Config.CancelFriendFailed) {
	  					Toast.makeText(FriendDetailActivity.this, "取消关注失败",
	  							Toast.LENGTH_LONG).show();
	  					
	  				} 

				}
			});		
		}
		back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		matchListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				MatchModel mm = (MatchModel) arg0.getItemAtPosition(position);
				mIntent = new Intent();
				mIntent.putExtra("matchId", mm.getId());
				mIntent.setClass(FriendDetailActivity.this, MatchDetailActivity.class);
				startActivity(mIntent);
			}
		});
		
		
	}

	private void findViewById() {
		addButton = (Button) findViewById(R.id.add_friend);
		username = (TextView) findViewById(R.id.title_friendname);
		username2 = (TextView) findViewById(R.id.username);
		matchListView = (ListView) findViewById(R.id.friendMatchList);
		offenseBar = (RoundProgressBar) findViewById(R.id.offense_bar);
		defenseBar = (RoundProgressBar) findViewById(R.id.defense_bar);
		zongheBar = (RoundProgressBar) findViewById(R.id.zonghe_bar);
		attend = (TextView) findViewById(R.id.attend_achieve);
		release = (TextView) findViewById(R.id.org_achieve);
		friendnum = (TextView) findViewById(R.id.friend_num);
		role = (TextView) findViewById(R.id.role);
		ageTv = (TextView) findViewById(R.id.age);
		sexTv = (TextView) findViewById(R.id.sex);
		signTv = (TextView) findViewById(R.id.show_state);
		avatar = (CircleImageView) findViewById(R.id.avatar);
		back = (TextView) findViewById(R.id.back);
		chengwei = (TextView) findViewById(R.id.chengwei);
	}

	protected void initDetail() {
		new Thread() {
			public void run() {
				friend = userController.friendDetail(id);
				if (friend == null) {
					Message msg = new Message();
					msg.what = 2;
					myhandler.sendMessage(msg);
				} else {
					Message msg = new Message();
					msg.what = 1;
					myhandler.sendMessage(msg);
				}
			}
		}.start();
	}
	public void reload() {
	    //Intent intent = getIntent();
		Intent intent = new Intent(FriendDetailActivity.this,FriendActivity.class);
	    overridePendingTransition(0, 0);
	    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
	    finish();
	    overridePendingTransition(0, 0);
	    startActivity(intent);
	}

	

}