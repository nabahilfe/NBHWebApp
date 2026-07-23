<!--
  Copyright (c) 2025–2026 Maximilian Weißböck
  Licensed under the MIT License (see LICENSE file).
-->

@import eu.nabahilfe.webapp.GlobalModelAttributes
@import java.util.List

@import eu.nabahilfe.webapp.members.MembershipFeeOpenForm
@import static eu.nabahilfe.webapp.DateFormatter.*

@param MembershipFeeOpenForm formRowData
@param List<MembershipFeeOpenForm> membershipFeesOpen

@param java.lang.String successMessage = null
@param java.lang.String errorMessage = null

@param String fullUsername = null
@param String username = null
@param Boolean isAuthenticated = false


@param eu.nabahilfe.webapp.security.CsrfHiddenInput csrfHiddenInput
@param eu.nabahilfe.webapp.security.ViewContext ctxt
@param GlobalModelAttributes globalModelAttributes


@template.layout.mainpage(
    globalModelAttributes = globalModelAttributes,
    csrfHiddenInput = csrfHiddenInput,
    ctxt = ctxt,
    content = @`

    <h3>Noch nicht vorgeschriebene Mitgliedsbeiträge für das aktuelle Jahr</h3>
    <!-- Info panel: fees can only be created from March 1st onwards -->
    <div class="alert alert-warning text-center" role="alert">
        <strong>Beitragsvorschreibungen können erst ab dem 1. März des aktuellen Jahres erstellt werden!</strong>
        <br>Bitte vor diesem Termin unbedingt den Mitgliedsbeitrag anpassen, falls dieser sich für das aktuelle Jahr ändern soll!
        <br>Die Vorschreibung wird automatisch auf Basis des aktuellen Mitgliedsbeitrags erstellt.
        <br>Wird <strong>Keine Vorschreibung</strong> ausgewählt, so wird für dieses Mitglied für das aktuelle Jahr keine Vorschreibung erstellt.
    </div>

    <!-- Display all MembershipFee records -->

    <form id="feesForm" action="/members/create-fees-batch" method="POST">
        ${csrfHiddenInput}
        <div id="memberIdContainer"></div>

        <table class="table table-bordered table-hover table-sm shadow h-100" id="memberTable">
            <thead class="table-light">
                <tr>
                    <th scope="col" data-col="0" style="cursor:pointer;">Name ↕</th>
                    <th scope="col">Adresse</th>
                    <th scope="col" data-col="2" style="cursor:pointer;">Rolle ↕</th>
                    <th scope="col" data-col="3" style="cursor:pointer;">Eintrittsdatum ↕</th>
                    <th scope="col" data-col="4" style="cursor:pointer;">Austrittsdatum ↕</th>
                    <th scope="col" class="text-center" title="Keine Vorschreibung erstellen">Keine Vorschreibung</th>
                </tr>
            </thead>

            <tbody>
                @for (MembershipFeeOpenForm mf : membershipFeesOpen)
                    <tr>
                        <td>${mf.getName()}</td>
                        <td>${mf.getAddress()}</td>
                        <td>${mf.getRoleName()}</td>
                        <td>${dateDE(mf.getJoiningDate())}</td>
                        <td>@if(mf.getResignationDate() != null) ${dateDE(mf.getResignationDate())} @else @endif</td>
                        <td class="text-center">
                            <input type="checkbox" class="form-check-input doNotCharge-cb"
                                   data-member-id="${mf.getMemberId()}">
                        </td>
                    </tr>
                @endfor
            </tbody>
        </table>

        <div class="text-end mt-2">
            <button type="submit" id="createFeesBtn" class="btn btn-primary bi-plus-circle"> Beitragsvorschreibungen erstellen</button>
        </div>
    </form>

    <br/>

    @template.components.display-statusmessage(successMessage, errorMessage)

    <script>
        document.addEventListener("DOMContentLoaded", () => {

            // --- Disable submit button before March 1st of the current year ---
            const createFeesBtn = document.getElementById("createFeesBtn");
            const now = new Date();
            const marchFirst = new Date(now.getFullYear(), 2, 1); // month is 0-based: 2 = March
            if (now < marchFirst) {
                createFeesBtn.disabled = true;
            }

            // --- Sorting ---
            const table = document.getElementById("memberTable");
            const tbody = table.querySelector("tbody");
            const sortState = {};

            table.querySelectorAll("th[data-col]").forEach(th => {
                th.addEventListener("click", () => {
                    const colIndex = parseInt(th.getAttribute("data-col"));
                    const asc = sortState[colIndex] !== true;
                    sortState[colIndex] = asc;

                    table.querySelectorAll("th[data-col]").forEach(h => {
                        h.textContent = h.textContent.replace(/[↕↑↓]/, "↕");
                    });
                    th.textContent = th.textContent.replace("↕", asc ? "↑" : "↓");

                    const rows = Array.from(tbody.querySelectorAll("tr"));
                    rows.sort((a, b) => {
                        const aText = a.cells[colIndex].textContent.trim().toLowerCase();
                        const bText = b.cells[colIndex].textContent.trim().toLowerCase();
                        return asc ? aText.localeCompare(bText, "de") : bText.localeCompare(aText, "de");
                    });
                    rows.forEach(row => tbody.appendChild(row));
                });
            });

            // --- Form submission: submit all memberIds with their doNotCharge flag ---
            document.getElementById("feesForm").addEventListener("submit", function (e) {
                e.preventDefault();
                const container = document.getElementById("memberIdContainer");
                container.innerHTML = "";

                document.querySelectorAll(".doNotCharge-cb").forEach(cb => {
                    const idInput = document.createElement("input");
                    idInput.type = "hidden";
                    idInput.name = "memberIds";
                    idInput.value = cb.getAttribute("data-member-id");
                    container.appendChild(idInput);

                    const flagInput = document.createElement("input");
                    flagInput.type = "hidden";
                    flagInput.name = "doNotChargeFlags";
                    flagInput.value = cb.checked;
                    container.appendChild(flagInput);
                });

                this.submit();
            });

        });
    </script>

`)