/**
 * @author wangwb
 * @function 首页球局列表项
 */
package cn.com.sfn.juqi.adapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import cn.com.sfn.juqi.controller.MatchController;
import cn.com.sfn.juqi.model.MatchModel;
import cn.com.sfn.juqi.widgets.CircleImageView;
import cn.com.wx.util.GlideUtils;

import com.example.juqi.R;
import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MatchItemAdapter extends BaseAdapter {
	private Context mContext = null;
	private List<MatchModel> mMatches = null;
	private MatchModel match = new MatchModel();
	private MatchController matchController=new MatchController();

	public MatchItemAdapter(Context context, List<MatchModel> matches) {
		mContext = context;
		mMatches = matches;
	}

	public void setMatches(List<MatchModel> matches) {
		mMatches = matches;
	}

	@Override
	public int getCount() {
		int count = 0;
		if (null != mMatches) {
			count = mMatches.size();
		}
		return count;
	}

	@Override
	public Object getItem(int position) {
		MatchModel matchModel = null;
		if (null != mMatches) {
			matchModel = mMatches.get(position);
		}
		return matchModel;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	/**
	 * 球局列表的getview方法
	 */
	@SuppressLint("InflateParams")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		if (null == convertView) {
			viewHolder = new ViewHolder();
			LayoutInflater layoutInflater = LayoutInflater.from(mContext);
			convertView = layoutInflater
					.inflate(R.layout.match_list_item, null);
			viewHolder.title = (TextView) convertView
					.findViewById(R.id.match_list_title);
			viewHolder.place = (TextView) convertView
					.findViewById(R.id.match_list_place);
			viewHolder.startDate = (TextView) convertView
					.findViewById(R.id.match_list_date);
			viewHolder.attendNum = (TextView) convertView
					.findViewById(R.id.attend_num);
			viewHolder.week = (TextView) convertView
					.findViewById(R.id.match_time);
			viewHolder.matchDuring = (TextView) convertView
					.findViewById(R.id.match_during);
			viewHolder.renshu = (ProgressBar) convertView
					.findViewById(R.id.renshu_bar);
			viewHolder.avatar = (CircleImageView) convertView
					.findViewById(R.id.match_avatar);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		MatchModel matchModel = (MatchModel) getItem(position);
		/*match = matchController.getinfo(matchModel.getId());
		Log.e("matchid", match.getId());
		if (match.getType().equals("1")) {		
			//convertView.setVisibility(View.GONE);//隐藏私密球局		
		} */
		
	
		if (null != matchModel) {
			GlideUtils.loadCircleImage(matchModel.getuImg(),viewHolder.avatar);
			viewHolder.title.setText(matchModel.getTitle());
			viewHolder.place.setText(matchModel.getS_name());
			viewHolder.startDate.setText(matchModel.getStart_time().substring(
					0, 10));
			if (matchModel.getAttendance().equals(matchModel.getNum())) {
				viewHolder.attendNum.setText("已满额");
			} else {
				viewHolder.attendNum.setText(matchModel.getAttendance() + "/"
						+ matchModel.getNum());
			}
			viewHolder.week.setText(getWeek(matchModel.getStart_time()
					.substring(0, 10)));
			viewHolder.matchDuring.setText(matchModel.getStart_time()
					.substring(11, 16)
					+ "-"
					+ addDateMinut(matchModel.getStart_time(),
							Integer.valueOf(matchModel.getDuration()))
							.substring(11, 16));
			viewHolder.renshu.setMax(Integer.valueOf(matchModel.getNum()));
			viewHolder.renshu.setProgress(Integer.valueOf(matchModel
					.getAttendance()));
			
		}
		return convertView;
	}

	private static class ViewHolder {
		CircleImageView avatar;
		TextView title;
		TextView place;
		TextView startDate;
		TextView attendNum;
		TextView week;
		TextView matchDuring;
		ProgressBar renshu;
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
		String Week = "星期";
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