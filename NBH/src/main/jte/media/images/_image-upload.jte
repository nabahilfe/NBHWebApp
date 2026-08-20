<!--
  Copyright (c) 2025–2026 Maximilian Weißböck
  Licensed under the MIT License (see LICENSE file).
-->

@import eu.nabahilfe.webapp.media.images.Gallery
@import eu.nabahilfe.webapp.security.CsrfHiddenInput

@param Gallery gallery
@param CsrfHiddenInput csrfHiddenInput


<div class="card shadow-sm mb-4">

    <div class="card-body">

        <h5 class="card-title">
            Bilder hinzufügen
        </h5>

        <form
            id="galleryUploadForm"
            method="post"
            action="/gallery/${gallery.getId()}/images"
            enctype="multipart/form-data">

            ${csrfHiddenInput}

            <div class="mb-3">

                <label
                    for="galleryImages"
                    class="form-label">

                    Bilder auswählen

                </label>

                <input
                    id="galleryImages"
                    type="file"
                    class="form-control"
                    name="images"
                    accept=".jpg,.jpeg,.png,.webp"
                    multiple
                    required>

                <div class="form-text">
                    Du kannst mehrere Bilder gleichzeitig auswählen.
                    Die Bilder werden automatisch optimiert und als
                    WebP gespeichert.
                </div>

            </div>

            <button
                id="galleryUploadSubmitBtn"
                type="submit"
                class="btn btn-primary">

                Bilder hochladen

            </button>

        </form>

        <script>
            document.addEventListener("DOMContentLoaded", () => {
                const form = document.getElementById("galleryUploadForm");
                const submitBtn = document.getElementById("galleryUploadSubmitBtn");

                if (!form || !submitBtn) {
                    return;
                }

                // Fresh page state (after redirect/refresh): button must be usable.
                submitBtn.disabled = false;

                form.addEventListener("submit", () => {
                    // Browser runs HTML validation first. If submit is valid,
                    // disable button to prevent duplicate uploads.
                    submitBtn.disabled = true;
                    submitBtn.textContent = "Bilder werden hochgeladen...";
                });

                // Re-enable when this page is shown again (e.g. back-forward cache).
                window.addEventListener("pageshow", () => {
                    submitBtn.disabled = false;
                    submitBtn.textContent = "Bilder hochladen";
                });
            });
        </script>

    </div>

</div>