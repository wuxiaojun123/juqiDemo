/**
 * @author wangwb
 * @function 我的页面中，点击球局凭证，具体的listview项
 */
package cn.com.sfn.juqi.adapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import cn.com.sfn.juqi.model.MatchModel;

import com.example.juqi.R;

public class CertificateItemAdapter extends BaseAdapter {
	private Context mContext = null;
	private ListItemClickHelp callback;
	private List<MatchModel> mCertificates = null;

	public CertificateItemAdapter(Context context,
			List<MatchModel> certificates, ListItemClickHelp callback) {
		mContext = context;
		this.callback = callback;
		mCertificates = certificates;
	}

	public void setMatches(List<MatchModel> certificates) {
		mCertificates = certificates;
	}

	@Override
	public int getCount() {
		int count = 0;
		if (null != mCertificates) {
			count = mCertificates.size();
		}
		return count;
	}

	@Override
	public Object getItem(int position) {
		MatchModel matchModel = null;
		if (null != mCertificates) {
			matchModel = mCertificates.get(position);
		}
		return matchModel;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	/**
	 * 球局凭证列表的getview方法
	 */
	@SuppressLint("InflateParams")
	@Override
	public View getView(int position, View convertView, final ViewGroup parent) {
		ViewHolder viewHolder = null;
		if (null == convertView) {
			viewHolder = new ViewHolder();
			LayoutInflater layoutInflater = LayoutInflater.from(mContext);
			// 初始化converView
			convertView = layoutInflater.inflate(
					R.layout.certificate_list_item, null);
			viewHolder.QRCode = (TextView) convertView
					.findViewById(R.id.qr_btn);
			viewHolder.state = (TextView) convertView
					.findViewById(R.id.certificate_state);
			viewHolder.detail = (TextView) convertView
					.findViewById(R.id.certificate_match_btn);
			viewHolder.title = (TextView) convertView
					.findViewById(R.id.certificate_list_title);
			viewHolder.place = (TextView) convertView
					.findViewById(R.id.certificate_list_place);
			viewHolder.date = (TextView) convertView
					.findViewById(R.id.certificate_list_date);
			viewHolder.week = (TextView) convertView
					.findViewById(R.id.certificate_time);
			viewHolder.during = (TextView) convertView
					.findViewById(R.id.certificate_during);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		// 值
		MatchModel matchModel = (MatchModel) getItem(position);
		if (null != matchModel) {
			viewHolder.title.setText(matchModel.getTitle());
			viewHolder.place.setText(matchModel.getS_name());
			viewHolder.date
					.setText(matchModel.getStart_time().substring(0, 10));
			viewHolder.week.setText(getWeek(matchModel.getStart_time()
					.substring(0, 10)));
			viewHolder.during.setText((matchModel.getStart_time().substring(11,
					16)
					+ "-" + addDateMinut(matchModel.getStart_time(),
					Integer.valueOf(matchModel.getDuration()))
					.substring(11, 16)));
			if (matchModel.getStatus().equals("招募中")) {
				viewHolder.state.setTextColor(Color.parseColor("#80ffffff"));
				viewHolder.state.setText(matchModel.getStatus());
			} else {
				viewHolder.state.setTextColor(android.graphics.Color.RED);
				viewHolder.state.setText(matchModel.getStatus());
			}
		}
		final int p = position;
		final int one = viewHolder.detail.getId();
		viewHolder.detail.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				callback.onClick(v, parent, p, one);
			}
		});
		final int two = viewHolder.QRCode.getId();
		viewHolder.QRCode.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				callback.onClick(v, parent, p, two);
			}
		});
		return convertView;
	}

	// 为ViewHolder定义列表项
	private static class ViewHolder {
		TextView title;
		TextView place;
		TextView state;
		TextView date;
		TextView week;
		TextView during;
		TextView QRCode;
		TextView detail;
	}

	public static String addDateMinut(String day, int x) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = null;
		try {
			date = format.parse(day);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		if (date == null)
			return "";
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.MINUTE, x);// 24小时制
		date = cal.getTime();
		cal = null;
		return format.format(date);
	}

	private String getWeek(String pTime) {
		String Week = "周";
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Calendar c = Calendar.getInstance();
		try {
			c.setTime(format.parse(pTime));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		if (c.get(Calendar.DAY_OF_WEEK) == 1) {
			Week += "日";
		}
		if (c.get(Calendar.DAY_OF_WEEK) == 2) {
			Week += "一";
		}
		if (c.get(Calendar.DAY_OF_WEEK) == 3) {
			Week += "二";
		}
		if (c.get(Calendar.DAY_OF_WEEK) == 4) {
			Week += "三";
		}
		if (c.get(Calendar.DAY_OF_WEEK) == 5) {
			Week += "四";
		}
		if (c.get(Calendar.DAY_OF_WEEK) == 6) {
			Week += "五";
		}
		if (c.get(Calendar.DAY_OF_WEEK) == 7) {
			Week += "六";
		}
		return Week;
	}
}