package cn.com.wx.util;

import android.content.Context;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

/**
 * Created by wuxiaojun on 17-3-2.
 */

public class GlideUtils {


    public static void loadImage(String imgUrl, ImageView imageView) {
        if (!TextUtils.isEmpty(imgUrl)) {
            Glide.with(imageView.getContext())
                    .load(imgUrl)
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
//                    .error(R.mipmap.img_default)
//                    .placeholder(R.mipmap.img_default)
                    .crossFade()
                    .into(imageView);
        }
        // .centerCrop()
    }

    public static void loadLocalImage(int resId, ImageView imageView) {
        Glide.with(imageView.getContext())
                .load(resId)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
//                .error(R.mipmap.img_default)
//                .placeholder(R.mipmap.img_default)
                .crossFade()
                .into(imageView);
        // .centerCrop()
    }

    public static void loadGIFImage(Context context, String imgUrl, ImageView imageView) {
        if (!TextUtils.isEmpty(imgUrl)) {
            Glide.clear(imageView);
            imageView.setImageDrawable(null);
            Glide.with(context)
                    .load(imgUrl)
                    .asBitmap()
                    .centerCrop()
//                    .placeholder(R.mipmap.img_default)
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .into(imageView);
        }
    }

    public static void loadCircleImage(String imgUrl, ImageView imageView) {
        if (!TextUtils.isEmpty(imgUrl)) {
            Glide.with(imageView.getContext())
                    .load(imgUrl)
                    .transform(new GlideCircleTransform(imageView.getContext()))
//                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
//                    .error(R.mipmap.img_default)
//                    .placeholder(R.mipmap.img_default)
                    .into(imageView);
        }
    }

    /*public static void setCacheDir(){
        File cacheDir = context.getExternalCacheDir();//指定的是数据的缓存地址
        int diskCacheSize = 1024 * 1024 * 30;//最多可以缓存多少字节的数据
        //设置磁盘缓存大小
        Glide.with(imageView.getContext()).setDiskCache(new DiskLruCacheFactory(cacheDir.getPath(), "glide", diskCacheSize));
    }*/

}
