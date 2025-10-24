package com.examplemonevo.Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class AccountCreationController {

    private Stage stage;
    private Scene scene;
    private Parent root;

    /** Called when user clicks "Cheque Account" */
    @FXML
    private void handleChequeAccount(ActionEvent event) throws IOException {
        loadScene(event, "/com/examplemonevo/ChequeCreation.fxml");
    }

    /** Called when user clicks "Savings Account" */
    @FXML
    private void handleSavingsAccount(ActionEvent event) throws IOException {
        loadScene(event, "/com/examplemonevo/SavingsCreation.fxml");
    }

    /** Called when user clicks "Investment Account" */
    @FXML
    private void handleInvestmentAccount(ActionEvent event) throws IOException {
        loadScene(event, "/com/examplemonevo/InvestmentCreation.fxml");
    }

    /** Called when user clicks "Back" */
    @FXML
    private void handleBack(ActionEvent event) throws IOException {
        loadScene(event, "/com/examplemonevo/Welcome.fxml");
    }

    /** Generic method to load any FXML file */
    private void loadScene(ActionEvent event, String fxmlPath) throws IOException {
        root = FXMLLoader.load(getClass().getResource(fxmlPath));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
