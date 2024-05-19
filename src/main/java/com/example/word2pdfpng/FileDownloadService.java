//package com.example.word2pdfpng;
//
//import org.springframework.stereotype.Service;
//
//import java.io.*;
//import java.net.HttpURLConnection;
//import java.net.URL;
//
//@Service
//public class FileDownloadService {
//
//    public void downloadFile(String fileUrl, String saveDir) {
//        try {
//            URL url = new URL(fileUrl);
//            HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
//            int responseCode = httpConn.getResponseCode();
//
//            // Check if the response is OK (HTTP 200)
//            if (responseCode == HttpURLConnection.HTTP_OK) {
//                // Get file name from URL
//                String fileName = "";
//                String disposition = httpConn.getHeaderField("Content-Disposition");
//                if (disposition != null) {
//                    int index = disposition.indexOf("filename=");
//                    if (index > 0) {
//                        fileName = disposition.substring(index + 10, disposition.length() - 1);
//                    }
//                } else {
//                    fileName = fileUrl.substring(fileUrl.lastIndexOf("/") + 1);
//                }
//
//                // Open input stream from the connection
//                InputStream inputStream = httpConn.getInputStream();
//                String saveFilePath = saveDir + File.separator + fileName;
//
//                // Open output stream to save the file
//                FileOutputStream outputStream = new FileOutputStream(saveFilePath);
//
//                // Read data from input stream and write to output stream
//                int bytesRead;
//                byte[] buffer = new byte[4096];
//                while ((bytesRead = inputStream.read(buffer)) != -1) {
//                    outputStream.write(buffer, 0, bytesRead);
//                }
//
//                // Close streams
//                outputStream.close();
//                inputStream.close();
//
//                System.out.println("File downloaded successfully.");
//            } else {
//                System.out.println("Failed to download file. HTTP error code: " + responseCode);
//            }
//            httpConn.disconnect();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//}



package com.example.word2pdfpng;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class FileDownloadService {

    private final Path fileStorageLocation = Paths.get("uploads");

    public Resource loadFileAsResource(String fileName) {
        try {
            Path filePath = fileStorageLocation.resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists()) {
                return resource;
            } else {
                throw new RuntimeException("File not found: " + fileName);
            }
        } catch (MalformedURLException ex) {
            throw new RuntimeException("File not found: " + fileName, ex);
        }
    }
}

