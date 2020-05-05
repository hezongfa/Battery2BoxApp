package com.batterbox.power.phone.app.act;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.batterbox.power.phone.app.R;
import com.batterbox.power.phone.app.act.main.MainActivity;
import com.batterbox.power.phone.app.aroute.ARouteHelper;
import com.batterbox.power.phone.app.entity.LanguageEntity;
import com.chenyi.baselib.ui.NavListActivity;
import com.chenyi.baselib.utils.LanguageUtil;
import com.chenyi.baselib.utils.StringUtil;
import com.chenyi.baselib.widget.vlayoutadapter.BaseViewHolder;
import com.chenyi.baselib.widget.vlayoutadapter.QuickDelegateAdapter;

import java.util.ArrayList;

/**
 * Created by ass on 2019-08-09.
 * Description
 */
@Route(path = ARouteHelper.SETTING_LANGUAGE)
public class SelectLanguageActivity extends NavListActivity<LanguageEntity> {
    String language_key;

    @Override
    protected int getBottomLayId() {
        return R.layout.lay_select_language_bottom;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setNavigationTitle(R.string.setting_2);
        language_key = LanguageUtil.getLanguage();
        setLoadMoreEnable(false);
        setRefreshEnable(false);
        findViewById(R.id.act_nav_list_mainLay).setBackgroundResource(R.mipmap.pic_bg);
        ArrayList<LanguageEntity> languageEntities = new ArrayList<>();
        languageEntities.add(new LanguageEntity(getString(R.string.l_3), 1, LanguageUtil.ES));
        languageEntities.add(new LanguageEntity(getString(R.string.l_1), 2, LanguageUtil.ZH));
        languageEntities.add(new LanguageEntity(getString(R.string.l_2), 3, LanguageUtil.EN));
        languageEntities.add(new LanguageEntity(getString(R.string.l_4), 4, LanguageUtil.DE));
        languageEntities.add(new LanguageEntity(getString(R.string.l_5), 5, LanguageUtil.FR));
        languageEntities.add(new LanguageEntity(getString(R.string.l_6), 6, LanguageUtil.IT));
        languageEntities.add(new LanguageEntity(getString(R.string.l_7), 7, LanguageUtil.PT));
        adapter.replaceAll(languageEntities);

        findViewById(R.id.lay_select_language_bottom_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LanguageUtil.saveLanguage(language_key);
                Intent intent = new Intent(SelectLanguageActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });
    }


    @Override
    protected QuickDelegateAdapter<LanguageEntity> getAdapter() {
        return new QuickDelegateAdapter<LanguageEntity>(this, R.layout.item_select_language) {
            @Override
            protected void onSetItemData(BaseViewHolder holder, LanguageEntity item, int viewType) {
                holder.setText(R.id.item_select_language_tv, StringUtil.fixNullStr(item.text));
                if (StringUtil.isEquals(language_key, item.key)) {
                    ((TextView) holder.getView(R.id.item_select_language_tv)).setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.ic_choced, 0);
                } else {
                    ((TextView) holder.getView(R.id.item_select_language_tv)).setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.ic_no_chose, 0);
                }
                holder.itemView.setOnClickListener(v -> {
                    language_key = item.key;
                    notifyDataSetChanged();
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

    }
}
