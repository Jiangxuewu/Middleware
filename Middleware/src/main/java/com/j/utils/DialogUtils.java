package com.j.utils;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface.OnCancelListener;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;

import com.j.R;

public class DialogUtils {
    private Progress progress;
    private Context context;
    private Dialogs dialog;

    public DialogUtils(Context context) {
        progress = new Progress(context);
        dialog = new Dialogs(context, R.style.MyDialog);
        this.context = context;
    }

    class Progress extends ProgressDialog {

        public Progress(Context context) {
            super(context);
        }

        public Progress(Context context, int theme) {
            super(context, theme);
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {

            return true;
        }

    }

    class Dialogs extends AlertDialog {

        public Dialogs(Context context) {
            super(context);
        }

        public Dialogs(Context context, int theme) {
            super(context, theme);
        }


        @Override
        public boolean onTouchEvent(MotionEvent event) {
            return true;
        }

    }

    /**
     * 显示Progress,返回键可以取消Progress
     *
     * @param message
     * @param listener
     */
    public void showProgress(String message, OnCancelListener listener) {

        if (null == progress) {
            progress = new Progress(context);
        }

        progress.setMessage(message);

        progress.setOnCancelListener(listener);

        if (!progress.isShowing()) {
            progress.show();
        }
    }

    /**
     * 返回键，不可以取消Progress
     *
     * @param message
     */
    public void showProgress(String message) {

        if (null == progress) {
            progress = new Progress(context);
        }

        progress.setMessage(message);

        progress.setCancelable(false);

        if (!progress.isShowing()) {
            progress.show();
        }
    }

    /**
     * dismiss Progress
     *
     * @see DialogUtils#dismissProgress()
     * @deprecated
     */
    public void dissmissProgress() {
        progress.dismiss();
    }

    /**
     * dismiss Progress
     */
    public void dismissProgress() {
        progress.dismiss();
    }

    /**
     * @param view
     */
    private void showDialog(View view) {
        if (null == dialog) {
            dialog = new Dialogs(context, R.style.MyDialog);
        }

        WindowManager manager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);

        int width = manager.getDefaultDisplay().getWidth();
        int height = manager.getDefaultDisplay().getHeight();

        Window window = dialog.getWindow();


        android.view.WindowManager.LayoutParams lp = window.getAttributes();
        lp.x = 0;
        lp.height = CommonUtils.dp2px(context, 320);
        lp.y = height - lp.height;
        lp.width = width - CommonUtils.dp2px(context, 20);// 宽度

        lp.alpha = 1.0F; // 透明度

        window.setAttributes(lp);

        dialog.setView(view);

        dialog.show();

    }

    /**
     * @param view
     * @param viewHight     单位dp
     * @param alpha
     * @param leftRightBank
     */
    public void showDialog(View view, int viewHight, float alpha,
                           int leftRightBank) {
        if (null == dialog) {
            dialog = new Dialogs(context, R.style.MyDialog);
        }

        WindowManager manager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);

        int width = manager.getDefaultDisplay().getWidth();
        int height = manager.getDefaultDisplay().getHeight();

        Window window = dialog.getWindow();

        android.view.WindowManager.LayoutParams lp = window.getAttributes();
        lp.x = 0;

        LayoutParams params = view.getLayoutParams();
        if (null != params) {
            lp.height = view.getLayoutParams().height;
            lp.y = height - lp.height;
        } else {
            if (viewHight == 0) {
                lp.height = height;
            } else {
                lp.height = CommonUtils.dp2px(context, viewHight);
            }
            lp.y = height - lp.height;
        }
        lp.width = width - CommonUtils.dp2px(context, leftRightBank);// 宽度

        lp.alpha = alpha; // 透明度

        window.setAttributes(lp);

        dialog.show();

        dialog.setContentView(view);

    }

    /**
     * Dialog显示在中间
     *
     * @param view
     * @param listener
     */
    public void showDialog(View view, OnCancelListener listener) {

        if (null == dialog) {
            dialog = new Dialogs(context, R.style.MyDialog);
        }

        dialog.setView(view);

        if (null != listener)
            dialog.setOnCancelListener(listener);

        dialog.show();
    }

    /**
     * Dialog显示在中间
     *
     * @param layoutResID
     */
    public void showDialog(int layoutResID) {
        if (null == dialog) {
            dialog = new Dialogs(context, R.style.MyDialog);
        }

        dialog.show();

        dialog.setContentView(layoutResID);
    }

    /**
     * dismiss dialog
     */
    public void dismissDialog() {
        if (null != dialog && dialog.isShowing()) {
            dialog.dismiss();
        }
        dialog = null;
    }

}
