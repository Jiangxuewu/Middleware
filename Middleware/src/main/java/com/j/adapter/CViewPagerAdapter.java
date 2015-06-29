package com.j.adapter;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by SkyJiang on 2015/6/25.
 */
public class CViewPagerAdapter extends PagerAdapter {
    private List<View> viewPagerItems;

    public void addView(View v) {
        if (null == viewPagerItems) {
            viewPagerItems = new ArrayList<View>();
        }
        viewPagerItems.add(v);
    }


    @Override
    public int getCount() {
        return null == viewPagerItems ? 0 : viewPagerItems.size();
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;
    }

    @Override
    public void destroyItem(View container, int position, Object object) {
        ((ViewPager) container).removeView(viewPagerItems.get(position));
    }

    /**
     * 载入图片进去，用当前的position 除以 图片数组长度取余数是关键
     */
    @Override
    public Object instantiateItem(View container, int position) {

        try {
            ((ViewPager) container).addView(viewPagerItems.get(position), 0);
        } catch (Exception ignored) {
        }
        return viewPagerItems.get(position);
    }

}
