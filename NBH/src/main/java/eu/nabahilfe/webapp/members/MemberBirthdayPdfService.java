/*
 * Copyright (c) 2025–2026 Maximilian Weißböck
 * Licensed under the MIT License (see LICENSE file).
 */

package eu.nabahilfe.webapp.members;

import static eu.nabahilfe.webapp.system.DateFormatter.dateReverseDE;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;

import org.springframework.stereotype.Service;

import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;

@Service
public class MemberBirthdayPdfService {

    private static final Locale LOCALE_AT = Locale.of("de", "AT");

    public byte[] createPdf(List<MemberBirthdayForm> birthdays, int year, int month) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        try (Document document = new Document(new PdfDocument(new PdfWriter(output)), PageSize.A4)) {
            PdfFont regular = PdfFontFactory.createFont(StandardFonts.HELVETICA);
            PdfFont bold = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);
            String monthName = Month.of(month).getDisplayName(TextStyle.FULL, LOCALE_AT);

            document.setMargins(36f, 36f, 36f, 36f);
            document.setFont(regular).setFontSize(11f);
            document.add(new Paragraph("NBH Geburtstagsliste für " + monthName + " " + year)
                    .setFont(bold)
                    .setFontSize(16f)
                    .setMarginBottom(18f));
            document.add(buildTable(birthdays, bold));
        } catch (IOException e) {
            throw new UncheckedIOException("Birthday PDF creation failed", e);
        }
        return output.toByteArray();
    }

    private Table buildTable(List<MemberBirthdayForm> birthdays, PdfFont bold) {
        Table table = new Table(UnitValue.createPercentArray(new float[] { 50, 25, 25 }))
                .useAllAvailableWidth();
        for (String heading : List.of("Name", "Geburtsdatum", "Alter")) {
            Cell headerCell = new Cell()
                    .add(new Paragraph(heading).setFont(bold))
                    .setBackgroundColor(ColorConstants.LIGHT_GRAY);
            if (!heading.equals("Name")) {
                headerCell.setTextAlignment(TextAlignment.CENTER);
            }
            table.addHeaderCell(headerCell);
        }

        if (birthdays.isEmpty()) {
            table.addCell(new Cell(1, 3).add(new Paragraph("Keine Geburtstage."))
                    .setTextAlignment(TextAlignment.CENTER));
        } else {
            for (MemberBirthdayForm birthday : birthdays) {
                table.addCell(new Cell().add(new Paragraph(birthday.getName())));
                table.addCell(new Cell().add(new Paragraph(dateReverseDE(birthday.getBirthdate())))
                        .setTextAlignment(TextAlignment.CENTER));
                table.addCell(new Cell().add(new Paragraph(Integer.toString(birthday.getAge())))
                        .setTextAlignment(TextAlignment.CENTER));
            }
        }
        return table;
    }
}