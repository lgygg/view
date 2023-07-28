package com.lgy.view.util;

import android.graphics.Rect;

/**
 * @author: Administrator
 * @date: 2023/7/28
 */
public class ViewUtil {
    /**
     * 类似与ImageView的FitCenter效果，即view沿着x,y轴伸缩，直到x，y有一个轴先占满，且居中
     * 返回坐标
     *
     * @param containerWidth
     * @param containerHeight
     * @param objectWidth
     * @param objectHeight
     * @return
     */
    public static Rect calculateFitCenterObjectRect(float containerWidth, float containerHeight, float objectWidth, float objectHeight) {

        // scale value to make fit center
        double scale = Math.min( (double)containerWidth / (double)objectWidth, (double)containerHeight / (double)objectHeight);

        int h = (int) (scale * objectHeight); // new height of the object
        int w = (int) (scale * objectWidth); // new width of the object

        int x = (int) ((containerWidth - w) * 0.5f); // new x location of the object relative to the container
        int y = (int) ((containerHeight - h) * 0.5f); // new y  location of the object relative to the container

        return new Rect(x, y, x + w, y + h);
    }

    /**
     * 类似与ImageView的FitCenter效果，即view沿着x,y轴伸缩，直到x，y有一个轴先占满，坐标不局中
     *
     * @param containerWidth
     * @param containerHeight
     * @param objectWidth
     * @param objectHeight
     * @return
     */
    public static Rect calculateFitObjectRect(float containerWidth, float containerHeight, float objectWidth, float objectHeight) {
        Rect rect = calculateFitCenterObjectRect(containerWidth,containerHeight,objectWidth,objectHeight);
        return new Rect(0, 0,  rect.right, rect.bottom);
    }
}
