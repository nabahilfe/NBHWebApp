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

        <h3>Textbaustein Vorschau: ${textContentForm.getContentDescription()}</h3>

        <form action="/textcontent/preview" method="post" class="mb-4">
            ${csrfHiddenInput}

             <div class="card-body">
                 <div class="border rounded p-3" style="min-height: 120px;">
                    $unsafe{textContentForm.getHtmlText()}
                 </div>
             </div>

            <div class="d-flex gap-2">
                <button type="submit" class="btn btn-secondary">Vorschau</button>
                <button type="submit" formaction="/textcontent/save" class="btn btn-success">Speichern</button>
            </div>
        </form>

    </div>

 `)