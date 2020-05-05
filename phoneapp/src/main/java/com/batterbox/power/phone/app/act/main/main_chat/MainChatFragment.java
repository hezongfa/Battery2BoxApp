package com.batterbox.power.phone.app.act.main.main_chat;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.batterbox.power.phone.app.BatterBoxApp;
import com.batterbox.power.phone.app.R;
import com.batterbox.power.phone.app.act.main.Constants;
import com.batterbox.power.phone.app.act.main.chat.ChatActivity;
import com.batterbox.power.phone.app.aroute.ARouteHelper;
import com.batterbox.power.phone.app.utils.PopUtil;
import com.batterbox.power.phone.app.utils.ScanUtil;
import com.chenyi.baselib.ui.BaseActivity;
import com.chenyi.baselib.ui.BaseFragment;
import com.tencent.imsdk.TIMConversationType;
import com.tencent.qcloud.tim.uikit.component.action.PopDialogAdapter;
import com.tencent.qcloud.tim.uikit.component.action.PopMenuAction;
import com.tencent.qcloud.tim.uikit.modules.chat.base.ChatInfo;
import com.tencent.qcloud.tim.uikit.modules.conversation.ConversationLayout;
import com.tencent.qcloud.tim.uikit.modules.conversation.ConversationListLayout;
import com.tencent.qcloud.tim.uikit.modules.conversation.base.ConversationInfo;
import com.tencent.qcloud.tim.uikit.utils.PopWindowUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by ass on 2020-05-02.
 * Description
 */
public class MainChatFragment extends BaseFragment {


    private View mBaseView;
    private ConversationLayout mConversationLayout;
    private ListView mConversationPopList;
    private PopDialogAdapter mConversationPopAdapter;
    private PopupWindow mConversationPopWindow;
    private List<PopMenuAction> mConversationPopActions = new ArrayList<>();
//    private Menu mMenu;

    @Override
    protected int getLayout() {
        return R.layout.fragment_main_chat;
    }

    @Override
    protected void findView() {

    }

    @Override
    protected void init(Bundle bundle) {
        initView();
    }

    private void initView() {
        findViewById(R.id.fragment_main_chat_friends_btn).setOnClickListener(v -> ARouteHelper.chat_friends().navigation());
        findViewById(R.id.fragment_main_chat_menu_btn).setOnClickListener(v -> showPop(v));


        mBaseView = findViewById(R.id.mBaseView);
        // 从布局文件中获取会话列表面板
        mConversationLayout = mBaseView.findViewById(R.id.conversation_layout);
//        mMenu = new Menu(getActivity(), (TitleBarLayout) mConversationLayout.getTitleBar(), Menu.MENU_TYPE_CONVERSATION);
        // 会话列表面板的默认UI和交互初始化
        mConversationLayout.initDefault();
        // 通过API设置ConversataonLayout各种属性的样例，开发者可以打开注释，体验效果
//        ConversationLayoutHelper.customizeConversation(mConversationLayout);
        mConversationLayout.getConversationList().setOnItemClickListener(new ConversationListLayout.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, ConversationInfo conversationInfo) {
                //此处为demo的实现逻辑，更根据会话类型跳转到相关界面，开发者可根据自己的应用场景灵活实现
                startChatActivity(conversationInfo);
            }
        });
        mConversationLayout.getConversationList().setOnItemLongClickListener(new ConversationListLayout.OnItemLongClickListener() {
            @Override
            public void OnItemLongClick(View view, int position, ConversationInfo conversationInfo) {
                startPopShow(view, position, conversationInfo);
            }
        });
