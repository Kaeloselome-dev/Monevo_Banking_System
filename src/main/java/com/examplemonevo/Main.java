package com.examplemonevo;

import java.sql.*;

/*public class Main {
    public static void main(String[] args) {
        try (Connection connection = com.examplemonevo.Utility.getConnection()) {
            com.examplemonevo.CustomerDAO customerDAO = new com.examplemonevo.CustomerDAOImpl(connection);
            com.examplemonevo.AccountDAO accountDAO = new com.examplemonevo.AccountDAOImpl(connection);

            // Create a new customer and account
            com.examplemonevo.Customer customer = new com.examplemonevo.Customer(1, "John", "Doe", "123 Main St");
            customerDAO.create(customer);

            com.examplemonevo.Account account = new com.examplemonevo.Account(12345, 1000.0, "Main Branch", "Savings");
            accountDAO.create(account);

            // Read a customer and account
            com.examplemonevo.Customer retrievedCustomer = customerDAO.read(1);
            com.examplemonevo.Account retrievedAccount = accountDAO.read(12345);

            System.out.println("Customer: " + retrievedCustomer.getFirstName() + " " + retrievedCustomer.getSurname());
            System.out.println("Account Balance: " + retrievedAccount.getBalance());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}*/

