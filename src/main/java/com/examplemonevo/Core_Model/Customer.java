package com.examplemonevo.Core_Model;

import java.util.ArrayList;

public abstract class Customer {
    private String customerId;
    private String firstName;
    protected String surname;
    private String address;
    private String phoneNumber;
    private String email;
    private String username;
    private String password;
    private String Customer_type;
    private ArrayList<Account> accounts;

    public Customer(String customerId, String firstName, String surname, String address, String email, String username, String password, String Customer_type) {
        this.customerId = customerId;
        this.firstName = firstName;
        this.surname = surname;
        this.address = address;
        this.username = username;
        this.password = password;
        this.email = email;
        this.accounts = new ArrayList<>();
    }


    public boolean login(String enteredUsername, String enteredPassword) {
        return this.username.equals(enteredUsername) && this.password.equals(enteredPassword);
    }

    public boolean changePassword(String oldPassword, String newPassword) {
        if (this.password.equals(oldPassword)) {
            this.password = newPassword;
            System.out.println("Password changed successfully");
            return true;
        }
        System.out.println("Invalid old password");
        return false;
    }

    // Method to add an account
    public void addAccount(Account account) {
        if (account != null) {
            accounts.add(account);
            System.out.println("Account " + account.getAccountNumber() + " added for customer " + this.firstName);
        }
    }


    public ArrayList<Account> getAccounts() {
        return new ArrayList<>(accounts);
    }


    public abstract boolean canOpenChequeAccount();


    public String getCustomerId() {
        return customerId;
    }

    public String getCustomer_type() {
        return Customer_type;
    }

    public void setCustomer_type(String customer_type) {
        Customer_type = customer_type;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getSurname() {
        return surname;
    }

    public String getAddress() {
        return address;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getPassword() {
        return password;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
    }


    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "customerId='" + customerId + '\'' +
                ", firstName='" + firstName + '\'' +
                //  ", surname='" + surname + '\'' +
                ", address='" + address + '\'' +
                ", username='" + username + '\'' +
                ", numberOfAccounts=" + accounts.size() +
                '}';
    }

}