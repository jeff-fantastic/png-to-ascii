package com.jefftastic.pngtoascii;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Wrangles what is input into the preferences
 * GUI and sends that data to the preferences manager
 * @see Preferences
 */

public class PreferencesController implements Initializable {

    @FXML
    public ChoiceBox<String> characterSet;
    @FXML
    public ChoiceBox<String> imageSize;
    @FXML
    public ChoiceBox<String> fontSet;

    /**
     * Initializes various fields.
     * @param url unused
     * @param resourceBundle unused
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Initialize font box
        List<String> list = Font.getFontNames();
        fontSet.getItems().addAll(list);

        // Initialize image sizes
        imageSize.getItems().addAll("512", "1024", "None");

        // Initialize character set
        characterSet.getItems().addAll("Generic", "Block", "Kanji");
    }

    /**
     * Called when the users font choice has changed.
     */
    private void fontChanged() {

    }

    /**
     * Fired when grayscale option is toggled.
     * @param actionEvent
     */
    public void grayscaleToggled(ActionEvent actionEvent) {

    }

    /**
     * Fired when apply button is pressed.
     */
    protected void applyPressed() {
        // Send field information to preferences class
    }

    /**
     * Fired when cancel button is pressed.
     */
    @FXML
    protected void cancelPressed() {
        // Close self
        Stage stage = (Stage) characterSet.getScene().getWindow();
        stage.close();
    }
}
