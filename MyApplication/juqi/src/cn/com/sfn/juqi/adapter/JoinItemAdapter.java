package cn.com.sfn.juqi.adapter;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import cn.com.sfn.juqi.model.AttendModel;

import com.example.juqi.R;

public class JoinItemAdapter extends BaseAdapter {
	private Context mContext = null;
	private List<AttendModel> mAttends = null;

	public JoinItemAdapter(Context context, List<AttendModel> attends) {
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
	 * 参与者2列表的getview方法
	 */
	@SuppressLint("InflateParams")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		if (null == convertView) {
			viewHolder = new ViewHolder();
			LayoutInflater layoutInflater = LayoutInflater.from(mContext);
			convertView = layoutInflater.inflate(R.layout.join_list_item, null);
			viewHolder.name = (TextView) convertView
					.findViewById(R.id.join_name);
			viewHolder.game_age = (TextView) convertView
					.findViewById(R.id.join_age);
			viewHolder.canyu = (TextView) convertView
					.findViewById(R.id.join_canyu);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		AttendModel attendModel = (AttendModel) getItem(position);
		if (null != attendModel) {
			viewHolder.name.setText(attendModel.getU_name());
			viewHolder.game_age.setText("球龄: " + attendModel.getU_age());
			viewHolder.canyu.setText("参与成就: V" + attendModel.getJoinLevel());
		}
		return convertView;
	}

	private static class ViewHolder {
		TextView name;
		TextView game_age;
		TextView canyu;
	}
}
