package com.j.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.Intent.ShortcutIconResource;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;
import android.util.DisplayMetrics;

public class CommonUtils {
    private static final int LUNCHER_FLAG = 0x10200000;
    private static final String TAG = CommonUtils.class.getSimpleName();

    /**
     * <p>
     * <uses-permission
     * android:name="com.android.launcher.permission.INSTALL_SHORTCUT"/>
     * </p>
     *
     * @param context
     * @param appName
     * @param icon
     * @param mainActPkg 上午4:41:10 Jiangxuewu
     */
    public static void addShortcut(Context context, String appName, int icon,
                                   String mainActPkg) {
        Intent shortcutIntent = new Intent(
                "com.android.launcher.action.INSTALL_SHORTCUT");

        shortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, appName);
        shortcutIntent.putExtra("duplicate", false);

        ComponentName comp = new ComponentName(context.getPackageName(),
                mainActPkg);
        Intent mainIntent = new Intent(Intent.ACTION_MAIN);
        mainIntent.setComponent(comp);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        mainIntent.setFlags(LUNCHER_FLAG);

        shortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, mainIntent);

        ShortcutIconResource iconRes = Intent.ShortcutIconResource.fromContext(
                context, icon);
        shortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, iconRes);

        context.sendBroadcast(shortcutIntent);
    }

    /**
     * @param smallVersin e.g x.xx.xx.xxxx
     * @param bigVersion  e.g x.xx.xx.xxx
     * @return true smallVersion is small.
     */
    public static boolean compareVersioin(String smallVersin, String bigVersion) {
        try {
            String[] small = smallVersin.split("\\.");
            String[] big = bigVersion.split("\\.");
            int count = small.length;

            for (int i = 0; i < count; i++) {
                if (Long.valueOf(small[i]) == Long.valueOf(big[i])) {
                    continue;
                } else {
                    return Long.valueOf(small[i]) < Long.valueOf(big[i]);
                }
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * <p>
     * <uses-permission
     * android:name="com.android.launcher.permission.UNINSTALL_SHORTCUT" />
     * </p>
     *
     * @param context
     * @param appName
     * @param mainActPkg 上午4:40:34 Jiangxuewu
     */
    public static void deleteShortcut(Context context, String appName,
                                      String mainActPkg) {
        Intent shortcutIntent = new Intent(
                "com.android.launcher.action.UNINSTALL_SHORTCUT");
        shortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, appName);
        ComponentName comp = new ComponentName(context.getPackageName(),
                mainActPkg);
        Intent mainIntent = new Intent(Intent.ACTION_MAIN);
        mainIntent.setComponent(comp);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        mainIntent.setFlags(LUNCHER_FLAG);

        shortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, mainIntent);

        context.sendBroadcast(shortcutIntent);
    }

    /**
     * @param context
     * @param dp
     * @return
     */
    public static int dp2px(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    /**
     * @param context
     * @param px
     * @return
     */
    public static int px2dp(Context context, float px) {
        final float scale = context.getResources().getDisplayMetrics().density;

        return (int) (px / scale + 0.5f);
    }

    /**
     * 获取两个日期之间的天数
     *
     * @param time1 yyyy-MM-dd
     * @param time2 yyyy-MM-dd
     * @return
     */
    public static int getQuot(String time1, String time2) {
        int quot = 0;
        SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date1 = ft.parse(time1);
            Date date2 = ft.parse(time2);
            long tmp = date1.getTime() - date2.getTime();
            quot = (int) (tmp / 1000 / 60 / 60 / 24);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return quot;
    }

    /**
     * 检测网络是否OK
     *
     * @param context
     * @return
     */
    public static boolean isNetOk(Context context) {
        NetworkInfo networkInfo = getActiveNetworkInfo(context);

        if (null == networkInfo) {
            return false;
        }

        return networkInfo.isAvailable()
                && !TextUtils.isEmpty(networkInfo.getTypeName())
                && !"UNKNOW".equalsIgnoreCase(networkInfo.getTypeName());
    }

    public static NetworkInfo getActiveNetworkInfo(Context context) {
        if (null == context) {
            return null;
        }
        ConnectivityManager connect = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connect != null) {
            NetworkInfo ActiveNetworkInfo = connect.getActiveNetworkInfo();
            return ActiveNetworkInfo;
        } else {
            return null;
        }
    }

    /**
     * 获取屏幕宽高
     *
     * @param context
     * @return
     */
    public static int[] getWidthHeight(Activity context) {
        DisplayMetrics dm = new DisplayMetrics();
        //获取屏幕信息
        context.getWindowManager().getDefaultDisplay().getMetrics(dm);

        int screenWidth = dm.widthPixels;

        int screenHeigh = dm.heightPixels;
        int[] res = {screenWidth, screenHeigh};
        return res;
    }
}
