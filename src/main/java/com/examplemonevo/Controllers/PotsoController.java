package com.examplemonevo.Controllers;

import com.examplemonevo.Core_Model.Customer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class PotsoController {

    private Customer customer;

    // This method will allow you to set the customer object from loginController
    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    // Called when the user clicks "Yes" (i.e., they have an account)
    @FXML
    private void handleYes(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/examplemonevo/Accountselection.fxml"));
        Parent root = loader.load();

        // Pass the customer object to the AccountSelectionController
        AccountSelectionController controller = loader.getController();
        controller.setCustomer(customer);

        // Transition to the next screen
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    // Called when the user clicks "No" (i.e., they need to create an account)
    @FXML
    private void handleNo(ActionEvent event) throws IOException {
        // Load the AccountCreationSelection.fxml
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/examplemonevo/AccountCreationSelection.fxml"));
        Parent root = loader.load();

        // Transition to the next screen
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }
}
