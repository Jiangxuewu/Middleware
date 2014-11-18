package com.j.http;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;

import android.text.TextUtils;
import android.util.Xml;

import com.j.log.FLog;

public class JHttpXML {
	private static final String TAG = JHttpXML.class.getSimpleName();

	private static JHttpXML mInstance = null;

	/**
	 * 获取实例
	 */
	public static JHttpXML getInstance() {
		if (null == mInstance) {
			mInstance = new JHttpXML();
		}
		return mInstance;
	}

	/**
	 * 构造函数
	 */
	protected JHttpXML() {

	}

	/**
	 * 解析response
	 */
	public Xml parse(HttpResponse response) {
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

			// return new JSONObject(jsonStr);
//			return new Xml();
			return null;

		} catch (Exception e) {
			FLog.e(TAG, "parse()1,exception occur:" + e.getLocalizedMessage());
			e.printStackTrace();
		}

		return null;
	}
}
