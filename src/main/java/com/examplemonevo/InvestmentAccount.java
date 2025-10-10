package com.examplemonevo;

public class InvestmentAccount extends Account implements InterestBearing {
    private static final double INTEREST_RATE = 0.05; 
    private static final double MIN_OPENING_BALANCE = 500.00;
    
    public InvestmentAccount(String accountNumber, double initialDeposit, String branch, Customer customer) {
        super(accountNumber, initialDeposit, branch, customer);
        validateInitialDeposit(initialDeposit);
    }

     public void deposit(double amount) {
        if (amount > 0) {
            this.balance += amount;
            System.out.println("Deposited: " + amount + " | New Balance: " + this.balance);
        } else {
            System.out.println("Invalid deposit amount");
        }
    }
    
    private void validateInitialDeposit(double initialDeposit) {
        if (initialDeposit < MIN_OPENING_BALANCE) {
            throw new IllegalArgumentException(
                "Investment account requires minimum opening balance of " + MIN_OPENING_BALANCE
            );
        }
    }

       @Override
    public void displayAccountInfo() {
        System.out.println("Account Number: " + getAccountNumber());
        System.out.println("Balance: BWP " + getBalance());
        System.out.println("Branch: " + getBranch());
        System.out.println("Customer: " + getCustomer().getFirstName() + " " + getCustomer().getSurname());
        System.out.println("Interest Rate: " + (INTEREST_RATE * 100) + "% monthly");
        System.out.println("Minimum Opening Balance: BWP " + MIN_OPENING_BALANCE);
    }
    
    @Override
    public boolean withdraw(double amount) {
        if (amount > 0 && amount <= this.balance) {
            this.balance -= amount;
            System.out.println("Withdrawn: " + amount + " | New Balance: " + this.balance);
            return true;
        } else {
            System.out.println("Invalid withdrawal amount or insufficient funds");
            return false;
        }
    }
    
    @Override
    public void calculateInterest() {
        double interest = this.balance * INTEREST_RATE;
        this.balance += interest;
        System.out.println("Applied monthly interest: " + interest + " | New Balance: " + this.balance);
    }
    
    @Override
    public double getInterestRate() {
        return INTEREST_RATE;
    }
}