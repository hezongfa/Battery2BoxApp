package com.chenyi.baselib.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;

import com.chenyi.baselib.utils.StringUtil;
import com.chenyi.baselib.utils.sys.KeyBoardUtil;

/**
 * Created by ass on 2018/12/22.
 * Description
 */
public abstract class SearchListActivity<T> extends NavListActivity {
    protected String searchHint;
    protected String searchText;
    protected EditText searchEt;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        searchText = getIntent().getStringExtra("searchText");
        searchHint = getIntent().getStringExtra("searchHint");
        searchEt = NavSearchEditBuilder.build(this, getEtLeftDrawable(), barLay);
        searchEt.setHint(StringUtil.fixNullStr(searchHint));
        searchEt.setText(StringUtil.fixNullStr(searchText));
        searchEt.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                KeyBoardUtil.hideKeyBoardState(SearchListActivity.this, v);
                page = 1;
                getData(page, pageSize);
                return true;
            }
            return false;
        });
    }

    protected int getEtLeftDrawable() {
        return 0;
    }

    protected String getSearchTextContent() {
        return searchEt.getText().toString();
    }
}