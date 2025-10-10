package com.examplemonevo;


public class ChequeAccount extends Account implements Withdrawable {
    private String employer;
    private String companyAddress;
    
    public ChequeAccount(String accountNumber, double initialDeposit, String branch, 
                        Customer customer, String employer, String companyAddress) {
        super(accountNumber, initialDeposit, branch, customer);
        this.employer = employer;
        this.companyAddress = companyAddress;
    }
    
public void deposit(double amount) {
        if (amount > 0) {
            this.balance += amount;
            System.out.println("Deposited: " + amount + " | New Balance: " + this.balance);
        } else {
            System.out.println("Invalid deposit amount");
        }
    }

    
    @Override
public void withdraw(double amount) {  // Change double to void
    if (amount > 0 && amount <= this.balance) {
        this.balance -= amount;
        System.out.println("Withdrawn: " + amount + " | New Balance: " + this.balance);
        // No return statement needed
    } else {
        System.out.println("Invalid withdrawal amount or insufficient funds");
        // No return statement needed
    }
}

    @Override
    public void displayAccountInfo() {
        System.out.println("Account Number: " + getAccountNumber());
        System.out.println("Balance: BWP " + getBalance());
        System.out.println("Branch: " + getBranch());
        System.out.println("Customer: " + getCustomer().getFirstName() + " " + getCustomer().getSurname());
        System.out.println("Employer: " + employer);
        System.out.println("Company Address: " + companyAddress);
        System.out.println("Interest Rate: 0% (No interest)");
    }
    
    public String getEmployer() { return employer; }
    public String getCompanyAddress() { return companyAddress; }
    
    @Override
    public String toString() {
        return super.toString() + 
               " ChequeAccount{" +
               "employer='" + employer + '\'' +
               ", companyAddress='" + companyAddress + '\'' +
               '}';
    }
}