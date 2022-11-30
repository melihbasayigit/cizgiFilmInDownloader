module com.melomanya.cizgifilmindownloader {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires java.net.http;
    requires java.datatransfer;
    requires java.desktop;
    requires javafx.web;

    opens com.melomanya.cizgifilmindownloader20 to javafx.fxml;
    exports com.melomanya.cizgifilmindownloader20;
}