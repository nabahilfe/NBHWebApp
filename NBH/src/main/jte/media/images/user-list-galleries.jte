<!--
  Copyright (c) 2025-2026 Maximilian Weissboeck
  Licensed under the MIT License (see LICENSE file).
-->

@import eu.nabahilfe.webapp.GlobalModelAttributes
@import eu.nabahilfe.webapp.media.images.Gallery

@param java.util.List<Gallery> galleries

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

    <h3>Bildgalerien</h3>

    <table class="table table-bordered table-hover table-sm shadow h-100" id="galleryTable">
        <thead class="table-light">
            <tr>
                <th scope="col">Beschreibung</th>
                <th scope="col">Galerie-Datum</th>
                <th scope="col">Anzeigen von</th>
                <th scope="col">Anzeigen bis</th>
                <th scope="col">Sichtbarkeit</th>
            </tr>
        </thead>

        <tbody>
            @for (Gallery gallery : galleries)
                <tr>
                    <td>
                        <a class="text-decoration-none" href="/gallery/user/${gallery.getId()}">${gallery.getDescription()}</a>
                    </td>
                    <td>${gallery.getGalleryDate() != null ? gallery.getGalleryDate().toString() : ""}</td>
                    <td>${gallery.getShowGalleryFrom() != null ? gallery.getShowGalleryFrom().toString() : ""}</td>
                    <td>${gallery.getShowGalleryTo() != null ? gallery.getShowGalleryTo().toString() : ""}</td>
                    <td>${Boolean.TRUE.equals(gallery.getIsPublic()) ? "Öffentlich" : "Nur Mitglieder"}</td>
                </tr>
            @endfor
        </tbody>
    </table>

    @template.components.display-statusmessage(successMessage, errorMessage)

`)
