module com.jefftastic.pngtoascii {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires javafx.swing;


    opens com.jefftastic.pngtoascii to javafx.fxml;
    exports com.jefftastic.pngtoascii;
}