package com.j.http;

import org.apache.http.HttpResponse;
import org.json.JSONObject;

public class JHttpParse {

//	private static final String TAG = JHttpParse.class.getSimpleName();

	private static JHttpParse mInstance = null;

	/**
	 * 获取实例
	 */
	public static JHttpParse getInstance() {
		if (null == mInstance) {
			mInstance = new JHttpParse();
		}
		return mInstance;
	}

	public JSONObject parseJson(HttpResponse response) {
		return JHttpJson.getInstance().parse(response);
	}

	public JSONObject parseGzip(HttpResponse response) {
		return JHttpJson.getInstance().parseGzip(response);
	}

	public String parseStr(HttpResponse response) {
		return JHttpJson.getInstance().parseStr(response);
	}

	// public HashMap<String, Object> parseXml(HttpResponse response,
	// List<String> list) {
	// return XmlUtils.parse(response, list);
	// }

}
