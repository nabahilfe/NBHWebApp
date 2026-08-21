<!--
  Copyright (c) 2025-2026 Maximilian Weissboeck
  Licensed under the MIT License (see LICENSE file).
-->

@import eu.nabahilfe.webapp.GlobalModelAttributes
@import eu.nabahilfe.webapp.media.images.Gallery
@import static eu.nabahilfe.webapp.DateFormatter.*

@param java.util.List<Gallery> galleries

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

    <h3>${(ctxt == null || !ctxt.isAuthenticated()) ? "Öffentliche " : "Alle "}Foto-Galerien</h3>

    <table class="table table-bordered table-hover table-sm shadow h-100" id="galleryTable">
        <thead class="table-light">
            <tr>
                <th scope="col">Titel</th>
                <th scope="col">Datum</th>
                <th scope="col">Beschreibung</th>
            </tr>
        </thead>

        <tbody>
            @for (Gallery gallery : galleries)
                <tr>
                    <td>
                        <a class="text-decoration-none" href="/gallery/user/${gallery.getId()}">${gallery.getDescription()}</a>
                    </td>
                    <td>${gallery.getGalleryDate() != null ? dateReverseDE(gallery.getGalleryDate()) : "Diverse"}</td>
					<td>${gallery.getRemark() != null ? gallery.getRemark() : ""}</td>
                </tr>
            @endfor
        </tbody>
    </table>

    @if (ctxt == null || !ctxt.isAuthenticated())
        <p class="text-muted small mt-3">Für angemeldete Mitglieder gib es möglicherweise weitere Foto-Galerien.</p>
    @endif

    @template.components.display-statusmessage(successMessage, errorMessage)

`)
