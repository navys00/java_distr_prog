module com.example.gomoku_rmi {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.rmi;


    opens com.example.gomoku_rmi to javafx.fxml;
    exports com.example.gomoku_rmi;
}