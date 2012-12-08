package com.jwowserver.login.opcodes;

import com.jwowserver.login.network.CPacket;
import com.jwowserver.login.network.ClientSocket;
import com.jwowserver.login.network.SPacket;
import com.jwowserver.login.opcodes.enums.AuthResults;
import com.jwowserver.login.opcodes.enums.Opcodes;

public class GenericWowPacketHandler {
	
	ClientSocket socket;
	
	public void init(ClientSocket socket) {
		this.socket = socket;
	}
	
	public void handlePacket(CPacket pck) {
		System.out.println("HandlePacket was called. Should be overriden");
	}
	
	public void sendPacket(SPacket pck) {
		socket.sendPacket(pck);
	}
	
	public void sendAuthResult(AuthResults result) {
		SPacket outPkt = new SPacket(Opcodes.CMD_AUTH_LOGON_CHALLENGE, 3);
		outPkt.write((byte) 0);
		outPkt.write((byte)result.getIntVal());
		
		sendPacket(outPkt);
	}
	
	public ClientSocket getSocket() {
		return socket;
	}
}
