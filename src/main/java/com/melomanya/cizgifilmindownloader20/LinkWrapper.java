package com.melomanya.cizgifilmindownloader20;

import java.io.File;
import java.io.IOException;
import java.net.*;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.text.DecimalFormat;

public class LinkWrapper {

    public static DecimalFormat decimalFormat;
    private String fileLink;
    private double fileSize = 0.0;

    private static String directoryPath;

    public LinkWrapper(String websiteURL) {
        if(decimalFormat == null)
            decimalFormat = new DecimalFormat("0.00");
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(websiteURL)).GET().build();
        HttpResponse<String> response;
        try {
            response = client.send(request,
                    HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        // get video link
        int startIndex = response.body().indexOf("file=");
        int lastIndex = response.body().indexOf("&amp;",startIndex);
        fileLink = "";
        for (int i=startIndex + 5; i<lastIndex; i++) {
            fileLink += response.body().charAt(i);
        }
        if(fileLink.startsWith("http")) {
            System.out.println("Dosya bulundu...");
        }
        else {
            String httpPrefix;
            if(fileLink.startsWith("/")) {
                httpPrefix = "http://www.cizgifilm.in";
            }
            else {
                httpPrefix = "http://www.cizgifilm.in/";
            }
            httpPrefix += fileLink;
            fileLink = httpPrefix;
        }
        try {
            calculateFileSize();
            System.out.println(decimalFormat.format(this.fileSize / 1024) + " MB boyutunda doysa bulundu.");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void calculateFileSize() throws IOException {
        URL url = new URL(this.fileLink);
        URLConnection connection = url.openConnection();
        this.fileSize = ((double) connection.getContentLengthLong() / 1024);
    }

    public String getFileLink() {
        return fileLink;
    }

    public double getFileSize() {
        return fileSize;
    }

    public static String getDirectoryPath() {
        return directoryPath;
    }

    public static void setDirectoryPath(File filePath) {
        directoryPath = filePath.getPath();
    }
}
