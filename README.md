# view

# MessengerHelper

## 作用
用于Messenger跨进程通信。

## 使用

客户端
```
    Client<ObjectData,String> client;
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.e("qew","onServiceConnected================");
            client = new Client<>(service);
            client.accept(new Receiver<String>() {
                @Override
                public void onAccept(String data) {
                    Toast.makeText(MainActivity.this, "data:"+data, Toast.LENGTH_SHORT).show();
                }
            });
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };
```
绑定服务和发送信息
```
        TextView textView = findViewById(R.id.text);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction("lgy.MessagerService");
                intent.setPackage("com.lgy.testservice");
                bindService(intent,serviceConnection, Context.BIND_AUTO_CREATE);
            }
        });
        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (client != null) {
                    ObjectData data = new ObjectData();
                    data.setName("梁桂毓");
                    data.setAge(18);
                    client.send(data);
                }
            }
        });
```

服务端
```
package com.lgy.testservice;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.lgy.testclient.ObjectData;
import com.lgy.view.messager.Receiver;
import com.lgy.view.messager.Server;

public class MessagerService extends Service {
    Server<String, ObjectData> server;
    public MessagerService() {
        server = new Server<>();
        server.accept(new Receiver<ObjectData>() {
            @Override
            public void onAccept(ObjectData data) {
                server.send(data.toString());
            }
        });
    }

    @Override
    public IBinder onBind(Intent intent) {
        return server.getBinder();
    }
}
```