package com.examplemonevo.DAOs;

import com.examplemonevo.Core_Model.Customer;
import com.examplemonevo.Core_Model.*;
import com.examplemonevo.Core_Model.Individual_Customer;
import com.examplemonevo.Core_Model.Corporate_Customer;
import com.examplemonevo.Interfaces.CustomerDAO;

import java.sql.*;
import java.util.ArrayList;


public class CustomerDAOImpl implements CustomerDAO {
    private Connection connection;

    public CustomerDAOImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void create(Customer customer) {
        String sql;
        if (customer instanceof Individual_Customer) {
            sql = "INSERT INTO customers (FirstName, Surname, Address, Email, Username,IDNumber, Password,CustomerType) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                statement.setString(1, customer.getFirstName());
                statement.setString(2, customer.getSurname());
                statement.setString(3, customer.getAddress());
                statement.setString(4, customer.getEmail());
                statement.setString(5, customer.getUsername());
                statement.setString(6, ((Individual_Customer) customer).getIdNumber());
                statement.setString(7, customer.getPassword());
                statement.setString(8,"Individual"); // Set CustomerType at index 8




                statement.executeUpdate();

                // Retrieve the generated customerId
                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int customerId = generatedKeys.getInt(1); // Get the generated ID
                        customer.setCustomerId(String.valueOf(customerId)); // Set the customerId for the customer object
                    }
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else if (customer instanceof Corporate_Customer) {
            Corporate_Customer businessCustomer = (Corporate_Customer) customer;
            sql = "INSERT INTO customers (CompanyName, Address, Email, RegistrationNumber, CompanyType, Username, Password, CustomerType) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                statement.setString(1, ((Corporate_Customer) customer).getCompanyName());
                statement.setString(2, customer.getAddress());
                statement.setString(3, customer.getEmail());
                statement.setString(4, ((Corporate_Customer) customer).getRegistrationNumber());
                statement.setString(5, ((Corporate_Customer) customer).getBusinessType());
                statement.setString(6, customer.getUsername());
                statement.setString(7, customer.getPassword());
                statement.setString(8, "Corporate");

                statement.executeUpdate();


                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int customerId = generatedKeys.getInt(1); // Get the generated ID
                        customer.setCustomerId(String.valueOf(customerId)); // Set the customerId for the customer object
                    }
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }



    @Override
    public Customer read(String customerId) {
        String sql = "SELECT * FROM customers WHERE CustomerID = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, customerId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    // Assuming there's a 'customerType' field to distinguish between individual and business customers
                    String customerType = resultSet.getString("customerType");

                    if ("Individual".equals(customerType)) {
                        return new Individual_Customer(
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


                    } else if ("Corporate".equals(customerType)) {
                        return new Corporate_Customer(
                                resultSet.getString("CustomerId"),
                                resultSet.getString("FirstName"),
                                resultSet.getString("Surname"),
                                resultSet.getString("Address"),
                                resultSet.getString("Email"),
                                resultSet.getString("Username"),
                                resultSet.getString("Password"),
                                resultSet.getString("CompanyName"),
                                resultSet.getString("CustomerType")
                        );
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    @Override
    public void update(Customer customer) {
        String sql = "UPDATE customers SET firstName = ?, surname = ?, address = ?, username = ?, password = ? WHERE customerId = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, customer.getFirstName());
            statement.setString(2, customer.getSurname());
            statement.setString(3, customer.getAddress());
            statement.setString(4, customer.getUsername());
            statement.setString(5, customer.getPassword());
            statement.setString(6, customer.getCustomerId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(String customerId) {
        String sql = "DELETE FROM customers WHERE customerId = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, customerId);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    @Override
    public ArrayList<Customer> getAllCustomers() {
        ArrayList<Customer> customers = new ArrayList<>();
        String sql = "SELECT * FROM customers";
        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                String customerType = resultSet.getString("customerType");

                if ("Individual".equals(customerType)) {
                    customers.add(new Individual_Customer(
                            resultSet.getString("CustomerId"),
                            resultSet.getString("FirstName"),
                            resultSet.getString("Surname"),
                            resultSet.getString("Address"),
                            resultSet.getString("Email"),
                            resultSet.getString("Username"),
                            resultSet.getString("IDNumber"),
                            resultSet.getString("Password"),
                            resultSet.getString("CustomerType")
                    ));
                } else if ("Business".equals(customerType)) {
                    customers.add(new Corporate_Customer(
                            resultSet.getString("CustomerId"),
                            resultSet.getString("FirstName"),
                            resultSet.getString("Surname"),
                            resultSet.getString("Address"),
                            resultSet.getString("Email"),
                            resultSet.getString("Username"),
                            resultSet.getString("Password"),
                            resultSet.getString("CompanyName"),
                            resultSet.getString("CustomerType")
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return customers;
    }


    @Override
    public Customer login(String username, String password) {
        String sql = "SELECT * FROM customers WHERE username = ? AND password = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, username);
            statement.setString(2, password);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    String customerType = resultSet.getString("CustomerType");
                    System.out.println("Raw type: [" + customerType + "]");

                    if (customerType == null) {
                        System.err.println("CustomerType is null for username: " + username);
                        return null;
                    }

                    String typeNormalized = customerType.trim().toLowerCase();
                    System.out.println("Normalized type: [" + typeNormalized + "]");

                    switch (typeNormalized) {
                        case "individual":
                            return new Individual_Customer(
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

                        case "corporate":
                        case "corparate":
                        case "business":
                            return new Corporate_Customer(
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

                        default:
                            System.err.println("Unknown customer type: " + customerType);
                            return null;
                    }
                } else {
                    System.out.println("No user found with the given username and password.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }





}