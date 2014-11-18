/**
 * 
 */
package com.j.http;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.zip.GZIPInputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.json.JSONObject;

import android.text.TextUtils;

import com.j.log.FLog;

/**
 * @author Jiangxuewu
 * 
 */
public class JHttpJson {

	private static final String TAG = JHttpJson.class.getSimpleName();

	private static JHttpJson mInstance = null;

	/**
	 * 获取实例
	 */
	public static JHttpJson getInstance() {
		if (null == mInstance) {
			mInstance = new JHttpJson();
		}
		return mInstance;
	}

	/**
	 * 构造函数
	 */
	protected JHttpJson() {

	}

	/**
	 * 解析response
	 */
	public JSONObject parse(HttpResponse response) {
		try {
			String jsonStr = parseStr(response);
			if (TextUtils.isEmpty(jsonStr)) {
				return null;
			}
//			if (jsonStr.contains("\\\\")) {
				jsonStr = jsonStr.replaceAll("\\\\", "");
				jsonStr = jsonStr.substring(jsonStr.indexOf("{"), jsonStr.lastIndexOf("}") + 1);
//			}

			return new JSONObject(jsonStr);

		} catch (Exception e) {
			FLog.e(TAG, "parse(),exception occur:" + e.getLocalizedMessage());
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * 解析response
	 * 
	 * @param response
	 * @return 下午12:25:38 Jiangxuewu
	 * 
	 */
	public JSONObject parseGzip(HttpResponse response) {
		if (null == response) {
			return null;
		}

		int statusCode = response.getStatusLine().getStatusCode();
		// 2xx开头的都表示成功
		if (statusCode < 200 || statusCode >= 300) {
			return null;
		}

		HttpEntity entity = response.getEntity();
		if (null == entity) {
			return null;
		}

		InputStream inputStream;
		try {
			inputStream = entity.getContent();
			if (null == inputStream) {
				return null;
			}

			inputStream = new GZIPInputStream(inputStream);

			InputStreamReader inputStreamReader = new InputStreamReader(
					inputStream);

			BufferedReader bufferedReader = new BufferedReader(
					inputStreamReader);

			StringBuilder stringBuilder = new StringBuilder();

			String string = null;
			while ((string = bufferedReader.readLine()) != null) {
				stringBuilder.append(string);
			}

			inputStream.close();

			String jsonStr = stringBuilder.toString();
			if (TextUtils.isEmpty(jsonStr)) {
				return null;
			}
			FLog.i("wxj", jsonStr);
			return new JSONObject(jsonStr);

		} catch (Exception e) {
			FLog.e(TAG,
					"parseGzip(),exception occur:" + e.getLocalizedMessage());
			e.printStackTrace();
		}

		return null;
	}

	public String parseStr(HttpResponse response) {
		if (null == response) {
			return null;
		}

		int statusCode = response.getStatusLine().getStatusCode();
		// 2xx开头的都表示成功
		if (statusCode < 200 || statusCode >= 300) {
			return null;
		}

		HttpEntity entity = response.getEntity();
		if (null == entity) {
			return null;
		}

		InputStream inputStream;
		try {
			inputStream = entity.getContent();
			if (null == inputStream) {
				return null;
			}

			InputStreamReader inputStreamReader = new InputStreamReader(
					inputStream);

			BufferedReader bufferedReader = new BufferedReader(
					inputStreamReader);

			StringBuilder stringBuilder = new StringBuilder();

			String string = null;
			while ((string = bufferedReader.readLine()) != null) {
				stringBuilder.append(string);
			}

			inputStream.close();

			String jsonStr = stringBuilder.toString();
			if (TextUtils.isEmpty(jsonStr)) {
				return null;
			}
			FLog.i(TAG, jsonStr);
			return jsonStr;
		} catch (Exception e) {
		}
		return null;
	}
}
