<!--
  Copyright (c) 2025-2026 Maximilian Weissboeck
  Licensed under the MIT License (see LICENSE file).
-->

@import eu.nabahilfe.webapp.GlobalModelAttributes
@import eu.nabahilfe.webapp.media.documents.Library

@param java.util.List<Library> libraries

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

    <h3>Dokument-Bibliotheken</h3>

    <table class="table table-bordered table-hover table-sm shadow h-100" id="libraryTable">
        <thead class="table-light">
            <tr>
                <th scope="col" data-sort-key="title">Titel <span class="sort-indicator" >↕</span></th>
                <th scope="col" class="text-center">Edit</th>
                <th scope="col" data-sort-key="showFrom">Anzeigen von <span class="sort-indicator" >↕</span></th>
                <th scope="col" data-sort-key="showTo">Anzeigen bis <span class="sort-indicator" >↕</span></th>
                <th scope="col" data-sort-key="isPublic">Sichtbarkeit <span class="sort-indicator" >↕</span></th>
                <th scope="col">Beschreibung</th>
                <th scope="col" class="text-center">Löschen</th>
            </tr>
        </thead>

        <tbody>
            @for (Library library : libraries)
                <tr
                    data-sort-title="${library.getDescription() != null ? library.getDescription().toLowerCase() : ""}"
                    data-sort-show-from="${library.getShowLibraryFrom() != null ? library.getShowLibraryFrom().toString() : ""}"
                    data-sort-show-to="${library.getShowLibraryTo() != null ? library.getShowLibraryTo().toString() : ""}"
                    data-sort-is-public="${Boolean.TRUE.equals(library.getIsPublic()) ? "1" : "0"}">
                    <td>
                        <a class="text-decoration-none" href="/library/${library.getId()}">${library.getDescription()}</a>
                    </td>
                    <td class="text-center">
                        <a class="btn btn-outline-primary btn-sm bi-pencil" href="/library/${library.getId()}/edit" role="button"></a>
                    </td>
                    <td>${library.getShowLibraryFrom() != null ? library.getShowLibraryFrom().toString() : ""}</td>
                    <td>${library.getShowLibraryTo() != null ? library.getShowLibraryTo().toString() : ""}</td>
                    <td>@if(Boolean.TRUE.equals(library.getIsPublic()))
                            <span class="badge bg-danger">Öffentlich</span>
                        @else
                            <span class="badge bg-success">Nur Mitglieder</span>
                        @endif
                    </td>
                    <td>${library.getRemark() != null ? library.getRemark() : ""}</td>
                    <td class="text-center">
                        <button type="button"
                                class="btn btn-outline-danger btn-sm bi-trash"
                                data-bs-toggle="modal"
                                data-bs-target="#confirmDeleteLibraryModal"
                                data-library-id="${library.getId()}"
                                data-library-description="${library.getDescription()}">
                        </button>
                    </td>
                </tr>
            @endfor
        </tbody>
    </table>

    <div class="modal fade" id="confirmDeleteLibraryModal" tabindex="-1" >
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title">Wirklich loeschen?</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <div class="modal-body">
                    <p class="mb-2">ACHTUNG!<br/>Alle Dokumente und die Bibliothek werden endgültig gelöscht!</p>
                    <p class="small text-muted mb-3" id="libraryToDeleteLabel"></p>
                    <div class="form-check">
                        <input class="form-check-input" type="checkbox" value="" id="confirmDeleteLibraryCheckbox">
                        <label class="form-check-label" for="confirmDeleteLibraryCheckbox">
                            Löschen bestaetigen
                        </label>
                    </div>
                    <input type="hidden" id="libraryIdToDelete">
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Abbrechen</button>
                    <button type="button" class="btn btn-danger" id="confirmDeleteLibraryBtn" disabled>Löschen</button>
                </div>
            </div>
        </div>
    </div>

    <form id="deleteLibraryForm" method="post" style="display:none;">
        ${csrfHiddenInput}
    </form>

    <form action="/library/new" method="get">
        <button class="btn btn-outline-primary bi-plus-circle" type="submit"> Neue Bibliothek erstellen</button>
    </form>

    <br/>

    @template.components.display-statusmessage(successMessage, errorMessage)

    <script>
        document.addEventListener("DOMContentLoaded", () => {
            const table = document.getElementById("libraryTable");
            const tbody = table ? table.querySelector("tbody") : null;
            const sortableHeaders = table ? table.querySelectorAll("th[data-sort-key]") : [];
            const sortState = { key: "title", direction: "asc" };

            const updateSortIcons = () => {
                sortableHeaders.forEach(th => {
                    const indicator = th.querySelector(".sort-indicator");
                    if (!indicator) {
                        return;
                    }

                    indicator.textContent = "↕";
                    if (th.dataset.sortKey === sortState.key) {
                        indicator.textContent = sortState.direction === "asc" ? "↑" : "↓";
                    }
                });
            };

            const compareValues = (a, b, key, direction) => {
                const multiplier = direction === "asc" ? 1 : -1;
                const readSortValue = row => {
                    switch (key) {
                        case "showFrom":
                            return row.dataset.sortShowFrom || "";
                        case "showTo":
                            return row.dataset.sortShowTo || "";
                        case "isPublic":
                            return row.dataset.sortIsPublic || "0";
                        case "title":
                        default:
                            return row.dataset.sortTitle || "";
                    }
                };

                const av = readSortValue(a);
                const bv = readSortValue(b);

                if (av < bv) {
                    return -1 * multiplier;
                }
                if (av > bv) {
                    return 1 * multiplier;
                }
                return 0;
            };

            const sortRows = (key, direction) => {
                if (!tbody) {
                    return;
                }

                const rows = Array.from(tbody.querySelectorAll("tr"));
                rows.sort((rowA, rowB) => {
                    const result = compareValues(rowA, rowB, key, direction);
                    if (result !== 0) {
                        return result;
                    }
                    const triggerA = rowA.querySelector("[data-library-id]");
                    const triggerB = rowB.querySelector("[data-library-id]");
                    const idA = Number(triggerA ? (triggerA.getAttribute("data-library-id") || "0") : "0");
                    const idB = Number(triggerB ? (triggerB.getAttribute("data-library-id") || "0") : "0");
                    return idB - idA;
                });

                rows.forEach(row => tbody.appendChild(row));
            };

            sortableHeaders.forEach(th => {
                th.style.cursor = "pointer";
                th.addEventListener("click", () => {
                    const key = th.dataset.sortKey;
                    if (!key) {
                        return;
                    }

                    if (sortState.key === key) {
                        sortState.direction = sortState.direction === "asc" ? "desc" : "asc";
                    }
                    else {
                        sortState.key = key;
                        sortState.direction = th.dataset.defaultDir || "asc";
                    }

                    sortRows(sortState.key, sortState.direction);
                    updateSortIcons();
                });
            });

            sortRows(sortState.key, sortState.direction);
            updateSortIcons();

            const modal = document.getElementById("confirmDeleteLibraryModal");
            const checkbox = document.getElementById("confirmDeleteLibraryCheckbox");
            const confirmBtn = document.getElementById("confirmDeleteLibraryBtn");
            const idInput = document.getElementById("libraryIdToDelete");
            const label = document.getElementById("libraryToDeleteLabel");
            const form = document.getElementById("deleteLibraryForm");

            if (!modal || !checkbox || !confirmBtn || !idInput || !form) {
                return;
            }

            modal.addEventListener("show.bs.modal", event => {
                const trigger = event.relatedTarget;
                const libraryId = trigger ? trigger.getAttribute("data-library-id") : "";
                const libraryDescription = trigger ? trigger.getAttribute("data-library-description") : "";

                idInput.value = libraryId || "";
                if (label) {
                    label.textContent = libraryDescription ? "Bibliothek: " + libraryDescription : "";
                }
                checkbox.checked = false;
                confirmBtn.disabled = true;
            });

            checkbox.addEventListener("change", () => {
                confirmBtn.disabled = !checkbox.checked;
            });

            confirmBtn.addEventListener("click", () => {
                if (!checkbox.checked || !idInput.value) {
                    return;
                }
                form.action = "/library/" + idInput.value + "/delete";
                form.submit();
            });
        });
    </script>

`)
