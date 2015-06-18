package com.j.swipebacklayout;

/**
 * Created by Administrator on 2015/6/17.
 */
public interface ISwipeBackLayout {
    /**
     * @return the SwipeBackLayout associated with this activity.
     */
    SwipeBackLayout getSwipeBackLayout();

    /**
     * set Drag Event enable
     *
     * @param enable true or false
     */
    void setSwipeBackEnable(boolean enable);

    /**
     * Scroll out contentView and finish the activity
     */
    void scrollToFinishActivity();
}
