package com.batterbox.power.phone.app.act.main.main_chat;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.batterbox.power.phone.app.R;
import com.batterbox.power.phone.app.aroute.ARouteHelper;
import com.batterbox.power.phone.app.entity.LBShopEntity;
import com.batterbox.power.phone.app.entity.NotifyDataEntity;
import com.batterbox.power.phone.app.http.HttpClient;
import com.batterbox.power.phone.app.http.NormalHttpCallBack;
import com.chenyi.baselib.entity.ResponseEntity;
import com.chenyi.baselib.ui.BasePagerListView;
import com.chenyi.baselib.ui.HeaderFooterViewModel;
import com.chenyi.baselib.utils.ImageLoaderUtil;
import com.chenyi.baselib.utils.StringUtil;
import com.chenyi.baselib.utils.print.FQT;
import com.chenyi.baselib.widget.vlayoutadapter.BaseViewHolder;
import com.chenyi.baselib.widget.vlayoutadapter.QuickDelegateAdapter;

import java.util.ArrayList;

public class MainChatNotifyView extends BasePagerListView<NotifyDataEntity> {
    public MainChatNotifyView(Context context) {
        super(context);
    }

    public MainChatNotifyView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected HeaderFooterViewModel getFooterView() {
        return new HeaderFooterViewModel(R.layout.item_bottom_empty_view, null) {
            @Override
            public void setData(BaseViewHolder viewHolder, Object object) {

            }
        };
    }

    @Override
    protected QuickDelegateAdapter<NotifyDataEntity> getAdapter() {
        return new QuickDelegateAdapter<NotifyDataEntity>(getContext(), R.layout.item_chat_notify_view) {
            @Override
            protected void onSetItemData(BaseViewHolder holder, NotifyDataEntity item, int viewType) {
                if (StringUtil.isEmpty(item.img)) {
                    holder.setVisible(R.id.item_chat_notify_view_top_ll, true);
                    holder.setVisible(R.id.item_chat_notify_view_top_iv, false);
                } else {
                    holder.setVisible(R.id.item_chat_notify_view_top_ll, false);
                    holder.setVisible(R.id.item_chat_notify_view_top_iv, true);
                    ImageLoaderUtil.load(context, StringUtil.fixNullStr(item.img), holder.getView(R.id.item_chat_notify_view_top_iv));
                }
                holder.setVisible(R.id.item_chat_notify_view_start_tv, !StringUtil.isEmpty(item.defaultBody));
                holder.setText(R.id.item_chat_notify_view_start_tv, StringUtil.fixNullStr(item.defaultBody));
                holder.setText(R.id.item_chat_notify_view_time_tv, StringUtil.fixNullStr(item.registtime));
                holder.setText(R.id.item_chat_notify_view_title_tv, StringUtil.fixNullStr(item.title));
                holder.setText(R.id.item_chat_notify_view_content_tv, item.getKeyValueStr());
                holder.setVisible(R.id.item_chat_notify_view_desc_tv, !StringUtil.isEmpty(item.remarke));
                holder.setText(R.id.item_chat_notify_view_desc_tv, StringUtil.fixNullStr(item.remarke));

                holder.itemView.setOnClickListener(v -> {
                    if (item.jumpType == 1) {
                        if (!StringUtil.isEmpty(item.jumpUrl)) {
                            if (item.jumpUrl.startsWith("ORDERLIST")) {
                                ARouteHelper.order_list().navigation();
                            } else if (item.jumpUrl.startsWith("WALLET")) {
                                ARouteHelper.wallet_my().navigation();
                            } else if (item.jumpUrl.startsWith("SHOP")) {
                                if (item.jumpUrl.contains("shopId=")) {
                                    LBShopEntity lbShopEntity = new LBShopEntity();
                                    lbShopEntity.shopId = item.jumpUrl.split("shopId=")[1];
                                    ARouteHelper.shop_detail(lbShopEntity).navigation();
                                }
                            }
                        }
                    } else if (item.jumpType == 2) {
                        ARouteHelper.hybrid_nav(item.jumpUrl).navigation();
                    }
                });
                holder.setOnClickListener(R.id.item_chat_notify_view_delete_iv, v -> HttpClient.getInstance().message_delete(item.id, new NormalHttpCallBack<ResponseEntity>(getContext()) {
                    @Override
                    public void onStart() {

                    }

                    @Override
                    public void onSuccess(ResponseEntity responseEntity) {
                        remove(item);
                    }

                    @Override
                    public void onFail(ResponseEntity responseEntity, String msg) {
                        FQT.showShort(getContext(), msg);
                    }
                }));
            }

            @Override
            public LayoutHelper onCreateLayoutHelper() {
                return new LinearLayoutHelper();
            }
        };
    }

    @Override
    protected void getData(int page, int pageSize) {
        HttpClient.getInstance().message_list(page, pageSize, new NormalHttpCallBack<ResponseEntity<ArrayList<NotifyDataEntity>>>(this) {
            @Override
            public void onStart() {

            }

            @Override
            public void onSuccess(ResponseEntity<ArrayList<NotifyDataEntity>> responseEntity) {
                handleListData(responseEntity.getData());
                checkEmpty();
            }

            @Override
            public void onFail(ResponseEntity<ArrayList<NotifyDataEntity>> responseEntity, String msg) {
                checkEmpty();
            }
        });
    }
}
