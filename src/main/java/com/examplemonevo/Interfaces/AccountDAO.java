package com.examplemonevo.Interfaces;
import com.examplemonevo.Core_Model.Account;

import java.util.ArrayList;


public interface AccountDAO {
    void create(Account account);
    Account read(String accountNumber);
    void update(Account account);
    void delete(String accountNumber);
    ArrayList<Account> getAllAccounts();
    void updateAccountBalance(String accountNumber, double newBalance);

}

