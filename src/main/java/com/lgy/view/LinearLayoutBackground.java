package com.lgy.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.util.AttributeSet;
import android.widget.LinearLayout;
/**
 * 可以设置点击背景效果的线性布局
 */
public class LinearLayoutBackground extends LinearLayout
{
	private Context mContext = null;
	private Drawable mBackground = null;
    private StateListDrawable statelistDrawable = null;
    
	public LinearLayoutBackground(Context context)
	{
		super(context);
		mContext = context;
	}
	public LinearLayoutBackground(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		mContext = context;
	}
	
	/**
     * 设置点击效果,图片ID为负值时表示不设置该值
     * 
     * @param normalImgId
     * 					正常情况下的ID
     * @param pressedImgId
     * 					点击时的ID
     * @param foucesdImgId
     * 					获取焦点时的ID
     * @param selectedImgId
     * 					选中时的ID
     * @param enabledImgId
     * 					不可用时的ID
     */
	@SuppressWarnings("deprecation")
	public void setBackgroundImg(int normalImgId,
			int pressedImgId,
			int foucesdImgId,
			int selectedImgId,
			int enabledImgId)
	{
		if(null == statelistDrawable)
		{
			statelistDrawable = new StateListDrawable();
			this.setBackgroundDrawable(null);
			 mBackground = statelistDrawable;
		     //必须设置回调，当改变状态时，会回掉该View进行invalidate()刷新操作.
		     mBackground.setCallback(this);  
		}
		
		Drawable normal = normalImgId > 0 ? mContext.getResources().getDrawable(normalImgId) : null;
	    Drawable pressed = pressedImgId > 0 ? mContext.getResources().getDrawable(pressedImgId) : null;
	    Drawable focus = foucesdImgId > 0 ? mContext.getResources().getDrawable(foucesdImgId) : null;
	    Drawable enabled = enabledImgId > 0 ? mContext.getResources().getDrawable(enabledImgId) : null;
	    Drawable selected = selectedImgId > 0 ? mContext.getResources().getDrawable(selectedImgId) : null;

	    //注意该处的顺序，只要有一个状态与之相配，背景就会被换掉
	    //所以不要把大范围放在前面了，如果sd.addState(new[]{},normal)放在第一个的话，就没有什么效果了
	    if(null != focus)
	    {
	    	statelistDrawable.addState(new int[]{android.R.attr.state_enabled, android.R.attr.state_focused}, focus);
	    }
	    if(null != pressed)
	    {
		    statelistDrawable.addState(new int[]{android.R.attr.state_pressed, android.R.attr.state_enabled}, pressed);
	    }
	    if(null != focus)
	    {
	    	statelistDrawable.addState(new int[]{android.R.attr.state_focused}, focus);
	    }
	    if(null != pressed)
	    {
	    	statelistDrawable.addState(new int[]{android.R.attr.state_pressed}, pressed);
	    }
	    if(null != selected)
	    {
	    	statelistDrawable.addState(new int[]{android.R.attr.state_selected}, selected);
	    }
	    if(null != enabled)
	    {
	    	statelistDrawable.addState(new int[]{-android.R.attr.state_enabled}, enabled);
	    }
	    if(null != normal)
	    {
	    	statelistDrawable.addState(new int[]{}, normal);
	    }
	}
	
