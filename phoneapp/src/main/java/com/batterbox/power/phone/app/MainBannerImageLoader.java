package com.batterbox.power.phone.app;

import android.content.Context;
import android.widget.ImageView;

import com.batterbox.power.phone.app.entity.ADEntity;
import com.chenyi.baselib.utils.ImageLoaderUtil;
import com.chenyi.baselib.widget.RoundImageView;
import com.youth.banner.loader.ImageLoader;

/**
 * 图片加载类
 */
public class MainBannerImageLoader extends ImageLoader {
    @Override
    public void displayImage(Context context, Object path, ImageView imageView) {
        if (path instanceof ADEntity && ((ADEntity) path).img != null) {
            ImageLoaderUtil.load(context, ((ADEntity) path).img, imageView);
        }
    }

    /**
     * 自定义圆角类
     *
     * @param context
     * @return
     */
    @Override
    public ImageView createImageView(Context context) {
        return new RoundImageView(context);

    }
}