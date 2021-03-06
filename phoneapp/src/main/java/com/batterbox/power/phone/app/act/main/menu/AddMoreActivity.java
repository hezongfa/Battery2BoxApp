package com.batterbox.power.phone.app.act.main.menu;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.batterbox.power.phone.app.R;
import com.batterbox.power.phone.app.aroute.ARouteHelper;
import com.batterbox.power.phone.app.entity.SearchUserEntity;
import com.batterbox.power.phone.app.utils.UserUtil;
import com.chenyi.baselib.ui.NavigationActivity;
import com.chenyi.baselib.utils.ImageLoaderUtil;
import com.chenyi.baselib.utils.StringUtil;
import com.chenyi.baselib.utils.print.FQL;
import com.tencent.imsdk.TIMFriendshipManager;
import com.tencent.imsdk.TIMValueCallBack;
import com.tencent.imsdk.friendship.TIMFriendRequest;
import com.tencent.imsdk.friendship.TIMFriendResult;
import com.tencent.imsdk.friendship.TIMFriendStatus;
import com.tencent.qcloud.tim.uikit.utils.TUIKitConstants;
import com.tencent.qcloud.tim.uikit.utils.ToastUtil;

@Route(path = ARouteHelper.CHAT_ADD_MORE)
public class AddMoreActivity extends NavigationActivity {
    @Autowired
    public SearchUserEntity searchUserEntity;
    @Autowired
    public String userId;
    private static final String TAG = AddMoreActivity.class.getSimpleName();

    private EditText mAddWording;
    private boolean mIsGroup = false;
    ImageView iv;
    TextView nameTv, addressTv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setNavigationTitle(R.string.main_chat_4);
        if (getIntent() != null && getIntent().getExtras() != null) {
            mIsGroup = getIntent().getExtras().getBoolean(TUIKitConstants.GroupType.GROUP, mIsGroup);
        }
        if (searchUserEntity != null) {
            userId = searchUserEntity.gfId;
        }
        mAddWording = findViewById(R.id.add_wording);
        iv = findViewById(R.id.contact_add_activity_iv);
        nameTv = findViewById(R.id.contact_add_activity_name_tv);
        addressTv = findViewById(R.id.contact_add_activity_address_tv);
//        if (StringUtil.isEquals(userId, UserUtil.getUserId())){
//            finish();
//        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.contact_add_activity;
    }

    @Override
    protected void onDelayLoad(@Nullable Bundle savedInstanceState) {
        super.onDelayLoad(savedInstanceState);
        if (searchUserEntity != null) {
            ImageLoaderUtil.load(this, StringUtil.fixNullStr(searchUserEntity.headImg), iv,R.drawable.default_head);
            nameTv.setText(StringUtil.fixNullStr(searchUserEntity.username));
            addressTv.setText(getString(R.string.main_chat_6) + StringUtil.fixNullStr(searchUserEntity.adress));
            nameTv.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, searchUserEntity.sex == 1 ? R.mipmap.ic_sex_1 : searchUserEntity.sex == 2 ? R.mipmap.ic_sex_2 : R.mipmap.ic_sex_0, 0);
        }
    }

    public void add(View view) {
        if (mIsGroup) {
            addGroup(view);
        } else {
            addFriend(view);
        }
    }

    public void addFriend(View view) {
        if (TextUtils.isEmpty(userId)) {
            return;
        }
        TIMFriendRequest timFriendRequest = new TIMFriendRequest(userId);
        timFriendRequest.setAddWording(mAddWording.getText().toString());
        timFriendRequest.setAddSource("android");
        TIMFriendshipManager.getInstance().addFriend(timFriendRequest, new TIMValueCallBack<TIMFriendResult>() {
            @Override
            public void onError(int i, String s) {
                FQL.e(TAG, "addFriend err code = " + i + ", desc = " + s);
                ToastUtil.toastShortMessage("Error code = " + i + ", desc = " + s);
            }

            @Override
            public void onSuccess(TIMFriendResult timFriendResult) {
                FQL.i(TAG, "addFriend success result = " + timFriendResult.toString());
                switch (timFriendResult.getResultCode()) {
                    case TIMFriendStatus.TIM_FRIEND_STATUS_SUCC:
                        ToastUtil.toastShortMessage("??????");
                        break;
                    case TIMFriendStatus.TIM_FRIEND_PARAM_INVALID:
                        if (TextUtils.equals(timFriendResult.getResultInfo(), "Err_SNS_FriendAdd_Friend_Exist")) {
                            ToastUtil.toastShortMessage("????????????????????????");
                            break;
                        }
                    case TIMFriendStatus.TIM_ADD_FRIEND_STATUS_SELF_FRIEND_FULL:
                        ToastUtil.toastShortMessage("?????????????????????????????????");
                        break;
                    case TIMFriendStatus.TIM_ADD_FRIEND_STATUS_THEIR_FRIEND_FULL:
                        ToastUtil.toastShortMessage("????????????????????????????????????");
                        break;
                    case TIMFriendStatus.TIM_ADD_FRIEND_STATUS_IN_SELF_BLACK_LIST:
                        ToastUtil.toastShortMessage("????????????????????????????????????");
                        break;
                    case TIMFriendStatus.TIM_ADD_FRIEND_STATUS_FRIEND_SIDE_FORBID_ADD:
                        ToastUtil.toastShortMessage("????????????????????????");
                        break;
                    case TIMFriendStatus.TIM_ADD_FRIEND_STATUS_IN_OTHER_SIDE_BLACK_LIST:
                        ToastUtil.toastShortMessage("????????????????????????????????????");
                        break;
                    case TIMFriendStatus.TIM_ADD_FRIEND_STATUS_PENDING:
                        ToastUtil.toastShortMessage("????????????????????????");
                        break;
                    default:
                        ToastUtil.toastLongMessage(timFriendResult.getResultCode() + " " + timFriendResult.getResultInfo());
                        break;
                }
                finish();
            }
        });
    }

    public void addGroup(View view) {
//        String id = mUserID.getText().toString();
//        if (TextUtils.isEmpty(id)) {
//            return;
//        }
//        TIMGroupManager.getInstance().applyJoinGroup(id, mAddWording.getText().toString(), new TIMCallBack() {
//
//            @Override
//            public void onError(int i, String s) {
//                FQL.e(TAG, "addGroup err code = " + i + ", desc = " + s);
//                ToastUtil.toastShortMessage("Error code = " + i + ", desc = " + s);
//            }
//
//            @Override
//            public void onSuccess() {
//                FQL.i(TAG, "addGroup success");
//                ToastUtil.toastShortMessage("?????????????????????");
//                finish();
//            }
//        });
    }
}
