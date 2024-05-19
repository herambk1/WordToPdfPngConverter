//package com.example.word2pdfpng;
//
//import org.apache.poi.xwpf.usermodel.XWPFDocument;
//import org.apache.pdfbox.pdmodel.PDDocument;
//import org.apache.pdfbox.pdmodel.PDPage;
//import org.apache.pdfbox.pdmodel.PDPageContentStream;
//import org.apache.pdfbox.pdmodel.font.PDType1Font;
//import org.apache.pdfbox.rendering.PDFRenderer;
//import org.springframework.stereotype.Service;
//
//import javax.imageio.ImageIO;
//import java.awt.image.BufferedImage;
//import java.io.*;
//
//@Service
//public class FileConverterService {
//
//    public File convertWordToPdf(File wordFile) throws IOException {
//        try (FileInputStream fis = new FileInputStream(wordFile);
//             XWPFDocument document = new XWPFDocument(fis);
//             PDDocument pdfDoc = new PDDocument()) {
//
//            PDPage page = new PDPage();
//            pdfDoc.addPage(page);
//
//            PDPageContentStream contentStream = new PDPageContentStream(pdfDoc, page);
//            contentStream.beginText();
//            contentStream.setFont(PDType1Font.HELVETICA, 12);
//            contentStream.newLineAtOffset(25, 700);
//
//            document.getParagraphs().forEach(p -> {
//                try {
//                    contentStream.showText(p.getText());
//                    contentStream.newLineAtOffset(0, -15);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            });
//
//            contentStream.endText();
//            contentStream.close();
//
//            File pdfFile = new File(wordFile.getParent(), wordFile.getName().replace(".docx", ".pdf"));
//            pdfDoc.save(pdfFile);
//            return pdfFile;
//        }
//    }
//
//    public File convertPdfToPng(File pdfFile) throws IOException {
//        try (PDDocument document = PDDocument.load(pdfFile)) {
//            PDFRenderer pdfRenderer = new PDFRenderer(document);
//            BufferedImage bim = pdfRenderer.renderImageWithDPI(0, 300);
//
//            File pngFile = new File(pdfFile.getParent(), pdfFile.getName().replace(".pdf", ".png"));
//            ImageIO.write(bim, "png", pngFile);
//            return pngFile;
//        }
//    }
//}




package com.example.word2pdfpng;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

@Service
public class FileConverterService {

    public String saveUploadedFile(MultipartFile file) throws IOException {
        String uploadDir = "uploads/";
        Files.createDirectories(Paths.get(uploadDir));
        String filePath = uploadDir + file.getOriginalFilename();
        try (FileOutputStream fos = new FileOutputStream(filePath)) {
            fos.write(file.getBytes());
        }
        return filePath;
    }

    public String convertDocxToPdf(String docxFilePath) throws IOException {
        try (XWPFDocument document = new XWPFDocument(Files.newInputStream(Paths.get(docxFilePath)));
             PDDocument pdfDocument = new PDDocument()) {
            PDPage page = new PDPage();
            pdfDocument.addPage(page);
            try (PDPageContentStream contentStream = new PDPageContentStream(pdfDocument, page)) {
                contentStream.setFont(PDType1Font.HELVETICA, 12);
                contentStream.beginText();
                contentStream.setLeading(14.5f);
                contentStream.newLineAtOffset(25, 700);
                document.getParagraphs().forEach(p -> {
                    try {
                        contentStream.showText(p.getText());
                        contentStream.newLine();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
                contentStream.endText();
            }
            String pdfFilePath = docxFilePath.replace(".docx", ".pdf");
            pdfDocument.save(pdfFilePath);
            return pdfFilePath;
        }
    }

    public String convertPdfToPng(String pdfFilePath) throws IOException {
        try (PDDocument document = PDDocument.load(new File(pdfFilePath))) {
            if (document.getNumberOfPages() == 0) {
                throw new IOException("PDF document contains no pages.");
            }

            // Convert PDF to PNG
            PDFRenderer pdfRenderer = new PDFRenderer(document);
            BufferedImage bim = pdfRenderer.renderImageWithDPI(0, 300);

            String pngFilePath = pdfFilePath.replace(".pdf", ".png");
            File pngFile = new File(pngFilePath);
            if (!pngFile.exists()) {
                pngFile.createNewFile();
            }
            ImageIO.write(bim, "png", pngFile);
            return pngFilePath;
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }
    }
}
