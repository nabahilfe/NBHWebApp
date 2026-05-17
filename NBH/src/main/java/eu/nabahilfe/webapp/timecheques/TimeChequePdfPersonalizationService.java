/*
 * Copyright (c) 2025–2026 Maximilian Weißböck
 * Licensed under the MIT License (see LICENSE file).
 */

package eu.nabahilfe.webapp.timecheques;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.lowagie.text.pdf.AcroFields;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfStamper;

public class TimeChequePdfPersonalizationService {


    public void personalizeTimeCheque(String memberName, String memberNumber, OutputStream output) throws IOException {

        InputStream is = getClass().getResourceAsStream("/pdf/ZeitScheck.pdf");
        if (is == null)
            throw new RuntimeException("ZeitScheck.pdf template not found!");

        PdfReader reader = new PdfReader(is);
        PdfStamper stamper = new PdfStamper(reader, output);

        AcroFields fields = stamper.getAcroFields();

        fields.setField("member_name", memberName);
        fields.setField("member_nmbr", memberNumber);

        stamper.setFormFlattening(true);

        stamper.close();
        reader.close();
    }



}
