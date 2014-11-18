package com.j.task;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.AsyncTask;

import com.j.R;
import com.j.utils.DialogUtils;

public abstract class Task<Params, Progress, Result> extends
		AsyncTask<Params, Progress, Result> {
	private Context context;
	private DialogUtils dialogUtils;

	private boolean isCancel = false;

	public Task(Context context) {
		this.context = context;
		dialogUtils = new DialogUtils(context);
	}

	private OnCancelListener cancelListener = new OnCancelListener() {

		@Override
		public void onCancel(DialogInterface dialog) {
			isCancel = true;
		}
	};

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		dialogUtils.showProgress(
				context.getResources().getString(R.string.pls_wait),
				cancelListener);
		isCancel = false;
	}

	@Override
	protected Result doInBackground(Params... params) {

		return handle(params);
	}

	@Override
	protected void onPostExecute(Result result) {
		super.onPostExecute(result);
		if (null != dialogUtils) {
			dialogUtils.dissmissProgress();
		}
		if (isCancel) {
			return;
		}

		handleResult(result);
	}

	public abstract void handleResult(Result result);

	public abstract Result handle(Params... params);

}
