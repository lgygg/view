package com.lgy.view.messager;

import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.Parcelable;
import android.os.RemoteException;

import androidx.annotation.NonNull;

import com.lgy.util.LogUtils;

/**
 * @author: Administrator
 * @date: 2023/4/9
 */
public class Server<T,E> implements IServer<T,E>{
    private Messenger messenger;
    private Receiver<E> receiver;
    private Messenger messengerAccept;
    public Server(){
        this.messenger = new Messenger(new ServerAcceptHandler());
    }
    @Override
    public void send(T data) {
        //把处理结果封装到Message返回给客户端
        Message reply = Message.obtain(null, Behaviour.ACCEPT_MSG_WHAT);
        Bundle bundle = new Bundle();
        bundle.putString(Behaviour.ACCEPT_DATA_KEY, (String)data);
        reply.setData(bundle);
        try {
            //msg.replyTo @ android.os.Messenger类型，Messager.replyTo指向的客户端的Messenger,而Messenger又持有客户端的一个Binder对象（MessengerImpl）。服务端正是利用这个Binder对象做的与客户端的通信。
            if (messengerAccept != null) {
                messengerAccept.send(reply);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void accept(Receiver<E> obj) {
        receiver = obj;
    }

    @Override
    public IBinder getBinder() {
        return messenger.getBinder();
    }

    private class ServerAcceptHandler extends Handler{
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if (msg != null) {
                //接收客户端的消息并处理
                if (msg.what == Behaviour.SEND_MSG_WHAT) {
                    if (receiver != null) {
                        int type = msg.arg1;
                        Bundle bundle = msg.getData();
                        //不设置ClassLoader会导致无法反序列化
                        bundle.setClassLoader(receiver.getClass().getClassLoader());
                        E data = null;
                        if (type == Type.TYPE_STRING) {
                            data = (E)bundle.getString(Behaviour.SEND_DATA_KEY);
                        }else if (type == Type.TYPE_OBJECT) {
                            data = (E)bundle.getParcelable(Behaviour.SEND_DATA_KEY);
                        }
                        Server.this.receiver.onAccept(data);
                    }
                    messengerAccept = msg.replyTo;
                }
            } else {
                LogUtils.e( "handle client empty msg");
            }

        }
    }
}
