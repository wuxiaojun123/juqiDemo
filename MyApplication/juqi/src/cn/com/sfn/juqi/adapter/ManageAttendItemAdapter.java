/**
 * @author wangwb
 * 参与管理列表的适配器
 */
package cn.com.sfn.juqi.adapter;

import java.util.List;

import cn.com.sfn.juqi.model.UserModel;
import cn.com.sfn.juqi.widgets.CircleImageView;
import cn.com.wx.util.GlideUtils;

import com.example.juqi.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ManageAttendItemAdapter extends BaseAdapter {
	private Context mContext = null;
	private List<UserModel> mAttends = null;

	public ManageAttendItemAdapter(Context context, List<UserModel> attends) {
		mContext = context;
		mAttends = attends;
	}

	public void setAttends(List<UserModel> attends) {
		mAttends = attends;
	}

	@Override
	public int getCount() {
		int count = 0;
		if (null != mAttends) {
			count = mAttends.size();
		}
		return count;
	}

	@Override
	public Object getItem(int position) {
		UserModel userModel = null;
		if (null != mAttends) {
			userModel = mAttends.get(position);
		}
		return userModel;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	/**
	 * 参与管理的getview方法
	 */
	@SuppressLint("InflateParams")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		if (null == convertView) {
			viewHolder = new ViewHolder();
			LayoutInflater layoutInflater = LayoutInflater.from(mContext);
			convertView = layoutInflater.inflate(R.layout.attend_list_item,
					null);
			viewHolder.phone = (TextView) convertView
					.findViewById(R.id.attend_list_phone);
			viewHolder.fee = (TextView) convertView
					.findViewById(R.id.attend_list_fee);
			viewHolder.name = (TextView) convertView
					.findViewById(R.id.attend_list_name);
			viewHolder.time = (TextView) convertView
					.findViewById(R.id.attend_list_time);
			viewHolder.avatar = (CircleImageView) convertView
					.findViewById(R.id.attend_avatar);
			viewHolder.status = (TextView) convertView
					.findViewById(R.id.attend_list_inspect_or_pay);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		UserModel userModel = (UserModel) getItem(position);
		if (null != userModel) {
			viewHolder.time.setText("报名时间:2016-05-20 20:00");
			viewHolder.name.setText(userModel.getNickName());
			viewHolder.phone.setText("手机:" + userModel.getUserMobile());
			viewHolder.fee.setText("报名金额:45.00");
//			viewHolder.avatar.setImageBitmap(userModel.getUserAvatar());
			GlideUtils.loadCircleImage(userModel.getUserAvatar(),viewHolder.avatar);
			viewHolder.status.setText("待付款");
		}
		return convertView;
	}

	private static class ViewHolder {
		TextView fee;
		TextView name;
		TextView time;
		TextView phone;
		CircleImageView avatar;
		TextView status;
	}
}