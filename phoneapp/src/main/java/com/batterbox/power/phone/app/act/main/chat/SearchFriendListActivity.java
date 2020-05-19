package com.batterbox.power.phone.app.act.main.chat;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;

import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.batterbox.power.phone.app.R;
import com.chenyi.baselib.ui.SearchListActivity;
import com.chenyi.baselib.utils.StringUtil;
import com.chenyi.baselib.utils.print.FQT;
import com.chenyi.baselib.widget.vlayoutadapter.BaseViewHolder;
import com.chenyi.baselib.widget.vlayoutadapter.QuickDelegateAdapter;
import com.github.promeg.pinyinhelper.Pinyin;
import com.tencent.imsdk.TIMFriendshipManager;
import com.tencent.imsdk.TIMValueCallBack;
import com.tencent.imsdk.friendship.TIMFriend;
import com.tencent.imsdk.friendship.TIMFriendPendencyRequest;
import com.tencent.imsdk.friendship.TIMFriendPendencyResponse;
import com.tencent.imsdk.friendship.TIMPendencyType;
import com.tencent.qcloud.tim.uikit.TUIKit;
import com.tencent.qcloud.tim.uikit.component.picture.imageEngine.impl.GlideEngine;
import com.tencent.qcloud.tim.uikit.modules.contact.ContactItemBean;
import com.tencent.qcloud.tim.uikit.utils.BackgroundTasks;
import com.tencent.qcloud.tim.uikit.utils.ThreadHelper;
import com.tencent.qcloud.tim.uikit.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