//        initTitleAction();
//        initPopMenuAction();
    }

    void showPop(View mAttachView) {
        List<PopUtil.PopItem> list = Arrays.asList(new PopUtil.PopItem(getString(R.string.main_chat_1), R.mipmap.ic_main_chat_add)
                , new PopUtil.PopItem(getString(R.string.main_chat_2), R.mipmap.ic_main_chat_scan)
                , new PopUtil.PopItem(getString(R.string.main_chat_3), R.mipmap.ic_main_chat_del));
        PopUtil.showMenuPop(getContext(), mAttachView, list, (popItem, position) -> {
            switch (position) {
                case 0:
                    ARouteHelper.chat_search_user().navigation();
                    break;
                case 1:
                    ScanUtil.scan((BaseActivity) getActivity());
                    break;
                case 2:
                    break;
            }
        });
    }


    private void initTitleAction() {
//        mConversationLayout.getTitleBar().setOnRightClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (mMenu.isShowing()) {
//                    mMenu.hide();
//                } else {
//                    mMenu.show();
//                }
//            }
//        });
    }

    private void initPopMenuAction() {

//        // 设置长按conversation显示PopAction
//        List<PopMenuAction> conversationPopActions = new ArrayList<PopMenuAction>();
//        PopMenuAction action = new PopMenuAction();
//        action.setActionName(getResources().getString(R.string.chat_top));
//        action.setActionClickListener(new PopActionClickListener() {
//            @Override
//            public void onActionClick(int position, Object data) {
//                mConversationLayout.setConversationTop(position, (ConversationInfo) data);
//            }
//        });
//        conversationPopActions.add(action);
//        action = new PopMenuAction();
//        action.setActionClickListener(new PopActionClickListener() {
//            @Override
//            public void onActionClick(int position, Object data) {
//                mConversationLayout.deleteConversation(position, (ConversationInfo) data);
//            }
//        });
//        action.setActionName(getResources().getString(R.string.chat_delete));
//        conversationPopActions.add(action);
//        mConversationPopActions.clear();
//        mConversationPopActions.addAll(conversationPopActions);
    }

    /**
     * 长按会话item弹框
     *
     * @param index            会话序列号
     * @param conversationInfo 会话数据对象
     * @param locationX        长按时X坐标
     * @param locationY        长按时Y坐标
     */
    private void showItemPopMenu(final int index, final ConversationInfo conversationInfo, float locationX, float locationY) {
        if (mConversationPopActions == null || mConversationPopActions.size() == 0)
            return;
        View itemPop = LayoutInflater.from(getActivity()).inflate(R.layout.pop_menu_layout, null);
        mConversationPopList = itemPop.findViewById(R.id.pop_menu_list);
        mConversationPopList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PopMenuAction action = mConversationPopActions.get(position);
                if (action.getActionClickListener() != null) {
                    action.getActionClickListener().onActionClick(index, conversationInfo);
                }
                mConversationPopWindow.dismiss();
            }
        });

        for (int i = 0; i < mConversationPopActions.size(); i++) {
            PopMenuAction action = mConversationPopActions.get(i);
            if (conversationInfo.isTop()) {
                if (action.getActionName().equals(getResources().getString(R.string.chat_top))) {
                    action.setActionName(getResources().getString(R.string.quit_chat_top));
                }
            } else {
                if (action.getActionName().equals(getResources().getString(R.string.quit_chat_top))) {
                    action.setActionName(getResources().getString(R.string.chat_top));
                }

            }
        }
        mConversationPopAdapter = new PopDialogAdapter();
        mConversationPopList.setAdapter(mConversationPopAdapter);
        mConversationPopAdapter.setDataSource(mConversationPopActions);
        mConversationPopWindow = PopWindowUtil.popupWindow(itemPop, mBaseView, (int) locationX, (int) locationY);
        mBaseView.postDelayed(new Runnable() {
            @Override
            public void run() {
                mConversationPopWindow.dismiss();
            }
        }, 10000); // 10s后无操作自动消失
    }

    private void startPopShow(View view, int position, ConversationInfo info) {
        showItemPopMenu(position, info, view.getX(), view.getY() + view.getHeight() / 2);
    }

    private void startChatActivity(ConversationInfo conversationInfo) {
        ChatInfo chatInfo = new ChatInfo();
        chatInfo.setType(conversationInfo.isGroup() ? TIMConversationType.Group : TIMConversationType.C2C);
        chatInfo.setId(conversationInfo.getId());
        chatInfo.setChatName(conversationInfo.getTitle());
        Intent intent = new Intent(BatterBoxApp.getInstance(), ChatActivity.class);
        intent.putExtra(Constants.CHAT_INFO, chatInfo);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        BatterBoxApp.getInstance().startActivity(intent);




    }


}
