package Service; 

import static org.mockito.ArgumentMatchers.anyCollection;

import DAO.AccountDAO;
import Model.Account; 

public class AccountService {

    private AccountDAO accountDAO; 

    public AccountService() {
        this.accountDAO = new AccountDAO(); 

    }   
    public AccountService(AccountDAO accountDAO) {
        this.accountDAO = accountDAO; 
        
    }
 
    public Account createAccount(Account account) {

        Account createdAccount = accountDAO.createAccount(account); 
        Boolean isUserNameBlank = account.getUsername().isEmpty();
        Boolean passLessThan4 = account.getPassword().length() < 4; 

        if (account == null || isUserNameBlank || passLessThan4) {
            return null; 
        }
      
        return createdAccount;     

    }

    public Account checkAccountExist(Account account) {
        Account checkedAccount = accountDAO.checkAccountExist(account); 
        if (checkedAccount != null && account != null) {
            if (account.getUsername().equalsIgnoreCase(checkedAccount.getUsername()) && account.getPassword().equalsIgnoreCase(checkedAccount.getPassword())){
                return checkedAccount;
            } else {
                return null; 
            }
        } else {
            return null; 
        }

    }

    
 }