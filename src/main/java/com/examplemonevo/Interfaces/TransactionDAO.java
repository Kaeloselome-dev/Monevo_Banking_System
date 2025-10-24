package com.examplemonevo.Interfaces;
import com.examplemonevo.Core_Model.Transaction;

import java.util.ArrayList;

public interface TransactionDAO {
    void create(Transaction transaction);
    Transaction read(String transactionId);
    void update(Transaction transaction);
    void delete(String transactionId);
    ArrayList<Transaction> getTransactionsByAccount(String accountNumber);
}

