package DAO; 

import java.sql.*;

import javax.sound.midi.SysexMessage;

import Util.ConnectionUtil;
import Model.Account;

public class AccountDAO {

    public Account createAccount(Account account) {
        String sqlCheckAccount = "SELECT * FROM ACCOUNT WHERE username = ?;";
        String sqlInsertAccount = "INSERT INTO Account (username, password) VALUES (?, ?);";
    
        String accountUserName = account.getUsername();
        String accountPassword = account.getPassword();
    
        Connection connection = null;
        PreparedStatement checkStatement = null;
        PreparedStatement preparedStatement = null;
        ResultSet rsCheck = null;
        ResultSet pKeyResultSet = null;
    
        try {
            connection = ConnectionUtil.getConnection();
    
            checkStatement = connection.prepareStatement(sqlCheckAccount);
            checkStatement.setString(1, accountUserName);
            rsCheck = checkStatement.executeQuery();
            if (rsCheck.next()) {
                return null; 
            }
    
            preparedStatement = connection.prepareStatement(sqlInsertAccount, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, accountUserName);
            preparedStatement.setString(2, accountPassword);
    
            int rowsUpdated = preparedStatement.executeUpdate();
    
            if (rowsUpdated > 0) {
                pKeyResultSet = preparedStatement.getGeneratedKeys();
                if (pKeyResultSet.next()) {
                    int accountPKey = pKeyResultSet.getInt(1);
                    return new Account(accountPKey, accountUserName, accountPassword);
                }
            }
    
        } catch (SQLException e) {
            System.out.println(e.getMessage());

        } finally {
            // Close ResultSet objects
            try {
                if (rsCheck != null) rsCheck.close();
                if (pKeyResultSet != null) pKeyResultSet.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
    
            // Close PreparedStatements
            try {
                if (checkStatement != null) checkStatement.close();
                if (preparedStatement != null) preparedStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
    
            // Close Connection
            try {
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    
        return null;
    }
    

    public Account checkAccountExist(Account account) {

        String sqlCheckAccount = "SELECT * FROM ACCOUNT WHERE username = ?;";

        Connection connection = null;
        PreparedStatement checkStatement = null;
        ResultSet rsCheck = null;

        try {
            connection = ConnectionUtil.getConnection();
            checkStatement = connection.prepareStatement(sqlCheckAccount);
            checkStatement.setString(1, account.getUsername());
            rsCheck = checkStatement.executeQuery();
    
            if (rsCheck.next()) {
                Account accountExist = new Account(
                    rsCheck.getInt(1),
                    rsCheck.getString("username"),
                    rsCheck.getString("password")
                );
                return accountExist;
            }
    
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if (rsCheck != null) rsCheck.close();
                if (checkStatement != null) checkStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
    
            try {
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    
        return null; 
    }


}

