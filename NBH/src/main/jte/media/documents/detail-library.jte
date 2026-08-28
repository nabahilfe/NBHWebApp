<!--
  Copyright (c) 2025-2026 Maximilian Weissboeck
  Licensed under the MIT License (see LICENSE file).
-->

@import eu.nabahilfe.webapp.GlobalModelAttributes
@import eu.nabahilfe.webapp.media.documents.LibraryForm

@param LibraryForm libraryForm
@param Long libraryId = null

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

    <h3>${libraryId == null ? "Neue Dokument-Bibliothek anlegen" : "Dokument-Bibliothek bearbeiten"}</h3>
    <br/>

    <div class="row">
        <form action="${libraryId == null ? "/library" : "/library/" + libraryId}" method="post">
            ${csrfHiddenInput}

            <div class="row g-3 justify-content-center">
                <div class="col-12 col-xl-6">
                    <div class="card shadow h-100" style="background-color: #ECFEFF;">
                        <div class="card-body">

                            <div class="form-group row g-2 mb-1 align-items-start">
                                <div class="col-sm-8">
                                    <label for="description" class="form-label">Name der Bibliothek <span class="text-danger">*</span></label>
                                    <input type="text"
                                           class="form-control"
                                           name="description"
                                           id="description"
                                           maxlength="250"
                                           placeholder="Sprechender Name fuer Dokument-Bibliothek"
                                           value="${libraryForm.getDescription() != null ? libraryForm.getDescription() : ""}"
                                           required>
                                </div>
                            </div>

                            <br/>

                            <div class="form-group row g-2 mb-1 align-items-start">
                                <div class="col-sm-4">
                                    <label for="showLibraryFrom" class="form-label">Anzeigen von</label>
                                    <input type="date"
                                           class="form-control"
                                           name="showLibraryFrom"
                                           id="showLibraryFrom"
                                           value="${libraryForm.getShowLibraryFrom() != null ? libraryForm.getShowLibraryFrom().toString() : ""}">
                                    <div class="form-text">Bibliothek wird erst ab angegebenem Datum sichtbar</div>
                                </div>
                                <div class="col-sm-4">
                                    <label for="showLibraryTo" class="form-label">Anzeigen bis</label>
                                    <input type="date"
                                           class="form-control"
                                           name="showLibraryTo"
                                           id="showLibraryTo"
                                           value="${libraryForm.getShowLibraryTo() != null ? libraryForm.getShowLibraryTo().toString() : ""}">
                                    <div class="form-text">Bibliothek wird nach angegebenem Datum nicht mehr angezeigt</div>
                                </div>
                                <div class="col-sm-4">
                                    <label for="isPublic" class="form-label">Sichtbarkeit</label>
                                    <div class="form-check pb-2">
                                        <input type="hidden" name="_isPublic" value="on"/>
                                        <input class="form-check-input" type="checkbox" id="isPublic" name="isPublic"
                                               checked="${Boolean.TRUE.equals(libraryForm.getIsPublic())}">
                                        <label class="form-check-label" for="isPublic">Öffentlich sichtbar</label>
                                    </div>
                                    <div class="form-text">Wenn ausgewählt, haben alle Besucher Zugriff</div>
                                </div>
                            </div>

                            <br/>

                            <div class="form-group row g-2 mb-1">
                                <div class="col-12">
                                    <label for="remark" class="form-label">Beschreibung <span class="text-danger">*</span></label>
                                    <textarea class="form-control"
                                              name="remark"
                                              id="remark"
                                              maxlength="250"
                                              rows="3"
                                              placeholder="Beschreibung bzw. weitere Informationen zur Bibliothek"
                                              required>${libraryForm.getRemark() != null ? libraryForm.getRemark() : ""}</textarea>
                                </div>
                            </div>

                            <br/>

                            <div class="form-group row">
                                <div class="col-sm-12">
                                    <button type="submit" class="btn btn-primary bi-floppy"> Speichern</button>
                                    <a href="/library/list" class="btn btn-outline-secondary">Abbrechen</a>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </form>

        <br/>
        @template.components.display-statusmessage(successMessage, errorMessage)

    </div>

`)
