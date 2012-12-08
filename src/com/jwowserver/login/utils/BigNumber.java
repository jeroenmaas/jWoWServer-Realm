package com.jwowserver.login.utils;

import java.math.BigInteger;

public class BigNumber extends BigInteger {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8462102891136974658L;

	public BigNumber(String val, int radix) {
		super("00" + val, radix);
	}
	
	public BigNumber(String string) {
		super(string);
	}
	
	public BigNumber(byte[] byteArray) {
		super(byteArray);
	}

	public byte[] toLittleEndianBytes() {
		return Utils.invertEndian(toByteArray());
	}
	
	public byte[] toLittleEndianBytes(int size) {
		return Utils.invertEndian(toByteArray(size));
	}

	@Override
	public byte[] toByteArray() {
		if(super.toByteArray()[0] == 0)
		{
			byte[] array = new byte[super.toByteArray().length-1];
			for(int i = 1; i < super.toByteArray().length; i++)
				array[i-1] = super.toByteArray()[i];
			
			return array;
		}
		
		return super.toByteArray();
	}
	
	public byte[] toByteArray(int size) {
		
		byte[] array = toByteArray();
		if(array.length != size)
		{
			byte[] newArray = new byte[size];
			for(int i = 0; i < size; i++)
			{	
				if(array.length+i < size)
					newArray[i] = 0;
				else
					newArray[i] = array[i-(size-array.length)]; //old 10 new 20. (old range 0tm9)
			}
			
			return newArray;
		}
		
		return array;
	}

	@Override
	public BigNumber modPow(BigInteger exponent, BigInteger m) {
		BigInteger value = super.modPow(exponent, m);
		return new BigNumber(value.toByteArray());
	}
	
	@Override
	public BigInteger mod(BigInteger m) {
		// TODO Auto-generated method stub
		return new BigNumber(super.mod(m).toByteArray());
	}

	@Override
	public BigInteger add(BigInteger val) {
		// TODO Auto-generated method stub
		return new BigNumber(super.add(val).toByteArray());
	}

	@Override
	public BigNumber multiply(BigInteger val) {
		// TODO Auto-generated method stub
		return new BigNumber(super.multiply(val).toByteArray());
	}

}
