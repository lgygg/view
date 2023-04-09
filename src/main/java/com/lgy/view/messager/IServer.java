package com.lgy.view.messager;

import android.os.IBinder;

/**
 * @author: Administrator
 * @date: 2023/4/9
 */
public interface IServer<T,E> extends Behaviour<T,E>{
    IBinder getBinder();
}
