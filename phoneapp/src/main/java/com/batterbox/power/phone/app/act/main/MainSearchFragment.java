package com.batterbox.power.phone.app.act.main;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.batterbox.power.phone.app.R;
import com.batterbox.power.phone.app.event.RefreshSearchLbsShopEvent;
import com.chenyi.baselib.ui.BaseFragment;
import com.chenyi.baselib.utils.StringUtil;

import org.greenrobot.eventbus.Subscribe;

/**
 * Created by ass on 2020-05-02.
 * Description
 */
public class MainSearchFragment extends BaseFragment {
    EditText searchEt;
    MainSearchListView mainSearchListView;

    @Override
    protected int getLayout() {
        return R.layout.fragment_main_search;
    }

    @Override
    protected void findView() {
        registerEventBus(this);
        searchEt = findViewById(R.id.fragment_main_search_et);
        searchEt.setHint(R.string.main_1);
        searchEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mainSearchListView.refreshData(searchEt.getText().toString(), null);
            }
        });
        mainSearchListView = findViewById(R.id.fragment_main_search_listView);
    }

    @Override
    protected void init(Bundle bundle) {
//        mainSearchListView.refreshData(searchEt.getText().toString(), null);
    }


    @Subscribe
    public void refreshLbsShop(RefreshSearchLbsShopEvent refreshSearchLbsShopEvent) {
        if (refreshSearchLbsShopEvent != null && !StringUtil.isEmpty(refreshSearchLbsShopEvent.lbShopEntities)) {
            mainSearchListView.refreshData(searchEt.getText().toString(), refreshSearchLbsShopEvent.lbShopEntities);
        }
    }
}
