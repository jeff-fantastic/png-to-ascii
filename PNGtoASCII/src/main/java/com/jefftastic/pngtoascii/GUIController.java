package com.jefftastic.pngtoascii;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Handles initializing JavaFX components in
 * the main GUI, as well as responding to
 * events evoked by the end user, usually from
 * interacting with the GUI.
 * @see Main
 */

public class GUIController implements Initializable {
    // Get FXML components
    @FXML
    private Label pixelRatioLabel;
    @FXML
    private Slider pixelRatioSlider;
    @FXML
    private TextArea asciiOutput;
    @FXML
    private ImageView imageView;
    @FXML
    private MenuItem loadImageButton;
    @FXML
    private MenuItem saveASCIIButton;
    @FXML
    private MenuItem exitButton;
    @FXML
    private MenuItem aboutButton;

    /**
     * Image that was loaded by end user
     */
    private Image userImage;

    /**
     * Fired when GUI is first created
     * @param url unused
     * @param resourceBundle unused
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    /**
     * Fired when user requests to load an image.
     */
    @FXML
    protected void onLoadImagePressed() throws FileNotFoundException {
        // Get image and load it
        File selectedImage = Main.fC.showOpenDialog(Main.mainStage);
        userImage = new Image(new FileInputStream(selectedImage.getAbsolutePath()));

        // Set image view
        imageView.setImage(userImage);
    }

    /**
     * Fired when user requests to save generated
     * ASCII art
     */
    @FXML
    protected void onSaveASCIIPressed() {

    }

    /**
     * Fired when program exit is requested
     */
    @FXML
    protected void onExitPressed() {

    }

    /**
     * Fired when About dialog is requested
     */
    @FXML
    protected void onAboutPressed() {

    }
}
