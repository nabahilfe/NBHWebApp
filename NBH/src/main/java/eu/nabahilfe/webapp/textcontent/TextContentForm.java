package eu.nabahilfe.webapp.textcontent;

public class TextContentForm {

    private String contentCode;
    private String contentDescription;

    private String mdText;
    private String htmlText;



    public String getContentCode() {
        return contentCode;
    }

    public void setContentCode(String contentCode) {
        this.contentCode = contentCode;
    }

    public String getMdText() {
        return mdText != null ? mdText : "";
    }

    public void setMdText(String mdText) {
        this.mdText = mdText;
    }

    public String getHtmlText() {
        return htmlText != null ? htmlText : "";
    }

    public void setHtmlText(String htmlText) {
        this.htmlText = htmlText;
    }

    public String getContentDescription() {
        return contentDescription;
    }

    public void setContentDescription(String contentDescription) {
        this.contentDescription = contentDescription;
    }


    @Override
    public String toString() {
        return "TextContentForm [contentCode=" + contentCode + ", contentDescription=" + contentDescription + ", mdText="
                + mdText + ", htmlText=" + htmlText + "]";
    }

}
