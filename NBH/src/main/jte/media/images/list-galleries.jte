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
				<th scope="col">Titel</th>
				<th scope="col" class="text-center">Edit</th>
				<th scope="col">Datum</th>
				<th scope="col">Anzeigen von</th>
				<th scope="col">Anzeigen bis</th>
				<th scope="col">Sichtbarkeit</th>
				<th scope="col">Beschreibung</th>
				<th scope="col" class="text-center">Löschen</th>
			</tr>
		</thead>

		<tbody>
			@for (Gallery gallery : galleries)
				<tr>
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

	<div class="modal fade" id="confirmDeleteGalleryModal" tabindex="-1" aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<h5 class="modal-title">Wirklich löschen?</h5>
					<button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
				</div>
				<div class="modal-body">
					<p class="mb-2">Es werden sämtliche Bilder aus der Foto-Galerie und die Galerie selbst gelöscht!</p>
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
