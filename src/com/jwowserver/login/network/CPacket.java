
package com.jwowserver.login.network;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import com.jwowserver.login.utils.Utils;

//Recieved Packet
public class CPacket {

    ByteBuffer bb;

    public CPacket(byte[] buff) {
        bb = ByteBuffer.wrap(buff);
        bb.order(ByteOrder.LITTLE_ENDIAN);
        bb.position(1); // skip cmd
    }

    public byte getCmd() {
        return bb.get(0);
    }

    public byte getInt8() {
        return bb.get();
    }

    public short getUInt8() {
        return (short) (bb.get() & 255);
    }

    public short getInt16() {
        return bb.getShort();
    }

    public int getInt32() {
        return bb.getInt();
    }

    public long getLong() {
        return bb.getLong();
    }

    public float getFloat() {
        return bb.getFloat();
    }

    public void skip(int bytes) {
        bb.position(bb.position() + bytes);
    }

    public byte[] getByteArray(int size) {
        byte array[] = new byte[size];
        for (int i = 0; i < size; i++) {
            array[i] = getInt8();
        }

        return Utils.invertEndian(array);
    }

}
