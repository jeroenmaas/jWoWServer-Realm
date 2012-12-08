package com.jwowserver.login.storage;

import java.sql.Connection;
import java.sql.SQLException;

import com.jwowserver.login.utils.Config;

public class Storage {
	
	private ConnectionPool pool;
	
	private RealmListStorage realmListStorage;
	private AccountStorage accountStorage = new AccountStorage();
	
	private static Storage singleton = new Storage();
	private Storage() {};
	
	static public Storage getInstance() { 
		return singleton;
	}
	
	public void init() {
		
		try {
			Config c = Config.getInstance();
			String host = c.getLoginDatabaseHost() + ":" + c.getLoginDatabasePort();
			String loginDatabaseUser = c.getLoginDatabaseUser();
			String loginDatabasePass = c.getLoginDatabasePass();
			String loginDatabaseTable = c.getLoginDatabaseTable();
			pool = new ConnectionPool("com.mysql.jdbc.Driver", host, loginDatabaseTable, loginDatabaseUser, loginDatabasePass, 1, 3, true);
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		try {
			Connection connection = pool.getConnection();
			realmListStorage = new RealmListStorage();
			realmListStorage.readDataBase(connection);
			free(connection);
			
			accountStorage.removeExpiredBans();
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public Connection getNewMysqlConnection() throws SQLException {
		return pool.getConnection();
	}
	
	public void free(Connection c) {
		pool.free(c);
	}
	
	public RealmListStorage getRealmListStorage() {
		return realmListStorage;
	}
	
	public AccountStorage getAccountStorage() {
		return accountStorage;
	}
}
