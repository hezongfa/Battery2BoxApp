//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.tencent.qcloud.tim.uikit;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Notification.Builder;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.SystemClock;
import android.os.Build.VERSION;
import android.text.TextUtils;

import com.tencent.imsdk.TIMConversationType;
import com.tencent.imsdk.TIMCustomElem;
import com.tencent.imsdk.TIMElem;
import com.tencent.imsdk.TIMElemType;
import com.tencent.imsdk.TIMGroupReceiveMessageOpt;
import com.tencent.imsdk.TIMLocationElem;
import com.tencent.imsdk.TIMMessage;
import com.tencent.imsdk.TIMMessageOfflinePushSettings;
import com.tencent.imsdk.TIMTextElem;
import com.tencent.imsdk.TIMUserProfile;
import com.tencent.imsdk.TIMValueCallBack;
import com.tencent.imsdk.log.QLog;

public class TIMOfflinePushNotification {
    public static Uri globleC2CRemindSound;
    public static Uri globleGroupRemindSound;
    private final String TAG = TIMOfflinePushNotification.class.getSimpleName();
    private String title;
    private String content;
    private TIMConversationType conversationType;
    private String conversationId;
    private String senderIdentifier;
    private String senderNickName;
    private String groupName;
    private byte[] ext;
    private Uri sound;
    private long opt = 0L;
    private String tag;
    private boolean isValid = false;

    public TIMOfflinePushNotification() {
    }

    public TIMOfflinePushNotification(Context context, TIMMessage msg) {
        String content = "";
        TIMConversationType type = msg.getConversation().getType();
        if (type != TIMConversationType.C2C && type != TIMConversationType.Group) {
            this.isValid = false;
//        } else if (msg.getMsg().lifetime() == 0L) {
//            this.isValid = false;
        } else {
            String descr = "";
            TIMMessageOfflinePushSettings settings = msg.getOfflinePushSettings();
            if (settings != null) {
                if (!settings.isEnabled()) {
                    this.isValid = false;
                    return;
                }

                this.setSound(settings.getAndroidSettings().getSound());
                this.setExt(settings.getExt());
                descr = settings.getDescr();
                this.title = settings.getAndroidSettings().getTitle();
            }

            this.opt = msg.getRecvFlag().getValue();
            this.setTag(msg.getConversation().getPeer());
            this.setConversationType(msg.getConversation().getType());
            this.setConversationId(msg.getConversation().getPeer());
            String identifier = msg.getSender();
            if (!TextUtils.isEmpty(identifier)) {
                this.setSenderIdentifier(identifier);
            }

            String sender;
            if (msg.getSenderGroupMemberProfile() != null) {
                sender = msg.getSenderGroupMemberProfile().getNameCard();
                if (!TextUtils.isEmpty(sender)) {
                    this.setSenderNickName(sender);
                }
            }
            msg.getSenderProfile(new TIMValueCallBack<TIMUserProfile>() {
                @Override
                public void onError(int i, String s) {

                }

                @Override
                public void onSuccess(TIMUserProfile timUserProfile) {
                    if (!TextUtils.isEmpty(timUserProfile.getNickName())) {
                        setSenderNickName(timUserProfile.getNickName());
                    }
                }
            });

            if (this.conversationType == TIMConversationType.C2C) {
                if (TextUtils.isEmpty(this.title)) {
                    this.setTitle(this.senderNickName);
                }
            } else {
                this.setGroupName(msg.getConversation().getPeer());
                if (TextUtils.isEmpty(this.title)) {
                    this.setTitle(this.groupName);
                }

                sender = this.senderNickName;
                if (TextUtils.isEmpty(sender)) {
                    sender = this.senderIdentifier;
                }

                content = content + sender + ": ";
            }

            if (TextUtils.isEmpty(this.title)) {
                this.setTitle(this.conversationId);
            }

            if (!TextUtils.isEmpty(descr)) {
                content = content + descr;
            } else {
                for(int i = 0; i < msg.getElementCount(); ++i) {
                    TIMElem elem = msg.getElement(i);
                    if (elem.getType() == TIMElemType.Sound) {
                        content = content + "[语音]";
                    } else if (elem.getType() == TIMElemType.File) {
                        content = content + "[文件]";
                    } else if (elem.getType() == TIMElemType.Text) {
                        TIMTextElem e = (TIMTextElem)elem;
                        content = content + e.getText();
                    } else if (elem.getType() == TIMElemType.Image) {
                        content = content + "[图片]";
                    } else if (elem.getType() == TIMElemType.Face) {
                        content = content + "[表情]";
                    } else if (elem.getType() == TIMElemType.Custom) {
                        TIMCustomElem e = (TIMCustomElem)elem;
                        if (!TextUtils.isEmpty(e.getDesc())) {
                            content = content + "[" + e.getDesc() + "]";
                        }

                        if (this.ext == null) {
                            this.setExt(e.getExt());
                        }
                    } else if (elem.getType() == TIMElemType.Location) {
                        TIMLocationElem e = (TIMLocationElem)elem;
                        content = content + "[位置信息]" + e.getDesc();
                    } else if (elem.getType() == TIMElemType.Video) {
                        content = content + "[视频]";
                    }
                }
            }

            this.setContent(content);
            if (this.getSound() == null) {
                if (this.conversationType == TIMConversationType.C2C && globleC2CRemindSound != null) {
                    this.setSound(globleC2CRemindSound);
                } else if (this.conversationType == TIMConversationType.Group && globleGroupRemindSound != null) {
                    this.setSound(globleGroupRemindSound);
                }
            }

            this.setIsValid(true);
        }
    }

