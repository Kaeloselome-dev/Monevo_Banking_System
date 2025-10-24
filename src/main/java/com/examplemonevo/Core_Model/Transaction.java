package com.examplemonevo.Core_Model;

import java.util.Date;

public class Transaction {
    private String transactionId;
    private String accountNumber;  // Associated Account
    private double amount;
    private Date date;
    private String type;  // Deposit, Withdrawal, Transfer
    private String description; // Optional description

    public Transaction(String transactionId, String accountNumber, double amount, Date date, String type, String description) {
        this.transactionId = transactionId;
        this.accountNumber = accountNumber;
        this.amount = amount;
        this.date = date;
        this.type = type;
        this.description = description;
    }

    // Getters and setters
    public String getTransactionId() { return transactionId; }
    public String getAccountNumber() { return accountNumber; }
    public double getAmount() { return amount; }
    public Date getDate() { return date; }
    public String getType() { return type; }
    public String getDescription() { return description; }

    public void setTransactionId(String transactionId) { this.transactionId = transactionId; }
    public void setAccountNumber(String accountNumber) { this.accountNumber = accountNumber; }
    public void setAmount(double amount) { this.amount = amount; }
    public void setDate(Date date) { this.date = date; }
    public void setType(String type) { this.type = type; }
    public void setDescription(String description) { this.description = description; }

    @Override
    public String toString() {
        return "Transaction{" +
                "transactionId='" + transactionId + '\'' +
                ", accountNumber='" + accountNumber + '\'' +
                ", amount=" + amount +
                ", date=" + date +
                ", type='" + type + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
