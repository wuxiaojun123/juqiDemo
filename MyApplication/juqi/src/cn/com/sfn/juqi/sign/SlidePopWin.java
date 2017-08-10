
package cn.com.sfn.juqi.sign;

import java.util.ArrayList;
import java.util.List;

import com.example.juqi.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings.System;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

public class SlidePopWin extends PopupWindow implements
        ExpandableListView.OnChildClickListener,
        ExpandableListView.OnGroupClickListener {
    private ExpandableListView expandableListView;
    private ArrayList<Group> groupList;
    private ArrayList<List<QueryType>> childList;
    public static Handler mHandler;
    private MyexpandableListAdapter adapter;
    private View mMenuView; // PopupWindow上面装载的View
    private Group group;
    private GroupHolder groupHolder;

    public SlidePopWin(final Activity context) {
        super(context);
        /* 将xml布局初始化为View,并初始化上面的控件 */
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mMenuView = inflater.inflate(R.layout.take_slide_pop, null);
        expandableListView = (ExpandableListView) mMenuView
                .findViewById(R.id.expandablelist);
        initData();
        adapter = new MyexpandableListAdapter(context);
        expandableListView.setAdapter(adapter);

        // 展开所有group
        for (int i = 0, count = expandableListView.getCount(); i < count; i++) {
            expandableListView.expandGroup(i);
        }

        expandableListView.setOnChildClickListener(this);
        expandableListView.setOnGroupClickListener(this);
        // 设置SignPopupWindow的View
        this.setContentView(mMenuView);
        // 设置SignPopupWindow弹出窗体的宽
        this.setWidth(LayoutParams.MATCH_PARENT);
        // 设置SignPopupWindow弹出窗体的高
        this.setHeight(LayoutParams.MATCH_PARENT);
        // 设置SignPopupWindow弹出窗体可点击
        this.setFocusable(true);
        // 设置SignPopupWindow弹出窗体动画效果
        this.setAnimationStyle(R.style.take_share_anim);
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0x80000000);
        // 设置SignPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);
        // 防止虚拟软键盘被弹出菜单遮住
        this.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        // mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
        mMenuView.setOnTouchListener(new OnTouchListener() {

            @SuppressLint("ClickableViewAccessibility")
            public boolean onTouch(View v, MotionEvent event) {

                int width = mMenuView.findViewById(R.id.attend_layout)
                        .getWidth();
                int x = (int) event.getX();
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    if (x > width) {
                        dismiss();
                    }
                }
                return true;
            }
        });

    }

    /***
     * InitData
     */
    void initData() {
        groupList = new ArrayList<Group>();
        Group group = null;
        for (int i = 0; i < 2; i++) {
            group = new Group();
            if (i == 0) {
                if (groupList.size() == 0) {
                    group.setTitle("活动地点");
                } else {
                    group.setTitle(groupList.get(i).getTitle());
                }

            } else {
                if (groupList.size() == 1) {
                    group.setTitle("排序方式");
                } else {
                    group.setTitle(groupList.get(i).getTitle());
                }
            }
            groupList.add(group);
        }

        childList = new ArrayList<List<QueryType>>();
        for (int i = 0; i < groupList.size(); i++) {
            ArrayList<QueryType> childTemp;
            String[] str1 = {
                    "朝阳区", "海淀区", "东城区", "西城区", "丰台区", "大兴区", "通州区",
                    "昌平区"
            };
            String[] str2 = {
                    "地理位置", "费用高低", "时间先后", "综合排序"
            };
            if (i == 0) {
                childTemp = new ArrayList<QueryType>();
                for (int j = 0; j < str1.length; j++) {
                    QueryType type = new QueryType();
                    type.setType(str1[j]);
                    childTemp.add(type);
                }
            } else {
                childTemp = new ArrayList<QueryType>();
                for (int j = 0; j < str2.length; j++) {
                    QueryType type = new QueryType();
                    type.setType(str2[j]);
                    childTemp.add(type);
                }
            }
            childList.add(childTemp);
        }

    }

    /***
     * 数据源
     * 
     * @author Administrator
     */
    class MyexpandableListAdapter extends BaseExpandableListAdapter {
        @SuppressWarnings("unused")
        private Context context;
        private LayoutInflater inflater;

        public MyexpandableListAdapter(Context context) {
            this.context = context;
            inflater = LayoutInflater.from(context);
        }

        // 返回父列表个数
        @Override
        public int getGroupCount() {
            return groupList.size();
        }

        // 返回子列表个数
        @Override
        public int getChildrenCount(int groupPosition) {
            return childList.get(groupPosition).size();
        }

        @Override
        public Object getGroup(int groupPosition) {

            return groupList.get(groupPosition);
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            return childList.get(groupPosition).get(childPosition);
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public boolean hasStableIds() {

            return true;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded,
                View convertView, ViewGroup parent) {
            // GroupHolder groupHolder = null;
            if (convertView == null) {
                groupHolder = new GroupHolder();
                convertView = inflater.inflate(R.layout.group, null);
                groupHolder.textView = (TextView) convertView
                        .findViewById(R.id.group);
                groupHolder.imageView = (ImageView) convertView
                        .findViewById(R.id.image);
                groupHolder.textView.setTextSize(15);
                convertView.setTag(groupHolder);
            } else {
                groupHolder = (GroupHolder) convertView.getTag();
            }

            groupHolder.textView.setText(((Group) getGroup(groupPosition))
                    .getTitle());
            if (isExpanded)// ture is Expanded or false is not isExpanded
                groupHolder.imageView.setImageResource(R.drawable.collapse);
            else
                groupHolder.imageView.setImageResource(R.drawable.next_page);
            return convertView;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition,
                boolean isLastChild, View convertView, ViewGroup parent) {
            ChildHolder childHolder = null;
            if (convertView == null) {
                childHolder = new ChildHolder();
                convertView = inflater.inflate(R.layout.child, null);

                childHolder.type = (TextView) convertView
                        .findViewById(R.id.type);

                convertView.setTag(childHolder);
            } else {
                childHolder = (ChildHolder) convertView.getTag();
            }

            childHolder.type.setText(((QueryType) getChild(groupPosition,
                    childPosition)).getType());

            return convertView;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }
    }

    @Override
    public boolean onGroupClick(final ExpandableListView parent, final View v,
            int groupPosition, final long id) {

        return false;
    }

    @Override
    public boolean onChildClick(ExpandableListView parent, View v,
            int groupPosition, int childPosition, long id) {

        Message mesg = new Message();
        Bundle data = new Bundle();
        data.putString("type", childList.get(groupPosition).get(childPosition).getType());
        mesg.setData(data);
        mesg.what = 1;
        mHandler.sendMessage(mesg);
        QueryType querytype = childList.get(groupPosition).get(childPosition);
        // groupHolder.textView.setText(((Group)
        // groupList.get(groupPosition)).getTitle()+"       "+querytype.getType());
        groupList = new ArrayList<Group>();
        Group group = null;
        for (int i = 0; i < 2; i++) {
            group = new Group();
            if (i == 0) {
                if (groupPosition == 0) {
                    group.setTitle("活动地点           " + querytype.getType());
                }
                else {
                    group.setTitle("活动地点");
                }
            } else {
                if (groupPosition == 1) {
                    group.setTitle("排序方式         " + querytype.getType());
                }
                else {
                    group.setTitle("排序方式");
                }
            }
            groupList.add(group);
        }
        adapter.notifyDataSetChanged();
        Log.e("groupPosition", Integer.toString(groupPosition));
        // dismiss();
        return false;
    }

    class GroupHolder {
        TextView textView;
        ImageView imageView;
    }

    class ChildHolder {
        TextView type;
    }
}
