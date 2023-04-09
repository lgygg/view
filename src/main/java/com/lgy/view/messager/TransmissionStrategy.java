package com.lgy.view.messager;

import android.os.Messenger;

/**
 * @author: Administrator
 * @date: 2023/4/9
 * 传输策略
 */
public interface TransmissionStrategy {

    void processingData(Object data);

    void send(Messenger messenger);
}
