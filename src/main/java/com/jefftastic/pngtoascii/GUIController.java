package com.jefftastic.pngtoascii;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.*;
import java.net.URL;
import java.util.Objects;
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
    public Slider brightnessSlider;
    @FXML
    public Slider contrastSlider;
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
    @FXML
    private Spinner<Integer> lineSkip;


    /**
     * Image that was loaded by end user
     */
    private Image userImage;
    /**
     * Radius of pixels to use in the
     * conversion process
     */
    private byte conversionRadius = 10;
    /**
     * Amount of lines to "drop" after successfully
     * generating one line of ASCII.<br><br>
     * A value of 0 will prevent lines from dropping.
     */
    private byte lineControl = 0;

    /**
     * Fired when GUI is first created
     * @param url unused
     * @param resourceBundle unused
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Connect listener to preferences
        Preferences.addListener(this::updatePreferences);

        // Set up spinner
        lineSkip.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 8, 0));
        lineSkip.valueProperty().addListener((observableValue, oldVal, newVal) -> {
            lineControl = newVal.byteValue();
            if (oldVal.byteValue() != newVal.byteValue() && userImage != null)
                asciiOutput.setText(ASCIIConverter.toASCII(userImage, conversionRadius, lineControl));
        });

        // Add slider listener
        pixelRatioSlider.valueProperty().addListener((observableValue, oldVal, newVal) -> {
            byte value = newVal.byteValue();
            pixelRatioLabel.setText("%d:1".formatted(value * value));
            conversionRadius = value;
            if (oldVal.intValue() != newVal.intValue() && userImage != null)
                asciiOutput.setText(ASCIIConverter.toASCII(userImage, conversionRadius, lineControl));
        });
    }

    /**
     * Updates various elements of the program to reflect
     * user preferences.
     */
    private void updatePreferences() {
        if (
            Preferences.preference.getOrDefault("grayscale", "true") == "true" &&
            imageView.getImage() != null
        )
            imageView.setImage(ASCIIConverter.RGBtoGS.imageToGrayscale(imageView.getImage()));
    }

    /**
     * Creates a window.
     */
    private void createWindow(String fxml, String title) {
        Parent root;
        try {
            root = FXMLLoader.load(Objects.requireNonNull(Main.class.getResource(fxml)));
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initOwner(Main.mainStage);
            stage.setTitle(title);
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            Alert error = new Alert(
                    Alert.AlertType.ERROR,
                    "Something has gone horribly wrong.\n\n" + e.getCause(),
                    ButtonType.OK
            );
            error.showAndWait();
        }
    }

    /**
     * Fired when user requests to load an image.
     */
    @FXML
    protected void onLoadImagePressed() throws FileNotFoundException {
        // Prepare file chooser
        Main.fC.setTitle("Please choose an image");
        FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("PNG Files (*.png)", "*.png");
        Main.fC.getExtensionFilters().clear();
        Main.fC.getExtensionFilters().add(filter);

        // Get image and load it
        File selectedImage = Main.fC.showOpenDialog(Main.mainStage);

        // Abort if image is null
        if (selectedImage == null)
            return;
        userImage = new Image(new FileInputStream(selectedImage.getAbsolutePath()));

        // Set image view
        imageView.setImage(userImage);
        asciiOutput.setText(ASCIIConverter.toASCII(userImage, conversionRadius, lineControl));
    }

    /**
     * Fired when user requests to save generated
     * ASCII art
     */
    @FXML
    protected void onSaveASCIIPressed() {
        // Prepare file chooser
        Main.fC.setTitle("Please select a save location");
        FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("TXT Files (*.txt)", "*.txt");
        Main.fC.getExtensionFilters().clear();
        Main.fC.getExtensionFilters().add(filter);

        // Open save dialog
        File savedFile = Main.fC.showSaveDialog(Main.mainStage);

        // Skip if user cancelled
        if (savedFile == null)
            return;

        // Begin writing to file
        try {
            FileWriter fW = new FileWriter(savedFile);
            BufferedWriter bW = new BufferedWriter(fW);
            String[] splitString = asciiOutput.getText().split("\n");

            for (String line : splitString)
                bW.write(line + "\n");
            bW.close();
        } catch (IOException e) {
            Alert error = new Alert(
                    Alert.AlertType.ERROR,
                    "Something has gone horribly wrong in the export process.\n\n" + e.getMessage(),
                    ButtonType.OK
            );
            error.showAndWait();
        }

    }

    /**
     * Fired when program exit is requested
     */
    @FXML
    protected void onExitPressed() {
        Main.mainStage.close();
    }

    /**
     * Fired when preferences menu is requested
     */
    @FXML
    protected void onPreferencePressed() { createWindow("preferences.fxml", "Preferences"); }

    /**
     * Fired when About dialog is requested
     */
    @FXML
    protected void onAboutPressed() { createWindow("about.fxml", "About this program"); }

    @FXML
    protected void onLinesHelpPressed() {
        // Create help alert
        Alert help = new Alert(
                Alert.AlertType.NONE,
                "This option determines how many lines of ASCII generation to skip after " +
                "a successful line generation. This can sometimes help with squashed ASCII " +
                "art, as most common fonts are not perfectly square.",
                ButtonType.OK
        );
        help.showAndWait();
    }

    /**
     * Called when invert color is requested
     * @param actionEvent Event that determines color inversion
     */
    @FXML
    protected void onInvertColorPressed(ActionEvent actionEvent) {
    }
}
