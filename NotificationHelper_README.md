# view

```java
package android.lgy.com.testokhttp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

import com.lgy.view.NotificationHelper;

public class TestNotificationActivity extends AppCompatActivity {

    NotificationHelper.BaseNotificationBuilder notificationBuilder;
    int i = 0;
    private static final String mStopAction = "com.lgy.stop"; // 暂停继续action
    private static final String mDoneAction = "com.lgy.done"; // 完成action
    private int mFlag = 0;
    private boolean mIsStop = false; // 是否在播放 默认未开始
    NotificationReceiver mReceiver;
    RemoteViews views;
    Button update;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_notification);
//        initNormalNotification();
//        initHighNotification();
//        initProgressNotification();
        update = findViewById(R.id.mbUpdateCustom);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendBroadcast(new Intent(mStopAction));
            }
        });
        createReceiver();
        initCustomNotification();
        Button button = findViewById(R.id.notify);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                notificationBuilder.setContentTitle("重要通知"+i) // 标题
//                        .setContentText("重要通知内容"+i); // 文本
//                notificationBuilder.doNotify();
//                i++;
                notificationBuilder.setContentTitle("Progress通知") // 标题
                        .setContentText("下载中："+i+"%")
                        .setProgress(100,0,true); // 文本
                notificationBuilder.doNotify();
                i++;
            }
        });
        Button cancel = findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                notificationBuilder.cancel();
            }
        });
    }

    private void initCustomNotification(){
        views = new RemoteViews(getPackageName(), R.layout.layout_notification);
        // 适配12.0及以上
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            mFlag = PendingIntent.FLAG_IMMUTABLE;
        } else {
            mFlag =  PendingIntent.FLAG_UPDATE_CURRENT;
        }
        // 添加暂停继续事件
        Intent intentStop = new Intent(mStopAction);
        PendingIntent pendingIntentStop = PendingIntent.getBroadcast(this, 0, intentStop, mFlag);
        views.setOnClickPendingIntent(R.id.btn_stop, pendingIntentStop);

        // 添加完成事件
        Intent intentDone = new Intent(mDoneAction);
        PendingIntent pendingIntentDone = PendingIntent.getBroadcast(this, 0, intentDone, mFlag);
        views.setOnClickPendingIntent(R.id.btn_done, pendingIntentDone);

        notificationBuilder = NotificationHelper.CustomNotification.builder(TestNotificationActivity.this,111,views);
        Intent intent = new Intent(this, MainActivity2.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);
        notificationBuilder
                .setSmallIcon(R.mipmap.ic_launcher) // 小图标
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher)) // 大图标
                .setContentIntent(pendingIntent) // 跳转配置
                .setAutoCancel(true).setOngoing(true);

    }

    /**
     * 创建广播接收器
     */
    private void createReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        // 添加接收事件监听
        intentFilter.addAction(mStopAction);
        intentFilter.addAction(mDoneAction);
        mReceiver = new NotificationReceiver();
        // 注册广播
        registerReceiver(mReceiver, intentFilter);
    }

    private class NotificationReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            // 拦截接收事件
            if (intent.getAction() == mStopAction) {
                // 改变状态
                mIsStop = !mIsStop;

                // 根据状态更新UI
                if (mIsStop) {
                    views.setTextViewText(R.id.tv_status,"那些你很冒险的梦-停止播放");
                    views.setTextViewText(R.id.btn_stop,"继续");
                    update.setText("继续");
                } else {
                    views.setTextViewText(R.id.tv_status,"那些你很冒险的梦-正在播放");
                    views.setTextViewText(R.id.btn_stop,"暂停");
                    update.setText("暂停");
                }
                notificationBuilder.doNotify();
            } else if (intent.getAction() == mDoneAction) {
                Toast.makeText(context, "完成", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void initProgressNotification(){
        notificationBuilder = NotificationHelper.ProgressNotification.builder(TestNotificationActivity.this,111);
        Intent intent = new Intent(this, MainActivity2.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);
        notificationBuilder.setContentTitle("Progress通知") // 标题
                .setContentText("下载中："+i+"%") // 文本
                .setSmallIcon(R.mipmap.ic_launcher) // 小图标
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher)) // 大图标
                .setPriority(NotificationCompat.PRIORITY_DEFAULT) // 7.0 设置优先级
                .setContentIntent(pendingIntent) // 跳转配置
                .setAutoCancel(true);

    }
    private void initHighNotification(){
        notificationBuilder = NotificationHelper.HighNotification.builder(TestNotificationActivity.this,222);
        Intent intent = new Intent(this, MainActivity2.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        Intent intent2 = new Intent(this, MainActivity3.class);
        PendingIntent pendingIntent2 = PendingIntent.getActivity(this, 0, intent2, PendingIntent.FLAG_IMMUTABLE);
        notificationBuilder.setContentTitle("重要通知") // 标题
                .setContentText("重要通知内容") // 文本
                .setSmallIcon(R.mipmap.ic_launcher) // 小图标
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher)) // 大图标
                .setContentIntent(pendingIntent) // 跳转配置
                .addAction(new NotificationCompat.Action(R.mipmap.ic_launcher_round,"hello",pendingIntent2))
                .setAutoCancel(false)
                .setNumber(99);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                notificationBuilder.setContentTitle("重要通知"+i) // 标题
                        .setContentText("重要通知内容"+i); // 文本
                notificationBuilder.doNotify();
                i++;
            }
        },10000);
    }
    private void initNormalNotification(){
        notificationBuilder = NotificationHelper.NormalNotification.builder(TestNotificationActivity.this,111);
        Intent intent = new Intent(this, MainActivity2.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE);
        notificationBuilder.setContentTitle("普通通知") // 标题
                .setContentText("普通通知内容") // 文本
                .setSmallIcon(R.mipmap.ic_launcher) // 小图标
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher)) // 大图标
                .setPriority(NotificationCompat.PRIORITY_DEFAULT) // 7.0 设置优先级
                .setContentIntent(pendingIntent) // 跳转配置
                .setAutoCancel(false);
    }
}
```