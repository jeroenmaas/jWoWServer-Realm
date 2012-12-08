package com.jwowserver.login.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class ShaInstance {
	
	MessageDigest crypt;
	
	public ShaInstance() {
		try {
			crypt = MessageDigest.getInstance("SHA-1");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace(); //Should NEVER occur
		}
		crypt.reset();
	}
	
	public ShaInstance update(byte[] data) {
		crypt.update(Utils.invertEndian(data));
		return this;
	}
	
	public ShaInstance updateLittleEndian(byte[] data) {
		crypt.update(data);
		return this;
	}
	
	public ShaInstance update(String data) {
		try {
			crypt.update(data.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return this;
	}
	
	public byte[] getDigest() {
		return Utils.invertEndian(crypt.digest());
	}
	
	public byte[] getLittleEndianDigest() {
		return crypt.digest();
	}
	
	public String getDigestStr() {
		return Utils.byteArrayToHexString(Utils.invertEndian(crypt.digest()));
	}

	public ShaInstance updateLittleEndian(String data) {
		try {
			update(data.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		return this;
	}
}
