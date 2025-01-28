package Service; 

import java.util.List;

import DAO.MessageDAO; 
import Model.Message; 


public class MessageService {

    private MessageDAO messageDAO;
    
    public MessageService() {
        this.messageDAO = new MessageDAO(); 
    }

    public MessageService(MessageDAO messageDAO) {
        this.messageDAO = messageDAO; 
    }


    public Message createMessage(Message message) {
        Message createdMessage = messageDAO.createMessage(message);

        if  (message == null || message.getMessage_text() == null || message.getMessage_text().isEmpty() || message.message_text.length() > 255) {
            return null; 

        }

        boolean userExists = messageDAO.doesUserExist(message.getPosted_by());
        if (!userExists) {
            return null;
        }
        return createdMessage;
        
    }

    public List<Message> getAllMessages() {
        List<Message> allMessages = messageDAO.getAllMessages();
        return allMessages; 
    }

    public Message getMessageById(int message_id) {
        Message message = messageDAO.getMessageById(message_id); 
        return message; 
    }

    public Message deleteMessageById(int message_id) {
        Message deletedMessage = messageDAO.deleteMessageById(message_id);
        return deletedMessage; 

    }

    public Message updateMessage(Message message, int message_id) {
        Message messageById = getMessageById(message_id);
        if (messageById != null) {
            message.setMessage_id(message_id);
            message.setPosted_by(messageById.getPosted_by());
            message.setTime_posted_epoch(messageById.getTime_posted_epoch());
        }
        Message updatedMessage = messageDAO.updateMessage(message, message_id); 
        if (updatedMessage == null || message.getMessage_text().isEmpty() || message.getMessage_text().length() > 255) {
            return null;
        }
        return updatedMessage;
    }

    public List<Message> getAllMessagesFromAccount(int account_id) {
        List<Message> allMessagesFromAccount = messageDAO.getAllMessageFromAccount(account_id);
        return allMessagesFromAccount;  
    }
    

}