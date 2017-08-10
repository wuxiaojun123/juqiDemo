/**
 * @author wangwb
 * @function 我的页面中，点击我的账单，具体的listview项
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
import cn.com.sfn.juqi.model.BillModel;

import com.example.juqi.R;

public class WithdrawDetailItemAdapter extends BaseAdapter {
	private Context mContext = null;
	private List<BillModel> mWithdrawDetails = null;

	public WithdrawDetailItemAdapter(Context context,
			List<BillModel> withdrawDetails) {
		mContext = context;
		mWithdrawDetails = withdrawDetails;
	}

	public void setFriends(List<BillModel> withdrawDetails) {
		mWithdrawDetails = withdrawDetails;
	}

	@Override
	public int getCount() {
		int count = 0;
		if (null != mWithdrawDetails) {
			count = mWithdrawDetails.size();
		}
		return count;
	}

	@Override
	public Object getItem(int position) {
		BillModel billModel = null;
		if (null != mWithdrawDetails) {
			billModel = mWithdrawDetails.get(position);
		}
		return billModel;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@SuppressLint("InflateParams")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		if (null == convertView) {
			viewHolder = new ViewHolder();
			LayoutInflater layoutInflater = LayoutInflater.from(mContext);
			convertView = layoutInflater.inflate(
					R.layout.withdraw_detail_list_item, null);
			viewHolder.date = (TextView) convertView
					.findViewById(R.id.date_list_name);
			viewHolder.state = (TextView) convertView
					.findViewById(R.id.state_list_name);
			viewHolder.withdraw = (TextView) convertView
					.findViewById(R.id.withdraw_list_name);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		BillModel billModel = (BillModel) getItem(position);
		if (null != billModel) {
			viewHolder.state.setText(billModel.getState());
			viewHolder.withdraw.setText(billModel.getMoney());
			viewHolder.date.setText(billModel.getDate());
		}
		return convertView;
	}

	private static class ViewHolder {
		TextView date;
		TextView state;
		TextView withdraw;
	}
}