package com.lgy.view.image;

import android.content.Context;
import android.widget.FrameLayout;

import com.lgy.util.LogUtils;


/**
 * 合成图片分为底图和上图，ComposedView是上图
 *
 */
public class ComposedView extends androidx.appcompat.widget.AppCompatImageView implements ComposeView.MoveListenner {
    private FrameLayout.LayoutParams layoutParams;
    private float left = 0;
    private float top = 0;
    private boolean canMove = false;
    private float x = 0;
    private float y = 0;
    public ComposedView(Context context) {
        super(context);
    }


    public float getPositionX(){
        return this.left;
    }
    public float getPositionY(){
        return this.top;
    }

    @Override
    public void onDown(float x, float y) {
        LogUtils.d(
                "x:"+x,
                "y:"+y,
                "left:"+this.getLeft(),
                "right:"+this.getRight()
                ,"top:"+this.getTop()
                ,"bottom:"+this.getBottom(),
                "getX:"+this.getX(),
                "getY:"+this.getY());
        this.canMove = this.getLeft() <x && x <this.getRight() && this.getTop()<y && y<this.getBottom();
        if (this.canMove) {
            this.x = x - this.getLeft();
            this.y = y - this.getTop();
        }
    }

    @Override
    public void onMove(float x, float y) {
        layoutParams = (FrameLayout.LayoutParams)getLayoutParams();
        LogUtils.d("onMove",
                "getX:"+this.getX(),
                "getY:"+this.getY());
        if (canMove) {
            this.left = (x-this.x);
            this.top =(y-this.y);

            if (canMove && this.left != layoutParams.leftMargin && this.top != layoutParams.topMargin) {
                layoutParams.leftMargin = (int)this.left;
                layoutParams.topMargin = (int)this.top;
                setLayoutParams(layoutParams);
            }
        }
    }

    @Override
    public void onUp(float x, float y) {
        this.canMove = false;
    }
}
