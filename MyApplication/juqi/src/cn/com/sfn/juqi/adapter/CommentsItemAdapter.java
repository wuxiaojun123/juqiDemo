package cn.com.sfn.juqi.adapter;

import java.util.List;

import com.example.juqi.R;

import cn.com.sfn.juqi.model.CommentsModel;
import cn.com.sfn.juqi.widgets.CircleImageView;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class CommentsItemAdapter extends BaseAdapter {
	private Context mContext = null;
	private List<CommentsModel> mComments = null;
	private ListItemClickHelp callback;

	public CommentsItemAdapter(Context context, List<CommentsModel> comments,
			ListItemClickHelp callback) {
		mContext = context;
		mComments = comments;
		this.callback = callback;
	}

	public void setComments(List<CommentsModel> comments) {
		mComments = comments;
	}

	@Override
	public int getCount() {
		int count = 0;
		if (null != mComments) {
			count = mComments.size();
		}
		return count;
	}

	@Override
	public Object getItem(int position) {
		CommentsModel commentsModel = null;
		if (null != mComments) {
			commentsModel = mComments.get(position);
		}
		return commentsModel;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, final ViewGroup parent) {
		ViewHolder viewHolder = null;
		if (null == convertView) {
			viewHolder = new ViewHolder();
			LayoutInflater layoutInflater = LayoutInflater.from(mContext);
			convertView = layoutInflater.inflate(R.layout.comments_list_item,
					null);
			viewHolder.name = (TextView) convertView
					.findViewById(R.id.comments_username);
			viewHolder.content = (TextView) convertView
					.findViewById(R.id.comments_content);
			viewHolder.avatar = (CircleImageView) convertView
					.findViewById(R.id.comments_avatar);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		CommentsModel commentsModel = (CommentsModel) getItem(position);
		if (null != commentsModel) {
			viewHolder.name.setText(commentsModel.getUserName());
			viewHolder.content.setText(commentsModel.getContent());
			viewHolder.avatar.setImageBitmap(commentsModel.getUserAvatar());
		}
		final int p = position;
		final int zero = viewHolder.avatar.getId();
		viewHolder.avatar.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				callback.onClick(v, parent, p, zero);
			}
		});
		final int one = viewHolder.content.getId();
		viewHolder.content.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				callback.onClick(v, parent, p, one);
			}
		});
		return convertView;
	}

	private static class ViewHolder {
		TextView name;
		TextView content;
		CircleImageView avatar;
	}
}
