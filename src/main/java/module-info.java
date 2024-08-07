module com.example.compiler {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.compiler to javafx.fxml;
    exports com.example.compiler;
}