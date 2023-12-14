module com.example.examediprr {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.examediprr to javafx.fxml;
    exports com.example.examediprr;
}