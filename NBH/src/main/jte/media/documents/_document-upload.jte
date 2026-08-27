<!--
  Copyright (c) 2025-2026 Maximilian Weissboeck
  Licensed under the MIT License (see LICENSE file).
-->

@import eu.nabahilfe.webapp.media.documents.Library
@import eu.nabahilfe.webapp.security.CsrfHiddenInput

@param Library library
@param CsrfHiddenInput csrfHiddenInput

<div class="card shadow-sm mb-4">

    <div class="card-body">

        <h5 class="card-title">
            Dokumente hinzufügen
        </h5>

        <form
            id="libraryUploadForm"
            method="post"
            action="/library/${library.getId()}/documents"
            enctype="multipart/form-data">

            ${csrfHiddenInput}

            <div class="mb-3">

                <label
                    for="libraryDocuments"
                    class="form-label">

                    PDF-Dokumente auswählen

                </label>

                <input
                    id="libraryDocuments"
                    type="file"
                    class="form-control"
                    name="documents"
                    accept=".pdf,application/pdf"
                    multiple
                    required
                    data-max-files="10">

                <div class="form-text">
                    Du kannst bis zu 10 PDF-Dokumente gleichzeitig auswählen.
                </div>

            </div>

            <button
                id="libraryUploadSubmitBtn"
                type="submit"
                class="btn btn-primary">

                Dokumente hochladen

            </button>

        </form>

        <script>
            document.addEventListener("DOMContentLoaded", () => {
                const form = document.getElementById("libraryUploadForm");
                const submitBtn = document.getElementById("libraryUploadSubmitBtn");

                if (!form || !submitBtn) {
                    return;
                }

                submitBtn.disabled = false;

                form.addEventListener("submit", () => {
                    submitBtn.disabled = true;
                    submitBtn.textContent = "Dokumente werden hochgeladen...";
                });

                window.addEventListener("pageshow", () => {
                    submitBtn.disabled = false;
                    submitBtn.textContent = "Dokumente hochladen";
                });
            });
        </script>

    </div>

</div>
