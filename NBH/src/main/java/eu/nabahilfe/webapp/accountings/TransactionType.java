package eu.nabahilfe.webapp.accountings;

public enum TransactionType {

    // Never Ever Change this names! They are used in the database!

    INCOME("Einnahme"),     // z.B. Verkauf eines Zeitschecks, Mitgliedsbeitrag, ...
    EXPENSE("Ausgabe");     // z.B. Weihnachtsessen, Spende, Büromaterial, ...

    private final String code;

    TransactionType(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

}

