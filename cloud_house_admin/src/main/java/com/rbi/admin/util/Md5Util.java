package com.rbi.admin.util;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.security.MessageDigest;
import java.util.UUID;

public class Md5Util {
	public static String getMD5(String str) throws Exception {
		try {
			// 生成一个MD5加密计算摘要
			MessageDigest md = MessageDigest.getInstance("MD5");
			// 计算md5函数
			md.update(str.getBytes());
			byte[] digest = md.digest();
//			return new BigInteger(1, digest).toString(16);
			return parseByte2HexStr(digest);
		} catch (Exception e) {
			throw new Exception("MD5加密出现错误");
		}
	}

	public static String getMD5(String content, String key) throws Exception {
		try {
			// 生成一个MD5加密计算摘要
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

	public static void main(String[] args) throws Exception {
		String md5 = getMD5("123456","26875117-76f1-431f-a006-47ac0fa3a213");
		UUID randomUUID = UUID.randomUUID();
		System.out.println(md5);
		System.out.println(randomUUID);
		System.out.println(getMD5("a671cc84-c918-4c9e-b49b-5bd680b41f37"));
	}
}
