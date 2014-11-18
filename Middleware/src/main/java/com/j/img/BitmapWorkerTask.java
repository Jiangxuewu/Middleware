package com.j.img;

import android.graphics.Bitmap;
import android.os.AsyncTask;

import com.j.utils.BitmapUtils;

/**
 * 1, 线程中加载图片
 * 
 * <p>
 * execute()该方法需要有三个参数分别是：filePath, reqWidth, reqHeight
 * <p>
 * 
 * @author Jxw
 * 
 */
public class BitmapWorkerTask extends AsyncTask<String, Void, Bitmap> {

	private OnDone listener = null;
	private String filePath;

	public void setOnCompleteListener(OnDone listener) {
		this.listener = listener;
	}

	public interface OnDone {
		public void onDone(String path, Bitmap bitmap);
	}

	public BitmapWorkerTask() {
	}

	@Override
	protected Bitmap doInBackground(String... params) {
		filePath = params[0];
		int reqWidth = 100;
		int reqHeight = 100;
		if (params.length == 3) {
			reqWidth = Integer.valueOf(params[1]);
			reqHeight = Integer.valueOf(params[2]);
		}
		return BitmapUtils.decodeSampledBitmapFormFileName(filePath, reqWidth,
				reqHeight);
	}

	@Override
	protected void onPostExecute(Bitmap bitmap) {
//		if (isCancelled()) {
//			bitmap = null;
//			return;
//		}
		if (null != listener) {
			listener.onDone(filePath, bitmap);
		}
	}

}
