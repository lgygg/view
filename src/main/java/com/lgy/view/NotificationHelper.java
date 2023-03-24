package com.lgy.view;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.widget.RemoteViews;

import androidx.core.app.NotificationCompat;

/**
 * @author: Administrator
 * @date: 2023/3/24
 *
 * 通知工具类
 */
public class NotificationHelper {


    public static class BaseNotificationBuilder{
        NotificationManager mManager;
        NotificationCompat.Builder mBuilder;
        int mNormalNotificationId;
        public BaseNotificationBuilder(Context context,int mNormalNotificationId,String mNormalChannelId,String mNormalChannelName,String mNormalDescription,int importance,boolean showBadge){
            mManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            this.mNormalNotificationId = mNormalNotificationId;
            // 适配8.0及以上 创建渠道
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel channel = new NotificationChannel(mNormalChannelId, mNormalChannelName, importance);
                channel.setDescription(mNormalDescription);
                channel.setShowBadge(showBadge); // 是否在桌面显示角标
                mManager.createNotificationChannel(channel);
            }
            // 构建配置
            mBuilder = new NotificationCompat.Builder(context, mNormalChannelId);
        }
        // 点击意图 // setDeleteIntent 移除意图
        public BaseNotificationBuilder setContentIntent(PendingIntent pendingIntent){
            mBuilder.setContentIntent(pendingIntent);
            return this;
        }
        //移除意图
        public BaseNotificationBuilder setDeleteIntent(PendingIntent pendingIntent){
            mBuilder.setDeleteIntent(pendingIntent);
            return this;
        }
        // 标题
        public BaseNotificationBuilder setContentTitle(String title){
            mBuilder.setContentTitle(title);
            return this;
        }
        // 正文
        public BaseNotificationBuilder setContentText(String contentText){
            mBuilder.setContentText(contentText);
            return this;
        }
        // 小图标
        public BaseNotificationBuilder setSmallIcon(int smallIcon){
            mBuilder.setSmallIcon(smallIcon);
            return this;
        }
        // 大图标
        public BaseNotificationBuilder setLargeIcon(Bitmap icon){
            mBuilder.setLargeIcon(icon);
            return this;
        }
        // 7.0 设置优先级
        public BaseNotificationBuilder setPriority(int priority){
            mBuilder.setPriority(priority);
            return this;
        }
        // 是否自动消失（点击）or mManager.cancel(mNormalNotificationId)、cancelAll、setTimeoutAfter()
        public BaseNotificationBuilder setAutoCancel(boolean autoCancel){
            mBuilder.setAutoCancel(autoCancel);
            return this;
        }
        // 通知
        public BaseNotificationBuilder doNotify(){
            mManager.notify(mNormalNotificationId,mBuilder.build());
            return this;
        }
        public BaseNotificationBuilder cancel(){
            mManager.cancel(mNormalNotificationId);
            return this;
        }
        public BaseNotificationBuilder cancelAll(){
            mManager.cancelAll();
            return this;
        }
        // 自定义桌面通知数量
        public BaseNotificationBuilder setNumber(int number){
            mBuilder.setNumber(number);
            return this;
        }
        // 通知上的操作
        public BaseNotificationBuilder addAction(NotificationCompat.Action action){
            mBuilder.addAction(action);
            return this;
        }
        // 通知类别，"勿扰模式"时系统会决定要不要显示你的通知
        public BaseNotificationBuilder setCategory(String category){
            mBuilder.setCategory(category);
            return this;
        }
        // 屏幕可见性，锁屏时，显示icon和标题，内容隐藏
        //NotificationCompat.VISIBILITY_PRIVATE	只有在没有锁屏时会显示通知
        //NotificationCompat.VISIBILITY_PUBLIC	任何 情况下都显示通知
        //NotificationCompat.VISIBILITY_SECRET	在pin、password安全锁和没有锁的情况下显示通知
        public BaseNotificationBuilder setVisibility(int visibility){
            mBuilder.setVisibility(visibility);
            return this;
        }
        // 第3个参数indeterminate，false表示确定的进度，比如100，true表示不确定的进度，会一直显示进度动画，直到更新状态下载完成，或删除通知
        public BaseNotificationBuilder setProgress(int max, int progress, boolean indeterminate){
            mBuilder.setProgress(max, progress, false);
            return this;
        }
        // 设置自定义通知view
        public BaseNotificationBuilder setCustomContentView(RemoteViews contentView){
            mBuilder.setCustomContentView(contentView);
            return this;
        }
        // 设置自定义通知view
        public BaseNotificationBuilder setCustomBigContentView(RemoteViews contentView){
            mBuilder.setCustomBigContentView(contentView);
            return this;
        }

