module com.jefftastic.pngtoascii {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.jefftastic.pngtoascii to javafx.fxml;
    exports com.jefftastic.pngtoascii;
}