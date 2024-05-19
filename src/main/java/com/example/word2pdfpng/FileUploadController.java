//package com.example.word2pdfpng;
//
//import org.apache.pdfbox.pdmodel.PDDocument;
//import org.apache.pdfbox.pdmodel.PDPage;
//import org.apache.pdfbox.pdmodel.PDPageContentStream;
//import org.apache.pdfbox.pdmodel.font.PDType1Font;
//import org.apache.pdfbox.rendering.PDFRenderer;
//import org.apache.poi.xwpf.usermodel.XWPFDocument;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.multipart.MultipartFile;
//import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
//
//import javax.imageio.ImageIO;
//import java.awt.image.BufferedImage;
//import java.io.File;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.nio.file.Files;
//import java.nio.file.Paths;
//
//@RestController
//@RequestMapping("/api/files")
//public class FileUploadController {
//
//    @PostMapping("/upload")
//    public ResponseEntity<String> handleFileUpload(@RequestParam("file") MultipartFile file) {
//        if (file.isEmpty()) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Please upload a file!");
//        }
//
//        try {
//            // Save the uploaded file to the local file system
//            String uploadedFilePath = saveUploadedFile(file);
//
//            // Convert the file to PDF
//            String pdfFilePath = convertDocxToPdf(uploadedFilePath);
//
//            // Convert the PDF to PNG
//            String pngFilePath = convertPdfToPng(pdfFilePath);
//
//            // Return the file download URI
//            String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
//                    .path("/api/files/download/")
//                    .path(Paths.get(pngFilePath).getFileName().toString())
//                    .toUriString();
//
//            return ResponseEntity.ok("File converted successfully: " + fileDownloadUri);
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("File conversion failed: " + e.getMessage());
//        }
//    }
//
//    private String saveUploadedFile(MultipartFile file) throws IOException {
//        String uploadDir = "uploads/";
//        Files.createDirectories(Paths.get(uploadDir));
//        String filePath = uploadDir + file.getOriginalFilename();
//        try (FileOutputStream fos = new FileOutputStream(filePath)) {
//            fos.write(file.getBytes());
//        }
//        return filePath;
//    }
//
//    private String convertDocxToPdf(String docxFilePath) throws IOException {
//        try (XWPFDocument document = new XWPFDocument(Files.newInputStream(Paths.get(docxFilePath)));
//             PDDocument pdfDocument = new PDDocument()) {
//            PDPage page = new PDPage();
//            pdfDocument.addPage(page);
//            try (PDPageContentStream contentStream = new PDPageContentStream(pdfDocument, page)) {
//                contentStream.setFont(PDType1Font.HELVETICA, 12);
//                contentStream.beginText();
//                contentStream.setLeading(14.5f);
//                contentStream.newLineAtOffset(25, 700);
//                document.getParagraphs().forEach(p -> {
//                    try {
//                        contentStream.showText(p.getText());
//                        contentStream.newLine();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                });
//                contentStream.endText();
//            }
//            String pdfFilePath = docxFilePath.replace(".docx", ".pdf");
//            pdfDocument.save(pdfFilePath);
//            return pdfFilePath;
//        }
//    }
//
//    private String convertPdfToPng(String pdfFilePath) throws IOException {
//        try (PDDocument document = PDDocument.load(new File(pdfFilePath))) {
//            PDFRenderer pdfRenderer = new PDFRenderer(document);
//            BufferedImage bufferedImage = pdfRenderer.renderImageWithDPI(0, 300);
//            String pngFilePath = pdfFilePath.replace(".pdf", ".png");
//            ImageIO.write(bufferedImage, "PNG", new File(pngFilePath));
//            return pngFilePath;
//        }
//    }
//}




package com.example.word2pdfpng;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;

@RestController
@RequestMapping("/api/files")
public class FileUploadController {

    private final FileConverterService fileConverterService;

    public FileUploadController(FileConverterService fileConverterService) {
        this.fileConverterService = fileConverterService;
    }

    @PostMapping("/upload")
    public ResponseEntity<String> handleFileUpload(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Please upload a file!");
        }

        try {
            // Save the uploaded file to the local file system
            String uploadedFilePath = fileConverterService.saveUploadedFile(file);

            // Convert the file to PDF
            String pdfFilePath = fileConverterService.convertDocxToPdf(uploadedFilePath);

            // Convert the PDF to PNG
            String pngFilePath = fileConverterService.convertPdfToPng(pdfFilePath);

            // Return the file download URI
            String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/api/files/download/")
                    .path(Paths.get(pngFilePath).getFileName().toString())
                    .toUriString();

            return ResponseEntity.ok("File converted successfully: " + fileDownloadUri);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("File conversion failed: " + e.getMessage());
        }
    }
}
