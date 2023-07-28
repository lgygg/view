package com.lgy.view.write;

import android.graphics.Bitmap;

/**
 * @author: Administrator
 * @date: 2023/7/28
 */
public interface WriteBoard {

    // 设置生成的图片的宽高
    void setViewWH(int viewW, int viewH);

    // 清除方法
    void clear();

    // 获取画图
    Bitmap getBitmap();

    // 是否存在书写
    boolean isMove();

    // 销毁
    void destroy();

    <T> T getView();
}
