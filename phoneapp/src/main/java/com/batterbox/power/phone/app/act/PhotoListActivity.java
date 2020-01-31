package com.batterbox.power.phone.app.act;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.batterbox.power.phone.app.R;
import com.batterbox.power.phone.app.aroute.ARouteHelper;
import com.chenyi.baselib.ui.NavigationActivity;
import com.chenyi.baselib.utils.ImageLoaderUtil;
import com.chenyi.baselib.utils.ViewUtil;
import com.chenyi.baselib.widget.recycleviewadapter.BaseViewHolder;
import com.chenyi.baselib.widget.recycleviewadapter.QuickRecycleAdapter;

import java.util.ArrayList;

/**
 * Created by ass on 2019-11-25.
 * Description
 */
@Route(path = ARouteHelper.PHOTO_LIST)
public class PhotoListActivity extends NavigationActivity {
    RecyclerView rv;
    @Autowired
    public ArrayList<String> img;

    @Override
    protected int getLayoutId() {
        return R.layout.act_photo_list;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rv = findViewById(R.id.act_photo_list_rv);
        rv.setLayoutManager(new GridLayoutManager(this, 2));
        rv.setAdapter(new QuickRecycleAdapter<String>(this, R.layout.item_photo_list, img == null ? new ArrayList<>() : img) {
            @Override
            protected void onSetItemData(BaseViewHolder holder, String item, int viewType) {
                ImageLoaderUtil.load_round(context, item, holder.getView(R.id.item_photo_list_iv), ViewUtil.getDimen(context, R.dimen.x15));
                holder.itemView.setOnClickListener(v -> ARouteHelper.show_big_imgs(img, item).navigation());
            }
        });
    }
}
