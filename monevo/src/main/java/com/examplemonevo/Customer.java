package com.examplemonevo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;


public class Customer {
    private String customerID;
    private String address;
    private String username;
    private String password;

    
    private final List<Account> accounts = new ArrayList<>();

    public Customer() {}

    public Customer(String customerID, String address, String username, String password) {
        this.customerID = customerID;
        this.address = address;
        this.username = username;
        this.password = password;
    }

    /* --- Getters / Setters --- */
    public String getCustomerID() { return customerID; }
    public void setCustomerID(String customerID) { this.customerID = customerID; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    
    public List<Account> getAccounts() {
        // return an unmodifiable view to protect internal list
        return Collections.unmodifiableList(accounts);
    }

    public Account getAccount(String accountNumber) {
        if (accountNumber == null) return null;
        for (Account acc : accounts) {
            
            if (accountNumber.equals(acc.getAccountNumber())) {
                return acc;
            }
        }
        return null;
    }

    public void addAccount(Account account) {
        Objects.requireNonNull(account, "account must not be null");
        accounts.add(account);
    }

    public boolean removeAccount(String accountNumber) {
        Account acc = getAccount(accountNumber);
        if (acc != null) {
            return accounts.remove(acc);
        }
        return false;
    }

    
    public Account openAccount(Account account) {
        addAccount(account);
        return account;
    }

    
    public boolean login(String username, String password) {
        if (this.username == null || this.password == null) return false;
        return this.username.equals(username) && this.password.equals(password);
    }

    public void logout() {
        
    }

    @Override
    public String toString() {
        return "Customer{" +
                "customerID='" + customerID + '\'' +
                ", username='" + username + '\'' +
                ", address='" + address + '\'' +
                ", accounts=" + accounts.size() +
                '}';
    }
}

