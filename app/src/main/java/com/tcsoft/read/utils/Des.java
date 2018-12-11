package com.tcsoft.read.utils;

import java.security.Key;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;

public class Des {
	private static String DESKey = "12345678";
	private final static String DES = "DES";
	private static byte[] iv1 = { (byte) 0x12, (byte) 0x34, (byte) 0x56,
			(byte) 0x78, (byte) 0x90, (byte) 0xAB, (byte) 0xCD, (byte) 0xEF };

	public static void main(String[] args) throws Exception {
		String rdid = "DZZBD";
		String key = "P2GD0769P2GD0769";
		String str = new Des().encrypt(rdid, key);
		String tmp = byteArrayToHexString(str.getBytes());
		System.out.println("原数据：" + rdid);
		System.out.println("一次加密：" + str);
		System.out.println("二次加密：" + tmp);
		tmp = "6B4854474C6463745A33513D0A";
		String tmp2 = new String(hexToBytes(tmp.toCharArray()));
		System.out.println("二次解密：" + tmp2);
		byte[] b = decrypt(key, Base64.decode(tmp2, 0));
		String str2 = new String(b, "UTF-8");
		System.out.println("解密后数据：" + str2);
		
		System.out.println("libcode1："+new Des().encrypt("P2GD0769005", "376B4A409E5789CE"));
		System.out.println("libcode2：" + byteArrayToHexString(new Des().encrypt("P2GD0769005", "376B4A409E5789CE").getBytes()));
		
		String test = "496D436776372B376753766B51436F2B6D31784E37413D3D";
		String tmp3 = new String(hexToBytes(test.toCharArray()));
		byte[] b3 = decrypt("376B4A409E5789CE", Base64.decode(tmp3, 0));
		String str3 = new String(b3, "UTF-8");
		System.out.println(str3);
//		String code = "P2GD0769P2GD0769";
//		String key = code.substring(0, 8) + code.substring(0, 8);
//		String rdid = "396B5449383346464332343D0A";
//		Des des = new Des();
//		byte[] b = des
//				.decrypt(key, Base64.decode(
//						new String(des.hexToBytes(rdid.toCharArray())),
//						Base64.DEFAULT));
//		rdid = new String(b, "UTF-8");
//		System.out.println(rdid);
	}

	public static byte[] hexToBytes(char[] hex) {
		int length = hex.length / 2;
		byte[] raw = new byte[length];
		for (int i = 0; i < length; i++) {
			int high = Character.digit(hex[i * 2], 16);
			int low = Character.digit(hex[i * 2 + 1], 16);
			int value = (high << 4) | low;
			if (value > 127)
				value -= 256;
			raw[i] = (byte) value;
		}
		return raw;
	}

	public static String byteArrayToHexString(byte[] b) {
		StringBuffer sb = new StringBuffer(b.length * 2);
		for (int i = 0; i < b.length; i++) {
			int v = b[i] & 0xff;
			if (v < 16) {
				sb.append('0');
			}
			sb.append(Integer.toHexString(v));
		}
		return sb.toString().toUpperCase();
	}

	public static byte[] decrypt(String key, byte[] data) throws Exception {
		try {
			SecureRandom sr = new SecureRandom();
			DESKeySpec dks = new DESKeySpec(key.getBytes());
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");

			Key secretKey = keyFactory.generateSecret(dks);
			Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
			IvParameterSpec iv = new IvParameterSpec(iv1);
			AlgorithmParameterSpec paramSpec = iv;
			cipher.init(2, secretKey, paramSpec);
			return cipher.doFinal(data);
		} catch (Exception e) {
			throw new Exception(e);
		}
	}

	public byte[] desEncrypt(byte[] plainText, String DESKey) throws Exception {
		IvParameterSpec iv = new IvParameterSpec(iv1);

		DESKeySpec dks = new DESKeySpec(DESKey.getBytes());
		SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
		SecretKey key = keyFactory.generateSecret(dks);
		Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
		cipher.init(1, key, iv);
		byte[] data = plainText;
		byte[] encryptedData = cipher.doFinal(data);
		return encryptedData;
	}

	public String encrypt(String input, String key) {
		String result = "input";
		try {
			result = base64Encode(desEncrypt(input.getBytes(), key));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public String base64Encode(byte[] s) {
		if (s == null)
			return null;
		return Base64.encodeToString(s, Base64.DEFAULT);

	}
}