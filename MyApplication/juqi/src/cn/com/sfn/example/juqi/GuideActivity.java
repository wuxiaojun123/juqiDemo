package cn.com.sfn.example.juqi;

import java.util.ArrayList;

import com.example.juqi.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;

public class GuideActivity extends Activity {
	// 包裹滑动图片的LinearLayout
	private ViewGroup viewPics;
	private ViewPager viewPager;
	// 分页显示的view的数组
	private ArrayList<View> pageViews;

	// 首次进入则叫出
	@SuppressLint("InflateParams")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// 将分页显示的view放入数组中
		LayoutInflater inflater = getLayoutInflater();
		pageViews = new ArrayList<View>();
		pageViews.add(inflater.inflate(R.layout.guide_page2, null));
		// 从指定的XML文件加载视图
		viewPics = (ViewGroup) inflater.inflate(R.layout.activity_guide, null);
		viewPager = (ViewPager) viewPics.findViewById(R.id.guidePages);
		setContentView(viewPics);
		// 设置viewpager的适配器和监听事件
		viewPager.setAdapter(new GuidePageAdapter());
		viewPager.setOnPageChangeListener(new GuidePageChangeListener());
	}

	private Button.OnClickListener Button_OnClickListener = new Button.OnClickListener() {
		public void onClick(View v) {
			// 设置已经向导
			setGuided();

			// 跳转
			Intent mIntent = new Intent();
			mIntent.setClass(GuideActivity.this, MainActivity.class);
			GuideActivity.this.startActivity(mIntent);
			GuideActivity.this.finish();
		}
	};

	private static final String SHAREDPREFERENCES_NAME = "my_pref";
	private static final String KEY_GUIDE_ACTIVITY = "guide_activity";

	private void setGuided() {
		SharedPreferences settings = getSharedPreferences(
				SHAREDPREFERENCES_NAME, 0);
		SharedPreferences.Editor editor = settings.edit();
		editor.putString(KEY_GUIDE_ACTIVITY, "false");
		editor.commit();
	}

	class GuidePageAdapter extends PagerAdapter {

		// 销毁position位置的界面
		@Override
		public void destroyItem(View v, int position, Object obj) {
			((ViewPager) v).removeView(pageViews.get(position));
		}

		@Override
		public void finishUpdate(View arg0) {

		}

		// 获取当前窗体界面数
		@Override
		public int getCount() {
			return pageViews.size();
		}

		// 初始化position位置的界面
		@Override
		public Object instantiateItem(View v, int position) {
			((ViewPager) v).addView(pageViews.get(position));

			// 测试
			if (position == 0) {
				Button closeButton = (Button) findViewById(R.id.btn_close_guide);
				closeButton.setOnClickListener(Button_OnClickListener);
			}
			return pageViews.get(position);
		}

		// 判断是否由对象生成界面
		@Override
		public boolean isViewFromObject(View v, Object obj) {
			return v == obj;
		}

		@Override
		public void startUpdate(View arg0) {

		}

		@Override
		public int getItemPosition(Object obj) {
			return super.getItemPosition(obj);
		}

		@Override
		public void restoreState(Parcelable arg0, ClassLoader arg1) {

		}

		@Override
		public Parcelable saveState() {
			return null;
		}
	}

	class GuidePageChangeListener implements OnPageChangeListener {
		@Override
		public void onPageScrollStateChanged(int arg0) {

		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {

		}

		@Override
		public void onPageSelected(int position) {

		}
	}
}
