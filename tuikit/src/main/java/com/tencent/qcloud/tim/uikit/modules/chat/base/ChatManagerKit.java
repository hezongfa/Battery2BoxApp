package com.tencent.qcloud.tim.uikit.modules.chat.base;

import android.text.TextUtils;

import com.tencent.imsdk.TIMCallBack;
import com.tencent.imsdk.TIMConversation;
import com.tencent.imsdk.TIMConversationType;
import com.tencent.imsdk.TIMElem;
import com.tencent.imsdk.TIMElemType;
import com.tencent.imsdk.TIMGroupSystemElem;
import com.tencent.imsdk.TIMManager;
import com.tencent.imsdk.TIMMessage;
import com.tencent.imsdk.TIMMessageListener;
import com.tencent.imsdk.TIMMessageOfflinePushSettings;
import com.tencent.imsdk.TIMSNSSystemElem;
import com.tencent.imsdk.TIMValueCallBack;
import com.tencent.imsdk.ext.message.TIMMessageLocator;
import com.tencent.imsdk.ext.message.TIMMessageReceipt;
import com.tencent.qcloud.tim.uikit.base.IUIKitCallBack;
import com.tencent.qcloud.tim.uikit.modules.conversation.ConversationManagerKit;
import com.tencent.qcloud.tim.uikit.modules.message.MessageInfo;
import com.tencent.qcloud.tim.uikit.modules.message.MessageInfoUtil;
import com.tencent.qcloud.tim.uikit.modules.message.MessageRevokedManager;
import com.tencent.qcloud.tim.uikit.utils.TUIKitLog;
import com.tencent.qcloud.tim.uikit.utils.ToastUtil;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class ChatManagerKit implements TIMMessageListener, MessageRevokedManager.MessageRevokeHandler {

    protected static final int MSG_PAGE_COUNT = 20;
    protected static final int REVOKE_TIME_OUT = 6223;
    private static final String TAG = ChatManagerKit.class.getSimpleName();
    protected ChatProvider mCurrentProvider;
    protected TIMConversation mCurrentConversation;

    protected boolean mIsMore;
    private boolean mIsLoading;

    protected void init() {
        destroyChat();
        TIMManager.getInstance().addMessageListener(this);
        MessageRevokedManager.getInstance().addHandler(this);
    }

    public void destroyChat() {
        mCurrentConversation = null;
        mCurrentProvider = null;
    }

    public abstract ChatInfo getCurrentChatInfo();

    public void setCurrentChatInfo(ChatInfo info) {
        if (info == null) {
            return;
        }
        mCurrentConversation = TIMManager.getInstance().getConversation(info.getType(), info.getId());
        mCurrentProvider = new ChatProvider();
        mIsMore = true;
        mIsLoading = false;
    }

    public void onReadReport(List<TIMMessageReceipt> receiptList) {
        TUIKitLog.i(TAG, "onReadReport:" + receiptList.size());
        if (!safetyCall()) {
            TUIKitLog.w(TAG, "onReadReport unSafetyCall");
            return;
        }
        if (receiptList.size() == 0) {
            return;
        }
        TIMMessageReceipt max = receiptList.get(0);
        for (TIMMessageReceipt msg : receiptList) {
            if (!TextUtils.equals(msg.getConversation().getPeer(), mCurrentConversation.getPeer())
                    || msg.getConversation().getType() == TIMConversationType.Group) {
                continue;
            }
            if (max.getTimestamp() < msg.getTimestamp()) {
                max = msg;
            }
        }
        mCurrentProvider.updateReadMessage(max);
    }

    @Override
    public boolean onNewMessages(List<TIMMessage> msgs) {
        TUIKitLog.i(TAG, "onNewMessages:" + msgs.size());
        if (null != msgs && msgs.size() > 0) {
            for (TIMMessage msg : msgs) {
                TIMConversation conversation = msg.getConversation();
                TIMConversationType type = conversation.getType();
                if (type == TIMConversationType.C2C) {
                    if (MessageInfoUtil.isTyping(msg)) {
                        notifyTyping();
                    } else {
                        onReceiveMessage(conversation, msg);
                    }
                } else if (type == TIMConversationType.Group) {
                    onReceiveMessage(conversation, msg);
                } else if (type == TIMConversationType.System) {
                    onReceiveSystemMessage(msg);
                }
            }
        }
        return false;
    }

    private void notifyTyping() {
        if (!safetyCall()) {
            TUIKitLog.w(TAG, "notifyTyping unSafetyCall");
            return;
        }
        mCurrentProvider.notifyTyping();
    }

    // GroupChatManager??????????????????
    protected void onReceiveSystemMessage(TIMMessage msg) {
        TIMElem ele = msg.getElement(0);
        TIMElemType eleType = ele.getType();
        // ???????????????????????????????????????????????????????????????????????? TIMUserConfig ?????? setFriendshipListener ??????
        if (eleType == TIMElemType.ProfileTips) {
            TUIKitLog.i(TAG, "onReceiveSystemMessage eleType is ProfileTips, ignore");
        } else if (eleType == TIMElemType.SNSTips) {
            TUIKitLog.i(TAG, "onReceiveSystemMessage eleType is SNSTips");
            TIMSNSSystemElem m = (TIMSNSSystemElem) ele;
            if (m.getRequestAddFriendUserList().size() > 0) {
                ToastUtil.toastLongMessage("??????????????????");
            }
            if (m.getDelFriendAddPendencyList().size() > 0) {
                ToastUtil.toastLongMessage("?????????????????????");
            }
        } else if (eleType == TIMElemType.GroupSystem) {
            TIMGroupSystemElem elem = (TIMGroupSystemElem) ele;
            ToastUtil.toastLongMessage("???????????????" + new String(elem.getUserData()));
        }
    }

    protected void onReceiveMessage(final TIMConversation conversation, final TIMMessage msg) {
        if (!safetyCall()) {
            TUIKitLog.w(TAG, "onReceiveMessage unSafetyCall");
            return;
        }
        if (conversation == null || conversation.getPeer() == null) {
            return;
        }
        addMessage(conversation, msg);
    }

    protected abstract boolean isGroup();

    protected void addMessage(TIMConversation conversation, TIMMessage msg) {
        if (!safetyCall()) {
            TUIKitLog.w(TAG, "addMessage unSafetyCall");
            return;
        }
        final List<MessageInfo> list = MessageInfoUtil.TIMMessage2MessageInfo(msg, isGroup());
        if (list != null && list.size() != 0 && mCurrentConversation.getPeer().equals(conversation.getPeer())) {
            mCurrentProvider.addMessageInfoList(list);
            for (MessageInfo msgInfo : list) {
                msgInfo.setRead(true);
                addGroupMessage(msgInfo);
            }
            mCurrentConversation.setReadMessage(msg, new TIMCallBack() {
                @Override
                public void onError(int code, String desc) {
                    TUIKitLog.v(TAG, "addMessage() setReadMessage failed, code = " + code + ", desc = " + desc);
                }

                @Override
                public void onSuccess() {
                    TUIKitLog.v(TAG, "addMessage() setReadMessage success");
                }
            });
        }
    }

    protected void addGroupMessage(MessageInfo msgInfo) {
        // GroupChatManagerKit??????????????????
    }

    public void deleteMessage(int position, MessageInfo messageInfo) {
        if (!safetyCall()) {
            TUIKitLog.w(TAG, "deleteMessage unSafetyCall");
            return;
        }
        if (messageInfo.remove()) {
            mCurrentProvider.remove(position);
        }
    }

    public void revokeMessage(final int position, final MessageInfo messageInfo) {
        if (!safetyCall()) {
            TUIKitLog.w(TAG, "revokeMessage unSafetyCall");
            return;
        }
        mCurrentConversation.revokeMessage(messageInfo.getTIMMessage(), new TIMCallBack() {
            @Override
            public void onError(int code, String desc) {
                if (code == REVOKE_TIME_OUT) {
                    ToastUtil.toastLongMessage("?????????????????????2??????");
                } else {
                    ToastUtil.toastLongMessage("????????????:" + code + "=" + desc);
                }
            }

            @Override
            public void onSuccess() {
                if (!safetyCall()) {
                    TUIKitLog.w(TAG, "revokeMessage unSafetyCall");
                    return;
                }
                mCurrentProvider.updateMessageRevoked(messageInfo.getId());
                ConversationManagerKit.getInstance().loadConversation(null);
            }
        });
    }

    public void sendMessage(final MessageInfo message, boolean retry, final IUIKitCallBack callBack) {
        if (!safetyCall()) {
            TUIKitLog.w(TAG, "sendMessage unSafetyCall");
            return;
        }
        if (message == null || message.getStatus() == MessageInfo.MSG_STATUS_SENDING) {
            return;
        }
        message.setSelf(true);
        message.setRead(true);
        assembleGroupMessage(message);
        //?????????????????????????????????????????????????????????
        if (message.getMsgType() < MessageInfo.MSG_TYPE_TIPS) {
            message.setStatus(MessageInfo.MSG_STATUS_SENDING);
            if (retry) {
                mCurrentProvider.resendMessageInfo(message);
            } else {
                mCurrentProvider.addMessageInfo(message);
            }
        }

        // ????????????????????????
//        TIMMessageOfflinePushSettings settings = new TIMMessageOfflinePushSettings();
//        settings.setExt("test".getBytes());
//        TIMMessageOfflinePushSettings.AndroidSettings androidSettings = settings.getAndroidSettings();
//        // OPPO????????????ChannelID????????????????????????????????????channelID????????????????????????
//        androidSettings.setOPPOChannelID("tuikit");
//        message.getTIMMessage().setOfflinePushSettings(settings);

        mCurrentConversation.sendMessage(message.getTIMMessage(), new TIMValueCallBack<TIMMessage>() {
            @Override
            public void onError(final int code, final String desc) {
                TUIKitLog.v(TAG, "sendMessage fail:" + code + "=" + desc);
                if (!safetyCall()) {
                    TUIKitLog.w(TAG, "sendMessage unSafetyCall");
                    return;
                }
                if (callBack != null) {
                    callBack.onError(TAG, code, desc);
                }
                message.setStatus(MessageInfo.MSG_STATUS_SEND_FAIL);
                mCurrentProvider.updateMessageInfo(message);

            }

            @Override
            public void onSuccess(TIMMessage timMessage) {
                TUIKitLog.v(TAG, "sendMessage onSuccess");
                if (!safetyCall()) {
                    TUIKitLog.w(TAG, "sendMessage unSafetyCall");
                    return;
                }
                if (callBack != null) {
                    callBack.onSuccess(mCurrentProvider);
                }
                message.setStatus(MessageInfo.MSG_STATUS_SEND_SUCCESS);
                message.setId(timMessage.getMsgId());
                mCurrentProvider.updateMessageInfo(message);
            }
        });
    }

    protected void assembleGroupMessage(MessageInfo message) {
        // GroupChatManager??????????????????
    }

    public void loadLocalChatMessages(MessageInfo lastMessage, final IUIKitCallBack callBack) {
        if (!safetyCall()) {
            TUIKitLog.w(TAG, "loadLocalChatMessages unSafetyCall");
            return;
        }
        if (mIsLoading) {
            return;
        }
        mIsLoading = true;
        if (!mIsMore) {
            mCurrentProvider.addMessageInfo(null);
            callBack.onSuccess(null);
            mIsLoading = false;
            return;
        }

        TIMMessage lastTIMMsg = null;
        if (lastMessage == null) {
            mCurrentProvider.clear();
        } else {
            lastTIMMsg = lastMessage.getTIMMessage();
        }
        final int unread = (int) mCurrentConversation.getUnreadMessageNum();
        mCurrentConversation.getLocalMessage(MSG_PAGE_COUNT
                , lastTIMMsg, new TIMValueCallBack<List<TIMMessage>>() {
                    @Override
                    public void onError(int code, String desc) {
                        mIsLoading = false;
                        callBack.onError(TAG, code, desc);
                        TUIKitLog.e(TAG, "loadChatMessages() getMessage failed, code = " + code + ", desc = " + desc);
                    }

                    @Override
                    public void onSuccess(List<TIMMessage> timMessages) {
                        mIsLoading = false;
                        if (!safetyCall()) {
                            TUIKitLog.w(TAG, "getLocalMessage unSafetyCall");
                            return;
                        }
                        if (unread > 0) {
                            mCurrentConversation.setReadMessage(null, new TIMCallBack() {
                                @Override
                                public void onError(int code, String desc) {
                                    TUIKitLog.e(TAG, "loadChatMessages() setReadMessage failed, code = " + code + ", desc = " + desc);
                                }

                                @Override
                                public void onSuccess() {
                                    TUIKitLog.d(TAG, "loadChatMessages() setReadMessage success");
                                }
                            });
                        }
                        if (timMessages.size() < MSG_PAGE_COUNT) {
                            mIsMore = false;
                        }
                        ArrayList<TIMMessage> messages = new ArrayList<>(timMessages);
                        Collections.reverse(messages);

                        List<MessageInfo> msgInfos = MessageInfoUtil.TIMMessages2MessageInfos(messages, isGroup());
                        mCurrentProvider.addMessageList(msgInfos, true);
                        for (int i = 0; i < msgInfos.size(); i++) {
                            MessageInfo info = msgInfos.get(i);
                            if (info.getStatus() == MessageInfo.MSG_STATUS_SENDING) {
                                sendMessage(info, true, null);
                            }
                        }
                        callBack.onSuccess(mCurrentProvider);
                    }
                });
    }

    public void loadChatMessages(MessageInfo lastMessage, final IUIKitCallBack callBack) {
        if (!safetyCall()) {
            TUIKitLog.w(TAG, "loadChatMessages unSafetyCall");
            return;
        }
        if (mIsLoading) {
            return;
        }
        mIsLoading = true;
        if (!mIsMore) {
            mCurrentProvider.addMessageInfo(null);
            callBack.onSuccess(null);
            mIsLoading = false;
            return;
        }

        TIMMessage lastTIMMsg = null;
        if (lastMessage == null) {
            mCurrentProvider.clear();
        } else {
            lastTIMMsg = lastMessage.getTIMMessage();
        }
        final int unread = (int) mCurrentConversation.getUnreadMessageNum();
        mCurrentConversation.getMessage(MSG_PAGE_COUNT
                , lastTIMMsg, new TIMValueCallBack<List<TIMMessage>>() {
                    @Override
                    public void onError(int code, String desc) {
                        mIsLoading = false;
                        List<MessageInfo> msgInfos = new ArrayList<>();
                        mCurrentProvider.addMessageList(msgInfos, true);
                        callBack.onError(TAG, code, desc);
                        TUIKitLog.e(TAG, "loadChatMessages() getMessage failed, code = " + code + ", desc = " + desc);
                    }

                    @Override
                    public void onSuccess(List<TIMMessage> timMessages) {
                        TUIKitLog.i(TAG, "loadChatMessages() timMessages size " + timMessages.size());
                        mIsLoading = false;
                        if (!safetyCall()) {
                            TUIKitLog.w(TAG, "getMessage unSafetyCall");
                            return;
                        }
                        if (unread > 0) {
                            mCurrentConversation.setReadMessage(null, new TIMCallBack() {
                                @Override
                                public void onError(int code, String desc) {
                                    TUIKitLog.e(TAG, "loadChatMessages() setReadMessage failed, code = " + code + ", desc = " + desc);
                                }

                                @Override
                                public void onSuccess() {
                                    TUIKitLog.d(TAG, "loadChatMessages() setReadMessage success");
                                }
                            });
                        }
                        if (timMessages.size() < MSG_PAGE_COUNT) {
                            mIsMore = false;
                        }
                        ArrayList<TIMMessage> messages = new ArrayList<>(timMessages);
                        Collections.reverse(messages);

                        List<MessageInfo> msgInfos = MessageInfoUtil.TIMMessages2MessageInfos(messages, isGroup());
                        mCurrentProvider.addMessageList(msgInfos, true);
                        for (int i = 0; i < msgInfos.size(); i++) {
                            MessageInfo info = msgInfos.get(i);
                            if (info.getStatus() == MessageInfo.MSG_STATUS_SENDING) {
                                sendMessage(info, true, null);
                            }
                        }
                        callBack.onSuccess(mCurrentProvider);
                    }
                });
    }

    @Override
    public void handleInvoke(TIMMessageLocator locator) {
        if (!safetyCall()) {
            TUIKitLog.w(TAG, "handleInvoke unSafetyCall");
            return;
        }
        if (locator.getConversationId().equals(getCurrentChatInfo().getId())) {
            TUIKitLog.i(TAG, "handleInvoke locator = " + locator);
            mCurrentProvider.updateMessageRevoked(locator);
        }
    }

    protected boolean safetyCall() {
        if (mCurrentConversation == null
                || mCurrentProvider == null
                || getCurrentChatInfo() == null
        ) {
            return false;
        }
        return true;
    }
}
