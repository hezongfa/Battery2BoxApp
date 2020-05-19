package com.batterbox.power.phone.app.act.main.chat;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.batterbox.power.phone.app.BatterBoxApp;
import com.batterbox.power.phone.app.R;
import com.batterbox.power.phone.app.act.main.Constants;
import com.batterbox.power.phone.app.aroute.ARouteHelper;
import com.chenyi.baselib.ui.NavigationActivity;
import com.tencent.imsdk.TIMConversationType;
import com.tencent.qcloud.tim.uikit.modules.chat.base.ChatInfo;
import com.tencent.qcloud.tim.uikit.modules.contact.ContactItemBean;
import com.tencent.qcloud.tim.uikit.modules.contact.ContactListView;
import com.tencent.qcloud.tim.uikit.utils.TUIKitConstants;

/**
 * Created by ass on 2020-05-03.
 * Description
 */
@Route(path = ARouteHelper.CHAT_FRIENDS)
public class FriendListActivity extends NavigationActivity {
    private ContactListView mContactListView;

    @Override
    protected int getLayoutId() {
        return R.layout.act_friend_list;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContactListView = findViewById(R.id.contact_list_view);
        mContactListView.setSingleSelectMode(true);
        mContactListView.setOnItemClickListener(new ContactListView.OnItemClickListener() {
            @Override
            public void onItemClick(int position, ContactItemBean contact) {
                startConversation(contact);
            }
        });
        mContactListView.setOnItemClickListener(new ContactListView.OnItemClickListener() {
            @Override
            public void onItemClick(int position, ContactItemBean contact) {
                if (position == 0) {
                    Intent intent = new Intent(BatterBoxApp.getInstance(), NewFriendActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    BatterBoxApp.getInstance().startActivity(intent);
//                } else if (position == 1) {
//                    Intent intent = new Intent(BatterBoxApp.getInstance(), GroupListActivity.class);
//                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    BatterBoxApp.getInstance().startActivity(intent);
//                } else if (position == 2) {
//                    Intent intent = new Intent(BatterBoxApp.getInstance(), BlackListActivity.class);
//                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    BatterBoxApp.getInstance().startActivity(intent);
                } else {
                    Intent intent = new Intent(BatterBoxApp.getInstance(), FriendProfileActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra(TUIKitConstants.ProfileType.CONTENT, contact);
                    startActivity(intent);
                }
            }
        });
//        mContactListView.setOnSelectChangeListener(new ContactListView.OnSelectChangedListener() {
//            @Override
//            public void onSelectChanged(ContactItemBean contact, boolean selected) {
//                if (selected) {
//                    if (mSelectedItem == contact) {
//                        // 相同的Item，忽略
//                    } else {
//                        if (mSelectedItem != null) {
//                            mSelectedItem.setSelected(false);
//                        }
//                        mSelectedItem = contact;
//                    }
//                } else {
//                    if (mSelectedItem == contact) {
//                        mSelectedItem.setSelected(false);
//                    }
//                }
//            }
//        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mContactListView.loadDataSource(ContactListView.DataSource.CONTACT_LIST);
    }

    public void startConversation(ContactItemBean mSelectedItem) {
//        if (mSelectedItem == null || !mSelectedItem.isSelected()) {
//            ToastUtil.toastLongMessage("请选择聊天对象");
//            return;
//        }
        ChatInfo chatInfo = new ChatInfo();
        chatInfo.setType(TIMConversationType.C2C);
        chatInfo.setId(mSelectedItem.getId());
        String chatName = mSelectedItem.getId();
        if (!TextUtils.isEmpty(mSelectedItem.getRemark())) {
            chatName = mSelectedItem.getRemark();
        } else if (!TextUtils.isEmpty(mSelectedItem.getNickname())) {
            chatName = mSelectedItem.getNickname();
        }
        chatInfo.setChatName(chatName);
        Intent intent = new Intent(BatterBoxApp.getInstance(), ChatActivity.class);
        intent.putExtra(Constants.CHAT_INFO, chatInfo);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        BatterBoxApp.getInstance().startActivity(intent);

        finish();
    }
}
