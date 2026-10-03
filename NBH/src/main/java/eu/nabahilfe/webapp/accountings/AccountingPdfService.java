/*
 * Copyright (c) 2025–2026 Maximilian Weißböck
 * Licensed under the MIT License (see LICENSE file).
 */

package eu.nabahilfe.webapp.accountings;

import static eu.nabahilfe.webapp.system.DateFormatter.dateReverseDE;
import static eu.nabahilfe.webapp.system.NumberFormatter.numberDE;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.math.BigDecimal;
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
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.layout.Canvas;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Tab;
import com.itextpdf.layout.element.TabStop;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.properties.TabAlignment;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;

@Service
public class AccountingPdfService {

    private static final Locale LOCALE_AT = Locale.of("de", "AT");
    private static final float MARGIN = 36f;
    private static final float TOP_MARGIN = 70f;
    private static final float FONT_SIZE = 9f;

    public String monthLabel(int month) {
        return month >= 1 && month <= 12
                ? Month.of(month).getDisplayName(TextStyle.FULL, LOCALE_AT)
                : "Alle";
    }

    public String transactionTypeLabel(String transactionType) {
        return TransactionType.EXPENSE.name().equals(transactionType) ? "Ausgaben" : "Einnahmen";
    }

    public byte[] createPdf(List<AccountingEntry> entries, BigDecimal total, int year, int month,
            String transactionType, String accountableClass) {

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        // immediateFlush=false keeps pages open so headers can show the total page count
        try (Document document = new Document(new PdfDocument(new PdfWriter(out)), PageSize.A4.rotate(), false)) {
            PdfDocument pdfDoc = document.getPdfDocument();

            // Standard fonts are bound to a single PdfDocument and cannot be cached statically
            PdfFont regular = PdfFontFactory.createFont(StandardFonts.HELVETICA);
            PdfFont bold = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);

            document.setMargins(TOP_MARGIN, MARGIN, MARGIN, MARGIN);
            document.setFont(regular).setFontSize(FONT_SIZE);
            document.add(buildTable(entries, total, bold));

            String title = year + " " + transactionTypeLabel(transactionType) + " NBH Maria Anzbach";
            String classLabel = accountableClass == null || accountableClass.isBlank() ? "Alle" : accountableClass;
            int totalPages = pdfDoc.getNumberOfPages();
            for (int i = 1; i <= totalPages; i++) {
                drawHeader(pdfDoc, pdfDoc.getPage(i), i, totalPages, title, monthLabel(month), classLabel,
                        regular, bold);
            }
        } catch (IOException e) {
            throw new UncheckedIOException("PDF creation failed", e);
        }
        return out.toByteArray();
    }

    private Table buildTable(List<AccountingEntry> entries, BigDecimal total, PdfFont bold) {
        Table table = new Table(UnitValue.createPercentArray(new float[] { 8, 12, 12, 13, 25, 8, 12, 10 }))
                .useAllAvailableWidth();

        for (String header : List.of("Datum", "Buchung von", "Art", "Mitglied", "Beschreibung", "Transaktion",
                "Veranlasst von", "Betrag")) {
            Cell cell = new Cell().add(new Paragraph(header).setFont(bold))
                    .setBackgroundColor(ColorConstants.LIGHT_GRAY);
            if ("Betrag".equals(header)) {
                cell.setTextAlignment(TextAlignment.RIGHT);
            }
            table.addHeaderCell(cell);
        }

        if (entries.isEmpty()) {
            table.addCell(new Cell(1, 8).add(new Paragraph("Keine Einträge gefunden."))
                    .setTextAlignment(TextAlignment.CENTER));
        } else {
            for (AccountingEntry e : entries) {
                table.addCell(textCell(dateReverseDE(e.getAccountingDate())));
                table.addCell(textCell(e.getCreatedBy() != null ? e.getCreatedBy().getName() : ""));
                table.addCell(textCell(e.getAccountableName()));
                table.addCell(textCell(e.getAccountableMember() != null ? e.getAccountableMember().getName() : ""));
                table.addCell(textCell(e.getDescription()));
                table.addCell(textCell(dateReverseDE(e.getTransactionDate())));
                table.addCell(textCell(e.getLiableMemberName()));
                table.addCell(textCell("€ " + numberDE(e.getTransactionAmount()))
                        .setTextAlignment(TextAlignment.RIGHT));
            }
        }

        table.addCell(new Cell(1, 7).add(new Paragraph("Summe").setFont(bold))
                .setTextAlignment(TextAlignment.RIGHT));
        table.addCell(new Cell().add(new Paragraph("€ " + numberDE(total)).setFont(bold))
                .setTextAlignment(TextAlignment.RIGHT));

        return table;
    }

    private static Cell textCell(String value) {
        return new Cell().add(new Paragraph(value != null ? value : ""));
    }

    private static void drawHeader(PdfDocument pdfDoc, PdfPage page, int pageNumber, int totalPages, String title,
            String monthLabel, String classLabel, PdfFont regular, PdfFont bold) {
        Rectangle pageSize = page.getPageSize();
        float width = pageSize.getWidth() - 2 * MARGIN;
        Rectangle area = new Rectangle(MARGIN, pageSize.getTop() - TOP_MARGIN + 10, width, TOP_MARGIN - 30);

        Paragraph header = new Paragraph()
                .addTabStops(new TabStop(width, TabAlignment.RIGHT))
                .add(new Text(title).setFont(bold).setFontSize(16))
                .add(new Text("   Monat: ").setFont(regular).setFontSize(FONT_SIZE))
                .add(new Text(monthLabel).setFont(bold).setFontSize(FONT_SIZE))
                .add(new Text("   Umsatz Art: ").setFont(regular).setFontSize(FONT_SIZE))
                .add(new Text(classLabel).setFont(bold).setFontSize(FONT_SIZE))
                .add(new Tab())
                .add(new Text("Seite " + pageNumber + " von " + totalPages).setFont(regular).setFontSize(FONT_SIZE));

        PdfCanvas pdfCanvas = new PdfCanvas(page.newContentStreamAfter(), page.getResources(), pdfDoc);
        try (Canvas canvas = new Canvas(pdfCanvas, area)) {
            canvas.add(header);
        }
        pdfCanvas.release();
    }
}
