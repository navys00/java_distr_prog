module com.example.gomoku_rmi {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.rmi;

    requires java.jws;
    requires java.xml.ws;
    requires java.sql;
    requires java.xml.bind;
    requires com.sun.xml.bind;
    requires org.eclipse.persistence.asm;
    requires org.eclipse.persistence.core;
    requires org.eclipse.persistence.moxy;
    requires org.eclipse.persistence.sdo;

    opens com.example.gomoku_rmi to javafx.fxml;
    exports com.example.gomoku_rmi;
}