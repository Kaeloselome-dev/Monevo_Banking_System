public class Corporate_Customer extends Customer {
    private String companyName;
    private String registrationNumber;
    private String businessType;
    private String contactPerson;
    
    public Corporate_Customer(String customerId, String companyName, String address,
                             String registrationNumber, String businessType,
                             String username, String password) {
        super(customerId, companyName, "", address, username, password);
        this.companyName = companyName;
        this.registrationNumber = registrationNumber;
        this.businessType = businessType;
    }
    
    @Override
    public boolean canOpenChequeAccount() {
        return true;
    }
    
    // Getters and Setters
    public String getCompanyName() { return companyName; }
    public String getRegistrationNumber() { return registrationNumber; }
    public String getBusinessType() { return businessType; }
    public String getContactPerson() { return contactPerson; }
    
    public void setContactPerson(String contactPerson) { this.contactPerson = contactPerson; }
    
    @Override
    public String toString() {
        return "Corporate_Customer{" +
                "customerId='" + getCustomerId() + '\'' +
                ", companyName='" + companyName + '\'' +
                ", address='" + getAddress() + '\'' +
                ", username='" + getUsername() + '\'' +
                ", registrationNumber='" + registrationNumber + '\'' +
                ", businessType='" + businessType + '\'' +
                ", contactPerson='" + contactPerson + '\'' +
                ", numberOfAccounts=" + getAccounts().size() +
                '}';
    }
}