package com.chenyi.baselib.widget.vlayoutadapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.alibaba.android.vlayout.DelegateAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ZongfaHe on 16/3/25.
 */
public abstract class BaseQuickDelegateAdapter<T, H extends BaseViewHolder> extends DelegateAdapter.Adapter {
//    protected static final String TAG = BaseQuickDelegateAdapter.class.getSimpleName();

    protected final Context context;


    protected final List<T> data;

    public BaseQuickDelegateAdapter(Context context) {
        this(context, null);
    }

    public BaseQuickDelegateAdapter(Context context, List<T> data) {
        this.data = data == null ? new ArrayList<T>() : new ArrayList<T>(data);
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return onGetViewHolder(parent, viewType);
    }

    public T getItem(int position) {
        if (position >= data.size() || -1 == position) return null;
        return data.get(position);
    }

    @Override
    public int getItemViewType(int position) {
        return 3333;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final H viewHolder = (H) holder;
        T item = getItem(position);
        onSetItemData(viewHolder, item, getItemViewType(position));
    }

    public List<T> getData() {
        return data;
    }

    public void add(T elem) {
        data.add(elem);
//        notifyItemRangeChanged(getItemCount() == 0 ? 1 : getItemCount() , 1);
//        notifyDataSetChanged();
        notifyItemInserted(getItemCount());
    }

    public void add(int index, T elem) {
        data.add(index, elem);
//        notifyItemRangeChanged(getItemCount() - 1, getItemCount() - 1);
        notifyItemInserted(index);
//        notifyDataSetChanged();
    }

    public void addAll(int index, List<T> elem) {
        data.addAll(index, elem);
//        notifyItemRangeChanged(getItemCount() == 0 ? 1 : (getItemCount()-1), 1);
        notifyDataSetChanged();
    }

    public void addAll(List<T> elem) {
        data.addAll(elem);
//        notifyItemRangeChanged(getItemCount() == 0 ? 1 : (getItemCount()-1), 1);
        notifyDataSetChanged();
    }

    public void set(T oldElem, T newElem) {
        set(data.indexOf(oldElem), newElem);
    }

    public void set(int index, T elem) {
        data.set(index, elem);
        notifyDataSetChanged();
    }

    public void remove(T elem) {
        data.remove(elem);

        notifyDataSetChanged();
    }

    public void remove(int index) {
        data.remove(index);
        notifyItemRemoved(index);
//        notifyDataSetChanged();
//        notifyItemRemoved(index);
    }

    public void replaceAll(List<T> elem) {
        data.clear();
        data.addAll(elem);
        notifyDataSetChanged();
    }

    public boolean contains(T elem) {
        return data.contains(elem);
    }

    /**
     * Clear data list
     */
    public void clear() {
        data.clear();
        notifyDataSetChanged();
    }

    protected abstract RecyclerView.ViewHolder onGetViewHolder(ViewGroup parent, int viewType);

    protected abstract void onSetItemData(H holder, T item, int viewType);
}
