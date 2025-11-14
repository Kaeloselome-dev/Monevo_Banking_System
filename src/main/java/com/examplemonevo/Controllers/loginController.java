package com.examplemonevo.Controllers;

import com.examplemonevo.Core_Model.Customer;
import com.examplemonevo.DAOs.CustomerDAOImpl;
import com.examplemonevo.Interfaces.CustomerDAO;
import com.examplemonevo.Utility.DB_Connection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class loginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button loginButton;

    @FXML
    private Button backButton;

    @FXML
    private void handleLogin(ActionEvent event) {
        loginButton.setDisable(true);
        loginButton.setText("Logging in...");

        String username = usernameField.getText().trim();
        String password = passwordField.getText();

        try {
            // Input validation
            if (username.isEmpty() || password.isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Validation Error", "Please enter both username and password");
                return;
            }

            if (username.length() < 3) {
                showAlert(Alert.AlertType.ERROR, "Validation Error", "Username must be at least 3 characters long");
                return;
            }



            // DAO login
            CustomerDAO customerDAO = new CustomerDAOImpl(DB_Connection.getConnection());
            Customer customer = customerDAO.login(username, password);

            if (customer != null) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/examplemonevo/Potso.fxml"));
                Parent root = loader.load();
                
                
                PotsoController potsoController = loader.getController();
                if (potsoController != null) {
                    potsoController.setCustomer(customer);  // Pass customer object to PotsoController
                } else {
                    showAlert(Alert.AlertType.ERROR, "Controller Error", "Failed to load the PotsoController.");
                    return;
                }

                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.show();
            } else {
                showAlert(Alert.AlertType.ERROR, "Login Failed", "Invalid username or password.");
            }

        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "System Error", "Failed to load the next screen: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "System Error", "An unexpected error occurred: " + e.getMessage());
            e.printStackTrace();
        } finally {
            loginButton.setDisable(false);
            loginButton.setText("Login");
        }
    }


    @FXML
    private void handleBack(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/examplemonevo/Welcome.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Navigation Error", "Failed to load welcome screen: " + e.getMessage());
        }
    }

    public void clearForm() {
        usernameField.clear();
        passwordField.clear();
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}