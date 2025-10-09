package com.examplemonevo;

public class InvestmentAccount extends Account implements InterestBearing, Withdrawable {
    private static final double INTEREST_RATE = 0.05;
    private static final double MINIMUM_DEPOSIT = 500;

    @Override
    public double calculateInterest() { return balance * INTEREST_RATE; }
    @Override
    public double getInterestRate() { return INTEREST_RATE; }
    @Override
    public void withdraw(double amount) {}
    @Override
    public void displayAccountInfo() {}
}
