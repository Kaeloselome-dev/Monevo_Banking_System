package com.examplemonevo.Controllers;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class WelcomeController {

    @FXML
    private Button loginButton;

    @FXML
    private Button signUpButton;

    @FXML
    private void handleLogin(ActionEvent event) {
        try {
            // Load the login FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/examplemonevo/login.fxml"));
            Parent root = loader.load();

            // Get the current stage from the event source
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            System.err.println("Failed to load login screen: " + e.getMessage());
            e.printStackTrace();
            // You could show an alert to the user here
        }
    }

    @FXML
    private void handleSignUp(ActionEvent event) {
        // Disable the button while loading to prevent multiple clicks
        signUpButton.setDisable(true);
        signUpButton.setText("Loading...");

        try {
            // Load the registration FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/examplemonevo/registrationview.fxml"));
            Parent root = loader.load();

            // Get the current stage from the event source
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            System.err.println("Failed to load registration screen: " + e.getMessage());
            e.printStackTrace();

            // Re-enable the button and reset text if loading fails
            signUpButton.setDisable(false);
            signUpButton.setText("Sign Up");

            // You could show an alert to the user here
        }
    }
}