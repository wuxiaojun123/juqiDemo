/**
 * @author wangwb
 * @function 我的页面中，点击我的好友，具体的listview项
 */
package cn.com.sfn.juqi.adapter;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import cn.com.sfn.juqi.model.UserModel;
import cn.com.sfn.juqi.widgets.CircleImageView;

import com.example.juqi.R;

public class FriendItemAdapter extends BaseAdapter {
	private Context mContext = null;
	private List<UserModel> mFriends = null;

	public FriendItemAdapter(Context context, List<UserModel> friends) {
		mContext = context;
		mFriends = friends;
	}

	public void setFriends(List<UserModel> friends) {
		mFriends = friends;
	}

	@Override
	public int getCount() {
		int count = 0;
		if (null != mFriends) {
			count = mFriends.size();
		}
		return count;
	}

	@Override
	public Object getItem(int position) {
		UserModel friendModel = null;
		if (null != mFriends) {
			friendModel = mFriends.get(position);
		}
		return friendModel;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	/**
	 * 朋友列表的getview方法
	 */
	@SuppressLint("InflateParams")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		if (null == convertView) {
			viewHolder = new ViewHolder();
			LayoutInflater layoutInflater = LayoutInflater.from(mContext);
			convertView = layoutInflater.inflate(R.layout.friend_list_item,
					null);
			viewHolder.avatar = (CircleImageView) convertView
					.findViewById(R.id.friend_avatar);
			viewHolder.name = (TextView) convertView
					.findViewById(R.id.friend_list_name);
			viewHolder.level = (TextView) convertView
					.findViewById(R.id.friend_list_level);
			viewHolder.cyachieve = (TextView) convertView
					.findViewById(R.id.friend_list_cyachieve);
			viewHolder.zzachieve = (TextView) convertView
					.findViewById(R.id.friend_list_zzachieve);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		UserModel friendModel = (UserModel) getItem(position);
		if (null != friendModel) {
			viewHolder.avatar.setImageBitmap(friendModel.getUserAvatar());
			viewHolder.name.setText(friendModel.getNickName());
			viewHolder.level.setText(friendModel.getStandard());
			viewHolder.cyachieve.setText(friendModel.getJoinEntire());
			viewHolder.zzachieve.setText(friendModel.getReleaseEntire());
		}
		return convertView;
	}

	private static class ViewHolder {
		TextView name;
		TextView level;
		TextView cyachieve;
		TextView zzachieve;
		CircleImageView avatar;
	}
}