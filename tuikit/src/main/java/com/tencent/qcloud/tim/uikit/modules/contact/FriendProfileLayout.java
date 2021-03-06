package com.tencent.qcloud.tim.uikit.modules.contact;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.chenyi.baselib.utils.StringUtil;
import com.tencent.imsdk.TIMCallBack;
import com.tencent.imsdk.TIMFriendshipManager;
import com.tencent.imsdk.TIMUserProfile;
import com.tencent.imsdk.TIMValueCallBack;
import com.tencent.imsdk.ext.group.TIMGroupPendencyItem;
import com.tencent.imsdk.friendship.TIMDelFriendType;
import com.tencent.imsdk.friendship.TIMFriend;
import com.tencent.imsdk.friendship.TIMFriendPendencyItem;
import com.tencent.imsdk.friendship.TIMFriendResponse;
import com.tencent.imsdk.friendship.TIMFriendResult;
import com.tencent.qcloud.tim.uikit.R;
import com.tencent.qcloud.tim.uikit.TUIKit;
import com.tencent.qcloud.tim.uikit.base.IUIKitCallBack;
import com.tencent.qcloud.tim.uikit.component.CircleImageView;
import com.tencent.qcloud.tim.uikit.component.LineControllerView;
import com.tencent.qcloud.tim.uikit.component.SelectionActivity;
import com.tencent.qcloud.tim.uikit.component.TitleBarLayout;
import com.tencent.qcloud.tim.uikit.component.dialog.TUIKitDialog;
import com.tencent.qcloud.tim.uikit.component.picture.imageEngine.impl.GlideEngine;
import com.tencent.qcloud.tim.uikit.modules.chat.GroupChatManagerKit;
import com.tencent.qcloud.tim.uikit.modules.chat.base.ChatInfo;
import com.tencent.qcloud.tim.uikit.modules.conversation.ConversationManagerKit;
import com.tencent.qcloud.tim.uikit.modules.group.apply.GroupApplyInfo;
import com.tencent.qcloud.tim.uikit.utils.TUIKitConstants;
import com.tencent.qcloud.tim.uikit.utils.TUIKitLog;
import com.tencent.qcloud.tim.uikit.utils.ToastUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FriendProfileLayout extends LinearLayout implements View.OnClickListener {

    private static final String TAG = FriendProfileLayout.class.getSimpleName();

    private final int CHANGE_REMARK_CODE = 200;

    //    private TitleBarLayout mTitleBar;
    private CircleImageView mHeadImageView;
    private TextView mNickNameView, mAddressView, mPhoneView;
    //    private LineControllerView mIDView;
    private LineControllerView mAddWordingView;
    private TextView mRemarkView;
    private LineControllerView mAddBlackView;
    //    private LineControllerView mChatTopView;
    private TextView mDeleteView;
    private TextView mChatView;

    private ContactItemBean mContactInfo;
    private ChatInfo mChatInfo;
    private TIMFriendPendencyItem mPendencyItem;
    private OnButtonClickListener mListener;
    private String mId;
    private String mNickname;
    private String mAddress;
    private String mRemark;
    private String mPhone;
    private String mAddWords;
    private int mGender;

    public FriendProfileLayout(Context context) {
        super(context);
        init();
    }

    public FriendProfileLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public FriendProfileLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.contact_friend_profile_layout, this);

        mHeadImageView = findViewById(R.id.avatar);
        mNickNameView = findViewById(R.id.name);
        mAddressView = findViewById(R.id.address);
        mPhoneView = findViewById(R.id.phone);
//        mIDView = findViewById(R.id.id);
        mAddWordingView = findViewById(R.id.add_wording);
        mAddWordingView.setCanNav(false);
        mAddWordingView.setSingleLine(false);
        mRemarkView = findViewById(R.id.remark);
        mRemarkView.setOnClickListener(this);
//        mChatTopView = findViewById(R.id.chat_to_top);
        mAddBlackView = findViewById(R.id.blackList);
        mDeleteView = findViewById(R.id.btnDel);
        mDeleteView.setOnClickListener(this);
        mChatView = findViewById(R.id.btnChat);
        mChatView.setOnClickListener(this);
