package com.examplemonevo.Controllers;

import com.examplemonevo.Core_Model.Account;
import com.examplemonevo.Core_Model.Transaction;
import com.examplemonevo.DAOs.TransactionDAOImpl;
import com.examplemonevo.Utility.DB_Connection;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.List;

public class TransactionHistoryController {

    @FXML
    private TableView<Transaction> transactionTable;

    @FXML
    private TableColumn<Transaction, String> transactionIdColumn;

    @FXML
    private TableColumn<Transaction, String> accountNumberColumn;

    @FXML
    private TableColumn<Transaction, Double> amountColumn;

    @FXML
    private TableColumn<Transaction, String> typeColumn;

    @FXML
    private TableColumn<Transaction, String> descriptionColumn;

    @FXML
    private TableColumn<Transaction, String> dateColumn;

    @FXML
    private Button backButton;

    private Account account;
    private TransactionDAOImpl transactionDAO;

    public TransactionHistoryController() {
        try {
            Connection connection = DB_Connection.getConnection();
            this.transactionDAO = new TransactionDAOImpl(connection);
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Database Error", "Failed to connect to the database.");
        }
    }

    /**
     * Called by the previous controller to set the current account.
     * Now also loads the transaction history immediately.
     */
    public void setAccount(Account account) {
        this.account = account;
        loadTransactionHistory(); // ✅ Load transactions now that account is available
    }

    @FXML
    public void initialize() {
        backButton.setOnAction(e -> goBackToDashboard());
    }

    /**
     * Load transactions for the current account and populate the table.
     */
    private void loadTransactionHistory() {
        if (account == null) {
            return; // Safety check
        }

        // Retrieve the transactions from the database
        List<Transaction> transactions = transactionDAO.getTransactionsByAccount(account.getAccountNumber());

        // Set the cell value factories for each column
        transactionIdColumn.setCellValueFactory(cellData -> new SimpleStringProperty(
                String.valueOf(cellData.getValue().getTransactionId())
        ));
        accountNumberColumn.setCellValueFactory(cellData -> new SimpleStringProperty(
                cellData.getValue().getAccountNumber()
        ));
        amountColumn.setCellValueFactory(cellData -> new SimpleDoubleProperty(
                cellData.getValue().getAmount()
        ).asObject());
        typeColumn.setCellValueFactory(cellData -> new SimpleStringProperty(
                cellData.getValue().getType()
        ));
        descriptionColumn.setCellValueFactory(cellData -> new SimpleStringProperty(
                cellData.getValue().getDescription()
        ));

        // Format the date for display
        dateColumn.setCellValueFactory(cellData -> {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            return new SimpleStringProperty(sdf.format(cellData.getValue().getDate()));
        });

        // Populate the table
        transactionTable.getItems().setAll(transactions);
    }

    /**
     * Navigate back to the dashboard scene.
     */
    private void goBackToDashboard() {
        try {
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(
                    getClass().getResource("/com/examplemonevo/Dashboard.fxml")
            );
            javafx.scene.Scene scene = new javafx.scene.Scene(loader.load());

            DashboardController controller = loader.getController();
            controller.setAccount(account);

            Stage stage = (Stage) backButton.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Navigation Error", "Unable to go back to the dashboard.");
        }
    }

    /**
     * Show a simple alert popup.
     */
    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
