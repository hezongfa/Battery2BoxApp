package com.batterbox.power.phone.app.act.main.chat;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.batterbox.power.phone.app.R;
import com.batterbox.power.phone.app.act.WelcomeActivity;
import com.batterbox.power.phone.app.act.main.Constants;
import com.chenyi.baselib.ui.NavigationActivity;
import com.chenyi.baselib.utils.StringUtil;
import com.chenyi.baselib.utils.print.FQL;
import com.chenyi.baselib.widget.PhotoPickHelper;
import com.tencent.qcloud.tim.uikit.modules.chat.base.ChatInfo;

import java.util.Set;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class ChatActivity extends NavigationActivity {

    @NeedsPermission({Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    public void requestPermission() {
    }

    @OnNeverAskAgain({Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    void showNeverAskForCamera() {
        PhotoPickHelper.showDefindDialog(this, getSupportFragmentManager());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        ChatActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    private static final String TAG = ChatActivity.class.getSimpleName();

    private ChatFragment mChatFragment;
    private ChatInfo mChatInfo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.chat_activity);
        chat(getIntent());
        ChatActivityPermissionsDispatcher.requestPermissionWithPermissionCheck(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.chat_activity;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        FQL.i(TAG, "onNewIntent");
        super.onNewIntent(intent);
        chat(intent);
    }

    @Override
    protected void onResume() {
        FQL.i(TAG, "onResume");
        super.onResume();
    }

    public void setTitleStr(String str) {
        setNavigationTitle(StringUtil.fixNullStr(str));
    }

    private void chat(Intent intent) {
        Bundle bundle = intent.getExtras();
        FQL.i(TAG, "bundle: " + bundle + " intent: " + intent);
        if (bundle == null) {
            Uri uri = intent.getData();
            if (uri != null) {
                // ???????????????????????????oppo scheme url??????
                Set<String> set = uri.getQueryParameterNames();
                if (set != null) {
                    for (String key : set) {
                        String value = uri.getQueryParameter(key);
                        FQL.i(TAG, "oppo push scheme url key: " + key + " value: " + value);
                    }
                }
            }
            startSplashActivity();
        } else {

            // ????????????????????????????????????oppo??????????????????????????????????????????????????????ChatActivity??????????????????ext??????
            String ext = bundle.getString("ext");
            FQL.i(TAG, "huawei push custom data ext: " + ext);

            Set<String> set = bundle.keySet();
            if (set != null) {
                for (String key : set) {
                    String value = bundle.getString(key);
                    FQL.i(TAG, "oppo push custom data key: " + key + " value: " + value);
                }
            }
            // ??????????????????????????????

            mChatInfo = (ChatInfo) bundle.getSerializable(Constants.CHAT_INFO);
            if (mChatInfo == null) {
                startSplashActivity();
                return;
            }
            mChatFragment = new ChatFragment();
            mChatFragment.setArguments(bundle);
            getFragmentManager().beginTransaction().replace(R.id.empty_view, mChatFragment).commitAllowingStateLoss();
        }
    }

    private void startSplashActivity() {
        Intent intent = new Intent(ChatActivity.this, WelcomeActivity.class);
        startActivity(intent);
        finish();
    }

}
