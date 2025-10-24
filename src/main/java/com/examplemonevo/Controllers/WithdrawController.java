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

public class WithdrawController {

    @FXML
    private TextField withdrawAmountField;

    @FXML
    private Button proceedButton;

    @FXML
    private Button backButton;

    private Account account;
    private AccountDAOImpl accountDAO;
    private TransactionDAOImpl transactionDAO;

    // Constructor: Initializes DAOs for Account and Transaction
    public WithdrawController() {
        try {
            Connection connection = DB_Connection.getConnection();
            this.accountDAO = new AccountDAOImpl(connection);
            this.transactionDAO = new TransactionDAOImpl(connection);
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Database Error", "Failed to connect to the database.");
        }
    }

    // Set the account for this withdraw transaction
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
        proceedButton.setOnAction(e -> handleWithdraw());
        backButton.setOnAction(e -> goBackToDashboard());
    }

    // Handle the withdrawal process
    @FXML
    private void handleWithdraw() {
        if (account == null) {
            showAlert("Error", "No account selected!");
            return;
        }

        // Get the account type from the database
        String accountType = accountDAO.getAccountType(account.getAccountNumber());
        if (accountType == null) {
            showAlert("Error", "Account not found!");
            return;
        }

        // Prevent withdrawals from Savings accounts
        if (accountType.equalsIgnoreCase("Savings Account")) {
            showAlert("Invalid Operation", "Withdrawals are not allowed from Savings accounts.");
            return;
        }


        String input = withdrawAmountField.getText().trim();
        if (input.isEmpty()) {
            showAlert("Invalid Input", "Please enter a withdrawal amount.");
            return;
        }

        try {
            double withdrawAmount = Double.parseDouble(input);
            if (withdrawAmount <= 0) {
                showAlert("Invalid Amount", "Amount must be greater than zero.");
                return;
            }

            if (withdrawAmount > account.getBalance()) {
                showAlert("Insufficient Funds", "You don't have enough balance for this withdrawal.");
                return;
            }

            // Update balance in the database
            double newBalance = account.getBalance() - withdrawAmount;
            accountDAO.updateAccountBalance(account.getAccountNumber(), newBalance);
            account.setBalance(newBalance);

            // Create a new transaction for this withdrawal
            Transaction withdrawTransaction = new Transaction(
                    UUID.randomUUID().toString(),
                    account.getAccountNumber(),
                    withdrawAmount,
                    new Date(), // ensures TransactionDate is NOT NULL
                    "Withdrawal",
                    "Withdrawal of " + withdrawAmount
            );

            transactionDAO.create(withdrawTransaction);

            showSuccessAlert("Success", "Withdrawal successful! New balance: " + newBalance);

            // Clear the field and return to dashboard
            withdrawAmountField.setText("");
            goBackToDashboard();

        } catch (NumberFormatException e) {
            showAlert("Invalid Input", "Please enter a valid numeric amount.");
        }
    }

    // Go back to the Dashboard view
    @FXML
    private void goBackToDashboard() {
        try {
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(getClass().getResource("/com/examplemonevo/Dashboard.fxml"));
            javafx.scene.Scene scene = new javafx.scene.Scene(loader.load());

            com.examplemonevo.Controllers.DashboardController controller = loader.getController();
            controller.setAccount(account);

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
        Alert.AlertType type = title.equalsIgnoreCase("Success") ? Alert.AlertType.INFORMATION : Alert.AlertType.ERROR;
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void showSuccessAlert(String title,String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION); // INFORMATION for success
        alert.setTitle("Success");
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
