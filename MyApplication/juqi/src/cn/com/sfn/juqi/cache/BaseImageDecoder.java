package cn.com.sfn.juqi.cache;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import cn.com.sfn.juqi.util.BitmapUtil;
import cn.com.sfn.juqi.util.DisplayUtil;
import cn.com.sfn.juqi.util.StreamUtil;


/**
 * TODO 从本地文件解析bitmap
 */
public class BaseImageDecoder  implements ImageDecoder {
	
	protected static final int BUFFER_SIZE = 32 * 1024; // 32 Kb
	private int reqWidth, reqHeight;
	
	public BaseImageDecoder(Context context) {
		// TODO Auto-generated constructor stub
		reqWidth = DisplayUtil.getDeviceWidthHeight(context)[0] - 2
				* DisplayUtil.dipToPixel(context,10.0f);// 默认要显示的图片宽度
		reqHeight = reqWidth * 2 / 3;
	}
	
 
	@Override
	public Bitmap decode(String filePath) {
		// TODO Auto-generated method stub
		BufferedInputStream imageStream = null;
		try {
			imageStream = new BufferedInputStream(new FileInputStream(filePath), BUFFER_SIZE);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(imageStream == null) return null;
		
		Bitmap bmp =  getBitmapByStream(imageStream, (int) new File(filePath).length());
		bmp = rotateBitmap(filePath, bmp);
		return bmp;
	}
	
	
	public void setReqWidthAndHeight(int reqWidth,int reqHeight ) {
		this.reqHeight = reqHeight;
		this.reqWidth = reqWidth;
	}
	
	
	private Bitmap getBitmapByStream(InputStream inputStream, int contentLength) {
		try {
			byte[] byteData = StreamUtil.readStream(inputStream);
			if (null == byteData || byteData.length <= 0)
				return null;
			inputStream.close();
			if (contentLength == byteData.length) {
				return decodeSampledBitmapFromByte(byteData);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	
	public Bitmap decodeSampledBitmapFromByte(byte[] data) {
		if (data == null || data.length <= 0)
			return null;
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeByteArray(data, 0, data.length, options);

		// Calculate inSampleSize
		int inSampleSize = calculateInSampleSize(options);
		// int newWidth = size[1];;
		// int newHeight= size[2];;

//		if (compression) {
			options.inSampleSize = inSampleSize;
//		} else {
//			options.inSampleSize = 1;
//			compression = true;
//		}
		options.inJustDecodeBounds = false;

		// Bitmap bitmap =
		// 对缩放后的图按所要展示的宽高显示
		// 得到缩放后图的宽高
		return BitmapFactory.decodeByteArray(data, 0, data.length, options);
	}
	
	/*
	 * 计算图片的缩放比
	 */
	public int calculateInSampleSize(BitmapFactory.Options options) {
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;
		int inWidthSampleSize = 0;
		int inHeightSampleSize = 0;
		if (reqHeight <= 0)
			reqHeight = reqWidth * height / width;
		if (width > reqWidth) {
			inWidthSampleSize = Math.round((float) width / (float) reqWidth);
		}
		if (height > reqHeight) {
			inHeightSampleSize = Math.round((float) height / (float) reqHeight);
		}
		
		inSampleSize = inWidthSampleSize > inHeightSampleSize ? inWidthSampleSize
				: inHeightSampleSize;
		inSampleSize = inSampleSize <= 0 ? 1 : inSampleSize;
		return inSampleSize;// new int[]{inSampleSize,newWidth,newHeight};
	}
	
	
	/*
	 * 最小的边为750
	 */
	public Bitmap calculateInSampleSize(String path, int px) throws Exception {
		 BitmapFactory.Options options = new BitmapFactory.Options();
		 options.inJustDecodeBounds = true;//不加载bitmap到内存中  
		 BitmapFactory.decodeFile(path, options);
		 int outWidth = options.outWidth;
		 int outHeight = options.outHeight;
		 options.inDither = false;  
	     options.inPreferredConfig = Bitmap.Config.ARGB_8888;
	     options.inSampleSize = 1;
	     options.inJustDecodeBounds = false;

 	     int min = outWidth < outHeight ? outWidth:outHeight;
	     if(min <= px) {
	    	 //不用压缩，最短边小于750
	    	 Bitmap bmp = BitmapFactory.decodeFile(path, options);
	    	 bmp = rotateBitmap(path, bmp);
		 	return bmp;
	     }
	     
	     int scale = 1;
	     int scale_x = outWidth / px;
	     int scale_y = outHeight / px;
	     scale = scale_x < scale_y ? scale_x : scale_y;
	     
	     options.inSampleSize = scale;
	     
	     Bitmap bmp = BitmapFactory.decodeFile(path, options);
	     if(bmp == null) return null;
	  
	     double scale_width = outWidth * 1.0 / px;
	     double scale_height = outHeight * 1.0/ px;
	     double sacle_final = scale_width < scale_height? scale_width:scale_height;
	     bmp = Bitmap.createScaledBitmap(bmp,(int) (outWidth / sacle_final),(int)( outHeight /sacle_final ), false);
    	 return rotateBitmap(path, bmp);
	}


	private Bitmap rotateBitmap(String path, Bitmap bmp) {
		int degree = BitmapUtil.readPictureDegree(path);

		if (degree != 0) {// 旋转照片角度
			bmp = BitmapUtil.rotateBitmap(bmp, degree);
		}
		return bmp;
	}

	
}
