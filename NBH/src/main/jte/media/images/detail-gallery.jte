<!--
  Copyright (c) 2025-2026 Maximilian Weissboeck
  Licensed under the MIT License (see LICENSE file).
-->

@import eu.nabahilfe.webapp.GlobalModelAttributes
@import eu.nabahilfe.webapp.media.images.GalleryForm

@import java.lang.Long

@param GalleryForm galleryForm
@param Long galleryId = null

@param java.lang.String successMessage = null
@param java.lang.String errorMessage = null

@param String fullUsername = null
@param String username = null
@param Boolean isAuthenticated = false

@param eu.nabahilfe.webapp.security.CsrfHiddenInput csrfHiddenInput
@param eu.nabahilfe.webapp.security.ViewContext ctxt
@param GlobalModelAttributes globalModelAttributes

@template.layout.mainpage(
    globalModelAttributes = globalModelAttributes,
    csrfHiddenInput = csrfHiddenInput,
    ctxt = ctxt,
    content = @`

    <h3>${galleryId == null ? "Neue Galerie anlegen" : "Galerie bearbeiten"}</h3>
    <br/>

    <div class="row">
        <form action="${galleryId == null ? "/gallery" : "/gallery/" + galleryId}" method="post">
            ${csrfHiddenInput}

            <div class="form-group row">
                <label for="description" class="col-sm-2 col-form-label">Beschreibung <span class="text-danger">*</span></label>
                <div class="col-sm-10">
                    <input type="text"
                           class="form-control"
                           name="description"
                           id="description"
                           maxlength="250"
                           placeholder="Kurzbeschreibung der Galerie"
                           value="${galleryForm.getDescription() != null ? galleryForm.getDescription() : ""}"
                           required>
                </div>
            </div>

            <br/>

            <div class="form-group row">
                <label for="galleryDate" class="col-sm-2 col-form-label">Galerie-Datum</label>
                <div class="col-sm-4">
                    <input type="date"
                           class="form-control"
                           name="galleryDate"
                           id="galleryDate"
                           value="${galleryForm.getGalleryDate() != null ? galleryForm.getGalleryDate().toString() : ""}">
                </div>

                <label for="showGalleryFrom" class="col-sm-2 col-form-label">Anzeigen von</label>
                <div class="col-sm-4">
                    <input type="date"
                           class="form-control"
                           name="showGalleryFrom"
                           id="showGalleryFrom"
                           value="${galleryForm.getShowGalleryFrom() != null ? galleryForm.getShowGalleryFrom().toString() : ""}">
                </div>
            </div>

            <br/>

            <div class="form-group row">
                <label for="showGalleryTo" class="col-sm-2 col-form-label">Anzeigen bis</label>
                <div class="col-sm-4">
                    <input type="date"
                           class="form-control"
                           name="showGalleryTo"
                           id="showGalleryTo"
                           value="${galleryForm.getShowGalleryTo() != null ? galleryForm.getShowGalleryTo().toString() : ""}">
                </div>

                <label for="isPublic" class="col-sm-2 col-form-label">Sichtbarkeit</label>
                <div class="col-sm-4 d-flex align-items-center">
                    <div class="form-check">
                        <input type="hidden" name="_isPublic" value="on"/>
                        <input class="form-check-input" type="checkbox" id="isPublic" name="isPublic"
                               checked="${Boolean.TRUE.equals(galleryForm.getIsPublic())}">
                        <label class="form-check-label" for="isPublic">Oeffentlich sichtbar</label>
                    </div>
                </div>
            </div>

            <br/>

            <div class="form-group row">
                <label for="remark" class="col-sm-2 col-form-label">Anmerkung</label>
                <div class="col-sm-10">
                    <textarea class="form-control"
                              name="remark"
                              id="remark"
                              maxlength="250"
                              rows="3"
                              placeholder="Interne Notiz">${galleryForm.getRemark() != null ? galleryForm.getRemark() : ""}</textarea>
                </div>
            </div>

            <br/>

            <div class="form-group row">
                <div class="col-sm-10">
                    <button type="submit" class="btn btn-primary bi-floppy"> Speichern</button>
                    <a href="${galleryId == null ? "/gallery/list" : "/gallery/" + galleryId}" class="btn btn-outline-secondary">Abbrechen</a>
                </div>
            </div>

            <br/>

            @template.components.display-statusmessage(successMessage, errorMessage)
        </form>
    </div>

`)