//        mTitleBar = findViewById(R.id.friend_titlebar);
//        mTitleBar.setTitle(getResources().getString(R.string.profile_detail), TitleBarLayout.POSITION.MIDDLE);
//        mTitleBar.getRightGroup().setVisibility(View.GONE);
//        mTitleBar.setOnLeftClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                ((Activity) getContext()).finish();
//            }
//        });
    }

    public void initData(Object data) {
        if (data instanceof ChatInfo) {
            mChatInfo = (ChatInfo) data;
            mId = mChatInfo.getId();
//            mChatTopView.setVisibility(View.VISIBLE);
//            mChatTopView.setChecked(ConversationManagerKit.getInstance().isTopConversation(mId));
//            mChatTopView.setCheckListener(new CompoundButton.OnCheckedChangeListener() {
//                @Override
//                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                    ConversationManagerKit.getInstance().setConversationTop(mId, isChecked);
//                }
//            });
            loadUserProfile(false);
            return;
        } else if (data instanceof ContactItemBean) {
            mContactInfo = (ContactItemBean) data;
            mId = mContactInfo.getId();
            loadUserProfile(false);
            return;
//            mNickname = mContactInfo.getNickname();
//            mRemarkView.setVisibility(VISIBLE);
//            mRemarkView.setText(mRemark = mContactInfo.getRemark());
//            mAddBlackView.setChecked(mContactInfo.isBlackList());
//            mAddBlackView.setCheckListener(new CompoundButton.OnCheckedChangeListener() {
//                @Override
//                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                    if (isChecked) {
//                        addBlack();
//                    } else {
//                        deleteBlack();
//                    }
//                }
//            });
//            if (!TextUtils.isEmpty(mContactInfo.getAvatarurl())) {
//                GlideEngine.loadImage(mHeadImageView, Uri.parse(mContactInfo.getAvatarurl()));
//            }
        } else if (data instanceof TIMFriendPendencyItem) {
            mPendencyItem = (TIMFriendPendencyItem) data;
            mId = mPendencyItem.getIdentifier();
            loadUserProfile(true);
            return;
        } else if (data instanceof GroupApplyInfo) {
            final GroupApplyInfo info = (GroupApplyInfo) data;
            TIMGroupPendencyItem item = ((GroupApplyInfo) data).getPendencyItem();
            mId = item.getIdentifer();
            if (TextUtils.isEmpty(mId)) {
                mId = item.getFromUser();
            }
            mNickname = item.getFromUser();
            mAddWordingView.setVisibility(View.VISIBLE);
            mAddWordingView.setContent(item.getRequestMsg());
            mRemarkView.setVisibility(GONE);
            mAddBlackView.setVisibility(GONE);
            mDeleteView.setText(R.string.refuse);
            mDeleteView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    refuseApply(info);
                }
            });
            mChatView.setText(R.string.accept);
            mChatView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    acceptApply(info);
                }
            });
        }

        if (!TextUtils.isEmpty(mNickname)) {
            mNickNameView.setText(mNickname);
        } else {
            mNickNameView.setText(mId);
        }
//        mIDView.setContent(mId);

    }

    private void updateViewsAdd(ContactItemBean bean) {
        mAddress = bean.getAddress();
        mPhone = bean.getPhone();
        mGender = bean.getGender();
        mNickname = mPendencyItem.getNickname();
        if (!TextUtils.isEmpty(mNickname)) {
            mNickNameView.setText(mNickname);
        } else {
            mNickNameView.setText(mId);
        }
        mNickNameView.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, mGender == 1 ? R.mipmap.ic_sex_1 : mGender == 2 ? R.mipmap.ic_sex_2 : R.mipmap.ic_sex_0, 0);
        setAddressValue(StringUtil.fixNullStr(mAddress));
        mPhoneView.setVisibility(GONE);
        if (!TextUtils.isEmpty(bean.getAvatarurl())) {
            GlideEngine.loadImage(mHeadImageView, Uri.parse(bean.getAvatarurl()));
        }
        mAddWordingView.setVisibility(View.VISIBLE);
        mAddWordingView.setContent(StringUtil.fixNullStr(mPendencyItem.getAddWording()));
        mRemarkView.setVisibility(GONE);
        mAddBlackView.setVisibility(GONE);
        mDeleteView.setText(R.string.refuse);
        mDeleteView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                refuse();
            }
        });
        mChatView.setText(R.string.accept);
        mChatView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                accept();
            }
        });
    }

    private void updateViews(ContactItemBean bean) {
        mContactInfo = bean;
//        mChatTopView.setVisibility(View.VISIBLE);
//        boolean top = ConversationManagerKit.getInstance().isTopConversation(mId);
//        if (mChatTopView.isChecked() != top) {
//            mChatTopView.setChecked(top);
//        }
//        mChatTopView.setCheckListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                ConversationManagerKit.getInstance().setConversationTop(mId, isChecked);
//            }
//        });
        mId = bean.getId();
        mNickname = bean.getNickname();
        mAddress = bean.getAddress();
        mPhone = bean.getPhone();
        mGender = bean.getGender();
        if (bean.isFriend()) {
            mRemarkView.setVisibility(VISIBLE);
            mRemarkView.setText(getContext().getString(R.string.profile_remark) + "???" + (mRemark = bean.getRemark()));
//            mAddBlackView.setVisibility(VISIBLE);
//            mAddBlackView.setChecked(bean.isBlackList());
//            mAddBlackView.setCheckListener(new CompoundButton.OnCheckedChangeListener() {
//                @Override
//                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                    if (isChecked) {
//                        addBlack();
//                    } else {
//                        deleteBlack();
//                    }
//                }
//            });
            mDeleteView.setVisibility(VISIBLE);
        } else {
            mRemarkView.setVisibility(GONE);
            mAddBlackView.setVisibility(GONE);
            mDeleteView.setVisibility(GONE);
        }

        if (!TextUtils.isEmpty(mNickname)) {
            mNickNameView.setText(mNickname);
        } else {
            mNickNameView.setText(mId);
        }
        mNickNameView.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, mGender == 1 ? R.mipmap.ic_sex_1 : mGender == 2 ? R.mipmap.ic_sex_2 : R.mipmap.ic_sex_0, 0);
