package com.lgy.view.write;

import java.io.ByteArrayOutputStream;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

import com.lgy.util.ScreenUtils;

public class HandWriteView extends LinearLayout implements WriteBoard{

	private PaintView paintView;
	private Context context;

	public HandWriteView(Context context) {
		super(context);
		this.context = context;
		init();
	}

	public HandWriteView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		init();
	}

	private void init() {
		this.setOrientation(LinearLayout.VERTICAL);
		this.setGravity(Gravity.CENTER_VERTICAL);
		paintView = new PaintView(getContext());
		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);
		int margin = ScreenUtils.dip2px(getContext(), 3);
		params.setMargins(margin, margin, margin, margin);
		this.addView(paintView, params);
		params = null;
	}

	// 设置生成的图片的宽高
	public void setViewWH(int viewW, int viewH) {
		if (null != paintView) {
			paintView.viewW = viewW;
			paintView.viewH = viewH;
		}
	}

	// 清除方法
	public void clear() {
		if (null != paintView) {
			paintView.clear();
		}
	}

	// 获取画图
	public Bitmap getBitmap() {
		if (null != paintView) {
			return paintView.getBitmap();
		}
		return null;
	}

	// 是否存在书写
	public boolean isMove() {
		if (null != paintView) {
			return paintView.isMove();
		}
		return false;
	}

	// 销毁
	public void destroy() {
		if (null != paintView) {
			paintView.destroy();
		}
		paintView = null;
	}

	@Override
	public View getView() {
		return this;
	}

	private class PaintView extends View {
		private Paint paint;
		private Canvas cacheCanvas;
		private Bitmap cacheBitmap;
		private Path path;
		private boolean isMove;
		private int viewH;
		private int viewW;

		public PaintView(Context context) {
			super(context);
			init();
		}

		private void init() {
			// 笔触
			paint = new Paint();
			paint.setAntiAlias(true);
			paint.setStyle(Paint.Style.STROKE);
			paint.setColor(Color.BLACK);
			paint.setStrokeWidth(ScreenUtils.dip2px(context, 2.5f));
			// 手画路径
			path = new Path();
		}

		@Override
		protected void onDraw(Canvas canvas) {
			canvas.drawBitmap(cacheBitmap, 0, 0, null);
			canvas.drawPath(path, paint);
		}

		@Override
		protected void onSizeChanged(int w, int h, int oldw, int oldh) {
			super.onSizeChanged(w,h,oldw,oldh);
			// 画布大小
			cacheBitmap = Bitmap.createBitmap(getWidth(), getHeight(), Config.ARGB_8888);
			cacheCanvas = new Canvas(cacheBitmap);
			cacheCanvas.drawColor(Color.TRANSPARENT);
		}

		private float cur_x, cur_y;

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
				path.moveTo(cur_x, cur_y);
				break;
			}
			case MotionEvent.ACTION_MOVE: {
				path.quadTo(cur_x, cur_y, x, y);
				cur_x = x;
				cur_y = y;
				if (cur_x > 2.0 && cur_y > 2.0) {
					isMove = true; // 判断是否书写文字
				}
				break;
			}
			case MotionEvent.ACTION_UP: {
				cacheCanvas.drawPath(path, paint);
				path.reset();
				break;
			}
			}
			invalidate();
			return true;
		}

		// 清除方法
		public void clear() {
			isMove = false;
			if (cacheCanvas != null) {
				paint.setColor(Color.WHITE);
				cacheCanvas.drawPaint(paint);
				paint.setColor(Color.BLACK);
				cacheCanvas.drawColor(Color.TRANSPARENT);
				invalidate();
			}
		}

		// 获取画图
		public Bitmap getBitmap() {
			if (null == cacheBitmap) {
				return null;
			}
			return changeBitmapWH(cacheBitmap);
		}

		// 是否存在书写
		public boolean isMove() {
			return isMove;
		}

		public void destroy() {
			if (null != cacheBitmap) {
				cacheBitmap.recycle();
			}
			cacheBitmap = null;
			paint = null;
			cacheCanvas = null;
		}

		// 缩放图片大小
		private Bitmap changeBitmapWH(Bitmap b) {
			int width = b.getWidth();
			int height = b.getHeight();
			// 未设置图片的大小则按原图显示
			viewW = ((viewW == 0) ? width : viewW);
			viewH = ((viewH == 0) ? height : viewH);
			// 计算缩放比例
			float scaleWidth = ((float) viewW) / width;
			float scaleHeight = ((float) viewH) / height;
			// 想要缩放的宽高
			Matrix matrix = new Matrix();
			matrix.postScale(scaleWidth, scaleHeight);
			Bitmap changeBitmap = Bitmap.createBitmap(b, 0, 0, width, height,
					matrix, true);
			return changeBitmap;
		}

		// 缩放图片大小
		private Bitmap compressBitmap(Bitmap bitmap) {
			// 将bitmap对象转化为字节数组
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			bitmap.compress(CompressFormat.JPEG, 100, os);
			byte[] bts = os.toByteArray();
			// 根据尺寸大小压缩图片
			Options options = new Options();
			options.inJustDecodeBounds = true;
			BitmapFactory.decodeByteArray(bts, 0, bts.length, options);
			// 获取缩放比例
			int width = bitmap.getWidth();
			viewW = ((viewW == 0) ? width : viewW);
			options.inSampleSize = Math.round(width / viewW);
			options.inJustDecodeBounds = false;
			return BitmapFactory.decodeByteArray(bts, 0, bts.length, options);
		}
	}
}
