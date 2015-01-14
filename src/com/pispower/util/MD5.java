package com.pispower.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;

public class MD5 {

	private static final String MD5 = "MD5";

	/**
	 * 获取输入字符串的MD5值
	 * 
	 * @param val
	 * @return 字符串
	 * @throws NoSuchAlgorithmException
	 */
	public static String getMD5(String val) throws NoSuchAlgorithmException {
		MessageDigest md5 = MessageDigest.getInstance(MD5);
		md5.update(val.getBytes());
		return bytesToHexString(md5.digest());
	}

	private static String bytesToHexString(byte[] bytes) {
		StringBuilder sb = new StringBuilder(bytes.length * 2);

		Formatter formatter = new Formatter(sb);

		for (byte b : bytes) {
			formatter.format("%02x", b);
		}
		formatter.close();
		return sb.toString();
	}

	/**
	 * 
	 * 获取输入文件的MD5值
	 * 
	 * @param file
	 * @return 字符串
	 * @throws NoSuchAlgorithmException
	 * @throws IOException
	 */
	public static String getFileMd5StringForAndroid(File file)
			throws NoSuchAlgorithmException, IOException {

		MessageDigest md5 = MessageDigest.getInstance(MD5);
		FileInputStream fileInputStream = new FileInputStream(file);
		byte[] bytes = new byte[1024];
		int byteCount;
		while ((byteCount = fileInputStream.read(bytes)) > 0) {
			md5.update(bytes, 0, byteCount);
		}
		fileInputStream.close();
		byte[] digest = md5.digest();
		return bytesToHexString(digest);
	}

	//
	// public static String bytesToHexStringOther(byte[] bytes) {
	// StringBuffer buf = new StringBuffer(bytes.length * 2);
	// for (byte b : bytes) {
	// String s = Integer.toString(0xFF & b, 16);
	// if (s.length() < 2)
	// buf.append('0');
	// buf.append(s);
	// }
	// return buf.toString();
	// }
	//
	// public static String bytesToHexStringUseBigInteger(byte[] bytes) {
	// String md5 = new BigInteger(1, bytes).toString(16);
	// while (md5.length() < 32) {
	// md5 = "0" + md5;
	// }
	// return md5;
	// }
	// /**
	// * 获取输入文件的MD5值
	// *
	// * @param file
	// * @return
	// * @throws IOException
	// * @throws NoSuchAlgorithmException
	// */
	// public static String getFileMd5String(File file) throws IOException,
	// NoSuchAlgorithmException {
	// MessageDigest md5 = MessageDigest.getInstance(MD5);
	// FileInputStream fileInputStream = new FileInputStream(file);
	// FileChannel fileChannel = fileInputStream.getChannel();
	// MappedByteBuffer buffer = fileChannel.map(MapMode.READ_ONLY, 0,
	// file.length());
	// md5.update(buffer);
	// fileChannel.close();
	// fileInputStream.close();
	// return bytesToHexString(md5.digest());
	// }

}