package com.app.service.pdf;

import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;

public interface PdfService {

    byte[] generateMail(Paragraph content, Paragraph title);

    Paragraph generateParagraph(String content, Font font, int aligment);

}
