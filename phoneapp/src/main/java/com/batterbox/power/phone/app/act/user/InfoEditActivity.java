package com.batterbox.power.phone.app.act.user;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.EditText;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.batterbox.power.phone.app.R;
import com.batterbox.power.phone.app.aroute.ARouteHelper;
import com.chenyi.baselib.ui.NavigationActivity;
import com.chenyi.baselib.utils.StringUtil;

/**
 * Created by ass on 2019-08-13.
 * Description
 */
@Route(path = ARouteHelper.USER_INFO_EDIT)
public class InfoEditActivity extends NavigationActivity {
    @Autowired
    public String title;
    @Autowired
    public String content;
    @Autowired
    public String hint;
    EditText et;

    @Override
    protected int getLayoutId() {
        return R.layout.act_info_edit;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setNavigationTitle(StringUtil.fixNullStr(title));
        et = findViewById(R.id.act_info_edit_et);
        et.setHint(StringUtil.fixNullStr(hint));
        et.setText(StringUtil.fixNullStr(content));
        initNavigationRight(getString(R.string.app_11), v -> {
            Intent intent = new Intent();
            intent.putExtra("content", et.getText().toString());
            setResult(RESULT_OK, intent);
            finish();
        });
    }
}
