package com.batterbox.power.phone.app.act.user;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.batterbox.power.phone.app.R;
import com.batterbox.power.phone.app.aroute.ARouteHelper;
import com.batterbox.power.phone.app.entity.SelectAreaEntity;
import com.batterbox.power.phone.app.http.HttpClient;
import com.batterbox.power.phone.app.http.NormalHttpCallBack;
import com.chenyi.baselib.entity.ResponseEntity;
import com.chenyi.baselib.ui.BaseActivity;
import com.chenyi.baselib.ui.NavListActivity;
import com.chenyi.baselib.utils.StringUtil;
import com.chenyi.baselib.utils.ViewUtil;
import com.chenyi.baselib.utils.print.FQT;
import com.chenyi.baselib.widget.vlayoutadapter.BaseViewHolder;
import com.chenyi.baselib.widget.vlayoutadapter.QuickDelegateAdapter;
import com.tencent.qcloud.tim.uikit.component.CustomLinearLayoutManager;
import com.tencent.qcloud.tim.uikit.component.indexlib.IndexBar.widget.IndexBar;

import java.util.ArrayList;

@Route(path = ARouteHelper.USER_SELECTAREA)
public class SelectAreaActivity extends NavListActivity<SelectAreaEntity> {
    @Autowired
    public ArrayList<SelectAreaEntity> list;
    @Autowired
    public String title;
    IndexBar mIndexBar;
    CustomLinearLayoutManager mManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FrameLayout pLayout = findViewById(R.id.act_nav_content_p_fl);
        TextView mTvSideBarHint = new TextView(this);
        FrameLayout.LayoutParams tlp = new FrameLayout.LayoutParams(ViewUtil.dip2px(this, 60), ViewUtil.dip2px(this, 60));
        tlp.gravity = Gravity.CENTER;
        mTvSideBarHint.setTextSize(ViewUtil.dip2px(this, 13));
        mTvSideBarHint.setTextColor(Color.WHITE);
        mTvSideBarHint.setGravity(Gravity.CENTER);
        mTvSideBarHint.setVisibility(View.GONE);
        mTvSideBarHint.setBackgroundResource(R.drawable.shape_side_bar_bg);
        mTvSideBarHint.setLayoutParams(tlp);
        pLayout.addView(mTvSideBarHint);
        mIndexBar = new IndexBar(this);
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(ViewUtil.dip2px(this, 24), FrameLayout.LayoutParams.MATCH_PARENT);
        lp.setMargins(0, ViewUtil.dip2px(this, 30), 0, ViewUtil.dip2px(this, 40));
        lp.gravity = Gravity.END;
        mIndexBar.setLayoutParams(lp);
        mIndexBar.setPressedBackground(ContextCompat.getColor(this, R.color.partTranslucent));
        mIndexBar.setPressedShowTextView(mTvSideBarHint)
                .setNeedRealIndex(false)
                .setLayoutManager(mManager);
        pLayout.addView(mIndexBar);
        setRefreshEnable(false);
        setLoadMoreEnable(false);
        if (title != null) {
            setNavigationTitle(title);
        } else {
            setNavigationTitle(R.string.user_15);
        }
    }

    @Override
    protected QuickDelegateAdapter<SelectAreaEntity> getAdapter() {
        mManager = new CustomLinearLayoutManager(this);
        recyclerView.setLayoutManager(mManager);
        return new QuickDelegateAdapter<SelectAreaEntity>(this, R.layout.item_select_area) {
            @Override
            protected void onSetItemData(BaseViewHolder holder, SelectAreaEntity item, int viewType) {
                holder.setText(R.id.item_select_area_tv, StringUtil.fixNullStr(item.name));
//                LinearLayout ll = holder.getView(R.id.item_select_area_ll);
//                ll.setPadding(ViewUtil.getDimen(context, R.dimen.x40) * item.getLevel(), 0, 0, 0);
                holder.setOnClickListener(R.id.item_select_area_tv, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        HttpClient.getInstance().im_getAreaData(item.id, new NormalHttpCallBack<ResponseEntity<ArrayList<SelectAreaEntity>>>((BaseActivity) SelectAreaActivity.this) {
                            @Override
                            public void onStart() {

                            }

                            @Override
                            public void onSuccess(ResponseEntity<ArrayList<SelectAreaEntity>> responseEntity) {
                                if (StringUtil.isEmpty(responseEntity.getData())) {
                                    Intent intent = new Intent();
                                    intent.putExtra("selectAreaEntity", item);
                                    setResult(RESULT_OK, intent);
                                    finish();
                                } else {
//                                    adapter.addAll(holder.getAdapterPosition() + 1, responseEntity.getData());
                                    ARouteHelper.user_selectarea(responseEntity.getData()).withString("title", item.getLevel() == 0 ? item.name : (title + "-" + item.name)).navigation(SelectAreaActivity.this, 1212);
                                }
                            }

                            @Override
                            public void onFail(ResponseEntity<ArrayList<SelectAreaEntity>> responseEntity, String msg) {
                                FQT.showShort(context, msg);
                            }
                        });
                    }
                });
            }

            @Override
            public LayoutHelper onCreateLayoutHelper() {
                return new LinearLayoutHelper();
            }
        };
    }

    @Override
    protected void onDelayLoad(@Nullable Bundle savedInstanceState) {
        if (StringUtil.isEmpty(list)) {
            HttpClient.getInstance().im_getAreaData(0, new NormalHttpCallBack<ResponseEntity<ArrayList<SelectAreaEntity>>>((BaseActivity) this) {
                @Override
                public void onStart() {

                }

                @Override
                public void onSuccess(ResponseEntity<ArrayList<SelectAreaEntity>> responseEntity) {
                    mIndexBar.setSourceDatas(responseEntity.getData()).invalidate();
                    adapter.replaceAll(responseEntity.getData());
                    checkEmpty();
                }

                @Override
                public void onFail(ResponseEntity<ArrayList<SelectAreaEntity>> responseEntity, String msg) {
                    checkEmpty();
                }
            });
        } else {
            mIndexBar.setSourceDatas(list).invalidate();
            adapter.replaceAll(list);
        }
    }

    @Override
    protected void getData(int page, int pageSize) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1212 && resultCode == RESULT_OK) {
            setResult(RESULT_OK, data);
            finish();
        }
    }
}
