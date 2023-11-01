package com.app.service.pdf;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;

@Service
public class PdfServiceImpl implements PdfService{
    @Override
    public byte[] generateMail(Paragraph title, Paragraph content) {
        Document document = new Document();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            PdfWriter.getInstance(document, baos);
            document.open();
            document.add(title);
            document.add(content);
            document.close();
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        return baos.toByteArray();
    }
    public Paragraph generateParagraph(String content, Font font, int alignment){
        Paragraph data = new Paragraph(content, font);
        data.setAlignment(alignment);
        return data;
    }



}
