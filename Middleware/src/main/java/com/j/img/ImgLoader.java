package com.j.img;

import android.R.integer;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.util.LruCache;
import android.view.View;
import android.widget.ImageView;

import com.j.R;

public class ImgLoader {

	private Context mContext;

	private LruCache<String, Bitmap> mMemoryCache;

	private Bitmap mPlaceHolderBitmap;

	private static ImgLoader mInstance = null;

	private ImgLoader(Context context) {
		mContext = context;
		mPlaceHolderBitmap = BitmapFactory.decodeResource(
				mContext.getResources(), R.drawable.default_img);
	}

	public static ImgLoader getInstance(Context context) {
		if (null == mInstance) {
			mInstance = new ImgLoader(context);
		}
		return mInstance;
	}

	/**
	 * 加载一张图片(图片可能太大, 自动压缩)
	 * 
	 * @param imageView
	 * @param path
	 * @param reqWidth
	 * @param reqHeight
	 */
	public void loadBitmap(ImageView imageView, String path, int reqWidth,
			int reqHeight) {

//		if (cancelPotentialWork(path, imageView)) {
//			final BitmapWorkerTask task = new BitmapWorkerTask(mContext,
//					imageView);
//			final AsyncDrawable asyncDrawable = new AsyncDrawable(
//					mContext.getResources(), mPlaceHolderBitmap, task);
//			imageView.setImageDrawable(asyncDrawable);
//			if (reqWidth != 0 && reqHeight != 0) {
//				task.execute(path, reqWidth + "", reqHeight + "");
//			} else {
//				task.equals(path);
//			}
//		}
	}
	
	public void loadDrawable(View view, String path, int reqWidth, integer reqHeight){
//			ImgCacheManager<Bitmap> img = new ImgCacheManager<Bitmap>(mContext);
			
	}
	

	private static boolean cancelPotentialWork(String filePath,
			ImageView imageView) {
		final BitmapWorkerTask bitmapWorkerTask = getBitmapWorkerTask(imageView);
//		if (bitmapWorkerTask != null) {
//			final String bitmapPath = bitmapWorkerTask.filePath;
//			// If bitmapData is not yet set or it differs from the new data
//			if (TextUtils.isEmpty(bitmapPath) || !bitmapPath.equals(filePath)) {
//				// Cancel previous task
//				bitmapWorkerTask.cancel(true);
//			} else {
//				// The same work is already in progress
//				return false;
//			}
//		}
		// No task associated with the ImageView, or an existing task was
		// cancelled
		return true;
	}

	private static BitmapWorkerTask getBitmapWorkerTask(ImageView imageView) {
		if (imageView != null) {
			final Drawable drawable = imageView.getDrawable();
			if (drawable instanceof AsyncDrawable) {
				final AsyncDrawable asyncDrawable = (AsyncDrawable) drawable;
				return asyncDrawable.getBitmapWorkerTask();
			}
		}
		return null;
	}

}
