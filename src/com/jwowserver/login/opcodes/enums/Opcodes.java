package com.jwowserver.login.opcodes.enums;

import java.util.HashMap;
import java.util.Map;

import com.jwowserver.login.opcodes.GenericWowPacketHandler;

public enum Opcodes {
	CMD_AUTH_LOGON_CHALLENGE(0, new com.jwowserver.login.opcodes.CMD_AUTH_LOGON_CHALLENGE()),
	CMD_AUTH_LOGON_PROOF(1, new com.jwowserver.login.opcodes.CMD_AUTH_LOGON_PROOF()),
	CMD_AUTH_RECONNECT_CHALLENGE(2, null),
	CMD_AUTH_RECONNECT_PROOF(3, null), 
	CMD_REALM_LIST(16, new com.jwowserver.login.opcodes.CMD_REALM_LIST());

    private byte opcodeId;
    private GenericWowPacketHandler handler;
    
    Opcodes(int opcodeId, GenericWowPacketHandler handler) {
    	this.opcodeId = (byte) opcodeId;
    	this.handler = handler;
    }

    public byte getId() {
        return opcodeId;
    }
    
    public GenericWowPacketHandler getHandler() {
    	return handler;
    }
    
    //We use this map to decrease conversion time int -> opcode
    private static final Map<Byte, Opcodes> opcodeMap = new HashMap<Byte, Opcodes>();
    static {
        for (Opcodes type : Opcodes.values()) {
            opcodeMap.put(type.opcodeId, type);
        }
    }
    
    public static Opcodes get(byte i) {
        return opcodeMap.get(i);
    }
}