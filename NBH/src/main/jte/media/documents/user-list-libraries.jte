<!--
  Copyright (c) 2025-2026 Maximilian Weissboeck
  Licensed under the MIT License (see LICENSE file).
-->

@import eu.nabahilfe.webapp.GlobalModelAttributes
@import eu.nabahilfe.webapp.media.documents.Library

@param java.util.List<Library> libraries

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

    <h3>${(ctxt == null || !ctxt.isAuthenticated()) ? "Öffentliche " : "Alle "}Dokumente</h3>

    <table class="table table-bordered table-hover table-sm shadow h-100" id="libraryTable">
        <thead class="table-light">
            <tr>
                <th scope="col">Titel</th>
                <th scope="col">Beschreibung</th>
            </tr>
        </thead>

        <tbody>
            @for (Library library : libraries)
                <tr>
                    <td>
                        <a class="text-decoration-none" href="/library/user/${library.getId()}">${library.getDescription()}</a>
                    </td>
                    <td>${library.getRemark() != null ? library.getRemark() : ""}</td>
                </tr>
            @endfor
        </tbody>
    </table>

    @if (ctxt == null || !ctxt.isAuthenticated())
        <p class="text-muted small mt-3">Für angemeldete Mitglieder gibt es möglicherweise weitere Dokumente.</p>
    @endif

    @template.components.display-statusmessage(successMessage, errorMessage)

`)