        // 设置是否为一个后台任务，默认为否；true表示是一个正在进行的后台任务，如音乐播放、文件下载、数据同步等
        public BaseNotificationBuilder setOngoing(boolean ongoing){
            mBuilder.setOngoing(ongoing);
            return this;
        }

        public static BaseNotificationBuilder builder(Context context,int mNormalNotificationId,String mNormalChannelId,String mNormalChannelName,String mNormalDescription,int importance,boolean showBadge){
            return new BaseNotificationBuilder(context, mNormalNotificationId, mNormalChannelId, mNormalChannelName, mNormalDescription, importance,showBadge);
        }
    }
    /**
     * 普通通知
     */
    public static class NormalNotification extends BaseNotificationBuilder{

        public NormalNotification(Context context, int mNormalNotificationId, String mNormalChannelId, String mNormalChannelName, String mNormalDescription, int importance,boolean showBadge) {
            super(context, mNormalNotificationId, mNormalChannelId, mNormalChannelName, mNormalDescription, importance,showBadge);
        }

        public static BaseNotificationBuilder builder(Context context, int mNormalNotificationId) {
            return BaseNotificationBuilder.builder(context, mNormalNotificationId, "mNormalChannelId", "mNormalChannelName", "mNormalDescription", NotificationManager.IMPORTANCE_LOW,false)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        }
    }
    /**
     * 重要通知
     */
    public static class HighNotification extends BaseNotificationBuilder{

        public HighNotification(Context context, int mNormalNotificationId, String mNormalChannelId, String mNormalChannelName, String mNormalDescription, int importance,boolean showBadge) {
            super(context, mNormalNotificationId, mNormalChannelId, mNormalChannelName, mNormalDescription, importance,showBadge);
        }

        public static BaseNotificationBuilder builder(Context context, int mHighNotificationId) {
            return BaseNotificationBuilder.builder(context, mHighNotificationId, "mHighChannelId", "mHighChannelName", "mHighDescription", NotificationManager.IMPORTANCE_HIGH,true)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setCategory(NotificationCompat.CATEGORY_MESSAGE) // 通知类别，"勿扰模式"时系统会决定要不要显示你的通知
                    .setVisibility(NotificationCompat.VISIBILITY_SECRET); // 屏幕可见性，锁屏时，显示icon和标题，内容隐藏
        }
    }

    /**
     * 重要通知
     */
    public static class ProgressNotification extends BaseNotificationBuilder{

        public ProgressNotification(Context context, int mNormalNotificationId, String mNormalChannelId, String mNormalChannelName, String mNormalDescription, int importance,boolean showBadge) {
            super(context, mNormalNotificationId, mNormalChannelId, mNormalChannelName, mNormalDescription, importance,showBadge);
        }

        public static BaseNotificationBuilder builder(Context context, int mHighNotificationId) {
            return BaseNotificationBuilder.builder(context, mHighNotificationId, "mProgressChannelId", "mProgressChannelName", "mProgressDescription", NotificationManager.IMPORTANCE_HIGH,true)
                    .setProgress(0,100,false);
        }
    }
    /**
     * diy通知
     */
    public static class CustomNotification extends BaseNotificationBuilder{

        public CustomNotification(Context context, int mNormalNotificationId, String mNormalChannelId, String mNormalChannelName, String mNormalDescription, int importance,boolean showBadge) {
            super(context, mNormalNotificationId, mNormalChannelId, mNormalChannelName, mNormalDescription, importance,showBadge);
        }

        public static BaseNotificationBuilder builder(Context context, int mHighNotificationId,RemoteViews contentView) {
            return BaseNotificationBuilder.builder(context, mHighNotificationId, "mCustomChannelId", "mCustomChannelName", "mCustomDescription", NotificationManager.IMPORTANCE_DEFAULT,true)
                    .setCustomBigContentView(contentView)
                    .setCustomContentView(contentView);
        }
    }

}
