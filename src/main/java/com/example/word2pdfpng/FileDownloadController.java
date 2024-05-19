//// FileDownloadController.java
//package com.example.word2pdfpng;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//public class FileDownloadController {
//
//    private final FileDownloadService fileDownloadService;
//
//    @Autowired
//    public FileDownloadController(FileDownloadService fileDownloadService) {
//        this.fileDownloadService = fileDownloadService;
//    }
//
//    @PostMapping("/download")
//    public ResponseEntity<String> downloadFile(@RequestParam("fileUrl") String fileUrl,
//                                               @RequestParam("saveDir") String saveDir) {
//        fileDownloadService.downloadFile(fileUrl, saveDir);
//        return ResponseEntity.ok("File download initiated.");
//    }
//}




package com.example.word2pdfpng;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Controller
public class FileDownloadController {

    private final FileDownloadService fileDownloadService;

    public FileDownloadController(FileDownloadService fileDownloadService) {
        this.fileDownloadService = fileDownloadService;
    }

    @GetMapping("/api/files/download/{fileName:.+}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName) {
        // Load file as Resource
        Resource resource = fileDownloadService.loadFileAsResource(fileName);

        // Try to determine file's content type
        String contentType;
        try {
            contentType = Files.probeContentType(Paths.get(fileName));
        } catch (IOException ex) {
            contentType = MediaType.APPLICATION_OCTET_STREAM_VALUE;
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }
}
