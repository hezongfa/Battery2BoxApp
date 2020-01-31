package com.batterbox.power.phone.app.act;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.batterbox.power.phone.app.R;
import com.batterbox.power.phone.app.aroute.ARouteHelper;
import com.batterbox.power.phone.app.entity.OrderEntity;
import com.batterbox.power.phone.app.http.HttpClient;
import com.batterbox.power.phone.app.http.NormalHttpCallBack;
import com.batterbox.power.phone.app.utils.CnyUtil;
import com.chenyi.baselib.entity.ResponseEntity;
import com.chenyi.baselib.ui.BaseActivity;
import com.chenyi.baselib.ui.NavListActivity;
import com.chenyi.baselib.utils.StringUtil;
import com.chenyi.baselib.utils.ViewUtil;
import com.chenyi.baselib.utils.print.FQT;
import com.chenyi.baselib.widget.ItemSlideHelper;
import com.chenyi.baselib.widget.dialog.DialogUtils;
import com.chenyi.baselib.widget.vlayoutadapter.BaseViewHolder;
import com.chenyi.baselib.widget.vlayoutadapter.QuickDelegateAdapter;

import java.util.ArrayList;

/**
 * Created by ass on 2019-08-03.
 * Description
 */
@Route(path = ARouteHelper.ORDER_LIST)
public class OrderListActivity extends NavListActivity<OrderEntity> {
    OrderEntity unfOrderEntity = new OrderEntity(true, false);
    OrderEntity fOrderEntity = new OrderEntity(true, true);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setNavigationTitle(R.string.main_5);
        recyclerView.addOnItemTouchListener(new ItemSlideHelper(this, new ItemSlideHelper.Callback() {
            @Override
            public int getHorizontalRange(RecyclerView.ViewHolder holder) {
                return ViewUtil.getDimen(OrderListActivity.this, R.dimen.x120);
            }

            @Override
            public RecyclerView.ViewHolder getChildViewHolder(View childView) {
                if (childView == null)
                    return null;

                return recyclerView.getChildViewHolder(childView);
            }

            @Override
            public View findTargetView(float x, float y) {
                return recyclerView.findChildViewUnder(x, y);
            }

            @Override
            public boolean checkTouchRule(RecyclerView.ViewHolder holder) {
                if (holder != null) {
                    if (holder.itemView instanceof TextView) return true;
                    OrderEntity entity = adapter.getItem(holder.getAdapterPosition());
                    if (entity != null) {
                        if (!(entity.state == 2 && entity.payState == 2)) {
                            return true;
                        }
                    }
                }
                return false;
            }

            @Override
            public int getSpanCount() {
                return 1;
            }

            @Override
            public void onSlideOpen() {

            }
        }, ViewUtil.getDimen(OrderListActivity.this, R.dimen.x120)));
    }

    @Override
    protected QuickDelegateAdapter<OrderEntity> getAdapter() {
        return new QuickDelegateAdapter<OrderEntity>(this, R.layout.item_order) {
            @Override
            public int getItemViewType(int position) {
                return getItem(position).showSection ? 1 : 2;
            }

            @Override
            protected RecyclerView.ViewHolder onGetViewHolder(ViewGroup parent, int viewType) {
                if (viewType == 1) {
                    return new BaseViewHolder(context, parent, R.layout.item_order_section);
                }
                return super.onGetViewHolder(parent, viewType);
            }

            @Override
            protected void onSetItemData(BaseViewHolder holder, OrderEntity item, int viewType) {
                if (viewType == 1) {
                    if (item.isDone) {
                        holder.setText(R.id.item_order_section_tv, R.string.order_0);
                        holder.setTextColor(R.id.item_order_section_tv, Color.parseColor("#1F9933"));
                    } else {
                        holder.setText(R.id.item_order_section_tv, R.string.order__0);
                        holder.setTextColor(R.id.item_order_section_tv, Color.RED);
                    }
                } else {
                    holder.itemView.setOnClickListener(v -> ARouteHelper.order_detail(item).navigation());
                    holder.setText(R.id.item_order_no_tv, getString(R.string.order_5, StringUtil.fixNullStr(item.code)));
                    holder.setText(R.id.item_order_state_tv, item.getState(context));
                    holder.setText(R.id.item_order_pay_state_tv, item.getPayState(context));
                    if (item.state == 2) {
                        holder.setTextColor(R.id.item_order_state_tv, Color.parseColor("#1F9933"));
                    } else {
                        holder.setTextColor(R.id.item_order_state_tv, Color.parseColor("#EE8822"));
                    }
                    if (item.payState == 2) {
                        holder.setTextColor(R.id.item_order_pay_state_tv, Color.parseColor("#1F9933"));
                    } else {
                        holder.setTextColor(R.id.item_order_pay_state_tv, Color.RED);
                    }

                    holder.setText(R.id.item_order_time_tv, StringUtil.fixNullStr(item.rentTime));
                    holder.setText(R.id.item_order_address_tv, StringUtil.fixNullStr(item.rentShopName));
                    holder.setText(R.id.item_order_price_tv, CnyUtil.getPriceByUnit(context, item.cost));
                    if (item.state == 2 && item.payState == 2) {
                        holder.setVisible(R.id.item_order_action_ll, false);
                        holder.setOnClickListener(R.id.item_order_del_tv, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                HttpClient.getInstance().order_delOrder(String.valueOf(item.orderId), new NormalHttpCallBack<ResponseEntity>((BaseActivity) OrderListActivity.this) {
                                    @Override
                                    public void onStart() {

                                    }

                                    @Override
                                    public void onSuccess(ResponseEntity responseEntity) {
                                        remove(item);
                                    }

                                    @Override
                                    public void onFail(ResponseEntity responseEntity, String msg) {
                                        FQT.showShort(OrderListActivity.this, msg);
                                    }
                                });
                            }
                        });
                    } else {
                        holder.setOnClickListener(R.id.item_order_error_tv, v -> ARouteHelper.helper_detail("0", "0").navigation());
                        holder.setOnClickListener(R.id.item_order_buy_tv, v -> DialogUtils.showDialog(getSupportFragmentManager(), null, getString(R.string.order_30), getString(R.string.app_16), new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                            }
                        }, getString(R.string.order_37), v1 -> buy(item.orderId)));
                    }

                }
            }

            @Override
            public LayoutHelper onCreateLayoutHelper() {
                return new LinearLayoutHelper();
            }
        };
    }

    @Override
    protected void getData(int page, int pageSize) {
        HttpClient.getInstance().order_findMyOrderList(page, pageSize, new NormalHttpCallBack<ResponseEntity<ArrayList<OrderEntity>>>(this) {
            @Override
            public void onStart() {

            }

            @Override
            public void onSuccess(ResponseEntity<ArrayList<OrderEntity>> responseEntity) {
                if (responseEntity != null) {
                    ArrayList<OrderEntity> finishList = new ArrayList<>();
                    ArrayList<OrderEntity> unfinishList = new ArrayList<>();
                    for (OrderEntity orderEntity : responseEntity.getData()) {
                        if (orderEntity.state == 2 && orderEntity.payState == 2) {
                            if (finishList.size() == 0 && !adapter.getData().contains(fOrderEntity)) {
                                finishList.add(fOrderEntity);
                            }
                            finishList.add(orderEntity);
                        } else {
                            if (unfinishList.size() == 0 && !adapter.getData().contains(unfOrderEntity)) {
                                unfinishList.add(unfOrderEntity);
                            }
                            unfinishList.add(orderEntity);
                        }
                    }
                    ArrayList<OrderEntity> list = new ArrayList<>();
                    list.addAll(unfinishList);
                    list.addAll(finishList);
                    handleListData(list);
                }
                checkEmpty();
            }

            @Override
            public void onFail(ResponseEntity<ArrayList<OrderEntity>> responseEntity, String msg) {
                checkEmpty();
            }
        });
    }

    private void buy(long orderId) {
        HttpClient.getInstance().order_purchase(String.valueOf(orderId), new NormalHttpCallBack<ResponseEntity>((BaseActivity) OrderListActivity.this) {
            @Override
            public void onStart() {

            }

            @Override
            public void onSuccess(ResponseEntity responseEntity) {
                refresh();
            }

            @Override
            public void onFail(ResponseEntity responseEntity, String msg) {
                FQT.showShort(OrderListActivity.this, msg);
            }
        });
    }
}
