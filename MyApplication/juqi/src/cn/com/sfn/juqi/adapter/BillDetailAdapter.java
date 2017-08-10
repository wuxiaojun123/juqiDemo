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

public class BillDetailAdapter extends BaseAdapter {
	private Context mContext = null;
	private List<BillModel> mBillOverviews = null;

	public BillDetailAdapter(Context context, List<BillModel> billOverviews) {
		mContext = context;
		mBillOverviews = billOverviews;
	}

	public void setFriends(List<BillModel> billOverviews) {
		mBillOverviews = billOverviews;
	}

	@Override
	public int getCount() {
		int count = 0;
		if (null != mBillOverviews) {
			count = mBillOverviews.size();
		}
		return count;
	}

	@Override
	public Object getItem(int position) {
		BillModel billModel = null;
		if (null != mBillOverviews) {
			billModel = mBillOverviews.get(position);
		}
		return billModel;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	/**
	 * 账单总览列表的getview方法
	 */
	@SuppressLint("InflateParams")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		if (null == convertView) {
			viewHolder = new ViewHolder();
			LayoutInflater layoutInflater = LayoutInflater.from(mContext);
			convertView = layoutInflater.inflate(R.layout.billdetail_list_item,
					null);
			viewHolder.user = (TextView) convertView
					.findViewById(R.id.user_list_name);
			viewHolder.date = (TextView) convertView
					.findViewById(R.id.bill_list_date);
			viewHolder.money = (TextView) convertView
					.findViewById(R.id.money_list_name);
			viewHolder.operate = (TextView) convertView
					.findViewById(R.id.operate_list_name);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		BillModel billModel = (BillModel) getItem(position);
		if (null != billModel) {
			viewHolder.user.setText(billModel.getName());
			viewHolder.money.setText(billModel.getMoney());
			viewHolder.date.setText(billModel.getDate());
			if (billModel.getOperate().equals("")) {
				viewHolder.operate.setVisibility(View.GONE);
			} else {
				viewHolder.operate.setVisibility(View.VISIBLE);
				viewHolder.operate.setText(billModel.getOperate());
			}
		}
		return convertView;
	}

	private static class ViewHolder {
		TextView user;
		TextView money;
		TextView date;
		TextView operate;
	}
}