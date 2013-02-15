
package com.jwowserver.login.storage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.jwowserver.login.opcodes.enums.AuthResults;
import com.jwowserver.login.storage.objects.Account;

public class AccountStorage {
    private PreparedStatement preparedStatement = null;
    private ResultSet rs = null;
    private Connection connect = null;

    public Account getAccountFromDB(String name) {
        try {
            connect = Storage.getInstance().getNewMysqlConnection();
            preparedStatement = connect
                    .prepareStatement("SELECT sha_pass_hash,id,locked,last_ip,gmlevel FROM account WHERE username = ?");
            preparedStatement.setString(1, name);
            rs = preparedStatement.executeQuery();

            if (rs.next())
            {
                Account acc = new Account();
                acc.setUsername(name);
                acc.setId(rs.getInt("id"));
                acc.setShaPassword(rs.getString("sha_pass_hash").toUpperCase());
                acc.setLocked(rs.getBoolean("locked"));
                acc.setLastIp(rs.getString("last_ip"));
                acc.setGmLevel(rs.getByte("gmlevel"));
                return acc;
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            close();
        }

        return null;
    }

    public int getCharacterCount(int realmId, int accountId) {
        try {
            connect = Storage.getInstance().getNewMysqlConnection();
            preparedStatement = connect
                    .prepareStatement("SELECT numchars FROM realmcharacters WHERE realmid = ? && acctid = ?");
            preparedStatement.setInt(1, realmId);
            preparedStatement.setInt(2, accountId);
            rs = preparedStatement.executeQuery();

            if (rs.next())
            {
                return rs.getInt("numchars");
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            close();
        }

        return 0;
    }

    public void removeExpiredBans() {
        try {
            connect = Storage.getInstance().getNewMysqlConnection();
            connect.prepareCall(
                    "UPDATE account_banned SET active = 0 WHERE unbandate<=UNIX_TIMESTAMP() AND unbandate<>bandate")
                    .execute();
            connect.prepareCall("DELETE FROM ip_banned WHERE unbandate<=UNIX_TIMESTAMP() AND unbandate<>bandate")
                    .execute();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            close();
        }
    }

    public boolean isBannedIp(String ip) {
        boolean banned = true;
        try {
            connect = Storage.getInstance().getNewMysqlConnection();
            preparedStatement = connect
                    .prepareStatement("SELECT unbandate FROM ip_banned WHERE (unbandate = bandate OR unbandate > UNIX_TIMESTAMP()) AND ip = ?");
            preparedStatement.setString(1, ip);
            rs = preparedStatement.executeQuery();

            if (!rs.next()) {
                banned = false;
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            close();
        }

        return banned;
    }

    public AuthResults getBanStatus(int accountId) {
        AuthResults banStatus = AuthResults.WOW_SUCCESS;
        try {
            connect = Storage.getInstance().getNewMysqlConnection();
            preparedStatement = connect
                    .prepareStatement("SELECT bandate,unbandate FROM account_banned WHERE id = ? AND active = 1 AND (unbandate > UNIX_TIMESTAMP() OR unbandate = bandate)");
            preparedStatement.setInt(1, accountId);
            rs = preparedStatement.executeQuery();

            if (rs.next())
            {
                if (rs.getString("bandate").equalsIgnoreCase(rs.getString("unbandate"))) {
                    banStatus = AuthResults.WOW_FAIL_BANNED;
                } else {
                    banStatus = AuthResults.WOW_FAIL_SUSPENDED;
                }
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            close();
        }

        return banStatus;
    }

    public void setAccountAuthenticated(Account acc) {
        try {
            connect = Storage.getInstance().getNewMysqlConnection();
            preparedStatement = connect
                    .prepareStatement("UPDATE account SET sessionkey = ?, last_ip = ?, last_login = NOW(), failed_logins = 0 WHERE username = ?");
            preparedStatement.setString(1, acc.getSessionKey());
            preparedStatement.setString(2, acc.getLastIp());
            preparedStatement.setString(3, acc.getUsername());
            preparedStatement.execute();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            close();
        }
    }

    public void incrementFailedLogins(Account acc) {
        try {
            connect = Storage.getInstance().getNewMysqlConnection();
            preparedStatement = connect
                    .prepareStatement("UPDATE account SET failed_logins = failed_logins + 1 WHERE username = ?");
            preparedStatement.setString(1, acc.getUsername());
            preparedStatement.execute();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            close();
        }
    }

    public int getFailedLoginCount(Account acc) {
        int count = 0;
        try {
            connect = Storage.getInstance().getNewMysqlConnection();
            preparedStatement = connect
                    .prepareStatement("SELECT failed_logins FROM account WHERE username = ?");
            preparedStatement.setString(1, acc.getUsername());
            rs = preparedStatement.executeQuery();

            if (rs.next()) {
                count = rs.getInt("failed_logins");
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            close();
        }
        return count;
    }

    public void banIp(String ip, int time, String bannerName, String reason) {
        try {
            connect = Storage.getInstance().getNewMysqlConnection();
            preparedStatement = connect
                    .prepareStatement("INSERT INTO ip_banned VALUES (?,UNIX_TIMESTAMP(),UNIX_TIMESTAMP()+?,?,?)");
            preparedStatement.setString(1, ip);
            preparedStatement.setInt(2, time);
            preparedStatement.setString(3, bannerName);
            preparedStatement.setString(4, reason);
            preparedStatement.execute();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            close();
        }
    }

    public void banAccount(Account acc, int time, String bannerName, String reason) {
        try {
            connect = Storage.getInstance().getNewMysqlConnection();
            preparedStatement = connect
                    .prepareStatement("INSERT INTO account_banned VALUES (?,UNIX_TIMESTAMP(),UNIX_TIMESTAMP()+?,?,?,1)");
            preparedStatement.setInt(1, acc.getId());
            preparedStatement.setInt(2, time);
            preparedStatement.setString(3, bannerName);
            preparedStatement.setString(4, reason);
            preparedStatement.execute();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
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

            if (connect != null) {
                Storage.getInstance().free(connect);
                connect = null;
            }

            if (preparedStatement != null) {
                preparedStatement.close();
            }
        } catch (Exception e) {

        }
    }
}
