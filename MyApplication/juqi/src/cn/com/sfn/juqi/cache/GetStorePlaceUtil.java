package cn.com.sfn.juqi.cache;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Environment;

import java.io.File;
import java.io.IOException;

import cn.com.sfn.juqi.util.StringUtils;

import static android.os.Environment.MEDIA_MOUNTED;

/**
 *  TODO 获取文件存储的位置(sd卡还是程序里面)
 */
public class GetStorePlaceUtil {
	public static File getCacheDirectory(Context context, boolean preferExternal) {
		return getCacheDirectory(context, null, preferExternal);
	}

	/**
	 * 

	 */
	public static File getCacheDirectory(Context context, String childPath,
										 boolean preferExternal) {
		File appCacheDir = null;
		String externalStorageState;
		try {
			externalStorageState = Environment.getExternalStorageState();
		} catch (NullPointerException e) { // (sh)it happens (Issue #660)
			externalStorageState = "";
		}
		if (preferExternal && MEDIA_MOUNTED.equals(externalStorageState)
				&& hasExternalStoragePermission(context)) {
			appCacheDir = getExternalCacheDir(context);
		}
		if (appCacheDir == null) {
			appCacheDir = context.getCacheDir();
		}
		if (appCacheDir == null) {
			String cacheDirPath = "/data/data/" + context.getPackageName()
					+ "/cache/";
			// L.w("Can't define system cache directory! '%s' will be used.",
			// cacheDirPath);
			appCacheDir = new File(cacheDirPath);
		}
		if (StringUtils.checkStr(childPath)) {
			try {
				appCacheDir = new File(appCacheDir, childPath);
			} catch (Exception e) {
				// L.i("Can't create \".nomedia\" file in application external cache directory");
			}
		}

		return appCacheDir;
	}

	private static File getExternalCacheDir(Context context) {
		File dataDir = new File(new File(
				Environment.getExternalStorageDirectory(), "Android"), "data");
		File appCacheDir = new File(
				new File(dataDir, context.getPackageName()), "cache");
		if (!appCacheDir.exists()) {
			if (!appCacheDir.mkdirs()) {
				// L.w("Unable to create external cache directory");
				return null;
			}
			try {
				new File(appCacheDir, ".nomedia").createNewFile();
			} catch (IOException e) {
				// L.i("Can't create \".nomedia\" file in application external cache directory");
			}
		}
		return appCacheDir;
	}

	private static final String EXTERNAL_STORAGE_PERMISSION = "android.permission.WRITE_EXTERNAL_STORAGE";

	private static boolean hasExternalStoragePermission(Context context) {
		int perm = context
				.checkCallingOrSelfPermission(EXTERNAL_STORAGE_PERMISSION);
		return perm == PackageManager.PERMISSION_GRANTED;
	}

}
