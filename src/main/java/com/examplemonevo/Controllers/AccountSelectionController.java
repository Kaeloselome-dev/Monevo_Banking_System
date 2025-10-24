package com.examplemonevo.Controllers;

import com.examplemonevo.Core_Model.*;
import com.examplemonevo.DAOs.AccountDAOImpl;
import com.examplemonevo.Utility.DB_Connection;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.sql.Connection;
import java.util.ArrayList;

public class AccountSelectionController {

    @FXML private Button chequeAccountButton;
    @FXML private Button savingsAccountButton;
    @FXML private Button investmentAccountButton;
    @FXML private Button backButton;

    private Customer customer;
    private AccountDAOImpl accountDAO;

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    @FXML
    public void initialize() {
        try {
            Connection connection = DB_Connection.getConnection();
            accountDAO = new AccountDAOImpl(connection);

            // Attach button actions using account class instead of string
            chequeAccountButton.setOnAction(e -> handleAccountSelection(ChequeAccount.class));
            savingsAccountButton.setOnAction(e -> handleAccountSelection(SavingsAccount.class));
            investmentAccountButton.setOnAction(e -> handleAccountSelection(InvestmentAccount.class));
            backButton.setOnAction(e -> handleBack());

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Database connection failed");
        }
    }

    private void handleAccountSelection(Class<? extends Account> accountClass) {
        if (customer == null) {
            showAlert("Error", "Customer not set");
            return;
        }

        // Fetch accounts only for this customer
        ArrayList<Account> accounts = accountDAO.getAccountsByCustomerId(customer.getCustomerId());

        // Find the first account matching the requested class
        Account selectedAccount = accounts.stream()
                .filter(acc -> acc.getClass().equals(accountClass))
                .findFirst()
                .orElse(null);

        if (selectedAccount != null) {
            loadDashboard(selectedAccount);
        } else {
            showAlert("No Account Found", "You do not have a " + accountClass.getSimpleName());
        }
    }

    private void loadDashboard(Account account) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/examplemonevo/Dashboard.fxml"));
            Scene scene = new Scene(loader.load());

            DashboardController controller = loader.getController();
            controller.setCustomer(account.getCustomer()); // Pass customer
            controller.setAccount(account);               // Pass selected account

            Stage stage = (Stage) chequeAccountButton.getScene().getWindow();
            stage.setScene(scene);
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Failed to load dashboard");
        }
    }

    private void handleBack() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/examplemonevo/Potso.fxml"));
            Scene scene = new Scene(loader.load());

            Stage stage = (Stage) backButton.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Failed to go back");
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
