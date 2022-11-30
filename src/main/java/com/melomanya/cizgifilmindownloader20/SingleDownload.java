package com.melomanya.cizgifilmindownloader20;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.function.Consumer;

public class SingleDownload extends Thread{
    private static int threadNumber = 0;
    private final String fileLink;
    private String fileName;
    private int kbytesCounter = 0;

    Consumer<Double> updateProgressbar;

    Consumer<String> consoleWrite;

    Consumer<Boolean> downloadStarted;

    public SingleDownload(LinkWrapper linkWrapper) {
        this.fileLink = linkWrapper.getFileLink();
        updateFileName();
        threadNumber++;
    }

    private void startDownload() {
        File file = new File(LinkWrapper.getDirectoryPath() + "/" + this.fileName);
        //consoleWrite.accept("Dosya Ismi: " + this.fileName);
        try (BufferedInputStream inputStream = new BufferedInputStream(new URL(this.fileLink).openStream());
             FileOutputStream fileOutputStream = new FileOutputStream(file)) {
            byte[] dataBuffer = new byte[1024];
            int bytesRead;
            //consoleWrite.accept("İndirme başladı...");
            while((bytesRead = inputStream.read(dataBuffer, 0, 1024)) != -1){
                fileOutputStream.write(dataBuffer, 0, bytesRead);
                this.kbytesCounter++;
                updateProgressbar.accept((double) kbytesCounter / 1024);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        //consoleWrite.accept("Dosya indirildi. Dosya yolu: " + file.getPath());
        System.out.println(file.getPath());
        threadNumber--;
        if(threadNumber < 1)
            downloadStarted.accept(false);
    }

    @Override
    public void run() {
        startDownload();
    }

    private void updateFileName() {
        String[] parts = this.fileLink.split("/");
        this.fileName = parts[parts.length-1].replace(".html", "");
        this.fileName += ".mp4";
    }

}
