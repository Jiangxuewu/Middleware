package com.j.img;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.LruCache;
import android.widget.ImageView;
import android.widget.TextView;

import com.j.R;
import com.j.img.BitmapWorkerTask.OnDone;
import com.j.utils.BitmapUtils;
import com.j.utils.CommonUtils;

import java.io.File;

public class ImgCacheManager implements OnDone {
    private static ImgCacheManager mInstance = null;
    private static LruCache<String, Bitmap> mMemoryCache;
    private Context context;

    // Get max available VM memory, exceeding this amount will throw an
    // OutOfMemory exception. Stored in kilobytes as LruCache takes an
    // int in its constructor.
    final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
    // Use 1/8th of the available memory for this memory cache.
    final int cacheSize = maxMemory / 8;

    public static ImgCacheManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new ImgCacheManager(context);
        }
        return mInstance;
    }

    private ImgCacheManager(Context context) {
        this.context = context;
        mMemoryCache = new LruCache<String, Bitmap>(cacheSize);
    }

    public Bitmap getBitmap(String path) {
        return mMemoryCache.get(path);
    }

    public void setImg(String path, ImageView iView, int minLength, int angle) {
        Bitmap bm = mMemoryCache.get(path);
        if (null == bm) {
            bm = BitmapFactory.decodeResource(context.getResources(),
                    R.drawable.default_img);
            loadBitmap(path);
        }

        BitmapDrawable drawable = new BitmapDrawable(bm);

        int right = drawable.getMinimumWidth();
        int bottom = drawable.getMinimumHeight();
        int dev = 2;
        if (Math.min(right, bottom) != 0) {
            dev = minLength / Math.min(right, bottom);
            if (minLength % Math.min(right, bottom) > 0) {
                dev++;
            }
        }
        while (right < minLength || bottom < minLength) {
            right *= dev;
            bottom *= dev;
        }

        Bitmap bmTmp = BitmapUtils.decodeSampledBitmapFormFileName(path, right,
                bottom);
        if (null == bmTmp) {
            return;
        }

        Matrix matrix = new Matrix();
        int width = bmTmp.getWidth();
        int height = bmTmp.getHeight();
        matrix.setRotate(angle);
        bmTmp = Bitmap.createBitmap(bmTmp, 0, 0, Math.min(right, width),
                Math.min(bottom, height), matrix, true);

        iView.setImageBitmap(bmTmp);
    }

    public void setImg(String path, TextView tView, int type, float minLenth,
                       int angle) {
        Bitmap bm = mMemoryCache.get(path);
        Drawable drawable = null;
        if (null == bm) {
            drawable = context.getResources().getDrawable(
                    R.drawable.default_img);
            loadBitmap(path);
        } else {
            Matrix matrix = new Matrix();
            int width = bm.getWidth();
            int height = bm.getHeight();
            matrix.setRotate(angle);
            Bitmap bmTmp = Bitmap.createBitmap(bm, 0, 0, width, height, matrix,
                    true);

            drawable = new BitmapDrawable(bmTmp);
        }

        int right = drawable.getMinimumWidth();
        int bottom = drawable.getMinimumHeight();
        int total = CommonUtils.dp2px(context, minLenth);
        int dev = 2;
        if (Math.min(right, bottom) != 0) {
            dev = total / Math.min(right, bottom);
            if (total % Math.min(right, bottom) > 0) {
                dev++;
            }
        }
        while (right < total || bottom < total) {
            right *= dev;
            bottom *= dev;
        }

        drawable.setBounds(0, 0, right, bottom);
        Drawable[] drawables = {null, null, null, null};
        drawables[type] = drawable;
        tView.setCompoundDrawables(drawables[0], drawables[1], drawables[2],
                drawables[3]);

    }

    private void loadBitmap(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            return;
        }
        BitmapWorkerTask task = new BitmapWorkerTask();
        task.setOnCompleteListener(this);
        task.execute(filePath);
    }

    public Bitmap getBitmapFromMemCache(String key) {
        return mMemoryCache.get(key);
    }

    public void addBitmapToMemoryCache(String key, Bitmap bitmap) {
        if (null == bitmap) {
            return;
        }
        if (getBitmapFromMemCache(key) == null) {
            mMemoryCache.put(key, bitmap);
        }
    }

    @Override
    public void onDone(String path, Bitmap bitmap) {
        addBitmapToMemoryCache(path, bitmap);
        if (null != listener) {
            listener.refresh();
        }
    }

    private OnUpdateListener listener;

    public interface OnUpdateListener {
        public void refresh();
    }

    public void setUpdateListener(OnUpdateListener listener) {
        this.listener = listener;
    }
}
