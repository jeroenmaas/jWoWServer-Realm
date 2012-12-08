package com.jwowserver.login.storage.objects;

public class Realm {
	int id;
	String name;
	String address;
	short port;
	short icon;
	short realmflags;
	short timezone;
	short allowedSecurityLevel;
	float population;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public short getPort() {
		return port;
	}
	public void setPort(short port) {
		this.port = port;
	}
	public short getIcon() {
		return icon;
	}
	public void setIcon(short icon) {
		this.icon = icon;
	}
	public short getRealmflags() {
		return realmflags;
	}
	public void setRealmflags(short realmflags) {
		this.realmflags = realmflags;
	}
	public short getTimezone() {
		return timezone;
	}
	public void setTimezone(short timezone) {
		this.timezone = timezone;
	}
	public short getAllowedSecurityLevel() {
		return allowedSecurityLevel;
	}
	public void setAllowedSecurityLevel(short allowedSecurityLevel) {
		this.allowedSecurityLevel = allowedSecurityLevel;
	}
	public float getPopulation() {
		return population;
	}
	public void setPopulation(float population) {
		this.population = population;
	}
}
