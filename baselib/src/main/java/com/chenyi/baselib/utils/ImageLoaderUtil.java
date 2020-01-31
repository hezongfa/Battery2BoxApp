package com.chenyi.baselib.utils;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;

import java.io.File;

/**
 * Created by ass on 2018/3/24.
 */

public class ImageLoaderUtil {
    private static RequestOptions options = new RequestOptions()
            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC);

    public static void load(Context context, String url, ImageView target) {
        if (target == null) return;
        Glide.with(context).setDefaultRequestOptions(options).load(StringUtil.fixNullStr(url)).into(target);
    }

    public static void load(Context context, File file, ImageView target) {
        if (target == null) return;
        Glide.with(context).setDefaultRequestOptions(options).load(file).into(target);
    }

    public static void load(Context context, String url, ImageView target, @DrawableRes int def) {
        if (target == null) return;
        //设置图片的显示格式ARGB_8888(指图片大小为32bit)
        RequestOptions options = new RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .error(def)
                .placeholder(def);
        Glide.with(context).setDefaultRequestOptions(options).load(StringUtil.fixNullStr(url)).into(target);
    }

    public static void load_round(Context context, String url, ImageView target, int corner) {
        RoundedCorners roundedCorners = new RoundedCorners(corner);
        RequestOptions options = RequestOptions.bitmapTransform(roundedCorners);
        Glide.with(context).load(StringUtil.fixNullStr(url)).apply(options).into(target);
    }
}
