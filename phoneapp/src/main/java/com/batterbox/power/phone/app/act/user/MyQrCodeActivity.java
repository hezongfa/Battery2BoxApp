package com.batterbox.power.phone.app.act.user;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.batterbox.power.phone.app.R;
import com.batterbox.power.phone.app.aroute.ARouteHelper;
import com.batterbox.power.phone.app.entity.UserEntity;
import com.batterbox.power.phone.app.utils.UserUtil;
import com.chenyi.baselib.ui.NavigationActivity;
import com.chenyi.baselib.utils.ImageLoaderUtil;
import com.chenyi.baselib.utils.StringUtil;

import cn.bertsir.zbar.utils.QRUtils;

/**
 * Created by ass on 2020-05-05.
 * Description
 */
@Route(path = ARouteHelper.USER_QRCODE)
public class MyQrCodeActivity extends NavigationActivity {
    @Override
    protected int getLayoutId() {
        return R.layout.act_my_qrcode;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setNavigationTitle(R.string.user_13);

    }

    @Override
    protected void onDelayLoad(@Nullable Bundle savedInstanceState) {
        super.onDelayLoad(savedInstanceState);
        UserEntity userEntity = UserUtil.getUserInfo();
        if (userEntity != null) {
            ImageLoaderUtil.load(this, StringUtil.fixNullStr(userEntity.headImg),findViewById(R.id.act_my_qrcode_user_iv));
            ((TextView)findViewById(R.id.act_my_name_tv)).setText(StringUtil.fixNullStr(userEntity.userName));
//            ((TextView)findViewById(R.id.act_my_name_tv)).setText(StringUtil.fixNullStr(userEntity.userName));
            Bitmap bitmap=QRUtils.getInstance().createQRCode(userEntity.mId+"",400,400);
            if (bitmap!=null){
                ((ImageView)findViewById(R.id.act_my_qrcode_iv)).setImageBitmap(bitmap);
            }

        }
    }
}
