<!--
  Copyright (c) 2025–2026 Maximilian Weißböck
  Licensed under the MIT License (see LICENSE file).
-->

@import eu.nabahilfe.webapp.GlobalModelAttributes
@import eu.nabahilfe.webapp.media.images.Gallery
@import eu.nabahilfe.webapp.media.images.GalleryImageInfo
@import static eu.nabahilfe.webapp.DateFormatter.*
@import static eu.nabahilfe.webapp.jte.TemplateHelpers.*

@import java.util.List

@param Gallery gallery
@param List<GalleryImageInfo> images
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

        <!-- Kopfbereich -->
        <div class="d-flex flex-column flex-sm-row
                    justify-content-between align-items-sm-center
                    gap-2 mb-4">

            <div>
                <h3 class="mb-1">${gallery.getDescription()}</h3>
                @if(gallery.getGalleryDate() != null)<div>${dateReverseDE(gallery.getGalleryDate())}</div>@endif
                @if(gallery.getRemark() != null)<div><i>${gallery.getRemark()}</i></div>@endif
                <div>
                    ${images.size()} Bilder
                </div>
            </div>

            <a href="${Boolean.TRUE.equals(readOnly) ? "/gallery/user-list" : "/gallery/list"}"
               class="btn btn-secondary">
                <i class="bi bi-arrow-left-square"></i> &nbsp; Zurück zu den Foto-Galerien
            </a>

        </div>


        @if (!Boolean.TRUE.equals(readOnly))
            <!-- Upload -->
            @template.media.images._image-upload(
                gallery = gallery,
                csrfHiddenInput = csrfHiddenInput
            )
	          @template.components.display-statusmessage(
	              successMessage,
	              errorMessage
	          )
        @endif


        <!-- Galerie -->
        @if (images.isEmpty())

            <div class="alert alert-info">
                <h5 class="alert-heading">
                    Noch keine Bilder
                </h5>

                <p class="mb-0">
                    In dieser Galerie wurden noch keine Bilder hochgeladen.
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

                            <div class="gallery-thumb-square rounded shadow-sm">
                                <img
                                    src="/gallery/${gallery.getId()}/image/${images.get(imageIndex).id()}/thumbnail"
                                    class="gallery-thumb-square-image"
                                    loading="lazy"
                                    alt="Bild">
                            </div>

                        </a>

                        @if (Boolean.TRUE.equals(readOnly)
                                && images.get(imageIndex).description() != null
                                && !images.get(imageIndex).description().isBlank())
                            <div class="mt-2 small text-center text-muted">
                                ${truncateWithEllipsis(images.get(imageIndex).description(), 40)}
                            </div>
                        @endif

                        @if (!Boolean.TRUE.equals(readOnly))
                            <div class="mt-2 d-flex justify-content-center gap-1 flex-wrap">
                                <button type="button"
                                        class="btn btn-outline-danger btn-sm"
                                        data-bs-toggle="modal"
                                        data-bs-target="#confirmDeleteImageModal"
                                        data-image-id="${images.get(imageIndex).id()}">
                                    Löschen
                                </button>

                                <button type="button"
                                        class="btn ${images.get(imageIndex).description() != null && !images.get(imageIndex).description().isBlank() ? "btn-outline-success" : "btn-outline-primary"} btn-sm"
                                        data-bs-toggle="modal"
                                        data-bs-target="#editImageDescriptionModal"
                                        data-image-id="${images.get(imageIndex).id()}">
                                    ${images.get(imageIndex).description() != null && !images.get(imageIndex).description().isBlank()
                                        ? truncateWithEllipsis(images.get(imageIndex).description(), 20)
                                        : "Beschreibung erfassen"}
                                </button>

                                <input type="hidden"
                                       id="imageDescription_${images.get(imageIndex).id()}"
                                       value="${images.get(imageIndex).description() != null ? images.get(imageIndex).description() : ""}">
                            </div>
                        @endif

                    </div>

                @endfor

            </div>


            @if (!Boolean.TRUE.equals(readOnly))
                <div class="modal fade" id="confirmDeleteImageModal" tabindex="-1" aria-hidden="true">
                    <div class="modal-dialog">
                        <div class="modal-content">
                            <div class="modal-header">
                                <h5 class="modal-title">Bild wirklich löschen?</h5>
                                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                            </div>
                            <div class="modal-body">
                                Das Bild wird endgültig gelöscht.
                                <input type="hidden" id="imageIdToDelete">
                            </div>
                            <div class="modal-footer">
                                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Abbrechen</button>
                                <button type="button" class="btn btn-danger" id="confirmDeleteImageBtn">Ja</button>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="modal fade" id="editImageDescriptionModal" tabindex="-1" aria-hidden="true">
                    <div class="modal-dialog">
                        <div class="modal-content">
                            <div class="modal-header">
                                <h5 class="modal-title">Bildbeschreibung bearbeiten</h5>
                                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                            </div>
                            <div class="modal-body">
                                <input type="hidden" id="imageIdToEditDescription">
                                <label for="imageDescriptionInput" class="form-label">Beschreibung</label>
                                <textarea class="form-control"
                                          id="imageDescriptionInput"
                                          maxlength="250"
                                          rows="4"
                                          placeholder="Beschreibung für dieses Bild"></textarea>
                            </div>
                            <div class="modal-footer">
                                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Abbrechen</button>
                                <button type="button" class="btn btn-primary" id="confirmImageDescriptionBtn">Speichern</button>
                            </div>
                        </div>
                    </div>
                </div>

                <form id="deleteImageForm" method="post" style="display:none;">
                    ${csrfHiddenInput}
                </form>

                <form id="imageDescriptionForm" method="post" style="display:none;">
                    ${csrfHiddenInput}
                    <input type="hidden" name="description" id="imageDescriptionFormValue">
                </form>
            @endif


            <div class="modal fade" id="galleryLightboxModal" tabindex="-1" aria-hidden="true">
                <div class="modal-dialog modal-dialog-centered modal-xl gallery-lightbox-dialog">
                    <div class="modal-content border-0 bg-transparent shadow-none">
                        <div class="modal-body p-0 position-relative">

                            <div id="galleryLightboxCarousel" class="carousel slide" data-bs-ride="false" data-bs-interval="false">
                                <div class="carousel-inner">
                                    @for (int imageIndex = 0; imageIndex < images.size(); imageIndex++)
                                        <div class="carousel-item ${imageIndex == 0 ? "active" : ""}">
                                            <div class="gallery-lightbox-frame mx-auto">
                                                <button type="button"
                                                        class="btn-close gallery-lightbox-close"
                                                        data-bs-dismiss="modal"
                                                        aria-label="Close"></button>

                                                <img
                                                    src="/gallery/${gallery.getId()}/image/${images.get(imageIndex).id()}"
                                                    class="d-block gallery-lightbox-image"
                                                    alt="Bild">

                                                @if (images.get(imageIndex).description() != null && !images.get(imageIndex).description().isBlank())
                                                    <div class="gallery-lightbox-description">
                                                        ${images.get(imageIndex).description()}
                                                    </div>
                                                @endif

                                                @if (images.size() > 1)
                                                    <button class="carousel-control-prev gallery-carousel-control gallery-carousel-control-prev"
                                                            type="button"
                                                            data-bs-target="#galleryLightboxCarousel"
                                                            data-bs-slide="prev">
                                                        <span class="carousel-control-prev-icon" aria-hidden="true"></span>
                                                        <span class="visually-hidden">Previous</span>
                                                    </button>
                                                    <button class="carousel-control-next gallery-carousel-control gallery-carousel-control-next"
                                                            type="button"
                                                            data-bs-target="#galleryLightboxCarousel"
                                                            data-bs-slide="next">
                                                        <span class="carousel-control-next-icon" aria-hidden="true"></span>
                                                        <span class="visually-hidden">Next</span>
                                                    </button>
                                                @endif
                                            </div>
                                        </div>
                                    @endfor
                                </div>
                            </div>

                        </div>
                    </div>
                </div>
            </div>

            <style>
                .gallery-thumb-square {
                    width: 100%;
                    aspect-ratio: 1 / 1;
                    overflow: hidden;
                    background: #f2f2f2;
                }

                .gallery-thumb-square-image {
                    width: 100%;
                    height: 100%;
                    object-fit: cover;
                    object-position: center 30%;
                    display: block;
                }

                #galleryLightboxModal .gallery-lightbox-dialog {
                    max-width: min(1920px, 96vw);
                }

                #galleryLightboxModal .carousel-item {
                    text-align: center;
                }

                #galleryLightboxModal .gallery-carousel-control {
                    top: 0;
                    bottom: 0;
                    opacity: 1;
                    width: 33.333%;
                    display: flex;
                    align-items: center;
                }

                #galleryLightboxModal .gallery-lightbox-frame {
                    display: inline-block;
                    position: relative;
                    max-width: 100%;
                    background: #1f1f1f;
                    border: 2px solid rgba(255, 255, 255, 0.9);
                    border-radius: 0.4rem;
                    overflow: hidden;
                }

                #galleryLightboxModal .gallery-lightbox-image {
                    max-width: 100%;
                    max-height: 88vh;
                    width: auto;
                    height: auto;
                    object-fit: contain;
                }

                #galleryLightboxModal .gallery-lightbox-description {
                    position: absolute;
                    left: 0;
                    right: 0;
                    bottom: 0;
                    z-index: 10;
                    padding: 0.6rem 0.85rem;
                    color: #fff;
                    background-color: rgba(108, 117, 125, 0.75);
                    font-size: 0.95rem;
                    line-height: 1.35;
                    text-align: center;
                    pointer-events: none;
                }

                #galleryLightboxModal .gallery-lightbox-close {
                    --bs-btn-close-bg: url("data:image/svg+xml,%3csvg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 16 16' fill='%23fff'%3e%3cpath d='M.293.293a1 1 0 0 1 1.414 0L8 6.586 14.293.293a1 1 0 1 1 1.414 1.414L9.414 8l6.293 6.293a1 1 0 0 1-1.414 1.414L8 9.414l-6.293 6.293a1 1 0 0 1-1.414-1.414L6.586 8 .293 1.707a1 1 0 0 1 0-1.414z'/%3e%3c/svg%3e");
                    position: absolute;
                    top: 0.6rem;
                    right: 0.6rem;
                    z-index: 30;
                    background-color: rgba(108, 117, 125, 0.75);
                    border-radius: 999px;
                    padding: 0.4rem;
                    opacity: 1;
                    filter: none;
                }

                #galleryLightboxModal .gallery-carousel-control .carousel-control-prev-icon,
                #galleryLightboxModal .gallery-carousel-control .carousel-control-next-icon {
                    background-color: rgba(108, 117, 125, 0.75);
                    border-radius: 0.6rem;
                    padding: 1.2rem;
                    background-size: 60% 60%;
                }

                #galleryLightboxModal .gallery-carousel-control-prev {
                    left: 0.35rem;
                    justify-content: flex-start;
                    padding-left: 0.35rem;
                }

                #galleryLightboxModal .gallery-carousel-control-next {
                    right: 0.35rem;
                    justify-content: flex-end;
                    padding-right: 0.35rem;
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

                    const keyNavHandler = event => {
                        if (!modal.classList.contains("show")) {
                            return;
                        }

                        if (event.key === "ArrowLeft") {
                            event.preventDefault();
                            carousel.prev();
                        }
                        else if (event.key === "ArrowRight") {
                            event.preventDefault();
                            carousel.next();
                        }
                    };

                    modal.addEventListener("shown.bs.modal", () => {
                        document.addEventListener("keydown", keyNavHandler);
                    });

                    modal.addEventListener("hidden.bs.modal", () => {
                        document.removeEventListener("keydown", keyNavHandler);
                    });

                    document.querySelectorAll("a[data-slide-index]").forEach(link => {
                        link.addEventListener("click", event => {
                            event.preventDefault();
                            const idx = Number.parseInt(link.dataset.slideIndex || "0", 10);
                            carousel.to(Number.isNaN(idx) ? 0 : idx);
                        });
                    });

                    const deleteModal = document.getElementById("confirmDeleteImageModal");
                    const deleteForm = document.getElementById("deleteImageForm");
                    const confirmDeleteBtn = document.getElementById("confirmDeleteImageBtn");
                    const imageIdInput = document.getElementById("imageIdToDelete");
                    const descriptionModal = document.getElementById("editImageDescriptionModal");
                    const descriptionForm = document.getElementById("imageDescriptionForm");
                    const confirmDescriptionBtn = document.getElementById("confirmImageDescriptionBtn");
                    const imageIdToEditDescription = document.getElementById("imageIdToEditDescription");
                    const imageDescriptionInput = document.getElementById("imageDescriptionInput");
                    const imageDescriptionFormValue = document.getElementById("imageDescriptionFormValue");

                    if (deleteModal && deleteForm && confirmDeleteBtn && imageIdInput) {
                        deleteModal.addEventListener("show.bs.modal", event => {
                            const trigger = event.relatedTarget;
                            const imageId = trigger ? trigger.getAttribute("data-image-id") : null;
                            imageIdInput.value = imageId || "";
                        });

                        confirmDeleteBtn.addEventListener("click", () => {
                            const imageId = imageIdInput.value;
                            if (!imageId) {
                                return;
                            }
                            deleteForm.action = "/gallery/${gallery.getId()}/image/" + imageId + "/delete";
                            deleteForm.submit();
                        });
                    }

                    if (descriptionModal && descriptionForm && confirmDescriptionBtn
                            && imageIdToEditDescription && imageDescriptionInput && imageDescriptionFormValue) {
                        descriptionModal.addEventListener("show.bs.modal", event => {
                            const trigger = event.relatedTarget;
                            const imageId = trigger ? trigger.getAttribute("data-image-id") : null;
                            imageIdToEditDescription.value = imageId || "";

                            const descriptionHolder = imageId ? document.getElementById("imageDescription_" + imageId) : null;
                            imageDescriptionInput.value = descriptionHolder ? descriptionHolder.value : "";
                        });

                        confirmDescriptionBtn.addEventListener("click", () => {
                            const imageId = imageIdToEditDescription.value;
                            if (!imageId) {
                                return;
                            }

                            imageDescriptionFormValue.value = imageDescriptionInput.value;
                            descriptionForm.action = "/gallery/${gallery.getId()}/image/" + imageId + "/description";
                            descriptionForm.submit();
                        });
                    }
                });
            </script>

        @endif

		<br/>

    `
)