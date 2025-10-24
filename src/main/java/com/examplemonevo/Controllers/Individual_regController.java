package com.examplemonevo.Controllers;

import com.examplemonevo.Core_Model.Individual_Customer;
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
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class Individual_regController {

    @FXML private TextField firstnameField;
    @FXML private TextField lastnameField;
    @FXML private TextField addressField;
    @FXML private TextField emailField;
    @FXML private TextField usernameField;
    @FXML private TextField IdNumberField; // Renamed field
    @FXML private TextField passwordField;
    @FXML private TextField confirmPasswordField;
    @FXML private Button signUpButton;
    @FXML private Button backButton;

    @FXML
    private void handleSignUp(ActionEvent event) {
        String firstname = firstnameField.getText().trim();
        String lastname = lastnameField.getText().trim();
        String address = addressField.getText().trim();
        String email = emailField.getText().trim();
        String username = usernameField.getText().trim();
        String IdNumber = IdNumberField.getText().trim(); // Renamed variable
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        // Basic field validation
        if (firstname.isEmpty() || lastname.isEmpty() || address.isEmpty() || email.isEmpty()
                || username.isEmpty() || IdNumber.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Validation Error", "Please fill in all fields.");
            return;
        }

        // Password match check
        if (!password.equals(confirmPassword)) {
            showAlert(Alert.AlertType.ERROR, "Password Mismatch", "Passwords do not match.");
            return;
        }

        // ID Number format check
        if (!IdNumber.matches("\\d+")) {
            showAlert(Alert.AlertType.ERROR, "Invalid ID Number", "ID Number must contain only digits.");
            return;
        }

        try {
            // ID number is now a String, you can store it as a string directly
            // Create customer with placeholder ID
            Individual_Customer customer = new Individual_Customer(
                    "0", // placeholder for customerId
                    firstname,
                    lastname,
                    address,
                    email,
                    username,
                    IdNumber,
                    password,
                    "Individual"
            );

            // Insert into DB
            CustomerDAO customerDAO = new CustomerDAOImpl(DB_Connection.getConnection());
            customerDAO.create(customer); // DAO sets customerId internally

            // Optional: show the assigned customerId
            showAlert(Alert.AlertType.INFORMATION, "Success",
                    "Account created successfully!\nYour Customer ID is: " + customer.getCustomerId());

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
            showAlert(Alert.AlertType.ERROR, "Navigation Error", "Failed to load welcome screen.");
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