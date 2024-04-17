package com.jefftastic.pngtoascii;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

/**
 * Handles the minimal amount of interactions with
 * the "About" window.
 */

public class AboutController {
    @FXML
    public Button exitAbout;

    @FXML
    public void exitPressed(ActionEvent actionEvent) {
        // Close self
        Stage stage = (Stage) exitAbout.getScene().getWindow();
        stage.close();
    }
}
