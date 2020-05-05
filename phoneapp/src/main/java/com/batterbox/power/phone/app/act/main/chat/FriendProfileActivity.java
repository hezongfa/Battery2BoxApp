package com.batterbox.power.phone.app.act.main.chat;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.batterbox.power.phone.app.R;
import com.batterbox.power.phone.app.act.main.Constants;
import com.chenyi.baselib.ui.NavigationActivity;
import com.tencent.imsdk.TIMConversationType;
import com.tencent.qcloud.tim.uikit.modules.chat.base.ChatInfo;
import com.tencent.qcloud.tim.uikit.modules.contact.ContactItemBean;
import com.tencent.qcloud.tim.uikit.modules.contact.FriendProfileLayout;
import com.tencent.qcloud.tim.uikit.utils.TUIKitConstants;

public class FriendProfileActivity extends NavigationActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setNavigationTitle(R.string.profile_detail);
        FriendProfileLayout layout = findViewById(R.id.friend_profile);
        layout.initData(getIntent().getSerializableExtra(TUIKitConstants.ProfileType.CONTENT));
        layout.setOnButtonClickListener(new FriendProfileLayout.OnButtonClickListener() {
            @Override
            public void onStartConversationClick(ContactItemBean info) {
                ChatInfo chatInfo = new ChatInfo();
                chatInfo.setType(TIMConversationType.C2C);
                chatInfo.setId(info.getId());
                String chatName = info.getId();
                if (!TextUtils.isEmpty(info.getRemark())) {
                    chatName = info.getRemark();
                } else if (!TextUtils.isEmpty(info.getNickname())) {
                    chatName = info.getNickname();
                }
                chatInfo.setChatName(chatName);
                Intent intent = new Intent(FriendProfileActivity.this, ChatActivity.class);
                intent.putExtra(Constants.CHAT_INFO, chatInfo);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }

            @Override
            public void onDeleteFriendClick(String id) {
//                Intent intent = new Intent(DemoApplication.instance(), MainActivity.class);
//                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.contact_friend_profile_activity;
    }

}
