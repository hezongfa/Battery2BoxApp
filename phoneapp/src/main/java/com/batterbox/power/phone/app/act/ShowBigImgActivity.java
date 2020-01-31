package com.batterbox.power.phone.app.act;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.batterbox.power.phone.app.R;
import com.batterbox.power.phone.app.aroute.ARouteHelper;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.ViewTarget;
import com.bumptech.glide.request.transition.Transition;
import com.chenyi.baselib.ui.BaseActivity;
import com.chenyi.baselib.utils.StringUtil;
import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;

import java.io.File;
import java.util.ArrayList;

@Route(path = ARouteHelper.SHOW_BIG_IMGS)
public class ShowBigImgActivity extends BaseActivity {

    @Autowired
    public String url;
    @Autowired
    public ArrayList<String> img;
    //    SubsamplingScaleImageView tempIv;
    ViewPager viewPager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_show_big_img);
        viewPager = findViewById(R.id.act_show_big_img_vp);


        viewPager.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return img == null ? 0 : img.size();
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                SubsamplingScaleImageView photoView = new SubsamplingScaleImageView(ShowBigImgActivity.this);
                container.addView(photoView);
                photoView.setOnClickListener(view -> finish());
                if (!StringUtil.isEmpty(img.get(position))) {
                    photoView.setDoubleTapZoomDuration(200);// 单位是ms
                    photoView.setPanEnabled(true);
                    photoView.setMaxScale(6.0f);
                    Glide.with(ShowBigImgActivity.this)
                            .load(img.get(position))
                            .downloadOnly(new ViewTarget<SubsamplingScaleImageView, File>(photoView) {

                                @Override
                                public void onResourceReady(@NonNull File resource, @Nullable Transition<? super File> transition) {
                                    photoView.setImage(ImageSource.uri(Uri.fromFile(resource)));
                                }
                            });

                }
                return photoView;
            }

            /**
             * 从当前container中删除指定位置（position）的View
             *
             * @param container
             * @param position
             * @param object
             */
            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                // super.destroyItem(container,position,object); 这一句要删除，否则报错
                container.removeView((View) object);
            }

            /**
             * 确定页视图是否与特定键对象关联
             *
             * @param view
             * @param object
             * @return
             */
            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }
        });
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                ((TextView) findViewById(R.id.act_show_big_img_count_tv)).setText((viewPager.getCurrentItem() + 1) + "/" + img.size());
//                tempIv = (SubsamplingScaleImageView) viewPager.getChildAt(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        if (img != null) {
            if (!StringUtil.isEmpty(url)) {
                viewPager.setCurrentItem(img.indexOf(url));
            }
            ((TextView) findViewById(R.id.act_show_big_img_count_tv)).setText((viewPager.getCurrentItem() + 1) + "/" + img.size());
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        overridePendingTransition(R.anim.alpha_to_front, R.anim.alpha_to_background);
    }
}
