package com.examplemonevo;

public class ChequeAccount extends Account implements Withdrawable {
    private String employerName;
    private String employerAddress;

    @Override
    public void withdraw(double amount) {}
    public void depositSalary(double amount) {}
    @Override
    public String displayAccountInfo() {
         
    }
}
