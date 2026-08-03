<!--
  Copyright (c) 2025–2026 Maximilian Weißböck
  Licensed under the MIT License (see LICENSE file).
-->

@import eu.nabahilfe.webapp.GlobalModelAttributes
@import java.util.List

@import static eu.nabahilfe.webapp.DateFormatter.*

@import eu.nabahilfe.webapp.timetransfers.TimeTransfer
@import eu.nabahilfe.webapp.org.Offer
@import eu.nabahilfe.webapp.members.Member

@param List<TimeTransfer> bookings

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

    <div class="container-fluid">
        <h3>Aktuelle fremd-verbuchte Zeitschecks</h3>
        <p>Es werden die letzten <strong>50</strong> fremd-verbuchten Zeitschecks angezeigt, für eine vereinfachte Kontrolle der aktuellen Buchungen.</p>

        <br/>

        @if(bookings == null || bookings.isEmpty())
            <div class="alert alert-info">Keine fremd-verbuchten Zeitschecks gefunden.</div>
        @else
            <table class="table table-bordered table-hover table-sm shadow h-100" id="bookingReportTable">
                <thead class="table-light">
                    <tr>
                        <th scope="col" data-sort-index="0" data-type="text" data-label="Verbucht am">Verbucht am</th>
                        <th scope="col" data-sort-index="1" data-type="text" data-label="Verbucht von">Verbucht von</th>
                        <th scope="col" data-sort-index="2" data-type="text" data-label="von Mitglied">von Mitglied</th>
                        <th scope="col" data-sort-index="3" data-type="text" data-label="an Mitglied">an Mitglied</th>
                        <th scope="col" data-sort-index="4" data-type="text" data-label="Datum">Datum</th>
                        <th scope="col" class="text-end" data-sort-index="5" data-type="number" data-label="Stunden">Stunden</th>
                        <th scope="col" data-sort-index="6" data-type="text" data-label="Kategorie">Kategorie</th>
                    </tr>
                </thead>
                <tbody>
                    @for(TimeTransfer tt : bookings)
                        !{Offer offer = tt.getOffer();}
                        !{Member createdBy = tt.getCreatedBy();}
                        !{Member fromMember = tt.getFromMember();}
                        !{Member toMember = tt.getToMember();}
                        !{String categoryCode = offer != null && offer.getCode() != null ? offer.getCode() : "";}
                        !{String categoryDesc = offer != null && offer.getDescription() != null ? offer.getDescription() : "";}
                        !{String categoryText = (categoryCode + " " + categoryDesc).trim();}
                        <tr>
                            <td data-sort-value="${tt.getCreatedAt() != null ? tt.getCreatedAt().toString() : ""}">
                                ${tt.getCreatedAt() != null ? dateDE(tt.getCreatedAt().toLocalDate()) : ""}
                            </td>
                            <td data-sort-value="${createdBy != null ? createdBy.getName().toLowerCase() : ""}">
                                ${createdBy != null ? createdBy.getName() : ""}
                            </td>
                            <td data-sort-value="${fromMember != null ? fromMember.getName().toLowerCase() : ""}">
                                ${fromMember != null ? fromMember.getName() : ""}
                            </td>
                            <td data-sort-value="${toMember != null ? toMember.getName().toLowerCase() : ""}">
                                ${toMember != null ? toMember.getName() : ""}
                            </td>
                            <td data-sort-value="${tt.getDateOfService() != null ? tt.getDateOfService().toString() : ""}">
                                ${dateDE(tt.getDateOfService())}
                            </td>
                            <td class="text-end" data-sort-value="${tt.getHours() != null ? tt.getHours() : 0}">
                                ${tt.getHours()}
                            </td>
                            <td data-sort-value="${categoryText.toLowerCase()}">
                                ${categoryText}
                                @if(tt.getNote() != null && !tt.getNote().isBlank())
                                    <br/><small class="text-muted">${tt.getNote()}</small>
                                @endif
                            </td>
                        </tr>
                    @endfor
                </tbody>
            </table>
        @endif

        <br/>
        @template.components.display-statusmessage(successMessage, errorMessage)

    </div>

    <script>
        document.addEventListener("DOMContentLoaded", () => {
            const table = document.getElementById("bookingReportTable");
            if (!table) return;

            const tbody = table.querySelector("tbody");
            const headers = Array.from(table.querySelectorAll("th[data-sort-index]"));
            let currentSort = { index: 0, asc: false }; // default: "Verbucht am" descending

            function sortRows(index, asc, type) {
                const rows = Array.from(tbody.querySelectorAll("tr"));
                rows.sort((a, b) => {
                    const aCell = a.children[index];
                    const bCell = b.children[index];
                    let aVal = aCell.getAttribute("data-sort-value") || "";
                    let bVal = bCell.getAttribute("data-sort-value") || "";
                    if (type === "number") {
                        aVal = parseFloat(aVal) || 0;
                        bVal = parseFloat(bVal) || 0;
                        return asc ? aVal - bVal : bVal - aVal;
                    }
                    return asc ? aVal.localeCompare(bVal, "de") : bVal.localeCompare(aVal, "de");
                });
                rows.forEach(row => tbody.appendChild(row));
            }

            function updateHeaderLabels() {
                headers.forEach(th => {
                    const index = parseInt(th.dataset.sortIndex, 10);
                    const baseLabel = th.dataset.label;
                    if (index === currentSort.index) {
                        th.textContent = baseLabel + (currentSort.asc ? " ↑" : " ↓");
                    } else {
                        th.textContent = baseLabel + " ↕";
                    }
                });
            }

            headers.forEach(th => {
                th.style.cursor = "pointer";
                th.addEventListener("click", () => {
                    const index = parseInt(th.dataset.sortIndex, 10);
                    const type = th.dataset.type || "text";
                    if (currentSort.index === index) {
                        currentSort.asc = !currentSort.asc;
                    } else {
                        currentSort.index = index;
                        currentSort.asc = true;
                    }
                    sortRows(index, currentSort.asc, type);
                    updateHeaderLabels();
                });
            });

            // initial sort: "Verbucht am" descending
            sortRows(0, false, "text");
            updateHeaderLabels();
        });
    </script>

`)