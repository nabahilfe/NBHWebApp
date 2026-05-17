/*
 * Copyright (c) 2025–2026 Maximilian Weißböck
 * Licensed under the MIT License (see LICENSE file).
 */

package eu.nabahilfe.webapp;


/**
 * Contains constant definitions for NBH
 */
public final class NbhConst {

    public static final int MIN_MEMBER_AGE = 18;

    public static final int PAGINATION_PAGE_SIZE = 15;

    public static final int FIRST_TIME_CHEQUE_HOURS = 5;
    public static final int REGULAR_TIME_CHEQUE_HOURS = 10;


    public static final int MAX_LEN_CODE = 10;
    public static final int MAX_LEN_LONG_CODE = 20;
    public static final int MAX_LEN_NAME = 80;
    public static final int MAX_LEN_SHORT_STRING = 250;
    public static final int MAX_LEN_LONG_STRING = 4000;

    public static final int REGISTRATION_CODE_TTL = 15; // in minutes

    public static final Integer START_MEMBER_NUMBER = 1000;

    public static final Integer MIN_HOURS_FOR_TIME_CHEQUE = 5;


    public static final String ACCOUNTING_INCOMING = "INCOMING";
    public static final String ACCOUNTING_OUTGOING = "OUTGOING";


    public static final String SOZIALKONTO_SALUTATION = "Sozialkonto";
    public static final String SOZIALKONTO_FIRST_NAME = "Sozialkonto";
    public static final String SOZIALKONTO_LAST_NAME = "Nachbarschaftshilfe";

    
    
    public static final String ADMIN_ROLE_NAME = "System-Administrator";
    public static final String ADMIN_EMAIL = "webmaster@nabahilfe.eu";
    public static final String ADMIN_ACCOUNT_FIRST_NAME = "Administrator";
    public static final String ADMIN_ACCOUNT_LAST_NAME = "System";
    

}
