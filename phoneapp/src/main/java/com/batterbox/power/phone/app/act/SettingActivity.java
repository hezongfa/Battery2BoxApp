package com.batterbox.power.phone.app.act;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.batterbox.power.phone.app.BuildConfig;
import com.batterbox.power.phone.app.R;
import com.batterbox.power.phone.app.aroute.ARouteHelper;
import com.batterbox.power.phone.app.http.DomainHelper;
import com.batterbox.power.phone.app.utils.ImUtil;
import com.batterbox.power.phone.app.utils.UserUtil;
import com.chenyi.baselib.ui.NavigationActivity;

/**
 * Created by ass on 2019-08-08.
 * Description
 */
@Route(path = ARouteHelper.SETTING)
public class SettingActivity extends NavigationActivity {
    @Override
    protected int getLayoutId() {
        return R.layout.act_setting;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setNavigationTitle(R.string.setting_1);
        findViewById(R.id.act_setting_language_tv).setOnClickListener(v -> ARouteHelper.setting_language().navigation());
        findViewById(R.id.act_setting_logout_btn).setOnClickListener(v -> {
            UserUtil.cleanToken();
            UserUtil.cleanUserInfo();
            ImUtil.logout();
            finish();
        });
        findViewById(R.id.act_setting_change_pwd_tv).setOnClickListener(v -> ARouteHelper.setting_changepwd().navigation());
        findViewById(R.id.act_setting_tv1).setOnClickListener(v -> ARouteHelper.hybrid_nav(DomainHelper.getSpecificationH5(), getString(R.string.setting_3)).navigation());
        findViewById(R.id.act_setting_tv2).setOnClickListener(v -> ARouteHelper.hybrid_nav(DomainHelper.getAgreementH5(), getString(R.string.setting_4)).navigation());
        findViewById(R.id.act_setting_tv3).setOnClickListener(v -> ARouteHelper.hybrid_nav(DomainHelper.getAboutUsH5(), getString(R.string.setting_5)).navigation());
        ((TextView) findViewById(R.id.act_setting_tv4)).setText(getString(R.string.app_name) + "  " + BuildConfig.VERSION_NAME);
    }
}
