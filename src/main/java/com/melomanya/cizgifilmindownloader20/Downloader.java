package com.melomanya.cizgifilmindownloader20;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.function.Consumer;

public class Downloader extends Thread{

    private ArrayList<LinkWrapper> links;
    private int kbytesCounter = 0;

    private double totalFileSize = 0;

    Consumer<Double> updateProgressbar;

    Consumer<String> consoleWrite;

    Consumer<Boolean> downloadStarted;
    public Downloader(ArrayList<LinkWrapper> temp) {
        this.links = temp;
        for (LinkWrapper link:temp) {
            totalFileSize += link.getFileSize();
        }
        System.out.println(totalFileSize);
    }

    private void startDownload() {
        for (LinkWrapper link:this.links) {
            String fileLink = link.getFileLink();
            String[] parts = fileLink.split("/");
            String fileName = parts[parts.length-1].replace(".html", "");
            fileName += ".mp4";
            File file = new File(LinkWrapper.getDirectoryPath() + "/" + fileName);
            //consoleWrite.accept("Dosya Ismi: " + this.fileName);
            try (BufferedInputStream inputStream = new BufferedInputStream(new URL(fileLink).openStream());
                 FileOutputStream fileOutputStream = new FileOutputStream(file)) {
                byte[] dataBuffer = new byte[1024];
                int bytesRead;
                //consoleWrite.accept("İndirme başladı...");
                while((bytesRead = inputStream.read(dataBuffer, 0, 1024)) != -1){
                    fileOutputStream.write(dataBuffer, 0, bytesRead);
                    this.kbytesCounter++;
                    double ratio = (double) kbytesCounter / totalFileSize;
                    updateProgressbar.accept(ratio);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            //consoleWrite.accept("Dosya indirildi. Dosya yolu: " + file.getPath());
            System.out.println(file.getPath());
        }
        downloadStarted.accept(false);
    }

    @Override
    public void run() {startDownload();}
}
