<!--
  Copyright (c) 2025-2026 Maximilian Weissboeck
  Licensed under the MIT License (see LICENSE file).
-->

@import eu.nabahilfe.webapp.GlobalModelAttributes
@import eu.nabahilfe.webapp.media.images.Gallery

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

	<h3>Foto-Galerien</h3>

	<table class="table table-bordered table-hover table-sm shadow h-100" id="galleryTable">
		<thead class="table-light">
			<tr>
				<th scope="col" data-sort-key="title">Titel <span class="sort-indicator" >↕</span></th>
				<th scope="col" class="text-center">Edit</th>
				<th scope="col" data-sort-key="galleryDate" data-default-dir="desc">Datum <span class="sort-indicator" >↕</span></th>
				<th scope="col" data-sort-key="showFrom">Anzeigen von <span class="sort-indicator" >↕</span></th>
				<th scope="col" data-sort-key="showTo">Anzeigen bis <span class="sort-indicator" >↕</span></th>
				<th scope="col" data-sort-key="isPublic">Sichtbarkeit <span class="sort-indicator" >↕</span></th>
				<th scope="col">Beschreibung</th>
				<th scope="col" class="text-center">Löschen</th>
			</tr>
		</thead>

		<tbody>
			@for (Gallery gallery : galleries)
				<tr
					data-sort-title="${gallery.getDescription() != null ? gallery.getDescription().toLowerCase() : ""}"
					data-sort-gallery-date="${gallery.getGalleryDate() != null ? gallery.getGalleryDate().toString() : ""}"
					data-sort-show-from="${gallery.getShowGalleryFrom() != null ? gallery.getShowGalleryFrom().toString() : ""}"
					data-sort-show-to="${gallery.getShowGalleryTo() != null ? gallery.getShowGalleryTo().toString() : ""}"
					data-sort-is-public="${Boolean.TRUE.equals(gallery.getIsPublic()) ? "1" : "0"}">
					<td>
						<a class="text-decoration-none" href="/gallery/${gallery.getId()}">${gallery.getDescription()}</a>
					</td>
					<td class="text-center">
						<a class="btn btn-outline-primary btn-sm bi-pencil" href="/gallery/${gallery.getId()}/edit" role="button"></a>
					</td>
					<td>${gallery.getGalleryDate() != null ? gallery.getGalleryDate().toString() : ""}</td>
					<td>${gallery.getShowGalleryFrom() != null ? gallery.getShowGalleryFrom().toString() : ""}</td>
					<td>${gallery.getShowGalleryTo() != null ? gallery.getShowGalleryTo().toString() : ""}</td>
					<td>@if(Boolean.TRUE.equals(gallery.getIsPublic()))
							<span class="badge bg-danger">Öffentlich</span>
						@else
							<span class="badge bg-success">Nur Mitglieder</span>
						@endif
					</td>
					<td>${gallery.getRemark() != null ? gallery.getRemark() : ""}</td>
					<td class="text-center">
						<button type="button"
								class="btn btn-outline-danger btn-sm bi-trash"
								data-bs-toggle="modal"
								data-bs-target="#confirmDeleteGalleryModal"
								data-gallery-id="${gallery.getId()}"
								data-gallery-description="${gallery.getDescription()}">
						</button>
					</td>
				</tr>
			@endfor
		</tbody>
	</table>

	<div class="modal fade" id="confirmDeleteGalleryModal" tabindex="-1" >
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<h5 class="modal-title">Wirklich löschen?</h5>
					<button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
				</div>
				<div class="modal-body">
					<p class="mb-2">ACHTUNG!<br/>Alle Bilder und die Galerie werden endgültig gelöscht!</p>
					<p class="small text-muted mb-3" id="galleryToDeleteLabel"></p>
					<div class="form-check">
						<input class="form-check-input" type="checkbox" value="" id="confirmDeleteGalleryCheckbox">
						<label class="form-check-label" for="confirmDeleteGalleryCheckbox">
							Löschen bestätigen
						</label>
					</div>
					<input type="hidden" id="galleryIdToDelete">
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Abbrechen</button>
					<button type="button" class="btn btn-danger" id="confirmDeleteGalleryBtn" disabled>Löschen</button>
				</div>
			</div>
		</div>
	</div>

	<form id="deleteGalleryForm" method="post" style="display:none;">
		${csrfHiddenInput}
	</form>

	<form action="/gallery/new" method="get">
		<button class="btn btn-outline-primary bi-plus-circle" type="submit"> Neue Galerie erstellen</button>
	</form>

	<br/>

	@template.components.display-statusmessage(successMessage, errorMessage)

	<script>
		document.addEventListener("DOMContentLoaded", () => {
			const table = document.getElementById("galleryTable");
			const tbody = table ? table.querySelector("tbody") : null;
			const sortableHeaders = table ? table.querySelectorAll("th[data-sort-key]") : [];
			const sortState = { key: "galleryDate", direction: "desc" };

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
						case "galleryDate":
							return row.dataset.sortGalleryDate || "";
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

				if (key === "galleryDate" || key === "showFrom" || key === "showTo") {
					const av = readSortValue(a);
					const bv = readSortValue(b);

					if (key === "galleryDate") {
						if (av === "" && bv !== "") {
							return 1;
						}
						if (av !== "" && bv === "") {
							return -1;
						}
					}

					if (av < bv) {
						return -1 * multiplier;
					}
					if (av > bv) {
						return 1 * multiplier;
					}
					return 0;
				}

				if (key === "isPublic") {
					const av = readSortValue(a);
					const bv = readSortValue(b);
					if (av < bv) {
						return -1 * multiplier;
					}
					if (av > bv) {
						return 1 * multiplier;
					}
					return 0;
				}

				const av = readSortValue(a);
				const bv = readSortValue(b);
				return av.localeCompare(bv) * multiplier;
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
					const triggerA = rowA.querySelector("[data-gallery-id]");
					const triggerB = rowB.querySelector("[data-gallery-id]");
					const idA = Number(triggerA ? (triggerA.getAttribute("data-gallery-id") || "0") : "0");
					const idB = Number(triggerB ? (triggerB.getAttribute("data-gallery-id") || "0") : "0");
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

			const modal = document.getElementById("confirmDeleteGalleryModal");
			const checkbox = document.getElementById("confirmDeleteGalleryCheckbox");
			const confirmBtn = document.getElementById("confirmDeleteGalleryBtn");
			const idInput = document.getElementById("galleryIdToDelete");
			const label = document.getElementById("galleryToDeleteLabel");
			const form = document.getElementById("deleteGalleryForm");

			if (!modal || !checkbox || !confirmBtn || !idInput || !form) {
				return;
			}

			modal.addEventListener("show.bs.modal", event => {
				const trigger = event.relatedTarget;
				const galleryId = trigger ? trigger.getAttribute("data-gallery-id") : "";
				const galleryDescription = trigger ? trigger.getAttribute("data-gallery-description") : "";

				idInput.value = galleryId || "";
				if (label) {
					label.textContent = galleryDescription ? "Galerie: " + galleryDescription : "";
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
				form.action = "/gallery/" + idInput.value + "/delete";
				form.submit();
			});
		});
	</script>

`)
