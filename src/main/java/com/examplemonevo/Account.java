package com.examplemonevo;

public abstract class Account {
    protected String accountNumber;
    protected double balance;
    protected String branch;
    protected Customer customer;
    
    
    
    public Account(String accountNumber, double balance, String branch, Customer customer) {
        this.accountNumber = accountNumber;
        this.balance = balance;
        this.branch = branch;
        this.customer = customer;
    }
    
    s
    public abstract boolean withdraw(double amount);
    
    
    public void deposit(double amount) {
        if (amount > 0) {
            this.balance += amount;
            System.out.println("Deposited: " + amount + " | New Balance: " + this.balance);
        } else {
            System.out.println("Invalid deposit amount");
        }
    }
    
   public void displayAccountInfo(){}
    
    public String getAccountNumber() { return accountNumber; }

    public double getBalance() { return balance; }

    public String getBranch() { return branch; }

    public Customer getCustomer() { return customer; }
    
    protected void setBalance(double balance) { this.balance = balance; }
    
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