package com.batterbox.power.phone.app.act.main.chat;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.batterbox.power.phone.app.R;
import com.batterbox.power.phone.app.aroute.ARouteHelper;
import com.chenyi.baselib.ui.NavigationActivity;
import com.chenyi.baselib.utils.print.FQL;
import com.tencent.imsdk.TIMFriendshipManager;
import com.tencent.imsdk.TIMValueCallBack;
import com.tencent.imsdk.friendship.TIMFriendPendencyItem;
import com.tencent.imsdk.friendship.TIMFriendPendencyRequest;
import com.tencent.imsdk.friendship.TIMFriendPendencyResponse;
import com.tencent.imsdk.friendship.TIMPendencyType;
import com.tencent.qcloud.tim.uikit.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

public class NewFriendActivity extends NavigationActivity {

    private static final String TAG = NewFriendActivity.class.getSimpleName();

    private ListView mNewFriendLv;
    private NewFriendListAdapter mAdapter;
    private TextView mEmptyView;
    private List<TIMFriendPendencyItem> mList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setNavigationTitle(R.string.new_friend);
        initNavigationRight(getString(R.string.add_friend), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ARouteHelper.chat_search_user().navigation();
//                Intent intent = new Intent(BatterBoxApp.getInstance(), AddMoreActivity.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                intent.putExtra("isGroup", false);
//                startActivity(intent);
            }
        });
        init();

    }

    @Override
    protected int getLayoutId() {
        return R.layout.contact_new_friend_activity;
    }

    @Override
    protected void onResume() {
        super.onResume();
        initPendency();
    }

    private void init() {
        mNewFriendLv = findViewById(R.id.new_friend_list);
        mEmptyView = findViewById(R.id.empty_text);
    }

    private void initPendency() {
        final TIMFriendPendencyRequest timFriendPendencyRequest = new TIMFriendPendencyRequest();
        timFriendPendencyRequest.setTimPendencyGetType(TIMPendencyType.TIM_PENDENCY_COME_IN);
        timFriendPendencyRequest.setSeq(0);
        timFriendPendencyRequest.setTimestamp(0);
        timFriendPendencyRequest.setNumPerPage(10);
        TIMFriendshipManager.getInstance().getPendencyList(timFriendPendencyRequest, new TIMValueCallBack<TIMFriendPendencyResponse>() {
            @Override
            public void onError(int i, String s) {
                FQL.e(TAG, "getPendencyList err code = " + i + ", desc = " + s);
                ToastUtil.toastShortMessage("Error code = " + i + ", desc = " + s);
            }

            @Override
            public void onSuccess(TIMFriendPendencyResponse timFriendPendencyResponse) {
                FQL.i(TAG, "getPendencyList success result = " + timFriendPendencyResponse.toString());
                if (timFriendPendencyResponse.getItems() != null) {
                    if (timFriendPendencyResponse.getItems().size() == 0) {
                        mEmptyView.setText(getResources().getString(R.string.no_friend_apply));
                        mNewFriendLv.setVisibility(View.GONE);
                        mEmptyView.setVisibility(View.VISIBLE);
                        return;
                    }
                }
                mNewFriendLv.setVisibility(View.VISIBLE);
                mList.clear();
                mList.addAll(timFriendPendencyResponse.getItems());
                mAdapter = new NewFriendListAdapter(NewFriendActivity.this, R.layout.contact_new_friend_item, mList);
                mNewFriendLv.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void finish() {
        super.finish();
    }

}