	/**
	 * 设置按钮背景颜色，传入值为-10时表示不设置值
	 * 
	 * @param normalColor
	 * 					正常的颜色
	 * @param pressedColor
	 * 					点击的颜色
	 * @param foucesdColor
	 * 					获取焦点时的颜色
	 * @param enabledColor
	 * 					不可选时的颜色
	 * @param roundRadius
	 * 					圆角的半径
	 * @param stokeWidth
	 * 					边框的宽度
	 * @param stokeColor
	 * 					边框的颜色
	 */
	@SuppressWarnings("deprecation")
	public void setBackgroundColor(int normalColor,
			int pressedColor,
			int foucesdColor,
			int enabledColor,
			int selectedColor,
			float roundRadius,
			int stokeWidth,
			int stokeColor)
	{
		if(null == statelistDrawable)
		{
			statelistDrawable = new StateListDrawable();
			this.setBackgroundDrawable(null);
			 mBackground = statelistDrawable;
		     //必须设置回调，当改变状态时，会回掉该View进行invalidate()刷新操作.
		     mBackground.setCallback(this);  
		}
		
		GradientDrawable gdNormal = null;
		if(normalColor != -10)
		{
			gdNormal = new GradientDrawable();
			gdNormal.setColor(normalColor);
			gdNormal.setCornerRadius(roundRadius);
			gdNormal.setStroke(stokeWidth, stokeColor);
		}
	    
		GradientDrawable gdPressed = null;
		if(pressedColor != -10)
		{
			gdPressed = new GradientDrawable();
		    gdPressed.setColor(pressedColor);
		    gdPressed.setCornerRadius(roundRadius);
		    gdPressed.setStroke(stokeWidth, stokeColor);
		}
	    
		GradientDrawable gdFocus = null;
		if(foucesdColor != -10)
		{
			gdFocus = new GradientDrawable();
		    gdFocus.setColor(foucesdColor);
		    gdFocus.setCornerRadius(roundRadius);
		    gdFocus.setStroke(stokeWidth, stokeColor);
		}
	    
		GradientDrawable gdEnabled = null;
		if(enabledColor != -10)
		{
			gdEnabled = new GradientDrawable();
		    gdEnabled.setColor(enabledColor);
		    gdEnabled.setCornerRadius(roundRadius);
		    gdEnabled.setStroke(stokeWidth, stokeColor);
		}
	    
		GradientDrawable gdSelected = null;
		if(selectedColor != -10)
		{
			gdSelected = new GradientDrawable();
		    gdSelected.setColor(selectedColor);
		    gdSelected.setCornerRadius(roundRadius);
		    gdSelected.setStroke(stokeWidth, stokeColor);
		}
	    
		Drawable normal = gdNormal;
	    Drawable pressed = gdPressed;
	    Drawable focus = gdFocus;
	    Drawable enabled = gdEnabled;
	    Drawable selected = gdSelected;

	    //注意该处的顺序，只要有一个状态与之相配，背景就会被换掉
	    //所以不要把大范围放在前面了，如果sd.addState(new[]{},normal)放在第一个的话，就没有什么效果了
	    if(null != focus)
	    {
	    	statelistDrawable.addState(new int[]{android.R.attr.state_enabled, android.R.attr.state_focused}, focus);
	    }
	    if(null != pressed)
	    {
		    statelistDrawable.addState(new int[]{android.R.attr.state_pressed, android.R.attr.state_enabled}, pressed);
	    }
	    if(null != focus)
	    {
	    	statelistDrawable.addState(new int[]{android.R.attr.state_focused}, focus);
	    }
	    if(null != pressed)
	    {
	    	statelistDrawable.addState(new int[]{android.R.attr.state_pressed}, pressed);
	    }
	    if(null != selected)
	    {
	    	statelistDrawable.addState(new int[]{android.R.attr.state_selected}, selected);
	    }
	    if(null != enabled)
	    {
	    	statelistDrawable.addState(new int[]{-android.R.attr.state_enabled}, enabled);
	    }
	    if(null != normal)
	    {
	    	statelistDrawable.addState(new int[]{}, normal);
	    }
	}
	
