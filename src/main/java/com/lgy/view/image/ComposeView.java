package com.lgy.view.image;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.widget.FrameLayout;
import com.lgy.util.PhotoUtil;
import com.lgy.view.util.ViewUtil;

/**
 * 合成图片分为底图和上图，ComposeView是底图
 */
public class ComposeView extends FrameLayout {

    //画布
    private Canvas canvas;
    //图片
    private Bitmap bitmap;
    //当前组件的宽
    private int mWidth = 0;
    //当前组件的高
    private int mHeight = 0;
    //bitmap在手机上显示的宽度
    private int bitmapW = 0;
    //bitmap在手机上显示的高度
    private int bitmapH = 0;
    private float cur_x = 0;
    private float cur_y = 0;
    private MoveListenner moveListenner;
    public ComposeView(Context context) {
        super(context);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (bitmap != null) {
            Rect rect = ViewUtil.calculateFitObjectRect(this.mWidth,this.mHeight,bitmap.getWidth(),bitmap.getHeight());
            this.bitmapW = rect.right;
            this.bitmapH = rect.bottom;
            canvas.drawBitmap(changeBitmapWH(bitmap,rect.right,rect.bottom), 0, 0, null);
        }
        canvas.drawColor(Color.TRANSPARENT);
    }

    private Bitmap changeBitmapWH(Bitmap b,int viewW,int viewH) {
        int width = b.getWidth();
        int height = b.getHeight();
        // 未设置图片的大小则按原图显示
        viewW = ((viewW == 0) ? width : viewW);
        viewH = ((viewH == 0) ? height : viewH);
        // 计算缩放比例
        float scaleWidth = ((float) viewW) / width;
        float scaleHeight = ((float) viewH) / height;
        // 想要缩放的宽高
        return PhotoUtil.zoomBitmap(bitmap,scaleWidth,scaleHeight);
    }

    public void setBitmap(Bitmap bitmap) {
        //不设置背景色会导致onDraw不执行
        setBackgroundColor(Color.TRANSPARENT);
        this.bitmap = bitmap;
        requestLayout();
        invalidate();
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        getParent().requestDisallowInterceptTouchEvent(true);
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                cur_x = x;
                cur_y = y;
                if (moveListenner!=null) {
                    moveListenner.onDown(cur_x,cur_y);
                }
                break;
            }
            case MotionEvent.ACTION_MOVE: {
                cur_x = x;
                cur_y = y;
                if (moveListenner!=null) {
                    moveListenner.onMove(cur_x,cur_y);
                }
                break;
            }
            case MotionEvent.ACTION_UP: {
                if (moveListenner!=null) {
                    moveListenner.onUp(cur_x,cur_y);
                }
                break;
            }
        }
        return true;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.mWidth = w;
        this.mHeight = h;
        //创建跟view一样大的bitmap，用来保存签名(在控件大小发生改变时调用。)
        Bitmap bitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        canvas = new Canvas(bitmap);
        canvas.drawColor(Color.YELLOW);
    }


    // 清除方法
    public void clear() {
        if (canvas != null) {
            canvas.drawColor(Color.TRANSPARENT);
            invalidate();
        }
    }


    public void destroy() {
        if (null != bitmap) {
            bitmap.recycle();
        }
        bitmap = null;
        canvas = null;
    }

    public void setMoveListenner(MoveListenner moveListenner) {
        this.moveListenner = moveListenner;
    }

    public interface MoveListenner{
        void onDown(float x,float y);
        void onMove(float x,float y);
        void onUp(float x,float y);
    }

    public int getBitmapMachineHeight() {
        return this.bitmapH;
    }

    public int getBitmapMachineWidth() {
        return this.bitmapW;
    }

    public int getBitmapWidth() {
        return bitmap.getWidth();
    }

    public int getBitmapHeight() {
        return bitmap.getHeight();
    }
}
