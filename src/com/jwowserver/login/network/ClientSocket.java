package com.jwowserver.login.network;

import java.io.IOException;
import java.net.Socket;

import com.jwowserver.login.storage.objects.Account;

public class ClientSocket {
	Socket socket;
	
	byte[] sessionKey; //Equals clients session key
	VerificationData verificationData;
	Account account = null;
	boolean verified = false;
	
	public ClientSocket(Socket socket) {
		this.socket = socket;
		verificationData = new VerificationData();
	}
	
	public VerificationData getVerificationData() {
		return verificationData;
	}
	
	public void sendPacket(SPacket pck) {
		try {
			socket.getOutputStream().write(pck.getByteArray());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	public boolean isVerified() {
		return verified;
	}

	public void setVerified(boolean verified) {
		this.verified = verified;
	}
	
	public String getIp() {
		return socket.getInetAddress().getHostAddress();
	}
}
