
package cn.com.sfn.example.juqi.accomplishments;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.juqi.R;

import java.util.List;

/***
 * 参与成就的适配器
 */
public class MyAccomplishmentsCanYuAdapter extends BaseAdapter {

    private Context mContext;
    private List<MyAccomplishmentsBean> list;

    public MyAccomplishmentsCanYuAdapter(Context context, List<MyAccomplishmentsBean> list) {
        this.mContext = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        if (list != null)
            return list.size();
        else
            return 0;
    }

    @Override
    public Object getItem(int arg0) {
        return list.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {
        return arg0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup arg2) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_accomplishments_canyu, null);
            viewHolder = new ViewHolder();
            viewHolder.id_tv_type = (TextView) convertView.findViewById(R.id.id_tv_type);
            viewHolder.id_tv_time = (TextView) convertView.findViewById(R.id.id_tv_time);
            viewHolder.id_tv_content = (TextView) convertView.findViewById(R.id.id_tv_content);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if (list != null) {
            MyAccomplishmentsBean bean = list.get(position);
            if (bean != null) {
                viewHolder.id_tv_type.setText(bean.title);
                viewHolder.id_tv_content.setText(bean.content);
                viewHolder.id_tv_time.setText(bean.time);
            }
        }
        return convertView;
    }

    class ViewHolder {
        TextView id_tv_type;
        TextView id_tv_time;
        TextView id_tv_content;
    }

}
