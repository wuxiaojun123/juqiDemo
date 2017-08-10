/**
 * @author wangwb
 * 球局管理列表的适配器
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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.com.sfn.juqi.controller.MatchController;
import cn.com.sfn.juqi.model.MatchModel;

import com.example.juqi.R;

public class ManageItemAdapter extends BaseAdapter {
    private Context mContext = null;
    private ListItemClickHelp callback;
    private List<MatchModel> mManages = null;
    private MatchModel match = new MatchModel();
    private MatchController matchController = new MatchController();

    public ManageItemAdapter(Context context, List<MatchModel> manages,
                             ListItemClickHelp callback) {
        mContext = context;
        mManages = manages;
        this.callback = callback;
    }

    public void setMatches(List<MatchModel> manages) {
        mManages = manages;
    }

    @Override
    public int getCount() {
        int count = 0;
        if (null != mManages) {
            count = mManages.size();
        }
        return count;
    }

    @Override
    public Object getItem(int position) {
        MatchModel matchModel = null;
        if (null != mManages) {
            matchModel = mManages.get(position);
        }
        return matchModel;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * 球局管理列表的getview方法
     */
    @SuppressLint("InflateParams")
    @Override
    public View getView(int position, View convertView, final ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (null == convertView) {
            viewHolder = new ViewHolder();
            LayoutInflater layoutInflater = LayoutInflater.from(mContext);
            // 初始化converView
            convertView = layoutInflater.inflate(R.layout.manage_list_item,
                    null);
            bindView(convertView, viewHolder);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        // 值
        MatchModel matchModel = mManages.get(position);
        if (null != matchModel) {
            viewHolder.title.setText(matchModel.getTitle());
            viewHolder.place.setText(matchModel.getS_name());
            if (matchModel.getFee().equals("0")) {
                viewHolder.fee.setTextColor(Color.parseColor("#80ffffff"));
                viewHolder.fee.setText("免费");
            } else {
                viewHolder.fee.setTextColor(android.graphics.Color.RED);
                viewHolder.fee.setText("收费");
            }
            viewHolder.date
                    .setText(matchModel.getStart_time().substring(0, 10));
            viewHolder.time.setText(getWeek(matchModel.getStart_time()
                    .substring(0, 10)));
            viewHolder.during.setText(matchModel.getStart_time().substring(11,
                    16)
                    + "-"
                    + addDateMinut(matchModel.getStart_time(),
                    Integer.valueOf(matchModel.getDuration()))
                    .substring(11, 16));

            match = matchController.getinfo(matchModel.getId());
            Log.e("matchid", matchModel.getId());
            if (match.getUserAndmatch().equals("3")) {
                viewHolder.attendnum.setText("已结束");
            } else {
                if (match.getUserAndmatch().equals("4")) {
                    viewHolder.attendnum.setText("发起者已取消");
                } else
                    viewHolder.attendnum.setText(matchModel.getAttendance() + "/"
                            + matchModel.getNum());
            }
        }
        final int p = position;
        final int zero = viewHolder.manageAttend.getId();
        viewHolder.manageAttend.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.onClick(v, parent, p, zero);
            }
        });
        final int one = viewHolder.manageMatch.getId();
        viewHolder.manageMatch.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.onClick(v, parent, p, one);
            }
        });
        final int two = viewHolder.manageAttend.getId();
        viewHolder.manageAttend.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.onClick(v, parent, p, two);
            }
        });
        final int three = viewHolder.detail.getId();
        viewHolder.detail.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.onClick(v, parent, p, three);
            }
        });
        final int four = viewHolder.changematch.getId();
        viewHolder.changematch.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.onClick(v, parent, p, four);
            }
        });

        return convertView;
    }


    private void bindView(View convertView, ViewHolder viewHolder) {
        viewHolder.fee = (TextView) convertView
                .findViewById(R.id.manage_fee);
        viewHolder.title = (TextView) convertView
                .findViewById(R.id.manage_list_title);
        viewHolder.place = (TextView) convertView
                .findViewById(R.id.manage_list_place);
        viewHolder.date = (TextView) convertView
                .findViewById(R.id.manage_list_date);
        viewHolder.time = (TextView) convertView
                .findViewById(R.id.manage_list_time);
        viewHolder.during = (TextView) convertView
                .findViewById(R.id.manage_list_during);
        viewHolder.attendnum = (TextView) convertView
                .findViewById(R.id.attend_total);
        viewHolder.detail = (LinearLayout) convertView
                .findViewById(R.id.manage_detail);
        viewHolder.manageMatch = (TextView) convertView
                .findViewById(R.id.manage_inspect_btn);
        viewHolder.manageAttend = (TextView) convertView
                .findViewById(R.id.manage_attend_btn);
        viewHolder.changematch = (TextView) convertView
                .findViewById(R.id.change_btn);
    }

    // 为ViewHolder定义列表项
    private class ViewHolder {
        TextView fee;
        TextView title;
        TextView place;
        TextView date;
        TextView time;
        TextView during;
        TextView attendnum;
        LinearLayout detail;
        TextView manageMatch;
        TextView manageAttend;
        TextView changematch;
    }

    public String addDateMinut(String day, int x) {
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

    public void addList(List<MatchModel> list) {
        if (list != null) {
            mManages.addAll(list);
            notifyDataSetChanged();
        }
    }

}