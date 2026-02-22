@import eu.nabahilfe.webapp.textcontent.TextContentForm

@param TextContentForm textContentForm = null


@param java.lang.String successMessage = null
@param java.lang.String errorMessage = null

@param eu.nabahilfe.webapp.security.CsrfHiddenInput csrfHiddenInput
@param eu.nabahilfe.webapp.security.ViewContext ctxt


@template.layout.mainpage(
    csrfHiddenInput = csrfHiddenInput,
    ctxt = ctxt,
    content = @`

    <div class="container-fluid">

        <h3>Textbaustein Vorschau für: ${textContentForm.getContentDescription()}</h3>

        <form action="/textcontent/save" method="post" class="mb-4">
            ${csrfHiddenInput}

            <input type="hidden" name="contentCode" value="${textContentForm.getContentCode()}">
            <input type="hidden" name="contentDescription" value="${textContentForm.getContentDescription()}">
            <input type="hidden" name="htmlText" value="${textContentForm.getHtmlText()}">
            <input type="hidden" name="mdText" value="${textContentForm.getMdText()}">

            <div class="card-body">
                <div class="border rounded p-3" style="min-height: 120px;">
                    $unsafe{textContentForm.getHtmlText()}
                </div>
            </div>

            <div class="d-flex gap-2">
                <button type="button" class="btn btn-outline-secondary" onclick="history.back()">Zurück</button>
                <button type="submit" class="btn btn-success">Speichern</button>
            </div>
        </form>



    </div

 `)