package edu.qmul.ta;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class SampleResumePdfGenerator {
    public static void main(String[] args) throws IOException {
        Path sourceText = Path.of("demo-data", "sample_resume.txt");
        Path outputPdf = Path.of("demo-data", "sample_resume.pdf");
        List<String> lines = Files.readAllLines(sourceText, StandardCharsets.UTF_8);

        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage();
            document.addPage(page);
            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                contentStream.beginText();
                contentStream.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 12);
                contentStream.setLeading(16);
                contentStream.newLineAtOffset(60, 740);
                for (String line : lines) {
                    contentStream.showText(line);
                    contentStream.newLine();
                }
                contentStream.endText();
            }
            document.save(outputPdf.toFile());
        }

        System.out.println("Generated sample PDF at " + outputPdf);
    }
}
