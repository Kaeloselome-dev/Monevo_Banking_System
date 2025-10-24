package com.examplemonevo.Controllers;

import com.examplemonevo.Core_Model.Account;
import com.examplemonevo.Core_Model.Transaction;
import com.examplemonevo.DAOs.AccountDAOImpl;
import com.examplemonevo.DAOs.TransactionDAOImpl;
import com.examplemonevo.Utility.DB_Connection;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.UUID;

public class DepositController {

    @FXML
    private TextField depositAmountField;

    @FXML
    private Button proceedButton;

    @FXML
    private Button backButton;

    private Account account;
    private AccountDAOImpl accountDAO;
    private TransactionDAOImpl transactionDAO;

    // Constructor: Initializes DAOs for Account and Transaction
    public DepositController() {
        try {
            // Initialize DB connection and DAOs
            Connection connection = DB_Connection.getConnection();
            this.accountDAO = new AccountDAOImpl(connection);
            this.transactionDAO = new TransactionDAOImpl(connection);
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Database Error", "Failed to connect to the database.");
        }
    }

    // Set the account for this deposit transaction
    public void setAccount(Account account) {
        this.account = account;
    }

    // Initializes event handlers when the view is loaded
    @FXML
    public void initialize() {
        setEventHandlers();
    }

    // Helper method to set up event handlers
    private void setEventHandlers() {
        proceedButton.setOnAction(e -> handleDeposit());
        backButton.setOnAction(e -> goBackToDashboard());
    }

    // Handle the deposit process
    @FXML
    private void handleDeposit() {
        if (account == null) {
            showAlert("Error", "No account selected!");
            return;
        }

        String input = depositAmountField.getText().trim();
        if (input.isEmpty()) {
            showAlert("Invalid Input", "Please enter a deposit amount.");
            return;
        }

        try {
            // Convert input to double and check if valid
            double depositAmount = Double.parseDouble(input);
            if (depositAmount <= 0) {
                showAlert("Invalid Amount", "Amount must be greater than zero.");
                return;
            }

            // Update balance in the database
            double newBalance = account.getBalance() + depositAmount;
            accountDAO.updateAccountBalance(account.getAccountNumber(), newBalance);
            account.setBalance(newBalance);

            // Create a new transaction for this deposit
            Transaction depositTransaction = new Transaction(
                    UUID.randomUUID().toString(), // Generate unique transaction ID
                    account.getAccountNumber(),
                    depositAmount,
                    new Date(),
                    "Deposit",
                    "Deposit of " + depositAmount
            );

            transactionDAO.create(depositTransaction);

            // Success message to user
            showAlert("Success", "Deposit successful! New balance: " + newBalance);

            // Clear the deposit field after success
            depositAmountField.setText("");

            // Go back to the dashboard after success
            goBackToDashboard();

        } catch (NumberFormatException e) {
            showAlert("Invalid Input", "Please enter a valid numeric amount.");
        }
    }

    // Go back to the Dashboard view
    @FXML
    private void goBackToDashboard() {
        try {
            // Load the Dashboard view
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(getClass().getResource("/com/examplemonevo/Dashboard.fxml"));
            javafx.scene.Scene scene = new javafx.scene.Scene(loader.load());

            // Pass account data to the Dashboard controller
            com.examplemonevo.Controllers.DashboardController controller = loader.getController();
            controller.setAccount(account);

            // Set the scene for the stage
            Stage stage = (Stage) backButton.getScene().getWindow();
            stage.setScene(scene);
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Navigation Error", "Unable to go back to the dashboard.");
        }
    }

    // Utility method to show alerts
    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR); // Changed to ERROR for error cases
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
