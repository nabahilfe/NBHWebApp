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
                type="submit"
                class="btn btn-primary">

                Bilder hochladen

            </button>

        </form>

    </div>

</div>