package Controller;

import java.security.MessageDigest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import Service.AccountService;
import Service.MessageService;

import java.util.List;
import java.util.Objects;

import io.javalin.Javalin;
import io.javalin.http.Context;
import Model.Account;
import Model.Message;
 

public class SocialMediaController {
    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    AccountService accountService;
    MessageService messageService;  

    public SocialMediaController() {
        this.accountService = new AccountService(); 
        this.messageService = new MessageService(); 
    }


    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.post("/register", this::createAccountHandler);
        app.post("/login", this:: userLoginHandler); 
        app.post("/messages", this:: createMessageHandler);
        app.get("/messages", this:: getAllMessagesHandler); 
        app.get("/messages/{message_id}", this:: getMessageByIdHandler);
        app.delete("/messages/{message_id}", this:: deleteMessageByIdHandler); 
        app.patch("/messages/{message_id}", this:: updateMessageHandler);
        app.get("/accounts/{account_id}/messages", this::allMessagesByAccountHandler); 


        return app;
    }


    /**
     * Account Handlers 
     * 
     */
    private void createAccountHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        Account requestAccount = objectMapper.readValue(ctx.body(), Account.class); 
        Account createdAccount = accountService.createAccount(requestAccount);
        if (createdAccount != null) {
            ctx.json(objectMapper.writeValueAsString(createdAccount)); 
        } else {
            ctx.status(400); 
        }
    }

    private void userLoginHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        Account requestAccount = objectMapper.readValue(ctx.body(), Account.class); 
        Account checkedAccount = accountService.checkAccountExist(requestAccount);
        if (checkedAccount != null){
            ctx.json(objectMapper.writeValueAsString(checkedAccount));
        } else {
            ctx.status(401); 
        }

    }

     /**
     * Message Handlers 
     * 
     */
    
    private void createMessageHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        Message messageFromRequest = objectMapper.readValue(ctx.body(), Message.class);
        Message createdMessage = messageService.createMessage(messageFromRequest);
        if (createdMessage != null) {
            ctx.json(objectMapper.writeValueAsString(createdMessage)); 
        } else {
            ctx.status(400); 
        }


    }

    private void getAllMessagesHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();

        List<Message> allMessages = messageService.getAllMessages(); 
        ctx.json(objectMapper.writeValueAsString(allMessages)); 


    } 

    private void getMessageByIdHandler(Context ctx) throws JsonProcessingException {

        ObjectMapper objectMapper = new ObjectMapper();
        int message_id = Integer.parseInt(Objects.requireNonNull(ctx.pathParam("message_id")));
        Message message = messageService.getMessageById(message_id); 
        if (message != null) {
            ctx.json(objectMapper.writeValueAsString(message)); 

        } else {
            ctx.status(200); 
        }
    }

    private void deleteMessageByIdHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        int message_id = Integer.parseInt(Objects.requireNonNull(ctx.pathParam("message_id")));
        Message deletedMessage = messageService.deleteMessageById(message_id); 
        if (deletedMessage != null) {
            ctx.json(objectMapper.writeValueAsString(deletedMessage)); 
        } else {
            ctx.status(200); 
        }


    }

    private void updateMessageHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        int message_id = Integer.parseInt(Objects.requireNonNull(ctx.pathParam("message_id")));
        Message messageFromRequest = objectMapper.readValue(ctx.body(), Message.class);
        Message updatedMessage = messageService.updateMessage(messageFromRequest, message_id);

        if (updatedMessage != null) {
            ctx.json(objectMapper.writeValueAsString(updatedMessage)); 
        } else {
            ctx.status(400); 
        }


    }

    private void allMessagesByAccountHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        int account_id = Integer.parseInt(Objects.requireNonNull(ctx.pathParam("account_id")));

        List<Message> allMessagesFromAccount = messageService.getAllMessagesFromAccount(account_id); 
        if (allMessagesFromAccount == null) {
            ctx.status(200); 
        } else {
            ctx.json(objectMapper.writeValueAsString(allMessagesFromAccount)); 

        }


    }
}
