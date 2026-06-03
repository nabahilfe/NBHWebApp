/*
 * Copyright (c) 2025–2026 Maximilian Weißböck
 * Licensed under the MIT License (see LICENSE file).
 */

package eu.nabahilfe.webapp;

import java.text.NumberFormat;
import java.util.Locale;


/**
 * Quick and dirty hack to display correct € format
 */
public class NumberFormatter {

    static NumberFormat formatter = NumberFormat.getNumberInstance(Locale.GERMAN);

    static {
        formatter.setMinimumFractionDigits(2);
        formatter.setMaximumFractionDigits(2);
    }

    public static String numberDE(Number n) {
        return formatter.format(n);
    }

}
