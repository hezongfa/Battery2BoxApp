package com.batterbox.power.phone.app.act;

import android.widget.ImageView;
import android.widget.TextView;

import com.batterbox.power.phone.app.BatterBoxApp;
import com.batterbox.power.phone.app.R;
import com.batterbox.power.phone.app.act.main.MainActivity;
import com.batterbox.power.phone.app.aroute.ARouteHelper;
import com.batterbox.power.phone.app.entity.UserEntity;
import com.batterbox.power.phone.app.utils.CnyUtil;
import com.batterbox.power.phone.app.utils.UserUtil;
import com.chenyi.baselib.utils.ImageLoaderUtil;
import com.chenyi.baselib.utils.StringUtil;
import com.chenyi.baselib.utils.ViewUtil;

/**
 * Created by ass on 2019-07-31.
 * Description
 */
public class MainMenuHelper {
    private MainActivity mainActivity;
    ImageView headIv;
    TextView nameTv, balanceTv, couponTv;


    public MainMenuHelper(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
        headIv = mainActivity.findViewById(R.id.lay_main_menu_head_iv);
        nameTv = mainActivity.findViewById(R.id.lay_main_menu_name_tv);
        balanceTv = mainActivity.findViewById(R.id.lay_main_menu_wallet_price_tv);
        couponTv = mainActivity.findViewById(R.id.lay_main_menu_coupon_count_tv);
        mainActivity.findViewById(R.id.lay_main_menu_wallet_rl).setOnClickListener(v -> ARouteHelper.wallet_my().navigation());
        mainActivity.findViewById(R.id.lay_main_menu_record_tv).setOnClickListener(v -> ARouteHelper.order_list().navigation());
        mainActivity.findViewById(R.id.lay_main_menu_coupon_tv).setOnClickListener(v -> ARouteHelper.coupon().navigation());
        mainActivity.findViewById(R.id.lay_main_menu_activity_tv).setOnClickListener(v -> ARouteHelper.promotion().navigation());
        mainActivity.findViewById(R.id.lay_main_menu_help_tv).setOnClickListener(v -> ARouteHelper.helper_detail(BatterBoxApp.lat + "", BatterBoxApp.lng + "").navigation());
        mainActivity.findViewById(R.id.lay_main_menu_more_tv).setOnClickListener(v -> ARouteHelper.setting().navigation());
        mainActivity.findViewById(R.id.lay_main_menu_head_ll).setOnClickListener(v -> ARouteHelper.user_info().navigation());
        mainActivity.findViewById(R.id.lay_main_menu_business_tv).setOnClickListener(v -> ARouteHelper.cooperation().navigation());
        mainActivity.findViewById(R.id.lay_main_menu_invite_tv).setOnClickListener(v -> ARouteHelper.share().navigation());

    }

    public void setData() {
        UserEntity userEntity = UserUtil.getUserInfo();
        if (userEntity != null) {
            ImageLoaderUtil.load_round(mainActivity, userEntity.headImg, headIv, ViewUtil.getDimen(mainActivity, R.dimen.x14));
            nameTv.setText(StringUtil.fixNullStr(userEntity.userName));
            balanceTv.setText(CnyUtil.getPriceByUnit(mainActivity, userEntity.balance));
            couponTv.setText(StringUtil.fixNullStr(userEntity.couponsNum, "0"));
        }
    }
}
