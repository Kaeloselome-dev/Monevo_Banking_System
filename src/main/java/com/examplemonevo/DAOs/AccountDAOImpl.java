package com.examplemonevo.DAOs;

import com.examplemonevo.Core_Model.*;
import com.examplemonevo.Interfaces.AccountDAO;
import com.examplemonevo.Utility.DB_Connection;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class AccountDAOImpl implements AccountDAO {
    private Connection connection;

    public AccountDAOImpl(Connection connection) {
        this.connection = connection;
    }


    private String generateAccountNumber(String accountType) throws SQLException {
        String prefix;

        switch (accountType) {
            case "Cheque Account":
                prefix = "CA";
                break;
            case "Savings Account":
                prefix = "SA";
                break;
            case "Investment Account":
                prefix = "IA";
                break;
            default:
                throw new IllegalArgumentException("Invalid account type: " + accountType);
        }

        String sql = "SELECT COUNT(*) FROM Accounts WHERE accountType = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, accountType);
            ResultSet rs = statement.executeQuery();
            int count = rs.next() ? rs.getInt(1) + 1 : 1;
            return prefix + String.format("%03d", count);
        }
    }

    @Override
    public void create(Account account) {
        String sql;
        try {
            String accountNumber = generateAccountNumber(account.getAccountType());

            if (account instanceof ChequeAccount) {
                sql = "INSERT INTO Accounts (AccountNumber, CustomerID, AccountType, Branch, Employer, CompanyAddress, Balance) VALUES (?, ?, ?, ?, ?, ?, ?)";
                try (PreparedStatement statement = connection.prepareStatement(sql)) {
                    statement.setString(1, accountNumber);
                    statement.setInt(2, Integer.parseInt(account.getCustomer().getCustomerId()));
                    statement.setString(3, "Cheque Account");
                    statement.setString(4, account.getBranch());
                    statement.setString(5, ((ChequeAccount) account).getEmployer());
                    statement.setString(6, ((ChequeAccount) account).getCompanyAddress());
                    statement.setDouble(7, account.getBalance());
                    statement.executeUpdate();
                }

            } else if (account instanceof SavingsAccount) {
                sql = "INSERT INTO Accounts (AccountNumber, Balance, Branch, CustomerID, AccountType) VALUES (?, ?, ?, ?, ?)";
                try (PreparedStatement statement = connection.prepareStatement(sql)) {
                    statement.setString(1, accountNumber);
                    statement.setDouble(2, account.getBalance());
                    statement.setString(3, account.getBranch());
                    statement.setInt(4, Integer.parseInt(account.getCustomer().getCustomerId()));
                    statement.setString(5, "Savings Account");
                    statement.executeUpdate();
                }

            } else if (account instanceof InvestmentAccount) {
                sql = "INSERT INTO Accounts (AccountNumber, Balance, Branch, CustomerID, AccountType, InterestRate) VALUES (?, ?, ?, ?, ?, ?)";
                try (PreparedStatement statement = connection.prepareStatement(sql)) {
                    statement.setString(1, accountNumber);
                    statement.setDouble(2, account.getBalance());
                    statement.setString(3, account.getBranch());
                    statement.setInt(4, Integer.parseInt(account.getCustomer().getCustomerId()));
                    statement.setString(5, "Investment Account");
                    statement.setDouble(6, ((InvestmentAccount) account).getInterestRate());
                    statement.executeUpdate();
                }
            }

            System.out.println("Account created successfully: " + accountNumber);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    @Override
    public Account read(String accountNumber) {
        String sql = "SELECT * FROM Accounts INNER JOIN Customers ON Accounts.CustomerID = Customers.CustomerID WHERE AccountNumber = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, accountNumber);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    String type = resultSet.getString("AccountType");
                    String branch = resultSet.getString("Branch");
                    double balance = resultSet.getDouble("Balance");
                    String customerType = resultSet.getString("CustomerType");

                    Customer customer;
                    if ("Individual".equalsIgnoreCase(customerType)) {
                        customer = new Individual_Customer(
                                resultSet.getString("CustomerId"),
                                resultSet.getString("FirstName"),
                                resultSet.getString("Surname"),
                                resultSet.getString("Address"),
                                resultSet.getString("Email"),
                                resultSet.getString("Username"),
                                resultSet.getString("IDNumber"), // still exists in Customers table
                                resultSet.getString("Password"),
                                resultSet.getString("CustomerType")
                        );

                    } else if ("Corporate".equalsIgnoreCase(customerType)) {
                        customer = new Corporate_Customer(
                                resultSet.getString("CustomerId"),
                                resultSet.getString("CompanyName"),
                                resultSet.getString("Address"),
                                resultSet.getString("Email"),
                                resultSet.getString("Username"),
                                resultSet.getString("Password"),
                                resultSet.getString("RegistrationNumber"),
                                resultSet.getString("CompanyType"),
                                resultSet.getString("CustomerType")
                        );
                    } else {
                        throw new IllegalArgumentException("Unknown customer type: " + customerType);
                    }

                    switch (type) {
                        case "Cheque Account":
                            return new ChequeAccount(
                                    accountNumber,
                                    balance,
                                    branch,
                                    customer,
                                    type,
                                    resultSet.getString("Employer"),
                                    resultSet.getString("CompanyAddress")
                            );

                        case "Savings Account":
                            return new SavingsAccount(accountNumber, balance, branch, customer);

                        case "Investment Account":
                            return new InvestmentAccount(accountNumber, balance, branch, customer);

                        default:
                            System.err.println("Unknown account type: " + type);
                            return null;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void update(Account account) {
        String sql = "UPDATE Accounts SET Balance = ?, Branch = ?, Employer = ?, CompanyAddress = ?, InterestRate = ? WHERE AccountNumber = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setDouble(1, account.getBalance());
            statement.setString(2, account.getBranch());

            if (account instanceof ChequeAccount) {
                statement.setString(3, ((ChequeAccount) account).getEmployer());
                statement.setString(4, ((ChequeAccount) account).getCompanyAddress());
                statement.setNull(5, Types.DOUBLE);
            } else if (account instanceof InvestmentAccount) {
                statement.setNull(3, Types.VARCHAR);
                statement.setNull(4, Types.VARCHAR);
                statement.setDouble(5, ((InvestmentAccount) account).getInterestRate());
            } else {
                statement.setNull(3, Types.VARCHAR);
                statement.setNull(4, Types.VARCHAR);
                statement.setNull(5, Types.DOUBLE);
            }

            statement.setString(6, account.getAccountNumber());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(String accountNumber) {
        String sql = "DELETE FROM Accounts WHERE AccountNumber = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, accountNumber);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public ArrayList<Account> getAllAccounts() {
        ArrayList<Account> accounts = new ArrayList<>();
        String sql = "SELECT * FROM Accounts INNER JOIN Customers ON Accounts.CustomerID = Customers.CustomerID";
        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                String type = resultSet.getString("AccountType");
                String accountNumber = resultSet.getString("AccountNumber");
                double balance = resultSet.getDouble("Balance");
                String branch = resultSet.getString("Branch");
                String customerType = resultSet.getString("CustomerType");

                Customer customer;
                if ("Individual".equalsIgnoreCase(customerType)) {
                    customer = new Individual_Customer(
                            resultSet.getString("CustomerId"),
                            resultSet.getString("FirstName"),
                            resultSet.getString("Surname"),
                            resultSet.getString("Address"),
                            resultSet.getString("Email"),
                            resultSet.getString("Username"),
                            resultSet.getString("IDNumber"),
                            resultSet.getString("Password"),
                            resultSet.getString("CustomerType")
                    );
                } else if ("Corporate".equalsIgnoreCase(customerType)) {
                    customer = new Corporate_Customer(
                            resultSet.getString("CustomerId"),
                            resultSet.getString("CompanyName"),
                            resultSet.getString("Address"),
                            resultSet.getString("Email"),
                            resultSet.getString("Username"),
                            resultSet.getString("Password"),
                            resultSet.getString("RegistrationNumber"),
                            resultSet.getString("CompanyType"),
                            resultSet.getString("CustomerType")
                    );
                } else {
                    continue;
                }

                switch (type) {
                    case "Cheque Account":
                        accounts.add(new ChequeAccount(accountNumber, balance, branch, customer, type,
                                resultSet.getString("Employer"), resultSet.getString("CompanyAddress")));
                        break;

                    case "Savings Account":
                        accounts.add(new SavingsAccount(accountNumber, balance, branch, customer));
                        break;

                    case "Investment Account":
                        accounts.add(new InvestmentAccount(accountNumber, balance, branch, customer));
                        break;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return accounts;
    }

    public ArrayList<Account> getAccountsByCustomerId(String customerId) {
        ArrayList<Account> accounts = new ArrayList<>();
        String sql = "SELECT * FROM Accounts INNER JOIN Customers ON Accounts.CustomerID = Customers.CustomerID WHERE Accounts.CustomerID = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, Integer.parseInt(customerId));
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    String type = resultSet.getString("AccountType");
                    String accountNumber = resultSet.getString("AccountNumber");
                    double balance = resultSet.getDouble("Balance");
                    String branch = resultSet.getString("Branch");
                    String customerType = resultSet.getString("CustomerType");

                    Customer customer;
                    if ("Individual".equalsIgnoreCase(customerType)) {
                        customer = new Individual_Customer(
                                resultSet.getString("CustomerId"),
                                resultSet.getString("FirstName"),
                                resultSet.getString("Surname"),
                                resultSet.getString("Address"),
                                resultSet.getString("Email"),
                                resultSet.getString("Username"),
                                resultSet.getString("IDNumber"),
                                resultSet.getString("Password"),
                                resultSet.getString("CustomerType")
                        );
                    } else if ("Corporate".equalsIgnoreCase(customerType)) {
                        customer = new Corporate_Customer(
                                resultSet.getString("CustomerId"),
                                resultSet.getString("CompanyName"),
                                resultSet.getString("Address"),
                                resultSet.getString("Email"),
                                resultSet.getString("Username"),
                                resultSet.getString("Password"),
                                resultSet.getString("RegistrationNumber"),
                                resultSet.getString("CompanyType"),
                                resultSet.getString("CustomerType")
                        );
                    } else {
                        continue;
                    }

                    switch (type) {
                        case "Cheque Account":
                            accounts.add(new ChequeAccount(accountNumber, balance, branch, customer, type,
                                    resultSet.getString("Employer"), resultSet.getString("CompanyAddress")));
                            break;
                        case "Savings Account":
                            accounts.add(new SavingsAccount(accountNumber, balance, branch, customer));
                            break;
                        case "Investment Account":
                            accounts.add(new InvestmentAccount(accountNumber, balance, branch, customer));
                            break;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return accounts;
    }

    public void updateAccountBalance(String accountNumber, double newBalance) {
        String sql = "UPDATE Accounts SET Balance = ? WHERE AccountNumber = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setDouble(1, newBalance);
            statement.setString(2, accountNumber);
            statement.executeUpdate();
            System.out.println("Updated balance for " + accountNumber + " to " + newBalance);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    public void applyMonthlyInterest() {
        String updateSQL = "UPDATE accounts " +
                "SET balance = balance + (balance * ?) " +
                "WHERE LOWER(TRIM(accountType)) = ?";

        double savingsRate = 0.0005;
        double investmentRate = 0.05;

        int updatedSavings = 0;
        int updatedInvestment = 0;

        try (
                PreparedStatement ps = connection.prepareStatement(updateSQL);
                PreparedStatement txPs = connection.prepareStatement(
                        "INSERT INTO transactions (AccountNumber, Amount, TransactionDate, TransactionType, Description) " +
                                "VALUES (?, ?, ?, ?, ?)"
                )
        ) {
            // Apply interest to Savings accounts
            ps.setDouble(1, savingsRate);
            ps.setString(2, "savings");
            updatedSavings = ps.executeUpdate();

            // Record transactions for Savings accounts
            recordInterestTransactions("Savings", savingsRate, txPs);

            // Apply interest to Investment accounts
            ps.setDouble(1, investmentRate);
            ps.setString(2, "investment");
            updatedInvestment = ps.executeUpdate();

            // Record transactions for Investment accounts
            recordInterestTransactions("Investment", investmentRate, txPs);

            System.out.println("Updated " + updatedSavings + " Savings accounts with interest.");
            System.out.println("Updated " + updatedInvestment + " Investment accounts with interest.");
            System.out.println("Interest applied to all applicable accounts!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void recordInterestTransactions(String type, double rate, PreparedStatement txPs) throws SQLException {
        String selectAccounts = "SELECT AccountNumber, balance FROM accounts WHERE LOWER(TRIM(accountType)) = ?";
        try (PreparedStatement selectPs = connection.prepareStatement(selectAccounts)) {
            selectPs.setString(1, type.toLowerCase());
            ResultSet rs = selectPs.executeQuery();

            while (rs.next()) {
                String accNum = rs.getString("AccountNumber");
                double amount = rs.getDouble("balance") * rate;

                txPs.setString(1, accNum);
                txPs.setDouble(2, amount);
                txPs.setString(3, LocalDateTime.now().toString());
                txPs.setString(4, "Interest");
                txPs.setString(5, type + " monthly interest applied");
                txPs.executeUpdate();
            }
        }
    }

    public String getAccountType(String accountNumber) {
        String sql = "SELECT AccountType FROM Accounts WHERE AccountNumber = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, accountNumber);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("AccountType"); // returns "Cheque Account", "Savings Account", etc.
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // if account not found
    }






}
