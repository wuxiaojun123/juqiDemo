/**
 * @author wangwb
 * 参与人列表的适配器
 */
package cn.com.sfn.juqi.adapter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import cn.com.sfn.juqi.model.AttendModel;
import cn.com.sfn.juqi.widgets.CircleImageView;

import com.example.juqi.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class AttendItemAdapter extends BaseAdapter {
	private Context mContext = null;
	private List<AttendModel> mAttends = null;

	public AttendItemAdapter(Context context, List<AttendModel> attends) {
		mContext = context;
		mAttends = attends;
	}

	public void setAttends(List<AttendModel> attends) {
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
		AttendModel attendModel = null;
		if (null != mAttends) {
			attendModel = mAttends.get(position);
		}
		return attendModel;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	/**
	 * 参与者列表的getview方法
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
		AttendModel attendModel = (AttendModel) getItem(position);
		String sta = "";
		if (null != attendModel) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String date = sdf.format(new Date(Long.valueOf(attendModel
					.getTime()) * 1000L));
			viewHolder.time.setText("报名时间:" + date.substring(0, 16));
			viewHolder.name.setText(attendModel.getU_name());
			viewHolder.phone.setText("手机:" + attendModel.getU_mobile());
			viewHolder.fee.setText("报名金额:" + attendModel.getFee());
			viewHolder.avatar.setImageBitmap(attendModel.getAvatar());
			if (attendModel.getStatus().equals("0")) {
				sta = "未支付";
			} else if (attendModel.getStatus().equals("2")) {
				sta = "未验票  已支付";
			} else if (attendModel.getStatus().equals("5")) {
				sta = "已验票  已支付";
			} else {
				sta = "已退出";
			}
			viewHolder.status.setText(sta);
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