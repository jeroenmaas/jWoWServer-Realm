package com.jwowserver.login.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Config {
	
	private static final String CONFIGFILE = "configuration.conf";
	private static final String CONFIG_VERSION = "2010062001";

	Properties configFile;
	
	private static Config singleton = new Config();
	
	static public Config getInstance() { 
		return singleton;
	}
	
	private Config() {
	    configFile = new Properties();
	    try {
			configFile.load(new FileInputStream(CONFIGFILE));
			
			if(configFile.isEmpty()) {
				throw new RuntimeException("Could not find configuration.conf");
			}
			
			if(!getConfigVersion().equalsIgnoreCase(CONFIG_VERSION)) {
				throw new RuntimeException("invalid " + CONFIGFILE + ". This application only works with version: " + CONFIG_VERSION);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public String getConfigVersion() {
		return (String) configFile.get("ConfVersion"); 
	}
	
	public String getLoginDatabaseHost() {
		return (String) configFile.get("LoginDatabaseHost"); 
	}
	
	public String getLoginDatabasePort() {
		return (String) configFile.get("LoginDatabasePort"); 
	}
	
	public String getLoginDatabaseUser() {
		return (String) configFile.get("LoginDatabaseUser"); 
	}
	
	public String getLoginDatabasePass() {
		return (String) configFile.get("LoginDatabasePass"); 
	}
	
	public int getLoginDatabaseMinConnections() {
		String s = (String) configFile.get("LoginDatabaseMinConnections");
		return  Integer.parseInt(s);
	}
	
	public int getLoginDatabaseMaxConnections() {
		String s = (String) configFile.get("LoginDatabaseMaxConnections");
		return  Integer.parseInt(s);
	}
	
	public String getLoginDatabaseTable() {
		return (String) configFile.get("LoginDatabaseTable"); 
	}
	
	public int getRealmServerPort() {
		String s = (String) configFile.get("RealmServerPort");
		return  Integer.parseInt(s);
	}
	
	public int getMaxWrongPasswords() {
		String s = (String) configFile.get("WrongPass.MaxCount");
		return  Integer.parseInt(s);
	}
	
	public int getWrongPassBanTime() {
		String s = (String) configFile.get("WrongPass.BanTime");
		return  Integer.parseInt(s);
	}
	
	public boolean getWrongPassBanType() {
		String s = (String) configFile.get("WrongPass.BanType");
		int i = Integer.parseInt(s);
		if(i == 0)
			return false;
		else
			return true;
	}
}
