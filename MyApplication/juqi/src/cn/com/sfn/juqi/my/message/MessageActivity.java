package cn.com.sfn.juqi.my.message;

import java.util.ArrayList;
import java.util.List;

import cn.com.sfn.juqi.adapter.FragmentAdapter;

import com.example.juqi.R;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MessageActivity extends FragmentActivity {

	private ViewPager mPageVp;

	private List<Fragment> mFragmentList = new ArrayList<Fragment>();
	private FragmentAdapter mFragmentAdapter;

	/**
	 * Tab显示内容TextView
	 */
	private TextView mTabCommentTv, mTabSystemTv, mTabActivityTv,
			mTabOfficialTv;
	/**
	 * Tab的那个引导线
	 */
	private ImageView mTabLineIv;
	private TextView back;
	/**
	 * Fragment
	 */
	private MessageCommentFragment mCommentFg;
	private MessageSystemFragment mSystemFg;
	private MessageActivityFragment mActivityFg;
	private MessageOfficialFragment mOfficialFg;
	/**
	 * ViewPager的当前选中页
	 */
	private int currentIndex;
	/**
	 * 屏幕的宽度
	 */
	private int screenWidth;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_message);
		findById();
		init();
		initTabLineWidth();
		back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

	private void findById() {
		mTabCommentTv = (TextView) this.findViewById(R.id.id_comment_tv);
		mTabSystemTv = (TextView) this.findViewById(R.id.id_system_tv);
		mTabActivityTv = (TextView) this.findViewById(R.id.id_activity_tv);
		mTabOfficialTv = (TextView) this.findViewById(R.id.id_official_tv);
		back = (TextView) this.findViewById(R.id.back_to_my);
		mTabLineIv = (ImageView) this.findViewById(R.id.id_tab_line_iv);
		mPageVp = (ViewPager) this.findViewById(R.id.id_page_vp);
	}

	private void init() {
		mCommentFg = new MessageCommentFragment();
		mSystemFg = new MessageSystemFragment();
		mActivityFg = new MessageActivityFragment();
		mOfficialFg = new MessageOfficialFragment();

		mFragmentList.add(mCommentFg);
		mFragmentList.add(mSystemFg);
		mFragmentList.add(mActivityFg);
		mFragmentList.add(mOfficialFg);
		mFragmentAdapter = new FragmentAdapter(
				this.getSupportFragmentManager(), mFragmentList);
		mPageVp.setAdapter(mFragmentAdapter);
		mPageVp.setCurrentItem(0);

		mPageVp.setOnPageChangeListener(new OnPageChangeListener() {

			/**
			 * state滑动中的状态 有三种状态（0，1，2） 1：正在滑动 2：滑动完毕 0：什么都没做。
			 */
			@Override
			public void onPageScrollStateChanged(int state) {

			}

			/**
			 * position :当前页面，及你点击滑动的页面 offset:当前页面偏移的百分比
			 * offsetPixels:当前页面偏移的像素位置
			 */
			@Override
			public void onPageScrolled(int position, float offset,
					int offsetPixels) {
				LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mTabLineIv
						.getLayoutParams();
				/**
				 * 利用currentIndex(当前所在页面)和position(下一个页面)以及offset来
				 * 设置mTabLineIv的左边距 滑动场景： 记4个页面, 从左到右分别为0,1,2,3 0->1; 1->2;2->3
				 * 3->2,2->1,1->0;
				 */

				if (currentIndex == 0 && position == 0)// 0->1
				{
					lp.leftMargin = (int) (offset * (screenWidth * 1.0 / 4) + currentIndex
							* (screenWidth / 4));

				} else if (currentIndex == 1 && position == 0) // 1->0
				{
					lp.leftMargin = (int) (-(1 - offset)
							* (screenWidth * 1.0 / 4) + currentIndex
							* (screenWidth / 4));

				} else if (currentIndex == 1 && position == 1) // 1->2
				{
					lp.leftMargin = (int) (offset * (screenWidth * 1.0 / 4) + currentIndex
							* (screenWidth / 4));
				} else if (currentIndex == 2 && position == 1) // 2->1
				{
					lp.leftMargin = (int) (-(1 - offset)
							* (screenWidth * 1.0 / 4) + currentIndex
							* (screenWidth / 4));
				} else if (currentIndex == 2 && position == 2) {
					lp.leftMargin = (int) (offset * (screenWidth * 1.0 / 4) + currentIndex
							* (screenWidth / 4));
				} else if (currentIndex == 3 && position == 2) {
					lp.leftMargin = (int) (-(1 - offset)
							* (screenWidth * 1.0 / 4) + currentIndex
							* (screenWidth / 4));
				}
				mTabLineIv.setLayoutParams(lp);
			}

			@Override
			public void onPageSelected(int position) {
				resetTextView();
				switch (position) {
				case 0:
					mTabCommentTv.setTextColor(Color.parseColor("#9adfff"));
					break;
				case 1:
					mTabSystemTv.setTextColor(Color.parseColor("#9adfff"));
					break;
				case 2:
					mTabActivityTv.setTextColor(Color.parseColor("#9adfff"));
					break;
				case 3:
					mTabOfficialTv.setTextColor(Color.parseColor("#9adfff"));
					break;
				}
				currentIndex = position;
			}
		});
		MyViewOnClickListener myViewOnClickListener = new MyViewOnClickListener();
		mTabCommentTv.setOnClickListener(myViewOnClickListener);
		mTabSystemTv.setOnClickListener(myViewOnClickListener);
		mTabActivityTv.setOnClickListener(myViewOnClickListener);
		mTabOfficialTv.setOnClickListener(myViewOnClickListener);
	}

	/**
	 * 设置滑动条的宽度为屏幕的1/4(根据Tab的个数而定)
	 */
	private void initTabLineWidth() {
		DisplayMetrics dpMetrics = new DisplayMetrics();
		getWindow().getWindowManager().getDefaultDisplay()
				.getMetrics(dpMetrics);
		screenWidth = dpMetrics.widthPixels;
		LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mTabLineIv
				.getLayoutParams();
		lp.width = screenWidth / 4;
		mTabLineIv.setLayoutParams(lp);
	}

	/**
	 * 重置颜色
	 */
	private void resetTextView() {
		mTabCommentTv.setTextColor(Color.parseColor("#9adfff"));
		mTabSystemTv.setTextColor(Color.parseColor("#9adfff"));
		mTabActivityTv.setTextColor(Color.parseColor("#9adfff"));
		mTabOfficialTv.setTextColor(Color.parseColor("#9adfff"));
	}

	/*
	 * 创建一个监听事件
	 */
	private class MyViewOnClickListener implements View.OnClickListener {

		@Override
		public void onClick(View v) {
			int mBtnId = v.getId();
			switch (mBtnId) {
			case R.id.id_comment_tv:
				mPageVp.setCurrentItem(0);
				resetTextView();
				mTabCommentTv.setTextColor(Color.parseColor("#9adfff"));
				break;
			case R.id.id_system_tv:
				mPageVp.setCurrentItem(1);
				resetTextView();
				mTabSystemTv.setTextColor(Color.parseColor("#9adfff"));
				break;
			case R.id.id_activity_tv:
				mPageVp.setCurrentItem(2);
				resetTextView();
				mTabActivityTv.setTextColor(Color.parseColor("#9adfff"));
				break;
			case R.id.id_official_tv:
				mPageVp.setCurrentItem(3);
				resetTextView();
				mTabOfficialTv.setTextColor(Color.parseColor("#9adfff"));
				break;
			}
		}
	}
}