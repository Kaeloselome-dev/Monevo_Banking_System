package com.examplemonevo.Controllers;

import com.examplemonevo.Core_Model.Corporate_Customer;
import com.examplemonevo.DAOs.CustomerDAOImpl;
import com.examplemonevo.Interfaces.CustomerDAO;
import com.examplemonevo.Utility.DB_Connection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;

public class Corperate_RegController {

    @FXML private TextField companyNameField;
    @FXML private TextField addressField;
    @FXML private TextField emailField;
    @FXML private TextField registrationNumberField;
    @FXML private ComboBox<String> businessTypeComboBox;
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private PasswordField checkPasswordField;
    @FXML private Button signUpButton;
    @FXML private Button backButton;

    @FXML
    public void initialize() {
        // Populate business type ComboBox
        ObservableList<String> businessTypes = FXCollections.observableArrayList(
                "Retail",
                "Manufacturing",
                "Service",
                "Technology",
                "Finance",
                "Other"
        );
        businessTypeComboBox.setItems(businessTypes);
    }

    @FXML
    private void handleSignUp(ActionEvent event) {
        String companyName = companyNameField.getText().trim();
        String address = addressField.getText().trim();
        String email = emailField.getText().trim();
        String registrationNumber = registrationNumberField.getText().trim();
        String businessType = businessTypeComboBox.getValue(); // from ComboBox
        String username = usernameField.getText().trim();
        String password = passwordField.getText();
        String confirmPassword = checkPasswordField.getText();

        // Basic field validation
        if (companyName.isEmpty() || address.isEmpty() || email.isEmpty()
                || registrationNumber.isEmpty() || businessType == null
                || username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Please fill in all fields.");
            return;
        }

        // Password match check
        if (!password.equals(confirmPassword)) {
            showAlert(Alert.AlertType.ERROR, "Password Mismatch", "Passwords do not match.");
            return;
        }

        // Registration number validation
        if (!registrationNumber.matches("\\w+")) { // letters and digits allowed
            showAlert(Alert.AlertType.ERROR, "Invalid Registration Number",
                    "Registration number must contain only letters or digits.");
            return;
        }

        try {
            // Create Corporate_Customer object
            Corporate_Customer company = new Corporate_Customer(
                    "0", // placeholder for customerId
                    companyName,
                    address,
                    email,
                    registrationNumber,
                    businessType,
                    username,
                    password,
                    "corporate"
            );
            company.setBusinessType(businessType);

            // Save to database
            CustomerDAO customerDAO = new CustomerDAOImpl(DB_Connection.getConnection());
            customerDAO.create(company);

            // Notify user
            showAlert(Alert.AlertType.INFORMATION, "Success",
                    "Company account created successfully!\nYour Customer ID is: " + company.getCustomerId());

            // Redirect to welcome screen
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/examplemonevo/AccountCreationSelection.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Navigation Error", "Failed to load welcome screen.");
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "System Error", "An unexpected error occurred.");
            e.printStackTrace();
        }
    }

    @FXML
    private void handleBack(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/examplemonevo/registrationview.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Navigation Error", "Failed to load registration view.");
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
