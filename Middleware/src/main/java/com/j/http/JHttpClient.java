/**
 * 
 */
package com.j.http;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map.Entry;

import org.apache.http.HttpEntity;
import org.apache.http.HttpException;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.AuthState;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.ExecutionContext;
import org.apache.http.protocol.HttpContext;

import com.j.log.FLog;

/**
 * @author Jiangxuewu
 *         <p>
 *         HttpClien基类
 *         <p>
 *         单例模式
 *         <p>
 *         设置通用参数
 */
public class JHttpClient {

	private static String TAG = JHttpClient.class.getSimpleName();

	private static JHttpClient mInstance = null;

	private DefaultHttpClient mHttpClient = null;

	private static final int J_HTTP_CONNECT_TIMEOUT = 80 * 1000;
	private static final int J_HTTP_SOCKET_TIMEOUT = 180 * 1000;

	/**
	 * 获取实例
	 */
	public static JHttpClient getInstance() {
		if (null == mInstance) {
			mInstance = new JHttpClient();
		}

		return mInstance;
	}

	/**
	 * 构造函数
	 */
	protected JHttpClient() {
		BasicHttpParams httpParams = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParams,
				J_HTTP_CONNECT_TIMEOUT);
		HttpConnectionParams.setSoTimeout(httpParams, J_HTTP_SOCKET_TIMEOUT);

		mHttpClient = new DefaultHttpClient(httpParams);
	}

	/**
	 * HTTP Get
	 * 
	 * @param uri
	 *            the textual URI representation to be parsed into a URI object.
	 * @param headers
	 *            request params
	 * @return request result
	 *         <p>
	 *         上午12:58:15 Jiangxuewu
	 * 
	 */
	public HttpResponse get(String uri, HashMap<String, String> headers) {
		if (null == mHttpClient) {
			return null;
		}

		HttpGet get = new HttpGet(uri);

		// 设置http-header
		if (null != headers) {
			for (Entry<String, String> entry : headers.entrySet()) {
				if (null == entry) {
					continue;
				}

				get.addHeader(entry.getKey(), entry.getValue());
			}
		}

		try {
			return mHttpClient.execute(get);
		} catch (Exception e) {
			FLog.e(TAG, "get()1,exception occur:" + e.getLocalizedMessage());
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * HTTP Get
	 * <p>
	 * 用用户名和密码设置auth
	 * 
	 * @param uri
	 *            the textual URI representation to be parsed into a URI object.
	 * @param user
	 *            user name
	 * @param pwd
	 *            password
	 * @param headers
	 *            request params
	 * @return request result
	 *         <p>
	 *         下午12:14:05 Jiangxuewu
	 * 
	 */
	public HttpResponse get(String uri, String user, String pwd,
			HashMap<String, String> headers) {
		if (null == mHttpClient) {
			return null;
		}

		HttpGet get = new HttpGet(uri);

		// 设置auth
		Credentials creds = new UsernamePasswordCredentials(user, pwd);
		mHttpClient.getCredentialsProvider().setCredentials(
				new AuthScope(AuthScope.ANY_HOST, AuthScope.ANY_PORT), creds);

		// 设置http-header
		if (null != headers) {
			for (Entry<String, String> entry : headers.entrySet()) {
				if (null == entry) {
					continue;
				}

				get.addHeader(entry.getKey(), entry.getValue());
			}
		}

		try {
			return mHttpClient.execute(get);
		} catch (Exception e) {
			FLog.e(TAG, "get()2,exception occur:" + e.getLocalizedMessage());
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * HTTP POST
	 * <p>
	 * 用用户名和密码设置auth
	 * 
	 * @param uri
	 *            the textual URI representation to be parsed into a URI object.
	 * @param user
	 *            user name
	 * @param pwd
	 *            password
	 * @param entity
	 *            request params
	 * @return request result
	 *         <p>
	 *         下午12:14:05 Jiangxuewu
	 * 
	 */
	public HttpResponse post(String uri, String user, String pwd,
			HttpEntity entity) {
		if (null == mHttpClient) {
			return null;
		}

		HttpPost post = new HttpPost(uri);

		// 设置auth
		Credentials creds = new UsernamePasswordCredentials(user, pwd);
		mHttpClient.getCredentialsProvider().setCredentials(
				new AuthScope(AuthScope.ANY_HOST, AuthScope.ANY_PORT), creds);
		mHttpClient.addRequestInterceptor(preemptiveAuth, 0);

		// 设置entity
		if (null != entity) {
			post.setEntity(entity);
		}

		try {
			return mHttpClient.execute(post);
		} catch (Exception e) {
			FLog.e(TAG, "post()1,exception occur:" + e.getLocalizedMessage());
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * HTTP POST
	 * <p>
	 * 用用户名和密码设置auth
	 * 
	 * @param uri
	 *            the textual URI representation to be parsed into a URI object.
	 * @param entity
	 *            request params
	 * @return request result
	 *         <p>
	 *         下午12:14:05 Jiangxuewu
	 * 
	 */
	public HttpResponse postEX(String uri, HttpEntity entity) {
		if (null == mHttpClient) {
			return null;
		}

		HttpPost post = new HttpPost(uri);

		// 设置entity
		if (null != entity) {
			post.setEntity(entity);
		}

		try {
			return mHttpClient.execute(post);
		} catch (Exception e) {
			FLog.e(TAG, "post()2,exception occur:" + e.getLocalizedMessage());
			e.printStackTrace();
		}

		return null;
	}

	private HttpRequestInterceptor preemptiveAuth = new HttpRequestInterceptor() {
		@Override
		public void process(HttpRequest request, HttpContext context)
				throws HttpException, IOException {
			AuthState authState = (AuthState) context
					.getAttribute(ClientContext.TARGET_AUTH_STATE);
			CredentialsProvider credsProvider = (CredentialsProvider) context
					.getAttribute(ClientContext.CREDS_PROVIDER);
			HttpHost targetHost = (HttpHost) context
					.getAttribute(ExecutionContext.HTTP_TARGET_HOST);
			if (authState.getAuthScheme() == null) {
				AuthScope authScope = new AuthScope(targetHost.getHostName(),
						targetHost.getPort());
				Credentials creds = credsProvider.getCredentials(authScope);
				if (creds != null) {
					authState.setAuthScheme(new BasicScheme());
					authState.setCredentials(creds);
				}
			}
		}
	};
}