	/**
	 * 设置按钮背景颜色，传入值为-10时表示不设置值
	 * 
	 * @param normalColor
	 * 					正常的颜色
	 * @param pressedColor
	 * 					点击的颜色
	 * @param foucesdColor
	 * 					获取焦点时的颜色
	 * @param enabledColor
	 * 					不可选时的颜色
	 * @param roundRadius
	 * 					四个角的圆角的半径
	 * @param stokeWidth
	 * 					边框的宽度
	 * @param stokeColor
	 * 					边框的颜色
	 */
	@SuppressWarnings("deprecation")
	public void setBackgroundColor2(int normalColor,
			int pressedColor,
			int foucesdColor,
			int enabledColor,
			int selectedColor,
			float[] roundRadius,
			int stokeWidth,
			int stokeColor)
	{
		if(null == statelistDrawable)
		{
			statelistDrawable = new StateListDrawable();
			this.setBackgroundDrawable(null);
			 mBackground = statelistDrawable;
		     //必须设置回调，当改变状态时，会回掉该View进行invalidate()刷新操作.
		     mBackground.setCallback(this);  
		}
		
		GradientDrawable gdNormal = null;
		if(normalColor != -10)
		{
			gdNormal = new GradientDrawable();
			gdNormal.setColor(normalColor);
			gdNormal.setCornerRadii(roundRadius);
			gdNormal.setStroke(stokeWidth, stokeColor);
		}
	    
		GradientDrawable gdPressed = null;
		if(pressedColor != -10)
		{
			gdPressed = new GradientDrawable();
		    gdPressed.setColor(pressedColor);
		    gdPressed.setCornerRadii(roundRadius);
		    gdPressed.setStroke(stokeWidth, stokeColor);
		}
	    
		GradientDrawable gdFocus = null;
		if(foucesdColor != -10)
		{
			gdFocus = new GradientDrawable();
		    gdFocus.setColor(foucesdColor);
		    gdFocus.setCornerRadii(roundRadius);
		    gdFocus.setStroke(stokeWidth, stokeColor);
		}
	    
		GradientDrawable gdEnabled = null;
		if(enabledColor != -10)
		{
			gdEnabled = new GradientDrawable();
		    gdEnabled.setColor(enabledColor);
		    gdEnabled.setCornerRadii(roundRadius);
		    gdEnabled.setStroke(stokeWidth, stokeColor);
		}
	    
		GradientDrawable gdSelected = null;
		if(selectedColor != -10)
		{
			gdSelected = new GradientDrawable();
		    gdSelected.setColor(selectedColor);
		    gdSelected.setCornerRadii(roundRadius);
		    gdSelected.setStroke(stokeWidth, stokeColor);
		}
	    
		Drawable normal = gdNormal;
	    Drawable pressed = gdPressed;
	    Drawable focus = gdFocus;
	    Drawable enabled = gdEnabled;
	    Drawable selected = gdSelected;

	    //注意该处的顺序，只要有一个状态与之相配，背景就会被换掉
	    //所以不要把大范围放在前面了，如果sd.addState(new[]{},normal)放在第一个的话，就没有什么效果了
	    if(null != focus)
	    {
	    	statelistDrawable.addState(new int[]{android.R.attr.state_enabled, android.R.attr.state_focused}, focus);
	    }
	    if(null != pressed)
	    {
		    statelistDrawable.addState(new int[]{android.R.attr.state_pressed, android.R.attr.state_enabled}, pressed);
	    }
	    if(null != focus)
	    {
	    	statelistDrawable.addState(new int[]{android.R.attr.state_focused}, focus);
	    }
	    if(null != pressed)
	    {
	    	statelistDrawable.addState(new int[]{android.R.attr.state_pressed}, pressed);
	    }
	    if(null != selected)
	    {
	    	statelistDrawable.addState(new int[]{android.R.attr.state_selected}, selected);
	    }
	    if(null != enabled)
	    {
	    	statelistDrawable.addState(new int[]{-android.R.attr.state_enabled}, enabled);
	    }
	    if(null != normal)
	    {
	    	statelistDrawable.addState(new int[]{}, normal);
	    }
	}
	
    
    @SuppressWarnings("deprecation")
	@Override
    protected void drawableStateChanged()
    {
        Drawable d = mBackground;
        if (d != null && d.isStateful())
        {
        	//获取新状态下的Drawable
            d.setState(getDrawableState());
        }
        super.drawableStateChanged();
        this.setBackgroundDrawable(d);
        //this.setBackground(d);
    }
    /**
     * 验证图片是否相等 ,在invalidateDrawable()会调用此方法，我们需要重写该方法。
     */
    @Override
    protected boolean verifyDrawable(Drawable who)
    {
        return who == mBackground || super.verifyDrawable(who);
    }
    
    /**
     * 背景的状态变化会调用此方法
     */
    @Override
    public void refreshDrawableState() 
    {
    	super.refreshDrawableState();
    	drawableStateChanged();
    }
}
