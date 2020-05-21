package com.batterbox.power.phone.app.act.main.chat;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.TextView;

import com.batterbox.power.phone.app.R;
import com.batterbox.power.phone.app.act.main.Constants;
import com.batterbox.power.phone.app.http.HttpClient;
import com.batterbox.power.phone.app.http.NormalHttpCallBack;
import com.batterbox.power.phone.app.utils.UserUtil;
import com.chenyi.baselib.entity.ResponseEntity;
import com.chenyi.baselib.ui.NavigationActivity;
import com.chenyi.baselib.utils.StringUtil;
import com.tencent.imsdk.TIMConversationType;
import com.tencent.imsdk.friendship.TIMFriendPendencyItem;
import com.tencent.qcloud.tim.uikit.modules.chat.base.ChatInfo;
import com.tencent.qcloud.tim.uikit.modules.contact.ContactItemBean;
import com.tencent.qcloud.tim.uikit.modules.contact.FriendProfileLayout;
import com.tencent.qcloud.tim.uikit.utils.TUIKitConstants;

public class FriendProfileActivity extends NavigationActivity implements FriendProfileLayout.OnAddressValueListener {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setNavigationTitle(R.string.profile_detail);
        FriendProfileLayout layout = findViewById(R.id.friend_profile);
        layout.setOnAddressValueListener(this);
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
//        Object data=getIntent().getSerializableExtra(TUIKitConstants.ProfileType.CONTENT);
//        String mId = null;
//        if (data instanceof ChatInfo) {
//            ChatInfo mChatInfo = (ChatInfo) data;
//            mId = mChatInfo.getId();
//        } else if (data instanceof ContactItemBean) {
//            ContactItemBean  mContactInfo = (ContactItemBean) data;
//            mId = mContactInfo.getId();
//        } else if (data instanceof TIMFriendPendencyItem) {
//            TIMFriendPendencyItem  mPendencyItem = (TIMFriendPendencyItem) data;
//            mId = mPendencyItem.getIdentifier();
//        }
//        if (StringUtil.isEquals(mId, UserUtil.getUserId())){
//            finish();
//        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.contact_friend_profile_activity;
    }


    @Override
    public void setAddressValue(TextView tv, String path) {
        HttpClient.getInstance().im_getAreaName(path, new NormalHttpCallBack<ResponseEntity<String>>() {
            @Override
            public void onStart() {

            }

            @Override
            public void onSuccess(ResponseEntity<String> responseEntity) {
                tv.setText(getString(R.string.main_chat_6) + StringUtil.fixNullStr(responseEntity.getData()));
            }

            @Override
            public void onFail(ResponseEntity<String> responseEntity, String msg) {

            }
        });
    }
}
