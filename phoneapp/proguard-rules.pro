# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

-dontpreverify
# --------------------
#  不优化指定的文件
#  -dontoptimize
# ---------------------
-dontoptimize

# --------------------------------
#  通过指定数量的优化能执行
#  -optimizationpasses n
# --------------------------------
-optimizationpasses 5

# --------------------------------
#    混淆时不使用大小写混合类名
#   -dontusemixedcaseclassnames
# --------------------------------
-dontusemixedcaseclassnames

# --------------------------------
#      指定不去忽略非公共的库类
#  -dontskipnonpubliclibraryclasses
# --------------------------------
-dontskipnonpubliclibraryclasses
-dontskipnonpubliclibraryclassmembers

# --------------------------------
#       不预校验
#    -dontpreverify
# --------------------------------
-dontpreverify

# --------------------------------
#      输出生成信息
#       -verbose
# --------------------------------
-verbose
-keepattributes Exceptions, SourceFile, LineNumberTable, Signature, InnerClasses
-ignorewarnings




# --------------------------------
#        优化选项
#   optimizations  {optimization_filter}
# --------------------------------
#-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*
-keep public class * extends android.os
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class * extends android.view.View
-keep public class com.android.vending.licensing.ILicensingService

-keep public final class com.ewg.vip.eshop.BuildConfig

-keepclasseswithmembernames class * {
    native <methods>;
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

-keepclassmembers class * extends android.app.Application{
	public void back*(android.view.View);
	public void back*(java.lang.Class);
}

-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}


#保持 Serializable 不被混淆
-keepnames class * implements java.io.Serializable

#保持 Serializable 不被混淆并且enum 类也不被混淆
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    !static !transient <fields>;
    !private <fields>;
    !private <methods>;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}

#-keepclassmembers class ** {
#    @com.csair.mbp.base.BaseReceiver$ReceiverMethod <methods>;
#}

# 继承 HybridActivity 的类中带 JavascriptInterface 注解的方法将不会被混淆
-keepclassmembers class * {
    @android.webkit.JavascriptInterface <methods>;
}

-keepclassmembers class * extends android.webkit.WebChromeClient {
    public void open*(android.webkit.ValueCallback, java.lang.String);
    public void open*(android.webkit.ValueCallback);
    public void open*(android.webkit.ValueCallback, java.lang.String, java.lang.String);
}
-keepclassmembers class * extends android.com.ewg.ecg.widget.LinearLayout {
	public void cl*(android.view.View);
	public void icl*(android.com.ewg.ecg.widget.AdapterView, android.view.View, int, long);
	public void lcl*(android.view.View);
	public void tc*(java.lang.CharSequence, int, int, int);
}
-keepclassmembers class * extends android.support.v4.app.Fragment {
#	public void cl*(android.view.View);
#	public void icl*(android.com.ewg.ecg.widget.AdapterView, android.view.View, int, long);
#	public void lcl*(android.view.View);
#	public void tc*(java.lang.CharSequence, int, int, int);
*;
}

-keepclassmembers class * extends android.com.ewg.ecg.widget.BaseAdapter {
	public void cl*(android.view.View);
	public void icl*(android.com.ewg.ecg.widget.AdapterView, android.view.View, int, long);
	public void lcl*(android.view.View);
	public void tc*(java.lang.CharSequence, int, int, int);
}

-keepclassmembers class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}

# Needed by google-api-client to keep generic types and @Key annotations accessed via reflection
-keepclassmembers class * {
  @com.google.api.client.util.Key <fields>;
}

# ------ R类不混淆 ------
-keepclassmembers class **.R$* { *;}
-keepattributes Signature
-keepattributes *Annotation*
-keepattributes EnclosingMethod

#-keep class com.ass.nb.pro.com.ewg.ecg.widget.dialog.BaseDialogFragment {
#    public void show(android.support.v4.app.FragmentManager, java.lang.String);
#}

###################################################################
######################## 第三方jar包不混淆 ########################
###################################################################

-keep class com.chenyi.baselib.utils.sys.KeyBoardUtil {
    public static void fixInputMethodManagerLeak(**);
 }


# ------ 保护 SaaS_AppAnalytics_Android_SDK_V4.0.9.jar 库（腾讯移动统计） ------
-dontwarn com.tendcloud.tenddata.**
-keep class com.tendcloud.** {*;}
-keep public class com.tendcloud.tenddata.** { public protected *;}
-keepclassmembers class com.tendcloud.tenddata.**{
public void *(***);
}
-keep class com.talkingdata.sdk.TalkingDataSDK {public *;}
-keep class com.apptalkingdata.** {*;}
-keep class dice.** {*; }
-dontwarn dice.**



