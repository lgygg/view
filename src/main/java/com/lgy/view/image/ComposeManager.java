package com.lgy.view.image;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.lgy.util.CalculationUtil;
import com.lgy.util.LogUtils;
import com.lgy.util.PhotoUtil;
import com.lgy.view.write.HandWriteView;
import com.lgy.view.write.WriteBoard;

/**
 * 管理合成Bitmap
 */
public class ComposeManager {
    private FrameLayout bodyLayout;
    private Context context;
    private WriteBoard writeKeyBoardView;
    private Bitmap foregroundBitmap;
    private Bitmap backgroundBitmap;
    private ComposeView composeView;
    private ComposedView item;
    public ComposeManager(Context context){
        this.context = context;
        bodyLayout = new FrameLayout(context);
        bodyLayout.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT));
    }

    public FrameLayout getView(){
        return bodyLayout;
    }

    public void setWriteKeyBoardView(WriteBoard writeKeyBoardView) {
        this.writeKeyBoardView = writeKeyBoardView;
    }

    public void showWriteBoardPage(){
        if (writeKeyBoardView == null) {
            writeKeyBoardView = new HandWriteView(context);
            writeKeyBoardView.setViewWH(800,400);
        }
        bodyLayout.addView(writeKeyBoardView.getView(),ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
    }

    public void setBackgroundBitmap(Bitmap bitmap){
        this.backgroundBitmap = bitmap;
    }
    public void setBackgroundPath(String path){
        this.backgroundBitmap = BitmapFactory.decodeFile(path);
    }

    public void paintFinish(){
        foregroundBitmap = writeKeyBoardView.getBitmap().copy(Bitmap.Config.ARGB_8888,true);
        bodyLayout.removeView(writeKeyBoardView.getView());

        composeView = new ComposeView(context);
        bodyLayout.addView(composeView,ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
        composeView.setBitmap(this.backgroundBitmap);

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                LogUtils.eTag("foregroundBitmap","foregroundBitmap:"+foregroundBitmap.isRecycled()
                , "backgroundBitmap:"+backgroundBitmap.isRecycled());
                item = new ComposedView(context);
                item.setImageBitmap(foregroundBitmap);
                composeView.setMoveListenner(item);
                double w = CalculationUtil.div(foregroundBitmap.getWidth(),composeView.getBitmapWidth(),2);
                double h = CalculationUtil.div(foregroundBitmap.getHeight(),composeView.getBitmapHeight(),2);
                composeView.addView(item,(int)(w*composeView.getBitmapMachineWidth()),(int)(h*composeView.getBitmapMachineHeight()));
            }
        },1000);
    }

    public void export(String savePath){
        new Thread(() -> {
            double wPer = CalculationUtil.div(this.backgroundBitmap.getWidth(),composeView.getBitmapMachineWidth(),2);
            double hPer = CalculationUtil.div(this.backgroundBitmap.getHeight(),composeView.getBitmapMachineHeight(),2);
            LogUtils.eTag("onClick",
                    "wPer:"+wPer,
                    "hPer:"+hPer,
                    "backgroundBm.getWidth:"+this.backgroundBitmap.getWidth(),
                    "backgroundBm.getHeight:"+this.backgroundBitmap.getHeight(),
                    "composeView.getmWidth:"+composeView.getBitmapMachineWidth(),
                    "composeView.getmHeight:"+composeView.getBitmapMachineHeight(),
                    "getPositionX:"+item.getPositionX(),
                    "getPositionY:"+item.getPositionY());
            Bitmap bitmap = PhotoUtil.combineBitmap(this.backgroundBitmap,this.foregroundBitmap, (int)(item.getPositionX()*wPer), (int) (item.getPositionY()*hPer));
            try {
                PhotoUtil.saveBitmap(bitmap, savePath);
            } catch (Exception e) {
                e.printStackTrace();
            }
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    composeView.setBitmap(bitmap);
                    item.setVisibility(View.GONE);
                }
            });
        }).start();
    }

    public void destroy(){
        bodyLayout = null;
        context = null;
        if (writeKeyBoardView != null) {
            writeKeyBoardView.destroy();
            writeKeyBoardView = null;
        }
        if (foregroundBitmap != null) {
            foregroundBitmap.recycle();
            foregroundBitmap = null;
        }
        if (backgroundBitmap != null) {
            backgroundBitmap.recycle();
            backgroundBitmap = null;
        }
        composeView = null;
        item = null;
    }
}
