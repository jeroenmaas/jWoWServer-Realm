package com.jwowserver.login.network;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import com.jwowserver.login.opcodes.enums.Opcodes;
import com.jwowserver.login.utils.Utils;

//Sending Packet
public class SPacket {

	ByteBuffer bb;
	
	public SPacket(Opcodes op, int size) {
		byte[] buff = new byte[size];
		bb = ByteBuffer.wrap(buff);
		bb.order(ByteOrder.BIG_ENDIAN);
		bb.put(op.getByteValue());
	}
	
	public void write(byte b) {
		bb.put(b);
	}
	
	public void write(short s) {
		bb.put( (byte) (s & 0xFF));
		bb.put( (byte) ((s >>> 8) & 0xFF));
	}
	
	public void write(short s, int offset) {
		bb.put(offset, (byte) (s & 0xFF));
		bb.put(offset+1, (byte) ((s >>> 8) & 0xFF));
	}
	
	public void write(int i) {
		bb.put( (byte) (i & 0xFF));
		bb.put( (byte) ((i >>> 8) & 0xFF));
		bb.put( (byte) ((i >>> 16) & 0xFF));
		bb.put( (byte) ((i >>> 24) & 0xFF));
	}
	
	public void write(byte[] a) {
		bb.put(Utils.invertEndian(a));
	}
	
	public byte[] getByteArray() {
		int length = bb.capacity() - bb.remaining();
		bb.rewind();
		byte[] bytesArray = new byte[length];
	    bb.get(bytesArray, 0, length);
		return bytesArray;
	}

	public void write(String s) {
		byte[] array = null;
		try {
			array = s.getBytes("UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		bb.put(array);
		bb.put((byte)0);
	}

	public void write(float f) {
		// TODO Auto-generated method stub
		bb.putFloat(f);
	}
	
	public void setPacketSize() {
		byte[] byteArray = getByteArray();
		byteArray[1] = (byte) (getPosition()-3 & 0xFF);
		byteArray[2] = (byte) ((getPosition()-3 >>> 8) & 0xFF);
		bb = ByteBuffer.wrap(byteArray);
		bb.position(byteArray.length);
	}
	
	public int getPosition() {
		return bb.position();
	}
}
