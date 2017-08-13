/*******************************************************************************
 * Copyright 2011-2014 Sergey Tarasevich
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package cn.com.sfn.juqi.cache;

import android.content.Context;
import android.graphics.Bitmap;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Base disk cache.
 *
 */
public class BaseDiskCache implements DiskCache {
	/** {@value */
	public static final int DEFAULT_BUFFER_SIZE = 32 * 1024; // 32 Kb
	/** {@value */
	public static final Bitmap.CompressFormat DEFAULT_COMPRESS_FORMAT = Bitmap.CompressFormat.PNG;
	/** {@value */
	public static final int DEFAULT_COMPRESS_QUALITY = 100;

	private static final String ERROR_ARG_NULL = " argument must be not null";
	private static final String TEMP_IMAGE_POSTFIX = ".tmp";

	protected final File cacheDir;
	protected final File reserveCacheDir;

	protected int bufferSize = DEFAULT_BUFFER_SIZE;

	protected Bitmap.CompressFormat compressFormat = DEFAULT_COMPRESS_FORMAT;
	protected int compressQuality = DEFAULT_COMPRESS_QUALITY;

	/**
	 *            Directory for file caching
	 */

	private static BaseDiskCache baseDiskCache;

	/**
	 *暂时使用单利模式
	 */
	public static BaseDiskCache getInstance(Context context) {
		if (baseDiskCache == null) {
			synchronized (BaseDiskCache.class) {
				if (baseDiskCache == null) {
					baseDiskCache = new BaseDiskCache(
							GetStorePlaceUtil.getCacheDirectory(context, true));
				}
			}
		}
		return baseDiskCache;
	}

	/**
	 * 带子级文件夹
	 */
	public BaseDiskCache(Context context, String childPath) {
		this(GetStorePlaceUtil.getCacheDirectory(context, childPath, true));
	}

	public BaseDiskCache(File cacheDir) {
		this(cacheDir, null);
	}

	/**
	 *
	 */
	private BaseDiskCache(File cacheDir, File reserveCacheDir) {
		if (cacheDir == null) {
			throw new IllegalArgumentException("cacheDir" + ERROR_ARG_NULL);
		}

		this.cacheDir = cacheDir;
		this.reserveCacheDir = reserveCacheDir;
	}

	@Override
	public File getDirectory() {
		return cacheDir;
	}

	@Override
	public File get(String imageUri) {
		return getFile(imageUri);
	}

	@Override
	public boolean save(String imageUri, Bitmap bitmap) throws IOException {
		File imageFile = getFile(imageUri);
		File tmpFile = new File(imageFile.getAbsolutePath());
		OutputStream os = new BufferedOutputStream(
				new FileOutputStream(tmpFile), bufferSize);
		boolean savedSuccessfully = false;

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		int options = 100;
		try {
			savedSuccessfully = bitmap.compress(Bitmap.CompressFormat.JPEG,
					options, baos);
			while (baos.toByteArray().length / 1024 > 300) {
				baos.reset();
				options -= 5;
				savedSuccessfully = bitmap.compress(Bitmap.CompressFormat.JPEG,
						options, baos);
			}
			os.write(baos.toByteArray());
			os.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			os.close();
			if (savedSuccessfully && !tmpFile.renameTo(imageFile)) {
				savedSuccessfully = false;
			}
			if (!savedSuccessfully) {
				tmpFile.delete();
			}
		}
		return savedSuccessfully;
	}

	@Override
	public boolean remove(String imageUri) {
		return getFile(imageUri).delete();
	}

	@Override
	public void close() {
		// Nothing to do
	}

	@Override
	public void clear() {
		File[] files = cacheDir.listFiles();
		if (files != null) {
			for (File f : files) {
				f.delete();
			}
		}
	}

	/**
	 * Returns file object (not null) for incoming image URI. File object can
	 * reference to non-existing file.
	 */
	protected File getFile(String imageUri) {
		String fileName = imageUri;
		File dir = cacheDir;
		if (!cacheDir.exists() && !cacheDir.mkdirs()) {
			if (reserveCacheDir != null
					&& (reserveCacheDir.exists() || reserveCacheDir.mkdirs())) {
				dir = reserveCacheDir;
			}
		}
		return new File(dir, fileName);
	}

	public void setBufferSize(int bufferSize) {
		this.bufferSize = bufferSize;
	}

	public void setCompressFormat(Bitmap.CompressFormat compressFormat) {
		this.compressFormat = compressFormat;
	}

	public void setCompressQuality(int compressQuality) {
		this.compressQuality = compressQuality;
	}

}