//        mAddressView.setText(getContext().getString(R.string.main_chat_6) + StringUtil.fixNullStr(mAddress));
        setAddressValue(StringUtil.fixNullStr(mAddress));

        mPhoneView.setText(getContext().getString(R.string.main_chat_9) + StringUtil.fixNullStr(mPhone));
        if (!TextUtils.isEmpty(bean.getAvatarurl())) {
            GlideEngine.loadImage(mHeadImageView, Uri.parse(bean.getAvatarurl()));
        }
//        mIDView.setContent(mId);
    }

    private void setAddressValue(String path) {
        if (onAddressValueListener != null) {
            onAddressValueListener.setAddressValue(mAddressView, path);
        }
    }

    OnAddressValueListener onAddressValueListener;

    public void setOnAddressValueListener(OnAddressValueListener onAddressValueListener) {
        this.onAddressValueListener = onAddressValueListener;
    }

    public interface OnAddressValueListener {
        void setAddressValue(TextView tv, String path);
    }

    private void loadUserProfile(final boolean isAdd) {
        ArrayList<String> list = new ArrayList<>();
        list.add(mId);
        final ContactItemBean bean = new ContactItemBean();
        bean.setFriend(false);
        TIMFriendshipManager.getInstance().getUsersProfile(list, false, new TIMValueCallBack<List<TIMUserProfile>>() {
            @Override
            public void onError(int i, String s) {
                TUIKitLog.e(TAG, "loadUserProfile err code = " + i + ", desc = " + s);
                ToastUtil.toastShortMessage("Error code = " + i + ", desc = " + s);
                ((Activity) getContext()).finish();
            }

            @Override
            public void onSuccess(List<TIMUserProfile> timUserProfiles) {
                if (timUserProfiles == null || timUserProfiles.size() != 1) {
                    return;
                }
                final TIMUserProfile profile = timUserProfiles.get(0);

//                if (!bean.isFriend()){
//
//                    return;
//                }
                bean.setGender(profile.getGender());
                bean.setNickname(profile.getNickName());
                bean.setId(profile.getIdentifier());
                bean.setAvatarurl(profile.getFaceUrl());
                bean.setAddress(profile.getLocation());
                if (profile.getCustomInfo() != null) {
//                    if (profile.getCustomInfo().containsKey("AreaCode")) {
//
//                        bean.setAddress(new String(profile.getCustomInfo().get("AreaCode")));
//                    }
                    if (profile.getCustomInfo().containsKey("Phone")) {
                        bean.setPhone(new String(profile.getCustomInfo().get("Phone")));
                    }
                }
                if (isAdd) {
                    updateViewsAdd(bean);
                } else {
                    updateViews(bean);
                }
            }
        });
        TIMFriendshipManager.getInstance().getBlackList(new TIMValueCallBack<List<TIMFriend>>() {
            @Override
            public void onError(int i, String s) {
                TUIKitLog.e(TAG, "getBlackList err code = " + i + ", desc = " + s);
                ToastUtil.toastShortMessage("Error code = " + i + ", desc = " + s);
            }

            @Override
            public void onSuccess(List<TIMFriend> timFriends) {
                if (timFriends != null && timFriends.size() > 0) {
                    for (TIMFriend friend : timFriends) {
                        if (TextUtils.equals(friend.getIdentifier(), mId)) {
                            bean.setBlackList(true);
                            if (isAdd) {
                                updateViewsAdd(bean);
                            } else {
                                updateViews(bean);
                            }
                            break;
                        }
                    }
                }
            }
        });
        TIMFriendshipManager.getInstance().getFriendList(new TIMValueCallBack<List<TIMFriend>>() {
            @Override
            public void onError(int i, String s) {
                TUIKitLog.e(TAG, "getFriendList err code = " + i + ", desc = " + s);
                ToastUtil.toastShortMessage("Error code = " + i + ", desc = " + s);
            }

            @Override
            public void onSuccess(List<TIMFriend> timFriends) {
                if (timFriends != null && timFriends.size() > 0) {
                    for (TIMFriend friend : timFriends) {
                        if (TextUtils.equals(friend.getIdentifier(), mId)) {
                            bean.setFriend(true);
                            bean.setRemark(friend.getRemark());
                            bean.setAvatarurl(friend.getTimUserProfile().getFaceUrl());
                            if (friend.getCustomInfo() != null) {
//                                if (friend.getCustomInfo().containsKey("AreaCode")) {
//                                    bean.setAddress(new String(friend.getCustomInfo().get("AreaCode")));
//                                }
                                if (friend.getCustomInfo().containsKey("Phone")) {
                                    bean.setPhone(new String(friend.getCustomInfo().get("Phone")));
                                }
                            }
                            break;
                        }
                    }
                }
                if (isAdd) {
                    updateViewsAdd(bean);
                } else {
                    updateViews(bean);
                }
            }
        });
    }

    private void accept() {
        TIMFriendResponse response = new TIMFriendResponse();
        response.setIdentifier(mId);
        response.setResponseType(TIMFriendResponse.TIM_FRIEND_RESPONSE_AGREE_AND_ADD);
        TIMFriendshipManager.getInstance().doResponse(response, new TIMValueCallBack<TIMFriendResult>() {
            @Override
            public void onError(int i, String s) {
                TUIKitLog.e(TAG, "accept err code = " + i + ", desc = " + s);
                ToastUtil.toastShortMessage("Error code = " + i + ", desc = " + s);
            }

            @Override
            public void onSuccess(TIMFriendResult timUserProfiles) {
                TUIKitLog.i(TAG, "accept success");
                mChatView.setText(R.string.accepted);
                ((Activity) getContext()).finish();
            }
        });
    }

    private void refuse() {
        TIMFriendResponse response = new TIMFriendResponse();
        response.setIdentifier(mId);
        response.setResponseType(TIMFriendResponse.TIM_FRIEND_RESPONSE_REJECT);
        TIMFriendshipManager.getInstance().doResponse(response, new TIMValueCallBack<TIMFriendResult>() {
            @Override
            public void onError(int i, String s) {
                TUIKitLog.e(TAG, "refuse err code = " + i + ", desc = " + s);
                ToastUtil.toastShortMessage("Error code = " + i + ", desc = " + s);
            }

            @Override
            public void onSuccess(TIMFriendResult timUserProfiles) {
                TUIKitLog.i(TAG, "refuse success");
                mDeleteView.setText(R.string.refused);
                ((Activity) getContext()).finish();
            }
        });
    }

    public void acceptApply(final GroupApplyInfo item) {
        GroupChatManagerKit.getInstance().getProvider().acceptApply(item, new IUIKitCallBack() {
            @Override
            public void onSuccess(Object data) {
                Intent intent = new Intent();
                intent.putExtra(TUIKitConstants.Group.MEMBER_APPLY, item);
                ((Activity) getContext()).setResult(Activity.RESULT_OK, intent);
                ((Activity) getContext()).finish();
            }

            @Override
            public void onError(String module, int errCode, String errMsg) {
                ToastUtil.toastLongMessage(errMsg);
            }
        });
    }

    public void refuseApply(final GroupApplyInfo item) {
        GroupChatManagerKit.getInstance().getProvider().refuseApply(item, new IUIKitCallBack() {
            @Override
            public void onSuccess(Object data) {
                Intent intent = new Intent();
                intent.putExtra(TUIKitConstants.Group.MEMBER_APPLY, item);
                ((Activity) getContext()).setResult(Activity.RESULT_OK, intent);
                ((Activity) getContext()).finish();
            }

            @Override
            public void onError(String module, int errCode, String errMsg) {
                ToastUtil.toastLongMessage(errMsg);
            }
        });
    }

    private void delete() {
        new TUIKitDialog(getContext())
                .builder()
                .setCancelable(true)
                .setCancelOutside(true)
                .setTitle(getContext().getString(R.string.profile_del1))
                .setDialogWidth(0.75f)
                .setPositiveButton(getContext().getString(R.string.sure), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        List<String> identifiers = new ArrayList<>();
                        identifiers.add(mId);
                        TIMFriendshipManager.getInstance().deleteFriends(identifiers, TIMDelFriendType.TIM_FRIEND_DEL_BOTH, new TIMValueCallBack<List<TIMFriendResult>>() {
                            @Override
                            public void onError(int i, String s) {
                                TUIKitLog.e(TAG, "deleteFriends err code = " + i + ", desc = " + s);
                                ToastUtil.toastShortMessage("Error code = " + i + ", desc = " + s);
                            }

                            @Override
                            public void onSuccess(List<TIMFriendResult> timUserProfiles) {
                                TUIKitLog.i(TAG, "deleteFriends success");
                                ConversationManagerKit.getInstance().deleteConversation(mId, false);
                                if (mListener != null) {
                                    mListener.onDeleteFriendClick(mId);
                                }
                                ((Activity) getContext()).finish();
                            }
                        });
                    }
                })
                .setNegativeButton(getContext().getString(R.string.cancel), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                })
                .show();

    }

    private void chat() {
        if (mListener != null || mContactInfo != null) {
            mListener.onStartConversationClick(mContactInfo);
        }
        ((Activity) getContext()).finish();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnChat) {
            chat();
        } else if (v.getId() == R.id.btnDel) {
            delete();
        } else if (v.getId() == R.id.remark) {
            Bundle bundle = new Bundle();
            bundle.putString(TUIKitConstants.Selection.TITLE, getResources().getString(R.string.profile_remark_edit));
            bundle.putString(TUIKitConstants.Selection.INIT_CONTENT, mRemark);
            bundle.putInt(TUIKitConstants.Selection.LIMIT, 20);
            SelectionActivity.startTextSelection(TUIKit.getAppContext(), bundle, new SelectionActivity.OnResultReturnListener() {
                @Override
                public void onReturn(Object text) {
                    mRemarkView.setText(getContext().getString(R.string.profile_remark) + "???" + (mRemark = text.toString()));
                    if (TextUtils.isEmpty(text.toString())) {
                        text = "";
                    }
                    modifyRemark(text.toString());
                }
            });
        }
    }

    private void modifyRemark(final String txt) {
        HashMap<String, Object> hashMap = new HashMap<>();
        // ??????????????????
        hashMap.put(TIMFriend.TIM_FRIEND_PROFILE_TYPE_KEY_REMARK, txt);
        TIMFriendshipManager.getInstance().modifyFriend(mId, hashMap, new TIMCallBack() {
            @Override
            public void onError(int i, String s) {
                TUIKitLog.e(TAG, "modifyRemark err code = " + i + ", desc = " + s);
            }

            @Override
            public void onSuccess() {
                mContactInfo.setRemark(txt);
                TUIKitLog.i(TAG, "modifyRemark success");
            }
        });
    }

    private void addBlack() {
        String[] idStringList = mId.split(",");

        List<String> idList = new ArrayList<>();
        for (String id : idStringList) {
            idList.add(id);
        }

        TIMFriendshipManager.getInstance().addBlackList(idList, new TIMValueCallBack<List<TIMFriendResult>>() {
            @Override
            public void onError(int i, String s) {
                TUIKitLog.e(TAG, "addBlackList err code = " + i + ", desc = " + s);
                ToastUtil.toastShortMessage("Error code = " + i + ", desc = " + s);
            }

            @Override
            public void onSuccess(List<TIMFriendResult> timFriendResults) {
                TUIKitLog.v(TAG, "addBlackList success");
            }
        });
    }

    private void deleteBlack() {
        String[] idStringList = mId.split(",");

        List<String> idList = new ArrayList<>();
        for (String id : idStringList) {
            idList.add(id);
        }
        TIMFriendshipManager.getInstance().deleteBlackList(idList, new TIMValueCallBack<List<TIMFriendResult>>() {
            @Override
            public void onError(int i, String s) {
                TUIKitLog.e(TAG, "deleteBlackList err code = " + i + ", desc = " + s);
                ToastUtil.toastShortMessage("Error code = " + i + ", desc = " + s);
            }

            @Override
            public void onSuccess(List<TIMFriendResult> timFriendResults) {
                TUIKitLog.i(TAG, "deleteBlackList success");
            }
        });
    }

    public void setOnButtonClickListener(OnButtonClickListener l) {
        mListener = l;
    }

    public interface OnButtonClickListener {
        void onStartConversationClick(ContactItemBean info);

        void onDeleteFriendClick(String id);
    }

}
