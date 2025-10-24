package com.examplemonevo.Core_Model;

import com.examplemonevo.Core_Model.Customer;

public abstract class Account {
    public String accountNumber;
    public double balance;
    public String branch;
    public Customer customer;
    public String accountType;
    
    
    
    public Account(String accountNumber, double balance, String branch, Customer customer, String accountType) {
        this.accountNumber = accountNumber;
        this.balance = balance;
        this.branch = branch;
        this.customer = customer;
        this.accountType = accountType;
    }
    
    
    
    public void deposit(double amount) {
        if (amount > 0) {
            this.balance += amount;
            System.out.println("Deposited: " + amount + " | New Balance: " + this.balance);
        } else {
            System.out.println("Invalid deposit amount");
        }
    }

    public double getInterestRate() {
        return 0; // default for accounts with no interest
    }
    
   public void displayAccountInfo(){}
    
    public String getAccountNumber() { return accountNumber; }

    public double getBalance() { return balance; }

    public String getBranch() { return branch; }

    public Customer getCustomer() { return customer; }

    public String getAccountType() { return accountType; }

    public void setBalance(double balance) { this.balance = balance; }
    
    @Override
    public String toString() {
        return "Account{" +
                "accountNumber='" + accountNumber + '\'' +
                ", balance=" + balance +
                ", branch='" + branch + '\'' +
                ", customer=" + customer.getFirstName() + " " + customer.getSurname() +
                '}';
    }
}