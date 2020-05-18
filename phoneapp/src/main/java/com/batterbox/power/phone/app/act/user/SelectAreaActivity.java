package com.batterbox.power.phone.app.act.user;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.LinearLayout;

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

import java.util.ArrayList;

@Route(path = ARouteHelper.USER_SELECTAREA)
public class SelectAreaActivity extends NavListActivity<SelectAreaEntity> {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setLoadMoreEnable(false);
        setNavigationTitle(R.string.user_15);
    }

    @Override
    protected QuickDelegateAdapter<SelectAreaEntity> getAdapter() {
        return new QuickDelegateAdapter<SelectAreaEntity>(this, R.layout.item_select_area) {
            @Override
            protected void onSetItemData(BaseViewHolder holder, SelectAreaEntity item, int viewType) {
                holder.setText(R.id.item_select_area_tv, StringUtil.fixNullStr(item.name));
                LinearLayout ll = holder.getView(R.id.item_select_area_ll);
                ll.setPadding(ViewUtil.getDimen(context, R.dimen.x40) * item.getLevel(), 0, 0, 0);
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
                                    adapter.addAll(holder.getAdapterPosition()+1, responseEntity.getData());
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
    protected void getData(int page, int pageSize) {
        HttpClient.getInstance().im_getAreaData(0, new NormalHttpCallBack<ResponseEntity<ArrayList<SelectAreaEntity>>>(this) {
            @Override
            public void onStart() {

            }

            @Override
            public void onSuccess(ResponseEntity<ArrayList<SelectAreaEntity>> responseEntity) {
                adapter.replaceAll(responseEntity.getData());
                checkEmpty();
            }

            @Override
            public void onFail(ResponseEntity<ArrayList<SelectAreaEntity>> responseEntity, String msg) {
                checkEmpty();
            }
        });
    }

}