# ------ 保护 ARouter ------
-keep public class com.alibaba.android.arouter.routes.**{*;}
-keep class * implements com.alibaba.android.arouter.facade.template.ISyringe{*;}
-keep interface * implements com.alibaba.android.arouter.facade.template.IProvider
-dontwarn com.alibaba.fastjson.**
-keep class com.alibaba.fastjson.**{*;}


# 保护okhttp
-dontwarn okhttp3.**
-keep class okhttp3.**{*;}

-dontwarn okio.**
-keep class okio.**{*;}

-keep class org.jsoup.**


# ----------------------------------
#      Retrofit RxJava RxAndroid
# ----------------------------------
-dontwarn javax.annotation.**
-dontwarn javax.inject.**
-dontwarn javax.lang.model.element.**

# Retrofit
-dontwarn retrofit2.**
-keep class retrofit2.** { *; }
-keepattributes Signature
-keepattributes Exceptions
# RxJava RxAndroid
-dontwarn sun.misc.**
-keepclassmembers class rx.internal.util.unsafe.*ArrayQueue*Field* {
    long producerIndex;
    long consumerIndex;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueProducerNodeRef {
    rx.internal.util.atomic.LinkedQueueNode producerNode;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueConsumerNodeRef {
    rx.internal.util.atomic.LinkedQueueNode consumerNode;
}

-keep class com.chenyi.shopkeeper.hybrid.IJavaScriptInterface { *; }


# Gson
-keep class com.google.gson.** { *; }
-dontwarn com.google.gson.**
#-keep public class com.google.gson.**
#-keep public class com.google.gson.** {public private protected *;}
#-keep class com.google.gson.stream.** { *; }
-keep class * implements com.google.gson.TypeAdapter
#-keepattributes EnclosingMethod

#
## ----------------------------------
##      freemarker
## ----------------------------------
#-keep class freemarker.** { *; }
#-dontwarn freemarker.**

# ----------------------------------
#      EventBus
# ----------------------------------

-keepattributes *Annotation*
-keepclassmembers class ** {
@org.greenrobot.eventbus.Subscribe <methods>;
}
-keep enum org.greenrobot.eventbus.ThreadMode { *; }

# Only required if you use AsyncExecutor
-keepclassmembers class * extends org.greenrobot.eventbus.util.ThrowableFailureEvent {
<init>(java.lang.Throwable);
}



# ----------------------------------
#      vLayout
# ----------------------------------
-keepattributes InnerClasses
-keep class com.alibaba.android.arouter.facade.model.**  { *; }
-keep class com.alibaba.android.vlayout.ExposeLinearLayoutManagerEx { *; }
-keep class android.support.v7.com.ewg.ecg.widget.RecyclerView$LayoutParams { *; }
-keep class android.support.v7.com.ewg.ecg.widget.RecyclerView$ViewHolder { *; }
-keep class android.support.v7.com.ewg.ecg.widget.ChildHelper { *; }
-keep class android.support.v7.com.ewg.ecg.widget.ChildHelper$Bucket { *; }
-keep class android.support.v7.com.ewg.ecg.widget.RecyclerView$LayoutManager { *; }


# ----------------------------------
#      glide
# ----------------------------------
-keep public class * implements com.bumptech.glide.module.GlideModule
#-keep public class * extends com.bumptech.glide.request.target.SimpleTarget
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}


#--------用到反射--------
-keep class com.chenyi.baselib.utils { *; }
-keep class android.support.design.widget.TabLayout { *; }

## ----------------------------------
##      ucrop
## ----------------------------------
-dontwarn com.yalantis.ucrop.**
-keep class com.yalantis.ucrop.** { *; }
-keep interface com.yalantis.ucrop** { *; }


-keep class sun.misc.Unsafe { *; }
-dontwarn java.nio.file.*
-dontwarn org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement
-dontwarn okio.**

-keep class com.bumptech.glide.Glide { *; }
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}
-dontwarn com.bumptech.glide.**


-dontwarn cn.bertsir.zbar.**
-keep class cn.bertsir.zbar.** { *;}

-dontwarn cn.jpush.**
-keep class cn.jpush.** { *; }

-dontwarn cn.jiguang.**
-keep class cn.jiguang.** { *; }
