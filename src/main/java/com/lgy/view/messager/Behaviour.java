package com.lgy.view.messager;

/**
 * @author: Administrator
 * @date: 2023/4/9
 */
public interface Behaviour<T,E> {
    int SEND_MSG_WHAT = 8899;
    int ACCEPT_MSG_WHAT = 8900;
    String SEND_DATA_KEY = "send_data";
    String ACCEPT_DATA_KEY = "accept_data";
    void send(T data);
    void accept(Receiver<E> obj);
}