public class SearchFriendListActivity extends SearchListActivity<ContactItemBean> {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRefreshEnable(false);
        setLoadMoreEnable(false);
        searchEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                getData(page, pageSize);
            }
        });
    }

    @Override
    protected QuickDelegateAdapter<ContactItemBean> getAdapter() {
        return new QuickDelegateAdapter<ContactItemBean>(this, R.layout.contact_selecable_adapter_item) {
            @Override
            protected void onSetItemData(BaseViewHolder holder, ContactItemBean contactBean, int viewType) {
                if (!TextUtils.isEmpty(contactBean.getRemark())) {
                    holder.setText(R.id.tvCity, contactBean.getRemark());
                } else if (!TextUtils.isEmpty(contactBean.getNickname())) {
                    holder.setText(R.id.tvCity, contactBean.getNickname());
                } else {
                    holder.setText(R.id.tvCity, contactBean.getId());
                }

                holder.setOnClickListener(R.id.selectable_contact_item, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!contactBean.isEnable()) {
                            return;
                        }
//                        holder.ccSelect.setChecked(!holder.ccSelect.isChecked());
//                        contactBean.setSelected(holder.ccSelect.isChecked());
//                        if (mOnClickListener != null) {
//                            mOnClickListener.onItemClick(position, contactBean);
//                        }
//                        if (isSingleSelectMode && position != mPreSelectedPosition && contactBean.isSelected()) {
//                            //单选模式的prePos处理
//                            mData.get(mPreSelectedPosition).setSelected(false);
//                            notifyItemChanged(mPreSelectedPosition);
//                        }
//                        mPreSelectedPosition = position;
                    }
                });
                holder.setVisible(R.id.conversation_unread, false);
                if (TextUtils.equals(TUIKit.getAppContext().getResources().getString(com.tencent.qcloud.tim.uikit.R.string.new_friend), contactBean.getId())) {
                    holder.setImageResource(R.id.ivAvatar, com.tencent.qcloud.tim.uikit.R.drawable.group_new_friend);
                    final TIMFriendPendencyRequest timFriendPendencyRequest = new TIMFriendPendencyRequest();
                    timFriendPendencyRequest.setTimPendencyGetType(TIMPendencyType.TIM_PENDENCY_COME_IN);
                    TIMFriendshipManager.getInstance().getPendencyList(timFriendPendencyRequest, new TIMValueCallBack<TIMFriendPendencyResponse>() {
                        @Override
                        public void onError(int i, String s) {
                            ToastUtil.toastShortMessage("Error code = " + i + ", desc = " + s);
                        }

                        @Override
                        public void onSuccess(TIMFriendPendencyResponse timFriendPendencyResponse) {
                            if (timFriendPendencyResponse.getItems() != null) {
                                int pendingRequest = timFriendPendencyResponse.getItems().size();
                                if (pendingRequest == 0) {
                                    holder.setVisible(R.id.conversation_unread, false);
                                } else {
                                    holder.setVisible(R.id.conversation_unread, true);
                                    holder.setText(R.id.conversation_unread, "" + pendingRequest);
                                }
                            }
                        }
                    });
                } else if (TextUtils.equals(TUIKit.getAppContext().getResources().getString(com.tencent.qcloud.tim.uikit.R.string.group), contactBean.getId())) {
                    holder.setImageResource(R.id.ivAvatar, com.tencent.qcloud.tim.uikit.R.drawable.group_common_list);
                } else if (TextUtils.equals(TUIKit.getAppContext().getResources().getString(com.tencent.qcloud.tim.uikit.R.string.blacklist), contactBean.getId())) {
                    holder.setImageResource(R.id.ivAvatar, com.tencent.qcloud.tim.uikit.R.drawable.group_black_list);
                } else {
                    if (TextUtils.isEmpty(contactBean.getAvatarurl())) {
                        if (contactBean.isGroup()) {
                            holder.setImageResource(R.id.ivAvatar, com.tencent.qcloud.tim.uikit.R.drawable.default_head);
                        } else {
                            holder.setImageResource(R.id.ivAvatar, com.tencent.qcloud.tim.uikit.R.drawable.default_head);
                        }
                    } else {
                        Log.d("vvvvv", "contactBean.getAvatarurl()=" + contactBean.getAvatarurl());
                        GlideEngine.loadImage(holder.getView(R.id.ivAvatar), Uri.parse(contactBean.getAvatarurl()));
                    }
                }

            }

            @Override
            public LayoutHelper onCreateLayoutHelper() {
                return new LinearLayoutHelper();
            }
        };
    }

    @Override
    protected void getData(int page, int pageSize) {
        if (page == 1) {
            loadFriendListDataAsync();
        }
    }


    private void loadFriendListDataAsync() {
        ThreadHelper.INST.execute(new Runnable() {
            @Override
            public void run() {
                // 压测时数据量比较大，query耗时比较久，所以这里使用新线程来处理
                List<TIMFriend> timFriends = TIMFriendshipManager.getInstance().queryFriendList();
                if (timFriends == null) {
                    timFriends = new ArrayList<>();
                }
                fillFriendListData(timFriends);
            }
        });
    }

    private void fillFriendListData(final List<TIMFriend> timFriends) {
        // 外部调用是在其他线程里面，但是更新数据同时会刷新UI，所以需要放在主线程做。
        BackgroundTasks.getInstance().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (timFriends.size() == 0) {
                    TIMFriendshipManager.getInstance().getFriendList(new TIMValueCallBack<List<TIMFriend>>() {
                        @Override
                        public void onError(int code, String desc) {
                            FQT.showShort(SearchFriendListActivity.this, desc);
                        }

                        @Override
                        public void onSuccess(List<TIMFriend> timFriends) {
                            if (timFriends == null) {
                                timFriends = new ArrayList<>();
                            }
                            assembleFriendListData(timFriends);
                        }
                    });
                } else {
                    assembleFriendListData(timFriends);
                }
            }
        });
    }

    private String getPinyin(String key) {
        StringBuilder pySb = new StringBuilder();
        //遍历target的每个char得到它的全拼音
        for (int i1 = 0; i1 < key.length(); i1++) {
            //利用TinyPinyin将char转成拼音
            //查看源码，方法内 如果char为汉字，则返回大写拼音
            //如果c不是汉字，则返回String.valueOf(c)
            pySb.append(Pinyin.toPinyin(key.charAt(i1)).toUpperCase());
        }
        return pySb.toString();
    }

    private void assembleFriendListData(final List<TIMFriend> timFriends) {
        ArrayList<ContactItemBean> list = new ArrayList<>();
        String key = getPinyin(searchEt.getText().toString());
        for (TIMFriend timFriend : timFriends) {
            ContactItemBean info = new ContactItemBean();
            info.covertTIMFriend(timFriend);
            if (!StringUtil.isEmpty(info.getNickname()) && getPinyin(info.getNickname()).contains(key)
                    || !StringUtil.isEmpty(info.getRemark()) && getPinyin(info.getRemark()).contains(key)
                    || !StringUtil.isEmpty(info.getTarget()) && getPinyin(info.getTarget()).contains(key)) {
                list.add(info);
            }
        }
        adapter.replaceAll(list);
    }
}
