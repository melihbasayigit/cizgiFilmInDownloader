package com.melomanya.cizgifilmindownloader20;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URI;

public class MainController {
    private Stage stage;

    private DirectoryChooser directoryChooser;
    private double totalFileSize;
    @FXML
    private Label InformationLabel;

    @FXML
    private Button downloadButton;

    @FXML
    private Button directoryButton;

    @FXML
    private TextField singleDownloadTextField;

    @FXML
    private Label pathLabel;

    @FXML
    private Button watchButton;

    @FXML
    private ProgressBar progressbar;

    @FXML
    public void initialize() {
        String home = System.getProperty("user.home");
        File defaultFile = new File(home + "/Downloads");
        directoryChooser = new DirectoryChooser();
        directoryChooser.setInitialDirectory(defaultFile);
        pathLabel.setText(defaultFile.getAbsolutePath());

    }

    @FXML
    protected void downloadButtonClick(ActionEvent event) throws InterruptedException {
        downloadStarted(true);
        totalFileSize = 0;
        if(!singleDownloadTextField.getText().isEmpty() && checkLink(singleDownloadTextField.getText())) {
            consoleWrite(singleDownloadTextField.getText());
            LinkWrapper.setDirectoryPath(new File(pathLabel.getText()));
            LinkWrapper linkWrapper = new LinkWrapper(singleDownloadTextField.getText());
            System.out.println(LinkWrapper.getDirectoryPath());
            // single download
            totalFileSize += linkWrapper.getFileSize() / 1024;
            SingleDownload download = new SingleDownload(linkWrapper);
            initializeSingleDownload(download);
            download.start();
        }
    }

    @FXML
    protected void watchButtonClick(ActionEvent event) throws IOException {
        if(!singleDownloadTextField.getText().isEmpty() && checkLink(singleDownloadTextField.getText())) {
            LinkWrapper tempLink = new LinkWrapper(singleDownloadTextField.getText());
            Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
            if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
                try {
                    desktop.browse(new URI(tempLink.getFileLink()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @FXML
    protected void directoryButtonClick(ActionEvent event) {
        File selectedDirectory = directoryChooser.showDialog(stage);
        if(selectedDirectory != null)
            pathLabel.setText(selectedDirectory.getAbsolutePath());
    }

    protected void consoleWrite(String text) {
        InformationLabel.setText(text);
    }


    void updateProgressbar(double completed) {
        progressbar.setProgress(completed / this.totalFileSize);
    }

    private void downloadStarted(boolean status) {
        watchButton.setDisable(status);
        directoryButton.setDisable(status);
        downloadButton.setDisable(status);
    }

    private boolean checkLink(String url) {
        if(url.startsWith("http://www.cizgifilm.in") && url.endsWith(".html") && !url.contains(" ")) {
            consoleWrite("Link doğru gözüküyor...");
            return true;
        } return  false;
    }

    private void initializeSingleDownload(SingleDownload download) {
        download.consoleWrite = this::consoleWrite;
        download.updateProgressbar = this::updateProgressbar;
        download.downloadStarted = this::downloadStarted;
    }

}
