package com.jwowserver.login.utils;

import java.math.BigInteger;
import java.util.Random;

public class Utils {
	public static String byteArrayToHexString(byte[] b) {
		String result = "";
		for (int i = 0; i < b.length; i++) {
			result += Integer.toString((b[i] & 0xff) + 0x100, 16).substring(1);
		}
		return result;
	}
	
	public static byte[] hexStringToByteArray(String s) {
	    int len = s.length();
	    byte[] data = new byte[len / 2];
	    for (int i = 0; i < len; i += 2) {
	        data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
	                             + Character.digit(s.charAt(i+1), 16));
	    }
	    return data;
	}
	
	public static byte[] invertEndian(byte[] array) {
		int len = array.length;
		byte[] data = new byte[len];
		for (int i = 0; i < len; i++) {
			data[i] = array[len-1-i];
		}
		
		return data;
	}
	
	public static String generateRandomHexString(int length) {
		Random rn = new Random();
		String hexString = "";
		for(int i = 0; i < length; i++)
		{
			int value = rn.nextInt(15) + 1;
			hexString += Integer.toHexString(value);
		}
		
		return hexString;
	}
	
	public static BigInteger createBigIntFromHexString(String hex) {
		return new BigInteger(hexStringToByteArray("00" + hex));
	}
}
