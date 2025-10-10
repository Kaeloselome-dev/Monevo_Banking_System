public class SavingsAccount extends Account implements InterestBearing {
    private static final double INTEREST_RATE = 0.0005; 
    
    public SavingsAccount(String accountNumber, double initialDeposit, String branch, Customer customer) {
        super(accountNumber, initialDeposit, branch, customer);
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
    public void displayAccountInfo() {
        System.out.println("Account Number: " + getAccountNumber());
        System.out.println("Balance: BWP " + getBalance());
        System.out.println("Branch: " + getBranch());
        System.out.println("Customer: " + getCustomer().getFirstName() + " " + getCustomer().getSurname());
        System.out.println("Interest Rate: " + (INTEREST_RATE * 100) + "% monthly");
        System.out.println("Withdrawals Allowed: No");
    }

    @Override
    public boolean withdraw(double amount) {
        System.out.println("Error: Withdrawals are not allowed from Savings Account");
        return false;
    }

    public double getBalance() { return balance; 
    }
    
    @Override
    public void calculateInterest() {
        double interest = getBalance() * INTEREST_RATE;
        deposit(interest);
        System.out.println("Applied monthly interest: " + interest);
    }
    
    @Override
    public double getInterestRate() {
        return INTEREST_RATE;
    }
}