package com.examplemonevo;

public class SavingsAccount extends Account implements InterestBearing, Withdrawable {
    private static final double Interest_Rate = 0.05;

    @Override
    public double calculateInterest() { return balance * Interest_Rate; }
    @Override
    public double getInterestRate() { return Interest_Rate; }
    @Override
    public void withdraw(double amount) {}
    @Override
    public void displayAccountInfo() {}
}
