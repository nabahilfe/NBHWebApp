<!--
  Copyright (c) 2025-2026 Maximilian Weissboeck
  Licensed under the MIT License (see LICENSE file).
-->

@import eu.nabahilfe.webapp.GlobalModelAttributes
@import eu.nabahilfe.webapp.media.documents.Library
@import eu.nabahilfe.webapp.media.documents.LibraryDocumentInfo

@import java.util.List

@param Library library
@param List<LibraryDocumentInfo> documents
@param Boolean readOnly = false

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

    <div class="container-fluid px-0">

        <div class="d-flex flex-column flex-sm-row justify-content-between align-items-sm-center gap-2 mb-4">
            <div>
                <h3 class="mb-1">${library.getDescription()}</h3>
                @if(library.getRemark() != null)<div><strong>${library.getRemark()}</strong></div>@endif
                <div>${documents.size()} Dokumente</div>
            </div>

            <a href="${Boolean.TRUE.equals(readOnly) ? "/library/user-list" : "/library/list"}" class="btn btn-secondary">
                <i class="bi bi-arrow-left-square"></i> &nbsp; Zurück zu den Bibliotheken
            </a>
        </div>

        @if (!Boolean.TRUE.equals(readOnly))
            @template.media.documents._document-upload(
                library = library,
                csrfHiddenInput = csrfHiddenInput
            )
            @template.components.display-statusmessage(
                successMessage,
                errorMessage
            )
        @endif

        @if (documents.isEmpty())
            <div class="alert alert-info">
                <h5 class="alert-heading">Noch keine Dokumente</h5>
                <p class="mb-0">In dieser Bibliothek wurden noch keine PDF-Dokumente hochgeladen.</p>
            </div>
        @else
            <div class="table-responsive">
                <table class="table table-striped table-hover align-middle shadow-sm">
                    <thead class="table-light">
                        <tr>
                            <th scope="col">Dateiname</th>
                            <th scope="col">Beschreibung</th>
                            <th scope="col" class="text-end">Größe</th>
                            @if (!Boolean.TRUE.equals(readOnly))
                                <th scope="col" class="text-center">Löschen</th>
                            @endif
                        </tr>
                    </thead>
                    <tbody>
                        @for (LibraryDocumentInfo document : documents)
                            <tr>
                                <td>
                                    <a href="/library/${library.getId()}/document/${document.id()}" target="_blank" rel="noopener">
                                        ${document.fileName()}
                                    </a>
                                </td>
                                <td>${document.description() != null ? document.description() : ""}</td>
                                <td class="text-end">${document.documentSize()} Bytes</td>
                                @if (!Boolean.TRUE.equals(readOnly))
                                    <td class="text-center">
                                        <form action="/library/${library.getId()}/document/${document.id()}/delete" method="post" class="d-inline">
                                            ${csrfHiddenInput}
                                            <button type="submit" class="btn btn-outline-danger btn-sm bi-trash"></button>
                                        </form>
                                    </td>
                                @endif
                            </tr>
                        @endfor
                    </tbody>
                </table>
            </div>
        @endif

        @if (Boolean.TRUE.equals(readOnly))
            @template.components.display-statusmessage(
                successMessage,
                errorMessage
            )
        @endif

    </div>
`)
