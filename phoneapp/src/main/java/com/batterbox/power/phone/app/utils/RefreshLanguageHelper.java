package com.batterbox.power.phone.app.utils;

import android.content.Context;

import com.batterbox.power.phone.app.R;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;

/**
 * Created by ass on 2019-10-09.
 * Description
 */
public class RefreshLanguageHelper {
    public static void init(Context context) {
        ClassicsHeader.REFRESH_HEADER_PULLDOWN = context.getString(R.string.refresh_1);
        ClassicsHeader.REFRESH_HEADER_REFRESHING = context.getString(R.string.refresh_2);
        ClassicsHeader.REFRESH_HEADER_RELEASE = context.getString(R.string.refresh_3);
        ClassicsHeader.REFRESH_HEADER_FINISH = context.getString(R.string.refresh_4);
        ClassicsFooter.REFRESH_FOOTER_LOADING = context.getString(R.string.refresh_5);
        ClassicsFooter.REFRESH_FOOTER_FINISH = context.getString(R.string.refresh_6);

    }
}
