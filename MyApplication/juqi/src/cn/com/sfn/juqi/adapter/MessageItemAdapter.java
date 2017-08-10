package cn.com.sfn.juqi.adapter;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import cn.com.sfn.juqi.model.MessageModel;

import com.example.juqi.R;

public class MessageItemAdapter extends BaseAdapter {
	private Context mContext = null;
	private List<MessageModel> mMessages = null;

	public MessageItemAdapter(Context context, List<MessageModel> messages) {
		mContext = context;
		mMessages = messages;
	}

	public void setAttends(List<MessageModel> messages) {
		mMessages = messages;
	}

	@Override
	public int getCount() {
		int count = 0;
		if (null != mMessages) {
			count = mMessages.size();
		}
		return count;
	}

	@Override
	public Object getItem(int position) {
		MessageModel messageModel = null;
		if (null != mMessages) {
			messageModel = mMessages.get(position);
		}
		return messageModel;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	/**
	 * 消息列表的getview方法
	 */
	@SuppressLint("InflateParams")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		if (null == convertView) {
			viewHolder = new ViewHolder();
			LayoutInflater layoutInflater = LayoutInflater.from(mContext);
			convertView = layoutInflater.inflate(R.layout.message_list_item,
					null);
			viewHolder.type = (TextView) convertView
					.findViewById(R.id.message_type);
			viewHolder.time = (TextView) convertView
					.findViewById(R.id.message_time);
			viewHolder.content = (TextView) convertView
					.findViewById(R.id.message_content);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		MessageModel messageModel = (MessageModel) getItem(position);
		if (null != messageModel) {
			viewHolder.type.setText(messageModel.getType());
			viewHolder.time.setText(messageModel.getTime());
			viewHolder.content.setText(messageModel.getContent());
		}
		return convertView;
	}

	private static class ViewHolder {
		TextView type;
		TextView time;
		TextView content;
	}
}