    public Uri getSound() {
        return this.sound;
    }

    public void setSound(Uri sound) {
        if (sound != null) {
            this.sound = sound;
        }

    }

    public boolean isValid() {
        return this.isValid;
    }

    void setIsValid(boolean isValid) {
        this.isValid = isValid;
    }

    void setTag(String tag) {
        this.tag = tag;
    }

    public String getTitle() {
        return this.title == null ? "" : this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return this.content == null ? "" : this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public byte[] getExt() {
        return this.ext == null ? "".getBytes() : this.ext;
    }

    public void setExt(byte[] ext) {
        if (ext != null) {
            this.ext = ext;
        }

    }

    public TIMConversationType getConversationType() {
        return this.conversationType;
    }

    void setConversationType(TIMConversationType conversationType) {
        this.conversationType = conversationType;
    }

    public String getConversationId() {
        return this.conversationId;
    }

    void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    public String getSenderIdentifier() {
        return this.senderIdentifier;
    }

    void setSenderIdentifier(String senderIdentifier) {
        if (!TextUtils.isEmpty(senderIdentifier)) {
            this.senderIdentifier = senderIdentifier;
        }
    }

    public String getSenderNickName() {
        return this.senderNickName;
    }

    void setSenderNickName(String senderNickName) {
        if (!TextUtils.isEmpty(senderNickName)) {
            this.senderNickName = senderNickName;
        }
    }

    public TIMGroupReceiveMessageOpt getGroupReceiveMsgOpt() {
        TIMGroupReceiveMessageOpt[] var1 = TIMGroupReceiveMessageOpt.values();
        int var2 = var1.length;

        for(int var3 = 0; var3 < var2; ++var3) {
            TIMGroupReceiveMessageOpt option = var1[var3];
            if (option.getValue() == this.opt) {
                return option;
            }
        }

        return TIMGroupReceiveMessageOpt.ReceiveAndNotify;
    }

    void setGroupReceiveMsgOpt(long opt) {
        this.opt = opt;
    }

    public String getGroupName() {
        return this.groupName;
    }

    void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public void doNotify(Context context, int iconID) {
        QLog.i(this.TAG, "notification: " + this.toString());
        NotificationManager manager = (NotificationManager)context.getApplicationContext().getSystemService("notification");
        if (manager == null) {
            QLog.e(this.TAG, "get NotificationManager failed");
        } else {
            Builder builder;
            String tickerStr;
            if (VERSION.SDK_INT >= 26) {
                tickerStr = "FakeNotification";
                String channelName = "FakeNotificationName";
                builder = new Builder(context, tickerStr);
                NotificationChannel channel = new NotificationChannel(tickerStr, channelName, 4);
                manager.createNotificationChannel(channel);
            } else {
                builder = new Builder(context);
            }

            tickerStr = "收到一条新消息";
            builder.setTicker(tickerStr);
            builder.setContentTitle(this.getTitle());
            builder.setContentText(this.getContent());
            builder.setSmallIcon(iconID);
            builder.setAutoCancel(true);
            builder.setDefaults(-1);
            if (this.sound != null) {
                builder.setDefaults(6);
                builder.setSound(this.sound);
            }

            Intent launch = context.getApplicationContext().getPackageManager().getLaunchIntentForPackage(context.getPackageName());
            builder.setContentIntent(PendingIntent.getActivity(context, (int)SystemClock.uptimeMillis(), launch, 134217728));
            manager.notify(this.tag, 520, builder.build());
        }
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("title: ").append(this.title).append("|content: ").append(this.content).append("|sid: ").append(this.conversationId).append("|sender: ").append(this.senderIdentifier).append("|senderNick: ").append(this.senderNickName).append("|tag: ").append(this.tag).append("|isValid: ").append(this.isValid);
        return builder.toString();
    }
}
