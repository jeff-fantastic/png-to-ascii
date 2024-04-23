package com.jefftastic.pngtoascii;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Wrangles what is input into the preferences
 * GUI and sends that data to the preferences manager
 * @see Preferences
 */

public class PreferencesController implements Initializable {
    // Get FXML elements
    @FXML
    public ChoiceBox<String> characterSet;
    @FXML
    public ChoiceBox<String> imageSize;
    @FXML
    public ChoiceBox<String> fontSet;
    @FXML
    public Button applyButton;
    @FXML
    public CheckBox grayscaleButton;

    /**
     * Initializes various fields.
     * @param url unused
     * @param resourceBundle unused
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Initialize config
        Preferences.initialize();

        // Declare variables
        String fontValue = Preferences.preference.get("font");
        String imageSizeValue = Preferences.preference.getOrDefault("imageSize", "sNone").substring(1);
        String charSetValue = Preferences.preference.get("charSet");

        // Initialize font box
        List<String> list = Font.getFontNames();
        fontSet.getItems().addAll(list);
        fontSet.setValue(fontValue);
        fontSet.getSelectionModel().selectedIndexProperty().addListener((observableValue, number, t1) -> enableApply());

        // Initialize image sizes
        imageSize.getItems().addAll("512", "1024", "None");
        imageSize.setValue(imageSizeValue);
        imageSize.getSelectionModel().selectedIndexProperty().addListener((observableValue, number, t1) -> enableApply());

        // Initialize character set
        characterSet.getItems().addAll("Generic", "Block", "Kanji");
        characterSet.setValue(charSetValue);
        characterSet.getSelectionModel().selectedIndexProperty().addListener((observableValue, number, t1) -> enableApply());
    }

    /**
     * Fired when grayscale option is toggled.
     */
    @FXML
    public void grayscaleToggled(ActionEvent actionEvent) {
        enableApply();
    }

    /**
     * Enables apply button.
     */
    private void enableApply() {
        applyButton.setDisable(false);
    }

    /**
     * Fired when apply button is pressed.
     */
    @FXML
    protected void applyPressed() {
        // Send field information to preferences class
        try {
            Preferences.preference.clear();
            Preferences.preference.put("charSet", characterSet.getValue());
            Preferences.preference.put("imageSize", "s" + imageSize.getValue());
            Preferences.preference.put("font", fontSet.getValue());
            Preferences.preference.put("grayscale", grayscaleButton.isSelected() ? "true" : "false");
            Preferences.savePreferences();
        } catch (IOException e) {
            e.printStackTrace(System.out);
        }

        // Exit window
        cancelPressed();
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
