package com.j.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;

import com.j.R;

/**
 * Created by SkyJiang on 2015/6/23.
 *
 * @attr ref R.styleable#MyCheckBox_back_drawable
 * @attr ref R.styleable#MyCheckBox_selected_drawable
 * @attr ref R.styleable#MyCheckBox_selected
 */
public class MyCheckBox extends Button implements View.OnClickListener {

    private boolean isSelected = false;

    private final Drawable backDrawable;
    private final Drawable selectedDrawable;

    private OnCheckedChangeListener mCheckedChangeListener;

    public MyCheckBox(Context context) {
        this(context, null);
    }

    public MyCheckBox(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyCheckBox(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        setBackgroundColor(Color.TRANSPARENT);

        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.MyCheckBox, defStyle, 0);

        backDrawable = a.getDrawable(R.styleable.MyCheckBox_back_drawable);
        selectedDrawable = a.getDrawable(R.styleable.MyCheckBox_selected_drawable);
        isSelected = a.getBoolean(R.styleable.MyCheckBox_selected, isSelected);

        if (null != backDrawable && !isSelected) {
            setBackgroundDrawable(backDrawable);
        } else if (null != selectedDrawable && isSelected) {
            setBackgroundDrawable(selectedDrawable);
        }

        setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        isSelected = !isSelected;
        if (null != mCheckedChangeListener) {
            mCheckedChangeListener.onCheckedChanged(this, isSelected);
        }
        update();
    }

    /**
     * selected
     *
     * @return true was selected, other not
     */
    public boolean isSelected() {
        return isSelected;
    }

    public void addCheckedListener(OnCheckedChangeListener listener) {
        mCheckedChangeListener = listener;
    }

    private void update() {
        if (null != backDrawable && !isSelected) {
            setBackgroundDrawable(backDrawable);
        } else if (null != selectedDrawable && isSelected) {
            setBackgroundDrawable(selectedDrawable);
        }
    }

    /**
     * Interface definition for a callback to be invoked when the checked state
     * of a compound button changed.
     */
    public static interface OnCheckedChangeListener {
        /**
         * Called when the checked state of a compound button has changed.
         *
         * @param buttonView The compound button view whose state has changed.
         * @param isChecked  The new checked state of buttonView.
         */
        void onCheckedChanged(MyCheckBox buttonView, boolean isChecked);
    }
}
