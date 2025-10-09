package com.examplemonevo;

public abstract class Account {
    protected String accountNumber;
    protected double balance;
    protected String branch;

    public void deposit(double amount) {}
    public double getBalance() { return balance; }
    public void displayAccountInfo() {}
}





