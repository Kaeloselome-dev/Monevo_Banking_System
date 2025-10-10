package com.examplemonevo;

public class Individual_Customer extends Customer {
    private String idNumber;  
    private String dateOfBirth;
    private String occupation;
    
   
    public Individual_Customer(String customerId, String firstName, String surname, 
                              String address, String username, String password,
                              String idNumber, String dateOfBirth) {
        super(customerId, firstName, surname, address, username, password);
        this.idNumber = idNumber;
        this.dateOfBirth = dateOfBirth;
    }
    
    
    @Override
    public boolean canOpenChequeAccount() {
        return occupation != null && !occupation.trim().isEmpty();
    }
    
    public ChequeAccount openChequeAccount(String accountNumber, double initialDeposit,
                                         String branch, String employer, String companyAddress) {
        if (!canOpenChequeAccount()) {
            System.out.println("Error: Employment information required for cheque account");
            return null;
        }
        ChequeAccount account = new ChequeAccount(accountNumber, initialDeposit, branch, this, employer, companyAddress);
        addAccount(account); 
        return account;
    }
    
    // Getters and Setters
    public String getIdNumber() { 
        return idNumber; 
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
        this.idNumber = idNumber; 
    }

    public void displayCustomerInfo() {
        System.out.println("Customer ID: " + getCustomerId());
        System.out.println("Name: " + getFirstName() + " " + getSurname());
        System.out.println("Address: " + getAddress());
        System.out.println("Username: " + getUsername());
        System.out.println("ID Number: " + idNumber);
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
                ", idNumber='" + idNumber + '\'' +
                ", dateOfBirth='" + dateOfBirth + '\'' +
                ", occupation='" + occupation + '\'' +
                ", numberOfAccounts=" + getAccounts().size() +
                '}';
    }
}