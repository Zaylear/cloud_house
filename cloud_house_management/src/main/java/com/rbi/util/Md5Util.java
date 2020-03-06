package com.rbi.util;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.security.MessageDigest;

public class Md5Util {
	public static String getMD5(String str) throws Exception {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(str.getBytes());
			byte[] digest = md.digest();
			return parseByte2HexStr(digest);
		} catch (Exception e) {
			throw new Exception("MD5加密出现错误");
		}
	}

	public static String getMD5(String content, String key) throws Exception {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] input = content.getBytes("utf-8");
			byte[] thedigest = md.digest(key.getBytes("utf-8"));
			SecretKeySpec skc = new SecretKeySpec(thedigest, "AES");
			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
			cipher.init(Cipher.ENCRYPT_MODE, skc);
			byte[] cipherText = new byte[cipher.getOutputSize(input.length)];
			int ctLength = cipher.update(input, 0, input.length, cipherText);
			ctLength += cipher.doFinal(cipherText, ctLength);
			return parseByte2HexStr(cipherText);
		} catch (Exception e) {
			throw new Exception("MD5加密出现错误");
		}
	}

	private static String parseByte2HexStr(byte buf[]) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < buf.length; i++) {
			String hex = Integer.toHexString(buf[i] & 0xFF);
			if (hex.length() == 1) {
				hex = '0' + hex;
			}
			sb.append(hex);
		}
		return sb.toString();
	}
}
