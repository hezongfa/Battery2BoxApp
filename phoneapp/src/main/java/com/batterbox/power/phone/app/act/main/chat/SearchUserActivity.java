package com.batterbox.power.phone.app.act.main.chat;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.batterbox.power.phone.app.R;
import com.batterbox.power.phone.app.aroute.ARouteHelper;
import com.batterbox.power.phone.app.entity.SearchUserEntity;
import com.batterbox.power.phone.app.http.HttpClient;
import com.batterbox.power.phone.app.http.NormalHttpCallBack;
import com.chenyi.baselib.entity.ResponseEntity;
import com.chenyi.baselib.ui.NavListActivity;
import com.chenyi.baselib.utils.ImageLoaderUtil;
import com.chenyi.baselib.utils.StringUtil;
import com.chenyi.baselib.widget.vlayoutadapter.BaseViewHolder;
import com.chenyi.baselib.widget.vlayoutadapter.QuickDelegateAdapter;

/**
 * Created by ass on 2020-05-04.
 * Description
 */
@Route(path = ARouteHelper.CHAT_SEARCH_USER)
public class SearchUserActivity extends NavListActivity<SearchUserEntity> {
    EditText et;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setNavigationVisible(false);
        setRefreshEnable(false);
        setLoadMoreEnable(false);
        findViewById(R.id.act_search_user_barback_btn).setOnClickListener(v -> finish());
        et = findViewById(R.id.act_search_user_baret);
        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                getData(1, pageSize);
            }
        });
        et.setOnEditorActionListener((arg0, arg1, arg2) -> {
            if (arg1 == EditorInfo.IME_ACTION_SEARCH) {
                getData(1, pageSize);
            }
            return false;

        });
        checkEmpty();
    }

    @Override
    protected int getTopLayId() {
        return R.layout.lay_search_user_bar;
    }

    @Override
    protected QuickDelegateAdapter<SearchUserEntity> getAdapter() {
        return new QuickDelegateAdapter<SearchUserEntity>(this, R.layout.item_search_user) {
            @Override
            protected void onSetItemData(BaseViewHolder holder, SearchUserEntity item, int viewType) {
                holder.setText(R.id.item_search_user_tv, StringUtil.fixNullStr(item.username));
                ImageLoaderUtil.load(context, StringUtil.fixNullStr(item.headImg), holder.getView(R.id.item_search_user_iv),R.drawable.default_head);
                holder.itemView.setOnClickListener(v -> {
                    item.phone = et.getText().toString();
                    ARouteHelper.chat_add_more(item, null).navigation();
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
        adapter.clear();
        HttpClient.getInstance().im_searchMember(et.getText().toString(), new NormalHttpCallBack<ResponseEntity<SearchUserEntity>>() {
            @Override
            public void onStart() {

            }

            @Override
            public void onSuccess(ResponseEntity<SearchUserEntity> responseEntity) {
                if (responseEntity.getData() != null && !StringUtil.isEmpty(responseEntity.getData().gfId) && StringUtil.stringToLong(responseEntity.getData().gfId) > 0) {
                    adapter.add(responseEntity.getData());
                }
                checkEmpty();
            }

            @Override
            public void onFail(ResponseEntity<SearchUserEntity> responseEntity, String msg) {
//                FQT.showShort(SearchUserActivity.this, msg);
                checkEmpty();
            }
        });
    }

}
