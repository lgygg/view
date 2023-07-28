package com.lgy.view.write;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import androidx.annotation.ColorInt;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class LinePathView extends View {

    private static final String TAG = "LinePathView";

    private Context mContext;
    //起点X
    private float mX;
    //起点Y
    private float mY;
    //手写画笔
    private final Paint mGesturePaint = new Paint();

    //画米字格的画笔
    private final Paint mMiPaint = new Paint();
    //画虚线
    private final Paint mDottedLinePaint = new Paint();
    //路径
    private final Path mPath = new Path();
    //画布
    private Canvas cacheCanvas;
    //生成的图片
    private Bitmap cachebBitmap;
    //画笔宽度 px；
    private int mPaintWidth = 10;
    //画笔颜色
    private int mPenColor = Color.BLACK;
    //背景色（指最终签名结果文件的背景颜色，默认为透明色）
    private int mBackColor = Color.TRANSPARENT;


    //文字
    private String mText;
    //获取一个字宽
    float mTextWidth;
    //获取一个字高
    float mTextHeight;
    //控件的宽度
    float mSumWidth;
    //控件的高度
    float mSumHeight;

    public LinePathView(Context context) {
        super(context);
        init(context);
    }

    public LinePathView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public LinePathView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }


    private void init(Context context) {
        this.mContext = context;
        //设置抗锯齿
        mGesturePaint.setAntiAlias(true);
        //设置签名笔画样式
        mGesturePaint.setStyle(Paint.Style.STROKE);
        mGesturePaint.setStrokeCap(Paint.Cap.ROUND);
        //设置笔画宽度
        mGesturePaint.setStrokeWidth(mPaintWidth);
        //设置签名颜色
        mGesturePaint.setColor(mPenColor);
        initMi();
        initDottedLine();
    }

    Rect rect;

    private void initMi() {
        int color = Color.BLACK;
        int strokeWidth = 10;
        //设置抗锯齿
        mMiPaint.setAntiAlias(true);

        mMiPaint.setTextSize(strokeWidth);
        //设置签名笔画样式
        mMiPaint.setStyle(Paint.Style.FILL);
//        mMiPaint.setStrokeCap(Paint.Cap.ROUND);
        //设置笔画宽度
        mMiPaint.setStrokeWidth(10);
        //设置签名颜色
        mMiPaint.setColor(color);
        rect = new Rect();
    }

    //画虚线
    private void initDottedLine() {
        int color = Color.parseColor("#339098A9");
        mDottedLinePaint.setStyle(Paint.Style.STROKE);
        mDottedLinePaint.setAntiAlias(true);
        mDottedLinePaint.setStrokeWidth(2);
        mDottedLinePaint.setColor(color);
        mDottedLinePaint.setPathEffect(new DashPathEffect(new float[]{4, 4}, 0));
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        //创建跟view一样大的bitmap，用来保存签名(在控件大小发生改变时调用。)
        cachebBitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        cacheCanvas = new Canvas(cachebBitmap);
        cacheCanvas.drawColor(mBackColor);
        drawText(cacheCanvas);
    }


    private void drawText(Canvas canvas) {
        String test = "我";
        //将内容的长和宽。添加到rect矩形中
        mMiPaint.getTextBounds(test, 0, test.length(), rect);
        //获取宽
        mTextWidth = rect.width();
        mTextHeight = rect.height();
        mSumWidth = getWidth();
        mSumHeight = getHeight();
        if (TextUtils.isEmpty(mText)) {
            return;
        }
        float centerY= mSumHeight / 2;
        char[] chars = mText.toCharArray();
        int sum1 = chars.length * 2;
        for (int i = 0; i < chars.length; i++) {
            String txt = chars[i] + "";
            //开始位置
            int multiple = 2 * i + 1;
            float x = mSumWidth * multiple / sum1 - mTextWidth / 2;
            float y = mSumHeight / 2 + mTextHeight / 2;
            canvas.drawText(txt, x, y, mMiPaint);

            float centerX = mSumWidth * multiple / sum1;
            drawDottedLine(centerX, centerY, canvas);
        }
    }

    /**
     * 字的开始位置
     * @param x
     * @param y
     * @param canvas
     */
    private void drawDottedLine(float x, float y, Canvas canvas) {
        float width = mTextWidth * 1.5f;
        float height = mTextHeight * 1.5f;
        // 左上至右下线
        float leftTopLineStartX = x - width / 2;
        float leftTopLineStartY = y - height / 2;
        float leftTopLineEndX = x + width / 2;
        float leftTopLineEndY = y + height / 2;
        drawDottedLine(leftTopLineStartX, leftTopLineStartY, leftTopLineEndX, leftTopLineEndY, canvas);

        // 水平线
        float horizontalLineStartX = leftTopLineStartX;
        float horizontalLineStartY = leftTopLineStartY + height / 2;
        float horizontalLineEndX = horizontalLineStartX + width;
        float horizontalLineEndY = horizontalLineStartY;
        drawDottedLine(horizontalLineStartX, horizontalLineStartY, horizontalLineEndX, horizontalLineEndY, canvas);

        // 右下至左上
        float lowerRightLineStartX = leftTopLineStartX;
        float lowerRightLineStartY = leftTopLineEndY;
        float lowerRightLineEndX = lowerRightLineStartX + width;
        float lowerRightLineEndY = leftTopLineStartY;
        drawDottedLine(lowerRightLineStartX, lowerRightLineStartY, lowerRightLineEndX, lowerRightLineEndY, canvas);

        // 纵向线
        float verticalLineStartX = leftTopLineStartX + width / 2;
        float verticalLineStartY = leftTopLineStartY;
        float verticalLineEndX = verticalLineStartX;
        float verticalLineEndY = leftTopLineStartY + height;
        drawDottedLine(verticalLineStartX, verticalLineStartY, verticalLineEndX,verticalLineEndY, canvas);
    }

    private void drawDottedLine(float startX, float startY, float endX, float endY, Canvas canvas) {
        Path path = new Path();
        path.moveTo(startX, startY);
        path.lineTo(endX, endY);
        canvas.drawPath(path, mDottedLinePaint);

    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //画此次笔画之前的签名
        canvas.drawBitmap(cachebBitmap, 0, 0, mGesturePaint);
        // 通过画布绘制多点形成的图形
        canvas.drawPath(mPath, mGesturePaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                touchDown(event);
                break;
            case MotionEvent.ACTION_MOVE:
                touchMove(event);
                break;
            case MotionEvent.ACTION_UP:
                //将路径画到bitmap中，即一次笔画完成才去更新bitmap，而手势轨迹是实时显示在画板上的。
                cacheCanvas.drawPath(mPath, mGesturePaint);
                mPath.reset();
                break;
        }
        // 更新绘制
        invalidate();
        return true;
    }


    // 手指点下屏幕时调用
    private void touchDown(MotionEvent event) {
        // 重置绘制路线
        mPath.reset();
        float x = event.getX();
        float y = event.getY();
        mX = x;
        mY = y;
        // mPath绘制的绘制起点
        mPath.moveTo(x, y);
    }

    // 手指在屏幕上滑动时调用
    private void touchMove(MotionEvent event) {
        final float x = event.getX();
        final float y = event.getY();
        final float previousX = mX;
        final float previousY = mY;
        final float dx = Math.abs(x - previousX);
        final float dy = Math.abs(y - previousY);
        // 两点之间的距离大于等于3时，生成贝塞尔绘制曲线
        if (dx >= 3 || dy >= 3) {
            // 设置贝塞尔曲线的操作点为起点和终点的一半
            float cX = (x + previousX) / 2;
            float cY = (y + previousY) / 2;
            // 二次贝塞尔，实现平滑曲线；previousX, previousY为操作点，cX, cY为终点
            mPath.quadTo(previousX, previousY, cX, cY);
            // 第二次执行时，第一次结束调用的坐标值将作为第二次调用的初始坐标值
            mX = x;
            mY = y;
        }
    }


    /**
     * 清除画板
     */
    public void clear() {
        if (cacheCanvas != null) {
            //更新画板信息
            mGesturePaint.setColor(mPenColor);
            cacheCanvas.drawColor(mBackColor);
            mGesturePaint.setColor(mPenColor);
            drawText(cacheCanvas);
            invalidate();
        }
    }

    /**
     * 保存画板
     *
     * @param path 保存到路径
     */
    public void save(String path) throws IOException {
        save(path, false, 0);
    }

    /**
     * 保存画板
     *
     * @param path       保存到路径
     * @param clearBlank 是否清除边缘空白区域
     * @param blank      要保留的边缘空白距离
     */
    public void save(String path, boolean clearBlank, int blank) throws IOException {

        Bitmap bitmap = cachebBitmap;
        //BitmapUtil.createScaledBitmapByHeight(srcBitmap, 300);//  压缩图片
        if (clearBlank) {
            bitmap = clearBlank(bitmap, blank);
        }
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos);
        byte[] buffer = bos.toByteArray();
        if (buffer != null) {
            File file = new File(path);
            if (file.exists()) {
                file.delete();
            }
            OutputStream outputStream = new FileOutputStream(file);
            outputStream.write(buffer);
            outputStream.close();
        }
    }

    /**
     * 保存画板
     *
     * @param clearBlank 是否清除边缘空白区域
     * @param blank      要保留的边缘空白距离
     */
    public Bitmap saveToBitmap(boolean clearBlank, int blank) {

        Bitmap bitmap = cachebBitmap;
        //BitmapUtil.createScaledBitmapByHeight(srcBitmap, 300);//  压缩图片
        if (clearBlank) {
            bitmap = clearBlank(bitmap, blank);
        }
//        int width = bitmap.getWidth();
//        int height = bitmap.getHeight();
//        int w;
//        int h;
//        if(width>2*height){
//            w = width -(width - 2*height);
//            h = height;
//        } else {
//            w = width;
//            h = height - (height - width/2);
//        }
//        return ThumbnailUtils.extractThumbnail(bitmap,w,h);
        return bitmap;
    }

    /**
     * 获取画板的bitmap
     *
     * @return
     */
    public Bitmap getBitMap() {
        setDrawingCacheEnabled(true);
        buildDrawingCache();
        Bitmap bitmap = getDrawingCache();
        setDrawingCacheEnabled(false);
        return bitmap;
    }

    /**
     * 逐行扫描 清楚边界空白。
     *
     * @param bp
     * @param blank 边距留多少个像素
     * @return
     */
    private Bitmap clearBlank(Bitmap bp, int blank) {
        int HEIGHT = bp.getHeight();
        int WIDTH = bp.getWidth();
        int top = 0, left = 0, right = 0, bottom = 0;
        int[] pixs = new int[WIDTH];
        boolean isStop;
        //扫描上边距不等于背景颜色的第一个点
        for (int y = 0; y < HEIGHT; y++) {
            bp.getPixels(pixs, 0, WIDTH, 0, y, WIDTH, 1);
            isStop = false;
            for (int pix : pixs) {
                if (pix != mBackColor) {
                    top = y;
                    isStop = true;
                    break;
                }
            }
            if (isStop) {
                break;
            }
        }
        //扫描下边距不等于背景颜色的第一个点
        for (int y = HEIGHT - 1; y >= 0; y--) {
            bp.getPixels(pixs, 0, WIDTH, 0, y, WIDTH, 1);
            isStop = false;
            for (int pix : pixs) {
                if (pix != mBackColor) {
                    bottom = y;
                    isStop = true;
                    break;
                }
            }
            if (isStop) {
                break;
            }
        }
        pixs = new int[HEIGHT];
        //扫描左边距不等于背景颜色的第一个点
        for (int x = 0; x < WIDTH; x++) {
            bp.getPixels(pixs, 0, 1, x, 0, 1, HEIGHT);
            isStop = false;
            for (int pix : pixs) {
                if (pix != mBackColor) {
                    left = x;
                    isStop = true;
                    break;
                }
            }
            if (isStop) {
                break;
            }
        }
        //扫描右边距不等于背景颜色的第一个点
        for (int x = WIDTH - 1; x > 0; x--) {
            bp.getPixels(pixs, 0, 1, x, 0, 1, HEIGHT);
            isStop = false;
            for (int pix : pixs) {
                if (pix != mBackColor) {
                    right = x;
                    isStop = true;
                    break;
                }
            }
            if (isStop) {
                break;
            }
        }
        if (blank < 0) {
            blank = 0;
        }
        //计算加上保留空白距离之后的图像大小
//        left = left - blank > 0 ? left - blank : 0;
//        top = top - blank > 0 ? top - blank : 0;
//        right = right + blank > WIDTH - 1 ? WIDTH - 1 : right + blank;
//        bottom = bottom + blank > HEIGHT - 1 ? HEIGHT - 1 : bottom + blank;

        left = Math.max(left - blank, 0);
        top = Math.max(top - blank, 0);
        right = Math.min(right + blank, WIDTH - 1);
        bottom = Math.min(bottom + blank, HEIGHT - 1);
        //防止创建null的bitmap  引发的崩溃
        if (left == 0 && top == 0 && right == 0 && bottom == 0) {
            left = 1;
            top = 1;
            right = 351;
            bottom = 251;
        }
        return Bitmap.createBitmap(bp, left, top, right - left, bottom - top);
    }

    /**
     * 设置画笔宽度 默认宽度为10px
     *
     * @param mPaintWidth
     */
    public void setPaintWidth(int mPaintWidth) {
        mPaintWidth = mPaintWidth > 0 ? mPaintWidth : 10;
        this.mPaintWidth = mPaintWidth;
        mGesturePaint.setStrokeWidth(mPaintWidth);

    }


    public void setBackColor(@ColorInt int backColor) {
        mBackColor = backColor;
    }


    /**
     * 设置画笔颜色
     *
     * @param mPenColor
     */
    public void setPenColor(int mPenColor) {
        this.mPenColor = mPenColor;
        mGesturePaint.setColor(mPenColor);
    }

    public void setText(String str) {
        this.mText = str;
    }
}
