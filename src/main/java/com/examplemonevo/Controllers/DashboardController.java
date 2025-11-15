package com.examplemonevo.Controllers;

import com.examplemonevo.Core_Model.*;
import com.examplemonevo.DAOs.AccountDAOImpl;
import com.examplemonevo.DAOs.TransactionDAOImpl;
import com.examplemonevo.Utility.DB_Connection;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.time.LocalDate;
import java.util.Date;
import java.util.UUID;

public class DashboardController {

    @FXML private Button logoutButton;
    @FXML private Button OpenAccountbtn;
    @FXML private Button withdrawMaxButton;
    @FXML private Button viewInfoButton;
    @FXML private Button depositButton;
    @FXML private Button withdrawButton;
    @FXML private Button historyButton;
    @FXML private Button Applyinterestbtn;
    @FXML private Text balanceText;
    @FXML private AnchorPane historyContainer;
    @FXML private Text accountTypeText;

    private Customer customer;
    private Account selectedAccount;
    private AccountDAOImpl accountDAO;
    private TransactionDAOImpl transactionDAO;
    private Account account;


    @FXML
    public void initialize() {
        try {
            Connection connection = DB_Connection.getConnection();
            accountDAO = new AccountDAOImpl(connection);
            transactionDAO = new TransactionDAOImpl(connection);


            depositButton.setOnAction(e -> openDepositPage());
            withdrawButton.setOnAction(e -> openWithdrawPage());
            historyButton.setOnAction(e -> openHistoryPage());
            logoutButton.setOnAction(e -> handleLogout());
            withdrawMaxButton.setOnAction(e -> handleWithdrawMax());
            viewInfoButton.setOnAction(e -> handleViewInfo());
            OpenAccountbtn.setOnAction(e -> OpenAccount()); // Assuming OpenAccountbtn calls OpenAccount()

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Database connection failed.");
        }
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public void setAccount(Account account) {
        this.selectedAccount = account;
        updateDashboard();
    }

    private void updateDashboard() {
        if (selectedAccount != null) {
            balanceText.setText(String.format("%.2f", selectedAccount.getBalance()));
            accountTypeText.setText(selectedAccount.getClass().getSimpleName());
            historyContainer.getChildren().clear(); // placeholder for transactions
        }
    }

    @FXML
    private void openDepositPage() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/examplemonevo/deposit.fxml"));
            Scene scene = new Scene(loader.load());

            DepositController controller = loader.getController();
            controller.setAccount(selectedAccount);

            Stage stage = (Stage) depositButton.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to open Deposit page.");
        }
    }

    @FXML
    private void openWithdrawPage() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/examplemonevo/Withdraw.fxml"));
            Scene scene = new Scene(loader.load());

            WithdrawController controller = loader.getController();
            controller.setAccount(selectedAccount);

            Stage stage = (Stage) withdrawButton.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to open Withdraw page.");
        }
    }

    @FXML
    private void openHistoryPage() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/examplemonevo/TransactionHistory.fxml"));
            Scene scene = new Scene(loader.load());

            TransactionHistoryController controller = loader.getController();
            controller.setAccount(selectedAccount);

            Stage stage = (Stage) historyButton.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to open Transaction History page.");
        }
    }

    @FXML
    private void handleApplyInterest() {
        if (selectedAccount == null) {
            showAlert("Error", "No account selected!");
            return;
        }

        LocalDate today = LocalDate.now();

        try {
            if (selectedAccount instanceof SavingsAccount sa) {
                if (sa.getLastInterestApplied() != null && sa.getLastInterestApplied().getMonth() == today.getMonth()) {
                    showAlert("Info", "Interest can only be applied once a month.");
                    return;
                }

                double interest = sa.getBalance() * sa.getInterestRate();
                double newBalance = sa.getBalance() + interest;

                // Round to 2 decimal places
                BigDecimal roundedBalance = BigDecimal.valueOf(newBalance).setScale(2, RoundingMode.HALF_UP);
                sa.setBalance(roundedBalance.doubleValue());
                sa.setLastInterestApplied(today);

                // Create a transaction for this interest
                Transaction interestTransaction = new Transaction(
                        UUID.randomUUID().toString(),
                        sa.getAccountNumber(),
                        interest,
                        new Date(),
                        "Interest",
                        "Interest applied: " + BigDecimal.valueOf(interest).setScale(2, RoundingMode.HALF_UP)
                );

                transactionDAO.create(interestTransaction); // Save transaction in DB

                balanceText.setText(roundedBalance.toString());
                showAlert("Interest Applied", "New balance for Savings Account: " + roundedBalance);

            } else if (selectedAccount instanceof InvestmentAccount ia) {
                if (ia.getLastInterestApplied() != null && ia.getLastInterestApplied().getMonth() == today.getMonth()) {
                    showAlert("Info", "Interest can only be applied once a month.");
                    return;
                }

                double interest = ia.getBalance() * ia.getInterestRate();
                double newBalance = ia.getBalance() + interest;

                // Round to 2 decimal places
                BigDecimal roundedBalance = BigDecimal.valueOf(newBalance).setScale(2, RoundingMode.HALF_UP);
                ia.setBalance(roundedBalance.doubleValue());
                ia.setLastInterestApplied(today);

                // Create a transaction for this interest
                Transaction interestTransaction = new Transaction(
                        UUID.randomUUID().toString(),
                        ia.getAccountNumber(),
                        interest,
                        new Date(),
                        "Interest",
                        "Interest applied: " + BigDecimal.valueOf(interest).setScale(2, RoundingMode.HALF_UP)
                );

                transactionDAO.create(interestTransaction); // Save transaction in DB

                balanceText.setText(roundedBalance.toString());
                showAlert("Interest Applied", "New balance for Investment Account: " + roundedBalance);

            } else if (selectedAccount instanceof ChequeAccount) {
                showAlert("Info", "Cheque Account cannot apply interest.");
            } else {
                showAlert("Info", "This account type does not earn interest.");
            }

            // Update account in DB
            accountDAO.update(selectedAccount);

        } catch (Exception e) {
            showAlert("Error", "Failed to apply interest: " + e.getMessage());
            e.printStackTrace();
        }
    }




    @FXML
    private void handleLogout() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/examplemonevo/Login.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = (Stage) logoutButton.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to logout.");
        }
    }


    @FXML
    private void OpenAccount() {
        try {
            // 1. Load the FXML using an instance of FXMLLoader
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/examplemonevo/AccountCreationSelection.fxml"));
            Scene scene = new Scene(loader.load());

            // 2. Get the controller instance for the new scene
            AccountCreationController controller = loader.getController();

            // 3. Set the previous page identifier
            // "Dashboard" MUST match the string used in AccountCreationController's handleBack method.
            controller.setPreviousPageIdentifier("Dashboard");

            // 4. Switch the scene
            Stage stage = (Stage) OpenAccountbtn.getScene().getWindow(); // Use OpenAccountbtn to get stage
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to open Account Creation Selection.");
        }
    }

    @FXML
    public void handleViewInfo() {
        if (selectedAccount != null) {
            String message = "Account Number: " + selectedAccount.getAccountNumber() +
                    "\nType: " + selectedAccount.getClass().getSimpleName() +
                    "\nBalance: " + selectedAccount.getBalance();

            if (selectedAccount instanceof SavingsAccount sa) {
                message += "\nInterest Rate: " + sa.getInterestRate();
            } else if (selectedAccount instanceof InvestmentAccount ia) {
                message += "\nInterest Rate: " + ia.getInterestRate();
            }

            showAlert("Account Info", message);
        }
    }


    @FXML
    public void handleWithdrawMax() {
        if (selectedAccount != null) {
            // Check if the selected account is a Savings account
            if (selectedAccount.getAccountType().equals("Savings Account")) {
                // Show alert if attempting to withdraw from a Savings account
                showAlert("Withdrawal Denied", "Withdrawals are not allowed from Savings accounts.");
            } else {
                // Proceed with the withdrawal if it's not a Savings account
                double amount = selectedAccount.getBalance();
                selectedAccount.setBalance(0.0); // Withdraw the full balance
                accountDAO.update(selectedAccount); // Update the account in the database
                updateDashboard(); // Refresh the dashboard
                showAlert("Withdraw Max", "Withdrawn full balance: " + amount);
            }
        }
    }

    // Utility
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}