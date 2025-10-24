package com.examplemonevo.Controllers;

import com.examplemonevo.Core_Model.SavingsAccount;
import com.examplemonevo.Core_Model.Customer;
import com.examplemonevo.DAOs.AccountDAOImpl;
import com.examplemonevo.DAOs.CustomerDAOImpl;
import com.examplemonevo.Utility.DB_Connection;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;

import java.sql.Connection;

public class SavingsAccountController {

    @FXML private TextField initialDepositField;
    @FXML private TextField branchField;
    @FXML private TextField customerIdField;

    private AccountDAOImpl accountDAO;
    private CustomerDAOImpl customerDAO;

    @FXML
    public void initialize() {
        try {
            Connection connection = DB_Connection.getConnection();
            accountDAO = new AccountDAOImpl(connection);
            customerDAO = new CustomerDAOImpl(connection);
        } catch (Exception e) {
            showAlert("Error", "Database connection failed.");
            e.printStackTrace();
        }
    }

    @FXML
    private void handleCreateSavingsAccount() {
        try {
            String depositText = initialDepositField.getText().trim();
            String branch = branchField.getText().trim();
            String customerId = customerIdField.getText().trim();

            if (depositText.isEmpty() || branch.isEmpty() || customerId.isEmpty()) {
                showAlert("Invalid Input", "Please fill all required fields.");
                return;
            }

            double initialDeposit = Double.parseDouble(depositText);
            if (initialDeposit < 500) {
                showAlert("Invalid Input", "Deposit must be greater than P500.");
                return;
            }

            Customer customer = customerDAO.read(customerId);
            if (customer == null) {
                showAlert("Customer Not Found", "No customer found with the given ID.");
                return;
            }

            SavingsAccount savingsAccount = new SavingsAccount(
                    "", initialDeposit, branch, customer
            );

            accountDAO.create(savingsAccount);
            showAlert("Success", "Savings Account created successfully!");
            clearFields();

            Stage stage = (Stage) initialDepositField.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/examplemonevo/Login.fxml"));
            Scene scene = new Scene(loader.load());
            stage.setScene(scene);
            stage.show();

        } catch (NumberFormatException e) {
            showAlert("Invalid Input", "Please enter a valid numeric deposit.");
        } catch (Exception e) {
            showAlert("Error", "An error occurred while creating the Savings Account.");
            e.printStackTrace();
        }
    }

    @FXML
    private void handleProceed() {
        handleCreateSavingsAccount();
    }

    @FXML
    private void handleBack() {
        try {
            Stage stage = (Stage) initialDepositField.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/examplemonevo/AccountCreationSelection.fxml"));
            Scene scene = new Scene(loader.load());
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            showAlert("Error", "Failed to go back to the previous page.");
            e.printStackTrace();
        }
    }

    private void clearFields() {
        initialDepositField.clear();
        branchField.clear();
        customerIdField.clear();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
