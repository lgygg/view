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

# ComposeManager
## 作用
把手写签字和底图进行合成。
## 使用

```
//步骤一：打开签名界面，进行手写签名，生成签名图片
        composeManager = new ComposeManager(this);
        composeManager.showWriteBoardPage();
//步骤二：设置底图，通过拖动签名图片，把签名摆放到正确位置
        composeManager.paintFinish();
//步骤三：合成图片，导出合成图
        composeManager.export(Environment.getExternalStorageDirectory().getAbsolutePath()+"/final.png");
```

具体例子：

```
public class TestImageActivity extends AppCompatActivity {

    ImageView close;
    ComposeManager composeManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        composeManager = new ComposeManager(this);
        composeManager.showWriteBoardPage();
        String path = Environment.getExternalStorageDirectory().getAbsolutePath()+"/DCIM/Camera/";
        String backgroundPath = path+"background.jpg";
        composeManager.setBackgroundPath(backgroundPath);
        setContentView(R.layout.activity_test_image2);//activity_test_image2就是一个简单的FrameLayout
        ViewGroup viewGroup = findViewById(R.id.body);
        viewGroup.addView(composeManager.getView(),ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);

        close = new ImageView(this);
        close.setImageResource(R.drawable.ic_launcher_background);
        viewGroup.addView(close,100,100);
        close.setOnClickListener(v -> {
            composeManager.paintFinish();

            Button button  = new Button(composeManager.getView().getContext());
            button.setText("导出");
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    composeManager.export(Environment.getExternalStorageDirectory().getAbsolutePath()+"/final.png");
                }
            });
            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT,FrameLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.gravity = Gravity.RIGHT;
            composeManager.getView().addView(button,layoutParams);
        });

        ActivityCompat.requestPermissions(TestImageActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE},101);
    }
}
```