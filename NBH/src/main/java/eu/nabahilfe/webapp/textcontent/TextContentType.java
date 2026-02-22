package eu.nabahilfe.webapp.textcontent;

public enum TextContentType {

    // NEVER EVER CHANGE THE CODE VALUES, AS THEY ARE USED TO IDENTIFY THE CONTENT IN THE DATABASE
    // But you can change the display values (the ones in brackets) as you like, they are only used for display purposes in the UI

    ABOUT_US("Über uns"),
    CONTACT("Kontakt"),
    EVENTS("Termine"),
    NEWS("Aktuelles"),
    FAQ("Häufige Fragen"),
    TERMS_OF_SERVICE("Nutzungsbedingungen"),
    PRIVACY_POLICY("Datenschutzbestimmungen"),
    LEGAL_NOTICE("Impressum");

    private final String code;

    TextContentType(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }


    String test() {
        return TextContentType.ABOUT_US.toString();
    }

}
