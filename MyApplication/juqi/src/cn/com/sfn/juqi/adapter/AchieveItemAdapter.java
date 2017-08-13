package cn.com.sfn.juqi.adapter;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import cn.com.sfn.juqi.model.AchieveModel;

import com.example.juqi.R;

public class AchieveItemAdapter extends BaseAdapter {
    private Context mContext = null;
    private List<AchieveModel> mAchieves = null;

    public AchieveItemAdapter(Context context, List<AchieveModel> achieves) {
        mContext = context;
        mAchieves = achieves;
    }

    public void setAttends(List<AchieveModel> achieves) {
        mAchieves = achieves;
    }

    @Override
    public int getCount() {
        int count = 0;
        if (null != mAchieves) {
            count = mAchieves.size();
        }
        return count;
    }

    @Override
    public Object getItem(int position) {
        AchieveModel achieveModel = null;
        if (null != mAchieves) {
            achieveModel = mAchieves.get(position);
        }
        return achieveModel;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * 成就列表的getview方法
     */
    @SuppressLint("InflateParams")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (null == convertView) {
            viewHolder = new ViewHolder();
            LayoutInflater layoutInflater = LayoutInflater.from(mContext);
            convertView = layoutInflater.inflate(R.layout.achieve_list_item,
                    null);
            viewHolder.rank = (TextView) convertView.findViewById(R.id.ac_rank);
            viewHolder.name = (TextView) convertView.findViewById(R.id.ac_name);
            viewHolder.score = (TextView) convertView
                    .findViewById(R.id.ac_score);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        AchieveModel achieveModel = mAchieves.get(position);
        if (null != achieveModel) {
            viewHolder.rank.setText(achieveModel.getRank());
            viewHolder.name.setText(achieveModel.getUsername());
            viewHolder.score.setText(achieveModel.getScore());
        }
        return convertView;
    }

    private static class ViewHolder {
        TextView rank;
        TextView name;
        TextView score;
    }
}
