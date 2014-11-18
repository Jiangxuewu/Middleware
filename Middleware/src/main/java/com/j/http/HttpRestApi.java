/**
 */
package com.j.http;

import java.util.Map.Entry;
import java.util.HashMap;
import java.util.TreeMap;

import android.text.TextUtils;

/**
 * <p>
 * 
 * @author Jiangxuewu
 *         <p>
 *         单例模式
 *         <p>
 *         HTTP Rest风格API
 *         <p>
 *         1,生成REST风格的URL
 *         <p>
 *         2,签名
 *         <p>
 */
public class HttpRestApi {
	private static HttpRestApi mInstance = null;

	/**
	 * 获取实例
	 */
	public static HttpRestApi getInstance() {
		if (null == mInstance) {
			mInstance = new HttpRestApi();
		}
		return mInstance;
	}

	protected HttpRestApi() {
	}

	/**
	 * 使用REST风格签名规则，生成REST风格URL
	 * 
	 * @param uri
	 *            RL前半截，后面跟上参数，组装成REST风格URI
	 * @param signParamName
	 *            在URL参数中用来指定签名的参数名，如"hash"
	 * @param secretKey
	 *            私钥，用于生成签名
	 * @param params
	 *            参数，用于生成签名
	 * @return 上午12:13:59 Jiangxuewu
	 * 
	 */
	public String getUrl(String uri, String signParamName, String secretKey,
			TreeMap<String, String> params) {
		if (TextUtils.isEmpty(uri) || null == params) {
			return null;
		}

		// 生成签名
		if (!TextUtils.isEmpty(signParamName)) {
			String sign = sign(secretKey, params);
			if (TextUtils.isEmpty(sign)) {
				return null;
			}

			params.put(signParamName, sign);
		}

		StringBuffer stringBuffer = new StringBuffer();

		stringBuffer.append(uri);
		if (!uri.endsWith("?")) {
			stringBuffer.append('?');
		}

		for (Entry<String, String> entry : params.entrySet()) {
			if (null != entry.getKey()) {
				stringBuffer.append(entry.getKey());
				stringBuffer.append('=');
			}

			if (null != entry.getValue()) {
				stringBuffer.append(entry.getValue());
			}

			stringBuffer.append('&');
		}

		// 移除最后一个'&'
		stringBuffer.deleteCharAt(stringBuffer.length() - 1);

		return stringBuffer.toString();
	}

	/**
	 * 参数中添加签名参数名
	 * 
	 * @param signParamName
	 *            在URL参数中用来指定签名的参数名，如"hash"
	 * @param secretKey
	 *            私钥，用于生成签名
	 * @param params
	 *            参数，用于生成签名
	 * @return 包含签名了的params 上午12:17:15 Jiangxuewu
	 * 
	 */
	public TreeMap<String, String> getParams(String signParamName,
			String secretKey, TreeMap<String, String> params) {
		if (null == signParamName || null == secretKey || null == params) {
			return null;
		}
		// 生成签名
		String sign = sign(secretKey, params);
		if (TextUtils.isEmpty(sign)) {
			return null;
		}

		params.put(signParamName, sign);

		return params;
	}

	/**
	 * 生成签名,算法: 1.私钥参数名1参数1参数名2参数2...参数名N参数N私钥, 组装成一个长的字符串
	 * 2.对组装出来的字符串进行MD5编码，生成32位的字符串
	 */

	/**
	 * 
	 * 参数签名
	 * 
	 * @param secretKey
	 *            私钥，用于生成签名
	 * @param params
	 *            参数，用于生成签名
	 * @return 生产的签名 上午12:19:52 Jiangxuewu
	 * 
	 */
	public static String sign(String secretKey, TreeMap<String, String> params) {
		if (null == params) {
			return null;
		}

		Md5Coder md5Coder = Md5Coder.getInstance();
		if (null == md5Coder) {
			return null;
		}

		StringBuffer stringBuffer = new StringBuffer();

		if (!TextUtils.isEmpty(secretKey)) {
			stringBuffer.append(secretKey);// TODO 需要与服务器同步
		}

		for (Entry<String, String> entry : params.entrySet()) {
			if (null != entry.getKey()) {
				stringBuffer.append(entry.getKey());
			}

			if (null != entry.getValue()) {
				stringBuffer.append(entry.getValue());
			}
		}

		if (!TextUtils.isEmpty(secretKey)) {
			stringBuffer.append(secretKey);// TODO 需要与服务器同步
		}

		return md5Coder.encode(stringBuffer.toString().getBytes())
				.toLowerCase();
	}
	
	/**
	 * 
	 * 参数签名
	 * 
	 * @param secretKey
	 *            私钥，用于生成签名
	 * @param params
	 *            参数，用于生成签名
	 * @return 生产的签名 上午12:19:52 Jiangxuewu
	 * 
	 */
	public static String sign(String secretKey, HashMap<String, String> params) {
		if (null == params) {
			return null;
		}

		Md5Coder md5Coder = Md5Coder.getInstance();
		if (null == md5Coder) {
			return null;
		}

		StringBuffer stringBuffer = new StringBuffer();

		if (!TextUtils.isEmpty(secretKey)) {
			stringBuffer.append(secretKey);// TODO 需要与服务器同步
		}

		for (Entry<String, String> entry : params.entrySet()) {
			if (null != entry.getKey()) {
				stringBuffer.append(entry.getKey());
			}

			if (null != entry.getValue()) {
				stringBuffer.append(entry.getValue());
			}
		}

		if (!TextUtils.isEmpty(secretKey)) {
			stringBuffer.append(secretKey);// TODO 需要与服务器同步
		}

		return md5Coder.encode(stringBuffer.toString().getBytes())
				.toLowerCase();
	}
	
}
