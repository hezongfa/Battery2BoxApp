package com.batterbox.power.phone.app.dialog;

import android.app.Dialog;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.batterbox.power.phone.app.R;
import com.batterbox.power.phone.app.aroute.ARouteHelper;
import com.batterbox.power.phone.app.entity.LBShopEntity;
import com.chenyi.baselib.utils.ImageLoaderUtil;
import com.chenyi.baselib.utils.StringUtil;
import com.chenyi.baselib.utils.ViewUtil;
import com.chenyi.baselib.widget.dialog.BaseDialogFragment;

/**
 * Created by ass on 2019-08-04.
 * Description
 */
public class ShopDesDialog extends BaseDialogFragment {
    LBShopEntity lbShopEntity;

    public void setLbShopEntity(LBShopEntity lbShopEntity) {
        this.lbShopEntity = lbShopEntity;
    }

    @Override
    protected Dialog dialog() {
        Dialog dialog = getBaseDialog();
        dialog.setContentView(R.layout.dialog_shop_des);
        // 设置宽度为屏宽, 靠近屏幕底部。
        Window window = dialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.gravity = Gravity.BOTTOM; // 紧贴底部
        lp.width = WindowManager.LayoutParams.MATCH_PARENT; // 宽度持平
        window.setWindowAnimations(R.style.BottomDialog);
        if (lbShopEntity != null) {
            ImageLoaderUtil.load_round(getContext(), lbShopEntity.shopIco, dialog.findViewById(R.id.dialog_shop_des_iv), ViewUtil.getDimen(getContext(), R.dimen.x16));
            ((TextView) dialog.findViewById(R.id.dialog_shop_des_name_tv)).setText(StringUtil.fixNullStr(lbShopEntity.shopName));
            ((TextView) dialog.findViewById(R.id.dialog_shop_des_time_tv)).setText(StringUtil.fixNullStr(lbShopEntity.businessTime));
            ((TextView) dialog.findViewById(R.id.dialog_shop_des_address_tv)).setText(StringUtil.fixNullStr(lbShopEntity.shopAdress));
            ((TextView) dialog.findViewById(R.id.dialog_shop_des_borrow_count_tv)).setText(getString(R.string.shop_1, lbShopEntity.rentCount));
            ((TextView) dialog.findViewById(R.id.dialog_shop_des_return_count_tv)).setText(getString(R.string.shop_2, lbShopEntity.returnCount));
        }
        dialog.findViewById(R.id.dialog_shop_des_rl).setOnClickListener(v -> dismissAllowingStateLoss());
        dialog.findViewById(R.id.dialog_shop_des_nav_btn).setOnClickListener(v -> {
            if (onNavListener!=null){
                onNavListener.onNav(lbShopEntity);
            }
            dismissAllowingStateLoss();
        });
        dialog.findViewById(R.id.dialog_shop_des_click_rl).setOnClickListener(v -> {
            ARouteHelper.shop_detail(lbShopEntity).navigation();
            dismissAllowingStateLoss();
        });
        return dialog;
    }

    OnNavListener onNavListener;

    public void setOnNavListener(OnNavListener onNavListener) {
        this.onNavListener = onNavListener;
    }

    public interface OnNavListener {
        void onNav(LBShopEntity lbShopEntity);
    }
}
