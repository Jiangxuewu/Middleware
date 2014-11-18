package com.j.http;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.message.BasicNameValuePair;

import android.content.Context;
import android.text.TextUtils;

/**
 * 1,提供post、get请求
 * <p>
 * 
 * @author Jxw
 * 
 */
public abstract class BaseHttpApi {
	/** 请求成功 */
	public static final int ZOK = 0;
	/** 请求失败 */
	public static final int ZFAILD = -1;

	protected static final String PARAMS_CHAR_CODE = "UTF-8";

	protected JHttpClient mHttpClient = null;
	protected Context mContext = null;

	/**
	 * 构造函数
	 */
	protected BaseHttpApi(Context context) {
		mContext = context;
		mHttpClient = JHttpClient.getInstance();
	}

	/** 解析http返回结果 */
	protected abstract <T> T parse(HttpResponse response);

	/** 获取应用参数 */
	protected abstract TreeMap<String, String> getParams();

	/** 获取签名字段 */
	protected abstract String getSignName();

	/** 获取签名迷药 */
	protected abstract String getSecretKey();

	/** 获取请求地址 */
	protected abstract String getBaseHttp();

	/**
	 * 发起GET请求
	 * 
	 * @param url
	 *            服务器地址
	 * @param headParams
	 *            业务参数
	 * @return 返回JSON
	 *         <p>
	 * 
	 */
	protected <T> T get(String url, HashMap<String, String> headParams) {
		if (null == mHttpClient || null == url) {
			return null;
		}

		HttpResponse response = mHttpClient.get(url, headParams);
		if (null == response) {
			return null;
		}

		return parse(response);
	}

	/**
	 * 发起POST请求
	 * 
	 * @param url
	 *            服务器地址
	 * @param entity
	 *            业务参数
	 * @return 返回JSON
	 *         <p>
	 * 
	 */
	protected <T> T post(String url, HttpEntity entity) {
		if (null == mHttpClient || null == url) {
			return null;
		}

		HttpResponse response = mHttpClient.postEX(url, entity);
		if (null == response) {
			return null;
		}

		return parse(response);
	}

	protected TreeMap<String, String> getSignParams() {
		TreeMap<String, String> treeMap = getParams();

		String signKey = getSignName();
		// 生成签名
		if (!TextUtils.isEmpty(signKey)) {
			String sign = HttpRestApi.sign(getSecretKey(), treeMap);
			if (!TextUtils.isEmpty(sign)) {
				treeMap.put(signKey, sign);
			}
		}
		return treeMap;
	}

	protected <T> T post() {

		HttpEntity entity = getEntity(getHashParams(getSignParams()));

		return post(getBaseHttp() + "?", entity);
	}

	protected <T> T get() {

		return get(getBaseHttp() + "?", getHashParams(getSignParams()));

	}

	// /////////通用方法
	protected HashMap<String, String> getHashParams(
			TreeMap<String, String> treeMap) {
		HashMap<String, String> hashMap = new HashMap<String, String>();

		String key = null;
		String value = null;

		for (Entry<String, String> entity : treeMap.entrySet()) {
			key = entity.getKey().toString();
			value = entity.getValue()/* .toString() */;
			hashMap.put(key, value);
		}

		return hashMap;
	}

	protected HttpEntity getEntity(HashMap<String, String> map) {

		List<NameValuePair> list = setParams(map);
		try {
			return new UrlEncodedFormEntity(list, PARAMS_CHAR_CODE);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}
	}

	protected List<NameValuePair> setParams(HashMap<String, String> map) {
		String tempKey, tempValue;
		List<NameValuePair> paramList = new ArrayList<NameValuePair>();

		for (Entry<String, String> entity : map.entrySet()) {
			tempKey = entity.getKey().toString();
			tempValue = entity.getValue()/* .toString() */;
			paramList.add(new BasicNameValuePair(tempKey, tempValue));
		}
		return paramList;
	}

}
