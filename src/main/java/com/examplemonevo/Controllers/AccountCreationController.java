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

    // This field stores the FXML name of the page that loaded this controller.
    private String previousPageIdentifier;

    // --- Account Selection Handlers (No Change Needed) ---

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

    // --- Smart Back Button Implementation ---

    /** * Called when user clicks "Back".
     * Uses an IF statement to return to the respective previous page.
     */
    @FXML
    private void handleBack(ActionEvent event) throws IOException {


        if (previousPageIdentifier == null) {
            // Default Fallback if the identifier is missing
            System.out.println("Previous page identifier not set. Defaulting to Welcome.");
            loadScene(event, "/com/examplemonevo/Welcome.fxml");
            return;
        }

        // 2. Use IF/ELSE IF to check the source and load the respective FXML
        // IMPORTANT: The strings here must match the strings passed by the source controllers.
        if (previousPageIdentifier.equals("Dashboard")) {
            System.out.println("Returning to Dashboard...");
            loadScene(event, "/com/examplemonevo/Dashboard.fxml");

        } else if (previousPageIdentifier.equals("SomeOtherPage")) {
            System.out.println("Returning to Some Other Page...");
            loadScene(event, "/com/examplemonevo/SomeOtherPage.fxml");

        } else {
            // Fallback for any unexpected identifier
            System.err.println("Unknown previous page identifier: " + previousPageIdentifier);
            loadScene(event, "/com/examplemonevo/Welcome.fxml");
        }
    }

    // --- Setter Method ---

    /**
     * Called by the source controller to set the "return address" (FXML name).
     */
    public void setPreviousPageIdentifier(String identifier) {
        this.previousPageIdentifier = identifier;
        System.out.println("AccountCreation loaded from: " + identifier);
    }

    // --- Generic Scene Loader ---

    /** Generic method to load any FXML file */
    private void loadScene(ActionEvent event, String fxmlPath) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource(fxmlPath));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}