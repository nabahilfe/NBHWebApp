<!--
  Copyright (c) 2025–2026 Maximilian Weißböck
  Licensed under the MIT License (see LICENSE file).
-->

@import eu.nabahilfe.webapp.GlobalModelAttributes
@import eu.nabahilfe.webapp.textcontent.TextContentForm

@param TextContentForm textContentForm = null

@param java.lang.String successMessage = null
@param java.lang.String errorMessage = null

@param eu.nabahilfe.webapp.security.CsrfHiddenInput csrfHiddenInput
@param eu.nabahilfe.webapp.security.ViewContext ctxt
@param GlobalModelAttributes globalModelAttributes


@template.layout.mainpage(
    globalModelAttributes = globalModelAttributes,
    csrfHiddenInput = csrfHiddenInput,
    ctxt = ctxt,
    content = @`


    <div class="container-fluid">
        <h2>${textContentForm.getContentDescription()}</h2>

        <form action="/textcontent/preview" method="post" class="mb-4">
            ${csrfHiddenInput}

            <input type="hidden" name="contentCode" value="${textContentForm.getContentCode()}">
            <input type="hidden" name="contentDescription" value="${textContentForm.getContentDescription()}">
            <input type="hidden" name="htmlText" value="${textContentForm.getHtmlText()}">

             <div class="card">
                <div class="card-header d-flex justify-content-between align-items-center">
                    <span>Markdown Text</span>
                </div>
            </div>

            <textarea class="form-control" id="mdText" name="mdText" rows="16" required>${textContentForm.getMdText()}</textarea>
            <div class="form-text">Nutze Markdown für Formatierung. Vorschau zeigt dir den erzeugten HTML Text an, den du dann auch veröffentliche kannst.</div>

            <br/>

            <div class="d-flex gap-2">
                <button type="submit" class="btn btn-secondary">Vorschau</button>
            </div>

        </form>


        <!-- display current html content -->
         <div class="card">
             <div class="card-header d-flex justify-content-between align-items-center">
                 <span>Vorschau des <strong>veröffentlichten</strong> Texts</span>
             </div>
             <div class="card-body">
                $unsafe{textContentForm.getHtmlText()}
             </div>
         </div>

    </div>

  `)