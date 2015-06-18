package com.j.views;

/**
 * Created by Administrator on 2015/6/18.
 */

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * 快速查询View
 *
 * @author Jiang
 */
public class MyQuickSearchView extends View {

    /**
     * 构造函数
     *
     * @param context
     */
    public MyQuickSearchView(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
    }

    public MyQuickSearchView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
    }

    public MyQuickSearchView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        // TODO Auto-generated constructor stub
    }

    //事件监听器
    private OnTouchingLetterChangedListener onTouchingLetterChangedListener = null;
    //显示的字母
    private String[] mAllChar = {"#", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K",
            "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X",
            "Y", "Z"};

    private int mChoose = -1;
    private Paint mPaint = new Paint();
    private boolean isShowBackground = false;

    /*
     * (non-Javadoc)
     * @see android.view.View#onDraw(android.graphics.Canvas)
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (null == mPaint) {
            return;
        }
        float height = getHeight();
        float width = getWidth();

        if (isShowBackground) {
            // 设置半透明,圆角背景
            RectF rect = new RectF(0, 0, width, height);
            mPaint.setColor(Color.parseColor("#40000000"));
            canvas.drawRoundRect(rect, width / 2, width / 2, mPaint);
        }

        float singleHeight = height / mAllChar.length;

        for (int i = 0; i < mAllChar.length; i++) {
            // 设置字体的颜色
            mPaint.setColor(getResources().getColor(android.R.color.black));
            // 设置字体
            mPaint.setTypeface(Typeface.DEFAULT);
            // 设置字体大小，自动适应屏幕大小
            mPaint.setTextSize(singleHeight);
            // 效果设置
            mPaint.setAntiAlias(true);
            if (i == mChoose) {
                mPaint.setColor(Color.RED);
                mPaint.setFakeBoldText(true);
            }
            // 计算宽度，把字母画在水平居中的位置
            float xPos = width / 2 - mPaint.measureText(mAllChar[i]) / 2;
            // 计算高度
            float yPos = singleHeight * i + singleHeight;
            // 把字母画出来
            canvas.drawText(mAllChar[i], xPos, yPos, mPaint);
            mPaint.reset();
        }

    }

    /*
     * (non-Javadoc)
     * @see android.view.View#dispatchTouchEvent(android.view.MotionEvent)
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        final int action = event.getAction();
        final float y = event.getY();
        final int oldChoose = mChoose;
        final OnTouchingLetterChangedListener listener = onTouchingLetterChangedListener;
        // 计算出当前点击的位置
        final int position = (int) (y / getHeight() * mAllChar.length);

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                isShowBackground = true;
                if (null != listener && oldChoose != position) {
                    if (position > 0 && position < mAllChar.length) {
                        listener.onTouchingLetterChanged(mAllChar[position]);
                        mChoose = position;
                        invalidate();
                    }
                }

                break;
            case MotionEvent.ACTION_MOVE:
                if (oldChoose != position && listener != null) {
                    if (position > 0 && position < mAllChar.length) {
                        listener.onTouchingLetterChanged(mAllChar[position]);
                        mChoose = position;
                        invalidate();
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                isShowBackground = false;
                mChoose = -1;
                invalidate();
                break;
        }
        return true;
    }

    /*
     * (non-Javadoc)
     * @see android.view.View#onTouchEvent(android.view.MotionEvent)
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }

    /**
     * @param onTouchingLetterChangedListener
     */
    public void setOnTouchingLetterChangedListener(OnTouchingLetterChangedListener onTouchingLetterChangedListener) {

        this.onTouchingLetterChangedListener = onTouchingLetterChangedListener;
    }

    /**
     * listener interface
     *
     * @author Jiang
     */
    public interface OnTouchingLetterChangedListener {
        public void onTouchingLetterChanged(String s);
    }

}
