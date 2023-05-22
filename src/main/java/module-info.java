module com.example.finalprojectgroup {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.finalprojectgroup to javafx.fxml;
    exports com.example.finalprojectgroup;
}