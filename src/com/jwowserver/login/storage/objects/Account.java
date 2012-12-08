package com.jwowserver.login.storage.objects;

public class Account {
	int id;
	String username;
	String shaPassword;
	byte gmLevel;
	String sessionKey;
	boolean locked;
	String lastIp;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getShaPassword() {
		return shaPassword;
	}
	public void setShaPassword(String shaPassword) {
		this.shaPassword = shaPassword;
	}
	public byte getGmLevel() {
		return gmLevel;
	}
	public void setGmLevel(byte gmLevel) {
		this.gmLevel = gmLevel;
	}
	public String getSessionKey() {
		return sessionKey;
	}
	public void setSessionKey(String sessionKey) {
		this.sessionKey = sessionKey;
	}
	public boolean isLocked() {
		return locked;
	}
	public void setLocked(boolean locked) {
		this.locked = locked;
	}
	public String getLastIp() {
		return lastIp;
	}
	public void setLastIp(String lastIp) {
		this.lastIp = lastIp;
	}
}
