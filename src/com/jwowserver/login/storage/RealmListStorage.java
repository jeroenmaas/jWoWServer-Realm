package com.jwowserver.login.storage;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import com.jwowserver.login.storage.objects.Realm;

public class RealmListStorage {
	private Statement statement = null;
	private ResultSet rs = null;

	private ArrayList<Realm> realms = new ArrayList<Realm>();

	public void readDataBase(Connection connect) throws Exception {
		try {
			// Statements allow to issue SQL queries to the database
			statement = connect.createStatement();
			// Result set get the result of the SQL query
			rs = statement.executeQuery("SELECT id, name, address, port, icon, realmflags, timezone, allowedSecurityLevel, population FROM realmlist WHERE (realmflags & 1) = 0 ORDER BY name");

			realms.clear();
			while (rs.next()) {

				Realm list = new Realm();
				list.setId(rs.getInt("id"));
				list.setName(rs.getString("Name"));
				list.setAddress(rs.getString("address"));
				list.setPort(rs.getShort("port"));
				list.setIcon(rs.getShort("icon"));
				list.setRealmflags(rs.getShort("realmflags"));
				list.setTimezone(rs.getShort("timezone"));
				list.setAllowedSecurityLevel(rs.getShort("allowedSecurityLevel"));
				list.setPopulation(rs.getFloat("population"));

				realms.add(list);
			}

		} catch (Exception e) {
			throw e;
		} finally {
			close();
		}

	}
	
	// You need to close the resultSet
	private void close() {
		try {
			if (rs != null) {
				rs.close();
			}

			if (statement != null) {
				statement.close();
			}
		} catch (Exception e) {
		}
	}
	
	public ArrayList<Realm> getRealms() {
		return realms;
	}
}