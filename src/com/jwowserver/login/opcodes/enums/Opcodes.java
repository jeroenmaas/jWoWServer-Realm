package com.jwowserver.login.opcodes.enums;
public enum Opcodes {
	CMD_AUTH_LOGON_CHALLENGE(0),
	CMD_AUTH_LOGON_PROOF(1),
	CMD_AUTH_RECONNECT_CHALLENGE(2), //Unimplemented
	CMD_AUTH_RECONNECT_PROOF(3), //Unimplemented
	CMD_REALM_LIST(16);

    private byte value;
    
    Opcodes(int intValIn) {
    	value = (byte) intValIn;
    }

    public byte getByteValue() {
        return value;
    }
}