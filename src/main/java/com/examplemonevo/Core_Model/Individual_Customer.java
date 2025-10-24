package com.examplemonevo.Core_Model;

import com.examplemonevo.Core_Model.Customer;

public class Individual_Customer extends Customer {
    private String IdNumber;
    private String dateOfBirth;
    private String occupation;
    private String surname;


   
    public Individual_Customer(String customerId, String firstName, String surname, String address,String email, String username,String idNumber, String password,String CustomerType) {
        super(customerId, firstName,surname, address,email, username, password,"Individual");
        this.surname = surname;
        this.IdNumber = idNumber;
        this.dateOfBirth = dateOfBirth;
    }
    
    
   @Override
    public boolean canOpenChequeAccount() {
        return occupation != null && !occupation.trim().isEmpty();
    }
    
    public ChequeAccount openChequeAccount(String accountNumber, double initialDeposit,
                                         String branch, String employer, String companyAddress) {
      /*  if (!canOpenChequeAccount()) {
            System.out.println("Error: Employment information required for cheque account");
            return null;
        }
        ChequeAccount account = new ChequeAccount(accountNumber, initialDeposit, branch, this, employer, companyAddress);
        addAccount(account); 
        return account;*/
        return null;
    }
    
    // Getters and Setters
    public String getIdNumber() {
        return IdNumber;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getDateOfBirth() {
        return dateOfBirth; 
    }
    
    public String getOccupation() { 
        return occupation; 
    }
    
    public void setOccupation(String occupation) { 
        this.occupation = occupation; 
    }
    
    public void setIdNumber(String idNumber) {
        this.IdNumber = idNumber;

    }

    public void displayCustomerInfo() {
        System.out.println("Customer ID: " + getCustomerId());
        System.out.println("Name: " + getFirstName() + " " + getSurname());
        System.out.println("Address: " + getAddress());
        System.out.println("Username: " + getUsername());
        System.out.println("ID Number: " + IdNumber);
        System.out.println("Date of Birth: " + dateOfBirth);
        System.out.println("Occupation: " + (occupation != null ? occupation : "Not specified"));
        System.out.println("Number of Accounts: " + getAccounts().size());
    }
    
    @Override
    public String toString() {
        return "Individual_Customer{" +
                "customerId='" + getCustomerId() + '\'' +
                ", firstName='" + getFirstName() + '\'' +
                ", surname='" + getSurname() + '\'' +
                ", address='" + getAddress() + '\'' +
                ", username='" + getUsername() + '\'' +
                ", idNumber='" + IdNumber + '\'' +
                ", dateOfBirth='" + dateOfBirth + '\'' +
                ", occupation='" + occupation + '\'' +
                ", numberOfAccounts=" + getAccounts().size() +
                '}';
    }
}