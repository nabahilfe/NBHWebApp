<!--
  Copyright (c) 2025–2026 Maximilian Weißböck
  Licensed under the MIT License (see LICENSE file).
-->


<!-- FIXME: Wozu ist das hier? Wo wird das verwendet? Wozu ist das da? Löschen!!! -->

@import eu.nabahilfe.webapp.GlobalModelAttributes
@import eu.nabahilfe.webapp.media.images.Gallery
@import eu.nabahilfe.webapp.media.images.GalleryImageInfo

@param Gallery gallery
@param GalleryImageInfo image

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

    <div class="container">

        <div class="d-flex
                    justify-content-between
                    align-items-center
                    mb-3">

            <div>
                <h3 class="mb-1">
                    ${gallery.getDescription()}
                </h3>

                <span class="text-body-secondary">
                    ${image.width()} × ${image.height()} px
                </span>
            </div>

            <a
                href="/gallery/${gallery.getId()}"
                class="btn btn-outline-secondary">

                <span aria-hidden="true">←</span>
                Galerie

            </a>

        </div>


        <div class="text-center">

            <img
                src="/gallery/${gallery.getId()}/image/${image.id()}/data"
                class="img-fluid rounded shadow"
                width="${image.width()}"
                height="${image.height()}"
                alt="Bild">

        </div>


        @template.components.display-statusmessage(
            successMessage,
            errorMessage
        )

    `
)