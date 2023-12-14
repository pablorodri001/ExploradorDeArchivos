module com.example.examediprr {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.apache.commons.io;


    opens com.example.examediprr to javafx.fxml;
    exports com.example.examediprr;
}