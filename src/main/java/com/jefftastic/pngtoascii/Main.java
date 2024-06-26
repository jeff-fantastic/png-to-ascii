package com.jefftastic.pngtoascii;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.Objects;

public class Main extends Application {
    public static FileChooser fC;
    public static Stage mainStage;

    @Override
    public void start(Stage stage) throws IOException {
        // Prepare file picker
        fC = new FileChooser();

        // Load main GUI
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("png-ascii-gui.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("PNG to ASCII");
        stage.setScene(scene);
        stage.show();

        // Set main stage
        mainStage = stage;
    }

    public static void main(String[] args) {
        launch();
    }
}