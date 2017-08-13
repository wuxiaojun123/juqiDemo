package cn.com.sfn.juqi.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.media.ExifInterface;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import cn.com.sfn.juqi.App;

public class BitmapUtil {

	private static final String TAG = "BitmapUtil";
	private static float roundPx = DisplayUtil.dipToPixel(App.getApplication(),10.0f);

	/*
	 * 如果图片是正方形的，将ratio设置为2，如果图片不是正方形，自己再做个截图吧！将图片截成需要的正方形。显示圆角边长1/4,则传入8，以此类推！
	 */
	public static Bitmap toRoundCorner(Bitmap bitmap, float ratio) {
		System.out.println("图片是否变成圆形模式了+++++++++++++");
		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
				bitmap.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(output);

		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		final RectF rectF = new RectF(rect);

		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		canvas.drawRoundRect(rectF, bitmap.getWidth() / ratio,
				bitmap.getHeight() / ratio, paint);

		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);
		return output;
	}

	// 圆角化
	public static Bitmap getRoundedCornerBitmap(Bitmap bitmap) {

		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
				bitmap.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(output);

		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		final RectF rectF = new RectF(rect);

		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);

		return output;
	}

	/*
	 * 得到不变形的图片
	 */
	public static Bitmap getBitmap(Bitmap bitmap, int width) {
		int w = bitmap.getWidth();
		int h = bitmap.getHeight();
		Matrix matrix = new Matrix();
		float scale = (float) width / w;
		// 保证图片不变形.
		matrix.postScale(scale, scale);
		// w,h是原图的属性.
		return Bitmap.createBitmap(bitmap, 0, 0, w, h, matrix, true);
	}

	/*
	 * 判断照片角度
	 */
	public static int readPictureDegree(String path) {
		int degree = 0;
		try {
			ExifInterface exifInterface = new ExifInterface(path);
			int orientation = exifInterface.getAttributeInt(
					ExifInterface.TAG_ORIENTATION,
					ExifInterface.ORIENTATION_NORMAL);
			switch (orientation) {
			case ExifInterface.ORIENTATION_ROTATE_90:
				degree = 90;
				break;
			case ExifInterface.ORIENTATION_ROTATE_180:
				degree = 180;
				break;
			case ExifInterface.ORIENTATION_ROTATE_270:
				degree = 270;
				break;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return degree;
	}

	/*
	 * 旋转照片
	 */
	public static Bitmap rotateBitmap(Bitmap bitmap, int degress) {
		if (bitmap != null && !bitmap.isRecycled()) {
			Matrix m = new Matrix();
			m.postRotate(degress);
			bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
					bitmap.getHeight(), m, true);
			return bitmap;
		}
		return bitmap;
	}

	/*
	 * 计算图片的缩放值
	 */
	public static int calculateInSampleSize(BitmapFactory.Options options,
			int reqWidth, int reqHeight) {
		int width = options.outWidth;
		int height = options.outHeight;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {
			int widthRatio = Math.round((float) width / (float) reqWidth);
			int heightRatio = Math.round((float) height / (float) reqHeight);
			inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
		}
		return inSampleSize;
	}

	/*
	 * 根据路径获得图片并压缩，返回bitmap用于显示
	 */
	public static Bitmap getSmallBitmap(String filePath, int reqWidth,
										int reqHeight) {
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(filePath, options);

		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(options, reqWidth,
				reqHeight);

		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;

		return BitmapFactory.decodeFile(filePath, options);
	}

	/*
	 * 把bitmap转换成String
	 */
	@SuppressLint("NewApi")
	public static String bitmapToString(String filePath) {

		Bitmap bm = getSmallBitmap(filePath, 0, 0);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bm.compress(Bitmap.CompressFormat.JPEG, 40, baos);
		byte[] b = baos.toByteArray();
		return Base64.encodeToString(b, Base64.DEFAULT);
	}

	/*
	 * 压缩图片，处理某些手机拍照角度旋转的问题
	 */
	public static String compressImage(Context context, String filePath,
									   String fileName, int q) throws FileNotFoundException {

		Bitmap bm = getSmallBitmap(filePath, 0, 0);

		int degree = readPictureDegree(filePath);

		if (degree != 0) {// 旋转照片角度
			bm = rotateBitmap(bm, degree);
		}

		File imageDir = null;// SDCardUtils.getImageDir(context);

		File outputFile = new File(imageDir, fileName);

		FileOutputStream out = new FileOutputStream(outputFile);

		bm.compress(Bitmap.CompressFormat.JPEG, q, out);

		return outputFile.getPath();
	}

	/**
	 * 以最省内存的方式读取本地资源的图片
	 * 
	 * @param context
	 * @param resId
	 * @return
	 */
	public static Bitmap readBitMap(Context context, int resId) {
		BitmapFactory.Options opt = new BitmapFactory.Options();
		opt.inPreferredConfig = Config.RGB_565;
		opt.inPurgeable = true;
		opt.inInputShareable = true;
		// 获取资源图片
		InputStream is = context.getResources().openRawResource(resId);
		return BitmapFactory.decodeStream(is, null, opt);
	}

	/** 水平方向模糊度 */
	private static float hRadius = 50;
	/** 竖直方向模糊度 */
	private static float vRadius = 50;
	/** 模糊迭代度 */
	private static int iterations = 1;

	/**
	 * 高斯模糊效果
	 * 

	 */
	public static Bitmap BoxBlurFilter(Bitmap bmp) {
		if (bmp == null) {
			return null;
		}
		try {
			int width = bmp.getWidth();
			int height = bmp.getHeight();
			int[] inPixels = new int[width * height];
			int[] outPixels = new int[width * height];
			Bitmap bitmap = Bitmap.createBitmap(width, height,
					Config.ARGB_8888);
			bmp.getPixels(inPixels, 0, width, 0, 0, width, height);
			for (int i = 0; i < iterations; i++) {
				blur(inPixels, outPixels, width, height, hRadius);
				blur(outPixels, inPixels, height, width, vRadius);
			}
			blurFractional(inPixels, outPixels, width, height, hRadius);
			blurFractional(outPixels, inPixels, height, width, vRadius);
			bitmap.setPixels(inPixels, 0, width, 0, 0, width, height);
			return bitmap;
		} catch (Exception e) {
			return bmp;
		}
	}

	public static void blur(int[] in, int[] out, int width, int height,
			float radius) {
		int widthMinus1 = width - 1;
		int r = (int) radius;
		int tableSize = 2 * r + 1;
		int divide[] = new int[256 * tableSize];

		for (int i = 0; i < 256 * tableSize; i++)
			divide[i] = i / tableSize;

		int inIndex = 0;

		for (int y = 0; y < height; y++) {
			int outIndex = y;
			int ta = 0, tr = 0, tg = 0, tb = 0;

			for (int i = -r; i <= r; i++) {
				int rgb = in[inIndex + clamp(i, 0, width - 1)];
				ta += (rgb >> 24) & 0xff;
				tr += (rgb >> 16) & 0xff;
				tg += (rgb >> 8) & 0xff;
				tb += rgb & 0xff;
			}

			for (int x = 0; x < width; x++) {
				out[outIndex] = (divide[ta] << 24) | (divide[tr] << 16)
						| (divide[tg] << 8) | divide[tb];

				int i1 = x + r + 1;
				if (i1 > widthMinus1)
					i1 = widthMinus1;
				int i2 = x - r;
				if (i2 < 0)
					i2 = 0;
				int rgb1 = in[inIndex + i1];
				int rgb2 = in[inIndex + i2];

				ta += ((rgb1 >> 24) & 0xff) - ((rgb2 >> 24) & 0xff);
				tr += ((rgb1 & 0xff0000) - (rgb2 & 0xff0000)) >> 16;
				tg += ((rgb1 & 0xff00) - (rgb2 & 0xff00)) >> 8;
				tb += (rgb1 & 0xff) - (rgb2 & 0xff);
				outIndex += height;
			}
			inIndex += width;
		}
	}

	public static void blurFractional(int[] in, int[] out, int width,
			int height, float radius) {
		radius -= (int) radius;
		float f = 1.0f / (1 + 2 * radius);
		int inIndex = 0;

		for (int y = 0; y < height; y++) {
			int outIndex = y;

			out[outIndex] = in[0];
			outIndex += height;
			for (int x = 1; x < width - 1; x++) {
				int i = inIndex + x;
				int rgb1 = in[i - 1];
				int rgb2 = in[i];
				int rgb3 = in[i + 1];

				int a1 = (rgb1 >> 24) & 0xff;
				int r1 = (rgb1 >> 16) & 0xff;
				int g1 = (rgb1 >> 8) & 0xff;
				int b1 = rgb1 & 0xff;
				int a2 = (rgb2 >> 24) & 0xff;
				int r2 = (rgb2 >> 16) & 0xff;
				int g2 = (rgb2 >> 8) & 0xff;
				int b2 = rgb2 & 0xff;
				int a3 = (rgb3 >> 24) & 0xff;
				int r3 = (rgb3 >> 16) & 0xff;
				int g3 = (rgb3 >> 8) & 0xff;
				int b3 = rgb3 & 0xff;
				a1 = a2 + (int) ((a1 + a3) * radius);
				r1 = r2 + (int) ((r1 + r3) * radius);
				g1 = g2 + (int) ((g1 + g3) * radius);
				b1 = b2 + (int) ((b1 + b3) * radius);
				a1 *= f;
				r1 *= f;
				g1 *= f;
				b1 *= f;
				out[outIndex] = (a1 << 24) | (r1 << 16) | (g1 << 8) | b1;
				outIndex += height;
			}
			out[outIndex] = in[width - 1];
			inIndex += width;
		}
	}

	public static int clamp(int x, int a, int b) {
		return (x < a) ? a : (x > b) ? b : x;
	}
	
}
