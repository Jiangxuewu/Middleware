/**
 *
 */
package com.j.http;

import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.j.log.FLog;

/**
 * @author Jiangxuewu
 *         <p>
 *         MD5编码，使用android自带的messageDigest实现
 */
public class Md5Coder {
	private static final String TAG = Md5Coder.class.getSimpleName();

	private MessageDigest mDigest = null;

	private final int MD5_BUF_LEN = 2048; // 2K

	private static Md5Coder mInstance = null;

	/**
	 * 获取实例
	 */
	public static Md5Coder getInstance() {
		if (null == mInstance) {
			mInstance = new Md5Coder();
		}
		return mInstance;
	}

	/**
	 * 构造函数
	 */
	public Md5Coder() {
		try {
			mDigest = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			FLog.e(TAG,
					"Md5Coder.java, Md5Coder(),exception occur:"
							+ e.getLocalizedMessage());
			e.printStackTrace();
		}
	}

	/**
	 * 加密
	 */
	public String encode(byte[] buf) {
		if (null == mDigest || null == buf || buf.length <= 0) {
			return null;
		}

		mDigest.update(buf);

		return toHexString(mDigest.digest());
	}

	/**
	 * MD5加密
	 * 
	 * @param data
	 * @return 下午1:44:18 Jiangxuewu
	 * 
	 */
	public String encode(String data) {
		return encode(data.getBytes());
	}

	/**
	 * 锋付账户登陆密码，md5加密接口[专用]
	 * 
	 * @param loginPwd
	 * @return
	 */
	public String encode(String loginPwd, String key) {
		String data = key + loginPwd + key;
		return encode(data.getBytes());
	}

	/**
	 * 加密
	 */
	public String encode(InputStream inputStream) {
		if (null == mDigest || null == inputStream) {
			return null;
		}

		byte[] buf = new byte[MD5_BUF_LEN];
		int readNum = 0;
		try {
			while ((readNum = inputStream.read(buf)) > 0) {
				mDigest.update(buf, 0, readNum);
			}
			return toHexString(mDigest.digest());
		} catch (IOException e) {
			FLog.e(TAG, "Md5Corder.java,encode inputstream,exception occur:"
					+ e.getLocalizedMessage());
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * 将byte转成32位字符串
	 */
	private String toHexString(byte[] b) {
		if (null == b) {
			return null;
		}

		StringBuilder sb = new StringBuilder(b.length * 2);
		for (int i = 0; i < b.length; i++) {
			final int value = b[i] & 255;
			if (value < 16) {
				sb.append('0');
			}
			sb.append(Integer.toHexString(value));
		}
		return sb.toString();
	}

}
