package com.examplemonevo.Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class RegistrationController {


        @FXML
        private Button IndividualCustomerbtn;

        @FXML
        private Button CorperateCustomerbtn;

         @FXML
         private Button backButton;

        @FXML
        private void CoperateRegistration(ActionEvent event) {
            try {

                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/examplemonevo/Coperatereg_form.fxml"));
                Parent root = loader.load();


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
        private void IndividualRegister(ActionEvent event) {

            try {
                // Load the registration FXML file
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/examplemonevo/Individualreg_form.fxml"));
                Parent root = loader.load();

                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.show();
            } catch (IOException e) {
                System.err.println("Failed to load registration screen: " + e.getMessage());
                e.printStackTrace();
            }
        }

    @FXML
    private void goBack(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/examplemonevo/Welcome.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Navigation Error",
                    "Failed to load welcome screen: " + e.getMessage());
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

