package cn.com.sfn.juqi.adapter;

import android.view.View;

/**
 * listview点击事件接口
 * 
 * @author Wangwb
 * 
 */
public interface ListItemClickHelp {
	void onClick(View item, View widget, int position, int which);
}