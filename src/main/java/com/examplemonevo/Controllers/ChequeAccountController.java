package com.examplemonevo.Controllers;

import com.examplemonevo.Core_Model.ChequeAccount;
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
import java.sql.SQLException;
import java.io.IOException;

public class ChequeAccountController {

    @FXML private TextField initialDepositField;
    @FXML private TextField branchField;
    @FXML private TextField employerField;
    @FXML private TextField companyAddressField;
    @FXML private TextField customerIdField;

    private final AccountDAOImpl accountDAO;
    private final CustomerDAOImpl customerDAO;

    public ChequeAccountController() throws SQLException {
        Connection connection = DB_Connection.getConnection();
        this.accountDAO = new AccountDAOImpl(connection);
        this.customerDAO = new CustomerDAOImpl(connection);
    }

    @FXML
    private void handleCreateChequeAccount() {
        try {

            String customerId = customerIdField.getText().trim();
            String branch = branchField.getText().trim();
            String employer = employerField.getText().trim();
            String companyAddress = companyAddressField.getText().trim();

            if (customerId.isEmpty() || branch.isEmpty() || initialDepositField.getText().isEmpty()) {
                showAlert("Invalid Input", "Please provide all required fields with valid values.");
                return;
            }

            double initialDeposit = Double.parseDouble(initialDepositField.getText());
            if (initialDeposit < 0) {
                showAlert("Invalid Input", "Initial deposit cannot be negative.");
                return;
            }


            Customer customer = customerDAO.read(customerId);
            if (customer == null) {
                showAlert("Customer Not Found", "No customer found with the given ID.");
                return;
            }


            ChequeAccount chequeAccount = new ChequeAccount(
                    "",  // Account number auto-generated
                    initialDeposit,
                    branch,
                    customer,
                    "Cheque Account",
                    employer,
                    companyAddress
            );


            accountDAO.create(chequeAccount);


            showAlert("Success", "Cheque Account created successfully!");


            redirectToWelcome();

        } catch (NumberFormatException e) {
            showAlert("Invalid Input", "Please enter a valid number for the initial deposit.");
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "An unexpected error occurred while creating the Cheque Account.");
        }
    }

    private void redirectToWelcome() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/examplemonevo/Welcome.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = (Stage) initialDepositField.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            showAlert("Navigation Error", "Failed to load the welcome screen.");
            e.printStackTrace();
        }
    }

    @FXML
    private void handleProceed() {
        handleCreateChequeAccount();
    }

    @FXML
    private void handleBack() {
        try {
            Stage stage = (Stage) initialDepositField.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/examplemonevo/AccountCreationSelection.fxml"));
            Scene scene = new Scene(loader.load());
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            showAlert("Navigation Error", "Failed to go back to the previous page.");
            e.printStackTrace();
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
