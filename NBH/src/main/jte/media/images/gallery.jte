<!--
  Copyright (c) 2025–2026 Maximilian Weißböck
  Licensed under the MIT License (see LICENSE file).
-->

@import eu.nabahilfe.webapp.GlobalModelAttributes
@import eu.nabahilfe.webapp.media.images.Gallery
@import eu.nabahilfe.webapp.media.images.GalleryImageInfo

@import java.util.List

@param Gallery gallery
@param List<GalleryImageInfo> images

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

        <!-- Kopfbereich -->
        <div class="d-flex flex-column flex-sm-row
                    justify-content-between align-items-sm-center
                    gap-2 mb-4">

            <div>
                <h3 class="mb-1">${gallery.getDescription()}</h3>

                <div class="text-body-secondary">
                    ${images.size()} Bilder
                </div>
            </div>

            <a href="/gallery"
               class="btn btn-outline-secondary">
                Zurück
            </a>

        </div>


        <!-- Upload -->
        @template.media.images._image-upload(
            gallery = gallery,
            csrfHiddenInput = csrfHiddenInput
        )


        <!-- Galerie -->
        @if (images.isEmpty())

            <div class="alert alert-info">
                <h5 class="alert-heading">
                    Noch keine Bilder
                </h5>

                <p class="mb-0">
                    In dieser Galerie wurden noch keine Bilder
                    hochgeladen.
                </p>
            </div>

        @else

            <div class="row g-2 g-md-3">

                @for (GalleryImageInfo image : images)

                    <div class="col-6 col-md-4 col-lg-3 col-xl-2">

                        <a
                            href="/gallery/${gallery.getId()}/image/${image.id()}"
                            class="d-block">

                            <img
                                src="/gallery/${gallery.getId()}/image/${image.id()}/thumbnail"
                                class="img-fluid rounded shadow-sm"
                                loading="lazy"
                                width="${image.width()}"
                                height="${image.height()}"
                                alt="Bild">

                        </a>

                    </div>

                @endfor

            </div>

        @endif


        @template.components.display-statusmessage(
            successMessage,
            errorMessage
        )

    `
)