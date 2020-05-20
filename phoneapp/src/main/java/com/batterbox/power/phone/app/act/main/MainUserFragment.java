package com.batterbox.power.phone.app.act.main;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.batterbox.power.phone.app.BatterBoxApp;
import com.batterbox.power.phone.app.R;
import com.batterbox.power.phone.app.aroute.ARouteHelper;
import com.batterbox.power.phone.app.entity.UserEntity;
import com.batterbox.power.phone.app.http.HttpClient;
import com.batterbox.power.phone.app.http.NormalHttpCallBack;
import com.batterbox.power.phone.app.utils.CnyUtil;
import com.batterbox.power.phone.app.utils.UserUtil;
import com.chenyi.baselib.entity.ResponseEntity;
import com.chenyi.baselib.ui.BaseFragment;
import com.chenyi.baselib.utils.ImageLoaderUtil;
import com.chenyi.baselib.utils.StringUtil;
import com.chenyi.baselib.utils.ViewUtil;

/**
 * Created by ass on 2020-05-02.
 * Description
 */
public class MainUserFragment extends BaseFragment {
    ImageView headIv;
    TextView nameTv, balanceTv, couponTv;


    @Override
    protected int getLayout() {
        return R.layout.fragment_main_user;
    }

    @Override
    protected void findView() {
        headIv = findViewById(R.id.lay_main_menu_head_iv);
        nameTv = findViewById(R.id.lay_main_menu_name_tv);
        balanceTv = findViewById(R.id.lay_main_menu_wallet_price_tv);
        couponTv = findViewById(R.id.lay_main_menu_coupon_count_tv);
        findViewById(R.id.lay_main_menu_wallet_rl).setOnClickListener(v -> ARouteHelper.wallet_my().navigation());
        findViewById(R.id.lay_main_menu_record_tv).setOnClickListener(v -> ARouteHelper.order_list().navigation());
        findViewById(R.id.lay_main_menu_coupon_tv).setOnClickListener(v -> ARouteHelper.coupon().navigation());
        findViewById(R.id.lay_main_menu_activity_tv).setOnClickListener(v -> ARouteHelper.promotion().navigation());
        findViewById(R.id.lay_main_menu_help_tv).setOnClickListener(v -> ARouteHelper.helper_detail(BatterBoxApp.lat + "", BatterBoxApp.lng + "").navigation());
        findViewById(R.id.lay_main_menu_more_tv).setOnClickListener(v -> ARouteHelper.setting().navigation());
        findViewById(R.id.lay_main_menu_head_ll).setOnClickListener(v -> ARouteHelper.user_info().navigation());
        findViewById(R.id.lay_main_menu_business_tv).setOnClickListener(v -> ARouteHelper.cooperation().navigation());
        findViewById(R.id.lay_main_menu_invite_tv).setOnClickListener(v -> ARouteHelper.share().navigation());
    }

    @Override
    protected void init(Bundle bundle) {
    }

    @Override
    public void onResume() {
        super.onResume();
        HttpClient.getInstance().user_info(new NormalHttpCallBack<ResponseEntity<UserEntity>>() {
            @Override
            public void onStart() {

            }

            @Override
            public void onSuccess(ResponseEntity<UserEntity> responseEntity) {
                if (responseEntity != null) {
                    UserUtil.saveUserInfo(responseEntity.getData());
                    setData(responseEntity.getData());
                }
            }

            @Override
            public void onFail(ResponseEntity<UserEntity> responseEntity, String msg) {

            }
        });

    }

    public void setData(UserEntity userEntity) {
        if (userEntity != null) {
            ImageLoaderUtil.load_round(getContext(), userEntity.headImg, headIv, ViewUtil.getDimen(getContext(), R.dimen.x14));
            nameTv.setText(StringUtil.fixNullStr(userEntity.userName));
            balanceTv.setText(CnyUtil.getPriceByUnit(getContext(), userEntity.balance));
            couponTv.setText(StringUtil.fixNullStr(userEntity.couponsNum, "0"));
        }
    }
}
