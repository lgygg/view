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
public class Client<T,E> implements Behaviour<T,E>{
    private Messenger messengerSend;
    private Messenger messengerAccept;
    private Handler sendHandler;
    private ClientAcceptHandler acceptHandler;
    private Receiver<E> receiver;
    public Client(IBinder service){
        messengerSend = new Messenger(service);
        messengerAccept = new Messenger(new ClientAcceptHandler());
    }
    @Override
    public void send(T data) {
        Message msg=Message.obtain(null,Behaviour.SEND_MSG_WHAT);
        Bundle bundle = new Bundle();
        int type = -1;
        if (data instanceof String) {
            type = Type.TYPE_STRING;
            bundle.putString(Behaviour.SEND_DATA_KEY,(String)data);
        }else if (data instanceof Parcelable) {
            type = Type.TYPE_OBJECT;
            bundle.putParcelable(Behaviour.SEND_DATA_KEY,(Parcelable)data);
        }
        msg.arg1 = type;
        msg.setData(bundle);
        //Client 发信时指定希望回信人，把客户端进程的Messenger对象设置到Message中
        msg.replyTo = messengerAccept;
        try {
            messengerSend.send(msg);//跨进程传递
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void accept(Receiver<E> obj) {
        receiver = obj;
    }


    private class ClientAcceptHandler extends Handler{
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if(msg!=null && msg.what== Behaviour.ACCEPT_MSG_WHAT){
                String resp = msg.getData().getString(Behaviour.ACCEPT_DATA_KEY);
                Client.this.receiver.onAccept((E)resp);
            }
        }
    }
}
