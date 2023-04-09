package com.lgy.view.messager;

import android.os.Messenger;

/**
 * @author: Administrator
 * @date: 2023/4/9
 */
public interface Receiver<T> {
    public void onAccept(T data);
}
