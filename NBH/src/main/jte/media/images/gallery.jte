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

                @for (int imageIndex = 0; imageIndex < images.size(); imageIndex++)

                    <div class="col-6 col-md-4 col-lg-3 col-xl-2">

                        <a
                            href="#"
                            data-bs-toggle="modal"
                            data-bs-target="#galleryLightboxModal"
                            data-slide-index="${imageIndex}"
                            class="d-block">

                            <img
                                src="/gallery/${gallery.getId()}/image/${images.get(imageIndex).id()}/thumbnail"
                                class="img-fluid rounded shadow-sm"
                                loading="lazy"
                                width="${images.get(imageIndex).width()}"
                                height="${images.get(imageIndex).height()}"
                                alt="Bild">

                        </a>

                    </div>

                @endfor

            </div>


            <div class="modal fade" id="galleryLightboxModal" tabindex="-1" aria-hidden="true">
                <div class="modal-dialog modal-dialog-centered modal-xl gallery-lightbox-dialog">
                    <div class="modal-content bg-dark border-0">
                        <div class="modal-body p-0 position-relative">

                            <button type="button" class="btn-close btn-close-white position-absolute top-0 end-0 m-3"
                                    data-bs-dismiss="modal" aria-label="Close" style="z-index: 20;"></button>

                            <div id="galleryLightboxCarousel" class="carousel slide" data-bs-ride="false" data-bs-interval="false">
                                <div class="carousel-inner">
                                    @for (int imageIndex = 0; imageIndex < images.size(); imageIndex++)
                                        <div class="carousel-item ${imageIndex == 0 ? "active" : ""}">
                                            <img
                                                src="/gallery/${gallery.getId()}/image/${images.get(imageIndex).id()}"
                                                class="d-block w-100"
                                                style="max-height: 88vh; object-fit: contain;"
                                                alt="Bild">
                                        </div>
                                    @endfor
                                </div>

                                @if (images.size() > 1)
                                    <button class="carousel-control-prev gallery-carousel-control" type="button" data-bs-target="#galleryLightboxCarousel" data-bs-slide="prev">
                                        <span class="carousel-control-prev-icon" aria-hidden="true"></span>
                                        <span class="visually-hidden">Previous</span>
                                    </button>
                                    <button class="carousel-control-next gallery-carousel-control" type="button" data-bs-target="#galleryLightboxCarousel" data-bs-slide="next">
                                        <span class="carousel-control-next-icon" aria-hidden="true"></span>
                                        <span class="visually-hidden">Next</span>
                                    </button>
                                @endif
                            </div>

                        </div>
                    </div>
                </div>
            </div>

            <style>
                #galleryLightboxModal .gallery-lightbox-dialog {
                    max-width: min(1920px, 96vw);
                }

                #galleryLightboxModal .gallery-carousel-control {
                    opacity: 0;
                    transition: opacity 0.2s ease-in-out;
                }

                #galleryLightboxModal .modal-body:hover .gallery-carousel-control {
                    opacity: 1;
                }
            </style>

            <script>
                document.addEventListener("DOMContentLoaded", () => {
                    const modal = document.getElementById("galleryLightboxModal");
                    const carouselEl = document.getElementById("galleryLightboxCarousel");

                    if (!modal || !carouselEl) {
                        return;
                    }

                    const carousel = new bootstrap.Carousel(carouselEl, {
                        interval: false,
                        ride: false,
                        wrap: true
                    });

                    document.querySelectorAll("a[data-slide-index]").forEach(link => {
                        link.addEventListener("click", event => {
                            event.preventDefault();
                            const idx = Number.parseInt(link.dataset.slideIndex || "0", 10);
                            carousel.to(Number.isNaN(idx) ? 0 : idx);
                        });
                    });
                });
            </script>

        @endif


        @template.components.display-statusmessage(
            successMessage,
            errorMessage
        )

    `
)