package com.example.word2pdfpng;

import java.io.*;
import java.net.*;

public class FileDownloader {
    
    public static void downloadFile(String fileUrl, String saveDir) {
        try {
            URL url = new URL(fileUrl);
            HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
            int responseCode = httpConn.getResponseCode();

            // Check if the response is OK (HTTP 200)
            if (responseCode == HttpURLConnection.HTTP_OK) {
                // Get file name from URL
                String fileName = "";
                String disposition = httpConn.getHeaderField("Content-Disposition");
                if (disposition != null) {
                    int index = disposition.indexOf("filename=");
                    if (index > 0) {
                        fileName = disposition.substring(index + 10, disposition.length() - 1);
                    }
                } else {
                    fileName = fileUrl.substring(fileUrl.lastIndexOf("/") + 1);
                }

                // Open input stream from the connection
                InputStream inputStream = httpConn.getInputStream();
                String saveFilePath = saveDir + File.separator + fileName;

                // Open output stream to save the file
                FileOutputStream outputStream = new FileOutputStream(saveFilePath);

                // Read data from input stream and write to output stream
                int bytesRead;
                byte[] buffer = new byte[4096];
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }

                // Close streams
                outputStream.close();
                inputStream.close();

                System.out.println("File downloaded successfully.");
            } else {
                System.out.println("Failed to download file. HTTP error code: " + responseCode);
            }
            httpConn.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
