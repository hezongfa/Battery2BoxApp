//package com.batterbox.power.phone.app.foc;
//
//import android.app.Notification;
//import android.app.NotificationChannel;
//import android.app.NotificationManager;
//import android.app.PendingIntent;
//import android.app.Service;
//import android.content.Context;
//import android.content.Intent;
//import android.os.Build;
//import android.os.IBinder;
//import android.support.annotation.RequiresApi;
//import android.support.v4.app.NotificationCompat;
//import android.util.Log;
//
//import com.batterbox.power.phone.app.R;
//import com.batterbox.power.phone.app.act.main.MainActivity;
//
//public class LocService extends Service {
//
//    private static final String TAG = LocService.class.getSimpleName();
//
//    @Override
//    public void onCreate() {
//        super.onCreate();
//        Log.d(TAG, "onCreate()");
//    }
//
//    @RequiresApi(api = Build.VERSION_CODES.O)
//    @Override
//    public int onStartCommand(Intent intent, int flags, int startId) {
//        Log.d(TAG, "onStartCommand()");
//// 在API11之后构建Notification的方式
////        Notification.Builder builder = new Notification.Builder
////                (this.getApplicationContext()); //获取一个Notification构造器
////        Intent nfIntent = new Intent(this, MainActivity.class);
////        NotificationChannel notificationChannel =
////                new NotificationChannel(id,name, NotificationManager.IMPORTANCE_HIGH);
////        builder.setContentIntent(PendingIntent.
////                getActivity(this, 0, nfIntent, 0)) // 设置PendingIntent
////                .setLargeIcon(BitmapFactory.decodeResource(this.getResources(),
////                        R.mipmap.ic_launcher)) // 设置下拉列表中的图标(大图标)
////                .setContentTitle(getString(R.string.app_name)) // 设置下拉列表里的标题
////                .setSmallIcon(R.mipmap.ic_launcher) // 设置状态栏内的小图标
////                .setContentText("要显示的内容") // 设置上下文内容
////                .setWhen(System.currentTimeMillis()); // 设置该通知发生的时间
////
////        Notification notification = builder.build(); // 获取构建好的Notification
//////        notification.defaults = Notification.DEFAULT_SOUND; //设置为默认的声音
//
//
//
//        NotificationManager notificationManager =
//                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
////通知渠道的ID
//        String id = "channel_01";
////用户可以看到的通知渠道的名字
//        CharSequence name = getString(R.string.app_name);
////用户可看到的通知描述
//        String description = getString(R.string.app_name);
////构建NotificationChannel实例
//        NotificationChannel notificationChannel =
//                new NotificationChannel(id,name,NotificationManager.IMPORTANCE_HIGH);
////配置通知渠道的属性
//        notificationChannel.setDescription(description);
////设置通知出现时的闪光灯
////        notificationChannel.enableLights(true);
////        notificationChannel.setLightColor(Color.RED);
////设置通知出现时的震动
////        notificationChannel.enableVibration(true);
////        notificationChannel.setVibrationPattern(new long[]{100,200,300,400,500,400,300,200,100});
////在notificationManager中创建通知渠道
//        notificationManager.createNotificationChannel(notificationChannel);
//        Intent nfIntent = new Intent(this, MainActivity.class);
//        Notification notification = new NotificationCompat.Builder(this,id)
//                //指定通知的标题内容
//                .setContentTitle("\""+getString(R.string.app_name)+"\""+getString(R.string.n_1))
//                //设置通知的内容
//                .setContentText(getString(R.string.n_2))
//                //指定通知被创建的时间
//                .setWhen(System.currentTimeMillis())
//                //设置通知的小图标
//                .setSmallIcon(R.mipmap.ic_launcher)
//                //设置通知的大图标
////                .setLargeIcon(BitmapFactory.decodeResource(getResources(),
////                        R.mipmap.ic_launcher))
//                //添加点击跳转通知跳转
//                .setContentIntent(PendingIntent.
//                        getActivity(this, 0, nfIntent, 0))
//                //实现点击跳转后关闭通知
//                .setAutoCancel(true)
//                .build();
//
//        startForeground(110, notification);// 开始前台服务
//        return super.onStartCommand(intent, flags, startId);
//    }
//
//    @Override
//    public IBinder onBind(Intent intent) {
//        Log.d(TAG, "onBind()");
//        // TODO: Return the communication channel to the service.
//        throw new UnsupportedOperationException("Not yet implemented");
//    }
//
//    @Override
//    public void onDestroy() {
//        Log.d(TAG, "onDestroy()");
//        stopForeground(true);// 停止前台服务--参数：表示是否移除之前的通知
//        super.onDestroy();
//    }
//}