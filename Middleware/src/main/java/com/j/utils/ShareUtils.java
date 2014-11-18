package com.j.utils;

import java.io.File;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

public class ShareUtils {
	/**
	 * 微信分享好友
	 * 
	 * @param file
	 *            分享的文件
	 * @param context
	 *            环境变量
	 * @param infoMsg
	 *            描述文字
	 */
	public static void shareToFriend(File file, Context context, String infoMsg) {
		Intent intent = new Intent();
		ComponentName comp = new ComponentName("com.tencent.mm",
				"com.tencent.mm.ui.tools.ShareImgUI");
		intent.setComponent(comp);
		intent.setAction("android.intent.action.SEND");
		intent.setType("image/*");
		intent.putExtra(Intent.EXTRA_TEXT, infoMsg);
		intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
		context.startActivity(intent);
	}

	/**
	 * 微信分享朋友圈
	 * 
	 * @param file
	 *            分享的文件
	 * @param context
	 *            环境变量
	 */
	public static void shareToTimeLine(File file, Context context) {
		Intent intent = new Intent();
		ComponentName comp = new ComponentName("com.tencent.mm",
				"com.tencent.mm.ui.tools.ShareToTimeLineUI");
		intent.setComponent(comp);
		intent.setAction("android.intent.action.SEND");
		intent.setType("image/*");
		intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
		context.startActivity(intent);
	}

	/**
	 * 调用系统分享图片
	 * 
	 * @param context
	 * @param path
	 * @param infoMsg
	 *            描述文字
	 */
	public static void shareSysImg(Context context, String path,
			CharSequence infoMsg) {
		Uri uri = Uri.parse("file:///" + path);
		Intent it = new Intent(Intent.ACTION_SEND);
		it.putExtra(Intent.EXTRA_STREAM, uri);
		it.setType("image/*");
		context.startActivity(Intent.createChooser(it, infoMsg));
	}
}
