package DAO; 

import java.security.AllPermission;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import Model.Message;
import Util.ConnectionUtil;; 

public class MessageDAO {

    public Message createMessage(Message message) {

        String sql = "INSERT INTO Message (posted_by, message_text, time_posted_epoch) VALUES (?,?,?);";
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet pKeyResultSet = null;
    
        try {
            connection = ConnectionUtil.getConnection();
            preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setInt(1, message.getPosted_by());
            preparedStatement.setString(2, message.getMessage_text());
            preparedStatement.setLong(3, message.getTime_posted_epoch());
    
            int rowsUpdated = preparedStatement.executeUpdate(); 
    
            if (rowsUpdated > 0) {
                pKeyResultSet = preparedStatement.getGeneratedKeys();
                if (pKeyResultSet.next()) {
                    int messagePKey = pKeyResultSet.getInt(1);
                    Message createdMessage = new Message(
                        messagePKey,
                        message.getPosted_by(),
                        message.getMessage_text(),
                        message.getTime_posted_epoch()
                    );
                    return createdMessage; 
                }
            } else {
                return null; 
            }
    
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if (pKeyResultSet != null) pKeyResultSet.close();
                if (preparedStatement != null) preparedStatement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    
        return null;
    }


    public boolean doesUserExist(int userId) {
        String sql = "SELECT * FROM Message WHERE posted_by = ?;";
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet rs = null;
    
        try {
            connection = ConnectionUtil.getConnection();
            statement = connection.prepareStatement(sql);
            statement.setInt(1, userId);
            rs = statement.executeQuery();
            return rs.next(); 
        } catch (SQLException e) {
            System.err.println("Error checking user existence: " + e.getMessage());
            return false; 
        } finally {
            try {
                if (rs != null) rs.close();
                if (statement != null) statement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    
    public List<Message> getAllMessages() {
        List<Message> allMessages = new ArrayList<>(); 
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;
    
        try {
            connection = ConnectionUtil.getConnection();
            String sql = "SELECT * FROM Message;"; 
            preparedStatement = connection.prepareStatement(sql);
            rs = preparedStatement.executeQuery(); 
    
            while (rs.next()) {
                Message message = new Message(
                    rs.getInt(1),
                    rs.getInt(2),
                    rs.getString(3),
                    rs.getLong(4)
                );
                allMessages.add(message); 
            }
    
        } catch (SQLException e) {
            System.out.println(e.getMessage()); 
        } finally {
            try {
                if (rs != null) rs.close();
                if (preparedStatement != null) preparedStatement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    
        return allMessages; 
    }    



    public Message getMessageById(int id) {
        String sql = "SELECT * FROM Message WHERE message_id = ?;";
        Message message = new Message(); 
        Connection connection = null;
        PreparedStatement prepareStatement = null;
        ResultSet rs = null;
    
        try {
            connection = ConnectionUtil.getConnection();
            prepareStatement = connection.prepareStatement(sql);
            prepareStatement.setInt(1, id); 
            rs = prepareStatement.executeQuery(); 
    
            if (rs.next()) {
                message.setMessage_id(rs.getInt(1));
                message.setPosted_by(rs.getInt(2));
                message.setMessage_text(rs.getString(3));
                message.setTime_posted_epoch(rs.getLong(4));
                return message; 
            }
    
        } catch (SQLException e) {
            System.out.println(e.getMessage()); 
        } finally {
            try {
                if (rs != null) rs.close();
                if (prepareStatement != null) prepareStatement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    
        return null; 
    }
    

    public Message deleteMessageById(int id) {
        MessageDAO messageDAO = new MessageDAO(); 
        Message message = messageDAO.getMessageById(id); 
        if (message != null) {
            String sql = "DELETE FROM Message WHERE message_id = ?;";
            
            Connection connection = null;
            PreparedStatement prepareStatement = null;
    
            try {
                connection = ConnectionUtil.getConnection();
                prepareStatement = connection.prepareStatement(sql);
                prepareStatement.setInt(1, id); 
                int rowsUpdated = prepareStatement.executeUpdate(); 
    
                if (rowsUpdated > 0) {
                    return message; 
                }
    
            } catch (SQLException e) {
                System.out.println(e.getMessage()); 
            } finally {
                try {
                    if (prepareStatement != null) prepareStatement.close();
                    if (connection != null) connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return null; 
    }
    

    public Message updateMessage(Message message, int message_id) {
        String sql = "UPDATE Message SET message_text = ? WHERE message_id = ?;";
        
        Connection connection = null;
        PreparedStatement prepareStatement = null;
    
        try {
            connection = ConnectionUtil.getConnection();
            prepareStatement = connection.prepareStatement(sql);
            prepareStatement.setString(1, message.getMessage_text()); 
            prepareStatement.setInt(2, message_id); 
            int rowsUpdated = prepareStatement.executeUpdate(); 
    
            if (rowsUpdated > 0) {
                return message; 
            }
    
        } catch (SQLException e) {
            System.out.println(e.getMessage()); 
        } finally {
            try {
                if (prepareStatement != null) prepareStatement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    
        return null; 
    }
    

    public List<Message> getAllMessageFromAccount(int account_id) {
        List<Message> allMessages = new ArrayList<>(); 
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;
    
        try {
            connection = ConnectionUtil.getConnection();
            String sql = "SELECT * FROM Message INNER JOIN Account ON Message.posted_by = Account.account_id WHERE account_id = ?;";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, account_id); 
            rs = preparedStatement.executeQuery(); 
    
            while (rs.next()) {
                Message message = new Message(
                    rs.getInt(1),
                    rs.getInt(2),
                    rs.getString(3),
                    rs.getLong(4)
                );
                allMessages.add(message); 
            }
    
        } catch (SQLException e) {
            System.out.println(e.getMessage()); 
        } finally {
            // Close ResultSet, PreparedStatement, and Connection
            try {
                if (rs != null) rs.close();
                if (preparedStatement != null) preparedStatement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        
        return allMessages; 
    }
    

}