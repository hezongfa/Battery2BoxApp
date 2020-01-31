package com.batterbox.power.phone.app.act;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.RelativeSizeSpan;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.batterbox.power.phone.app.R;
import com.batterbox.power.phone.app.aroute.ARouteHelper;
import com.batterbox.power.phone.app.entity.SharePageEntity;
import com.batterbox.power.phone.app.http.HttpClient;
import com.batterbox.power.phone.app.http.NormalHttpCallBack;
import com.chenyi.baselib.entity.ResponseEntity;
import com.chenyi.baselib.ui.NavigationActivity;
import com.chenyi.baselib.utils.StringUtil;
import com.chenyi.baselib.utils.print.FQT;

/**
 * Created by ass on 2019-10-15.
 * Description
 */
@Route(path = ARouteHelper.SHARE)
public class ShareActivity extends NavigationActivity {
    @Override
    protected int getLayoutId() {
        return R.layout.act_share;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void onDelayLoad(@Nullable Bundle savedInstanceState) {
        super.onDelayLoad(savedInstanceState);
        HttpClient.getInstance().us_myInvitation(new NormalHttpCallBack<ResponseEntity<SharePageEntity>>(this) {
            @Override
            public void onStart() {

            }

            @Override
            public void onSuccess(ResponseEntity<SharePageEntity> responseEntity) {
                if (responseEntity != null && responseEntity.getData() != null) {
                    SpannableStringBuilder sb = new SpannableStringBuilder();
                    String time = String.valueOf(responseEntity.getData().giveTime);
                    String str = getString(R.string.share_1, time);
                    sb.append(str);
                    int start = str.indexOf(time);
                    int end = start + time.length();
                    sb.setSpan(new RelativeSizeSpan(1.3f), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    ((TextView) findViewById(R.id.act_share_tv1)).setText(sb);
                    ((TextView) findViewById(R.id.act_share_tv2)).setText(String.valueOf(responseEntity.getData().invitations));
                    ((TextView) findViewById(R.id.act_share_tv3)).setText(String.valueOf(responseEntity.getData().invitationTimes));
                    findViewById(R.id.act_share_btn).setOnClickListener(v -> {
                        Intent intent = new Intent(Intent.ACTION_SEND);
                        intent.setType("text/plain");
                        intent.putExtra(Intent.EXTRA_TEXT, StringUtil.fixNullStr(responseEntity.getData().url));
                        startActivity(Intent.createChooser(intent, getString(R.string.app_name)));
                    });
                }
            }

            @Override
            public void onFail(ResponseEntity<SharePageEntity> responseEntity, String msg) {
                FQT.showShort(ShareActivity.this, msg);
            }
        });
    }
}
