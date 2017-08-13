package cn.com.wx.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;

import java.lang.reflect.Field;


public class DisplayUtil {
	public static int[] wh;


	
	/**
	 * @param activity
	 * @return 状态栏的高度
	 */
	public static int getStatusHeight(Activity activity) {
		Rect frame = new Rect();
		activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
		int statusBarHeight = frame.top;
		return statusBarHeight;
	}
	
	 public static int getStatusBarHeight(Context context){
	        Class<?> c = null;
	        Object obj = null;
	        Field field = null;
	        int x = 0, statusBarHeight = 0;
	        try {
	            c = Class.forName("com.android.internal.R$dimen");
	            obj = c.newInstance();
	            field = c.getField("status_bar_height");
	            x = Integer.parseInt(field.get(obj).toString());
	            statusBarHeight = context.getResources().getDimensionPixelSize(x);
	        } catch (Exception e1) {
	            e1.printStackTrace();
	        }
	        return statusBarHeight;
	    }

	/**
	 * 根据手机的分辨率从 dip 的单位 转成为 px(像素)
	 */
	public static int dipToPixel(Context context, float dpValue) {
		float scale = context.getResources()
				.getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	/**
	 * 根据手机的分辨率从 px(像素) 的单位 转成为 dip
	 */
	public static int pixelToDip(Context context, float pxValue) {
		float scale = context.getResources()
				.getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}

	/**
	 * @return 返回屏幕的宽高
	 */
	public static int[] getDeviceWidthHeight(Context context) {
		WindowManager wm = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics dm = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(dm);
		return new int[] { dm.widthPixels, dm.heightPixels };
	}

	/**
	 * （适配图片素材） 该方法是通过图片的wh，来等比设置view的wh，所以如果当前设置的imageview的src的话，
	 * 所以请在外层将scaleType设置成是centerCrop或者Fitxy
	 * @param view
	 */
	public static int adapterUI(View view) {
		if (view == null)
			return 0;

		int referW = 640;

		Drawable drawable = view.getBackground();

		if (drawable == null || !(drawable instanceof BitmapDrawable)) {
			if (view instanceof ImageView)
				drawable = ((ImageView) view).getDrawable();
		}
		if (drawable == null || !(drawable instanceof BitmapDrawable))
			return 0;
		Bitmap bp = ((BitmapDrawable) drawable).getBitmap();
		ViewGroup.LayoutParams lp = view.getLayoutParams();

		float scalew = bp.getWidth() * 1.0f / referW;
		float bw = DisplayUtil.wh[0] * scalew;
		lp.width = (int) (bw);
		lp.height = (int) (bw / (bp.getWidth() * 1.0f / bp.getHeight()));
		// 临时加的
		return lp.height;
	}

	public static int adapterUI(View view, int w, int h) {
		if (view == null)
			return 0;

		int referW = 640;

		ViewGroup.LayoutParams lp = view.getLayoutParams();

		float scalew = w * 1.0f / referW;
		float bw = DisplayUtil.wh[0] * scalew;
		lp.width = (int) (bw);
		if(h != 0)//等于0表示高度自适应
		lp.height = (int) (bw / (w * 1.0f / h));
		// 临时加的
		return lp.height;
	}

	public static void adapterHeadLayout(View view) {
		/*if (view != null) {
			int h = adapterUI(view);
			View left = view.findViewById(R.id.leftImg);

			((ImageView) left).setImageDrawable(DisplayUtil.createSelector(
					YmallApplication.getContext(), R.drawable.back_img));
			adapterUI(left, 48, h);
			left.getLayoutParams().width = left.getLayoutParams().width + left.getPaddingLeft() + left.getPaddingRight();

			View r = view.findViewById(R.id.rightImg);
			adapterUI(r, 48, h);
			r.getLayoutParams().width = r.getLayoutParams().width + r.getPaddingLeft() + r.getPaddingRight();
		}*/
	}

	public static Drawable createSelector(Context mContext, int res) {
		/*if (res <= 0)
			return null;

		Drawable mIcon = mContext.getResources().getDrawable(res);

		if (mIcon instanceof StateListDrawable)
			return mIcon;

		StateListDrawable states = new StateListDrawable();
		Drawable d = new BitmapDrawable(mContext.getResources(),
				BitmapUtil2.setAlpha(mIcon, 255 / 2));

		states.addState(new int[] { android.R.attr.state_pressed }, d);
		states.addState(new int[] { android.R.attr.state_selected }, d);
		states.addState(new int[] { android.R.attr.state_focused }, d);
		states.addState(new int[] { -android.R.attr.state_enabled }, d);
		states.addState(new int[] {}, mIcon);
		return states;*/
		return null;
	}
	
	/*
	 * sp转换成px
	 */
	public static float spTopx(Context context, float sp){
		DisplayMetrics metrics =context.getResources()
				.getDisplayMetrics();
		return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, metrics);
	}
	// 将px值转换为dip或dp值，保证尺寸大小不变
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    // 将dip或dp值转换为px值，保证尺寸大小不变
    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    // 将px值转换为sp值，保证文字大小不变
    public static int px2sp(Context context, float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    // 将sp值转换为px值，保证文字大小不变
    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    // 屏幕宽度（像素）
    public static int getWindowWidth(Activity context) {
        DisplayMetrics metric = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(metric);
        return metric.widthPixels;
    }

    // 屏幕高度（像素）
    public static int getWindowHeight(Activity context) {
        DisplayMetrics metric = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(metric);
        return metric.heightPixels;
    }
}
