<!--
  Copyright (c) 2025-2026 Maximilian Weissboeck
  Licensed under the MIT License (see LICENSE file).
-->

@import eu.nabahilfe.webapp.GlobalModelAttributes
@import eu.nabahilfe.webapp.media.documents.Library
@import eu.nabahilfe.webapp.media.documents.LibraryDocumentInfo
@import static eu.nabahilfe.webapp.jte.TemplateHelpers.*

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
                <table class="table table-bordered table-hover table-sm shadow h-100">
                    <thead class="table-light">
                        <tr>
                            <th scope="col">Dateiname</th>
                            <th scope="col">Beschreibung</th>
                            @if (!Boolean.TRUE.equals(readOnly))
                                <th scope="col" class="text-center">Edit</th>
                            @endif
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
                                @if (!Boolean.TRUE.equals(readOnly))
                                    <td class="text-center">
                                        <button type="button"
                                                class="btn ${document.description() != null && !document.description().isBlank() ? "btn-outline-success" : "btn-outline-primary"} btn-sm"
                                                data-bs-toggle="modal"
                                                data-bs-target="#editDocumentDescriptionModal"
                                                data-document-id="${document.id()}"
                                                title="Beschreibung bearbeiten"
                                                aria-label="Beschreibung bearbeiten">
                                            <i class="bi bi-pencil"></i>
                                        </button>
                                        <input type="hidden"
                                               id="documentDescription_${document.id()}"
                                               value="${document.description() != null ? document.description() : ""}">
                                    </td>
                                @endif
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

                @if (!Boolean.TRUE.equals(readOnly))
                    <div class="modal fade" id="editDocumentDescriptionModal" tabindex="-1" aria-hidden="true">
                        <div class="modal-dialog">
                            <div class="modal-content">
                                <div class="modal-header">
                                    <h5 class="modal-title">Dokumentbeschreibung bearbeiten</h5>
                                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                                </div>
                                <div class="modal-body">
                                    <input type="hidden" id="documentIdToEditDescription">
                                    <label for="documentDescriptionInput" class="form-label">Beschreibung</label>
                                    <textarea class="form-control"
                                              id="documentDescriptionInput"
                                              maxlength="250"
                                              rows="4"
                                              placeholder="Beschreibung für dieses Dokument"></textarea>
                                </div>
                                <div class="modal-footer">
                                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Abbrechen</button>
                                    <button type="button" class="btn btn-primary" id="confirmDocumentDescriptionBtn">Speichern</button>
                                </div>
                            </div>
                        </div>
                    </div>

                    <form id="documentDescriptionForm" method="post" style="display:none;">
                        ${csrfHiddenInput}
                        <input type="hidden" name="description" id="documentDescriptionFormValue">
                    </form>
                @endif
        @endif

        @if (Boolean.TRUE.equals(readOnly))
            @template.components.display-statusmessage(
                successMessage,
                errorMessage
            )
        @endif

        @if (!Boolean.TRUE.equals(readOnly))
            <script>
                document.addEventListener("DOMContentLoaded", () => {
                    const descriptionModal = document.getElementById("editDocumentDescriptionModal");
                    const descriptionForm = document.getElementById("documentDescriptionForm");
                    const confirmDescriptionBtn = document.getElementById("confirmDocumentDescriptionBtn");
                    const documentIdToEditDescription = document.getElementById("documentIdToEditDescription");
                    const documentDescriptionInput = document.getElementById("documentDescriptionInput");
                    const documentDescriptionFormValue = document.getElementById("documentDescriptionFormValue");

                    if (!descriptionModal || !descriptionForm || !confirmDescriptionBtn
                            || !documentIdToEditDescription || !documentDescriptionInput || !documentDescriptionFormValue) {
                        return;
                    }

                    descriptionModal.addEventListener("show.bs.modal", event => {
                        const trigger = event.relatedTarget;
                        const documentId = trigger ? trigger.getAttribute("data-document-id") : null;
                        documentIdToEditDescription.value = documentId || "";

                        const descriptionHolder = documentId ? document.getElementById("documentDescription_" + documentId) : null;
                        documentDescriptionInput.value = descriptionHolder ? descriptionHolder.value : "";
                    });

                    confirmDescriptionBtn.addEventListener("click", () => {
                        const documentId = documentIdToEditDescription.value;
                        if (!documentId) {
                            return;
                        }

                        documentDescriptionFormValue.value = documentDescriptionInput.value;
                        descriptionForm.action = "/library/${library.getId()}/document/" + documentId + "/description";
                        descriptionForm.submit();
                    });
                });
            </script>
        @endif

    </div>
`)
