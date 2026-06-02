<!--
  Copyright (c) 2025–2026 Maximilian Weißböck
  Licensed under the MIT License (see LICENSE file).
-->

@import java.util.List

@import eu.nabahilfe.webapp.members.MembershipFeeOpenForm

@param MembershipFeeOpenForm formRowData
@param List<MembershipFeeOpenForm> membershipFeesOpen

@param java.lang.String successMessage = null
@param java.lang.String errorMessage = null

@param String fullUsername = null
@param String username = null
@param Boolean isAuthenticated = false


@param eu.nabahilfe.webapp.security.CsrfHiddenInput csrfHiddenInput
@param eu.nabahilfe.webapp.security.ViewContext ctxt


@template.layout.mainpage(
    csrfHiddenInput = csrfHiddenInput,
    ctxt = ctxt,
    content = @`

    <h3>Noch nicht verbuchte Mitgliedsbeiträge für das aktuelle Jahr</h3>

    <br/>

    <!-- Display all MembershipFee records -->

    <table class="table table-hover table-sm" id="memberTable">
        <thead>
            <tr>
                <th scope="col" data-col="0" style="cursor:pointer;">Name ↕</th>
                <th scope="col">Adresse</th>
                <th scope="col" data-col="2" style="cursor:pointer;">Rolle ↕</th>
                <th scope="col" class="text-center">Beitrag erstellen</th>
            </tr>
        </thead>

        <tbody class="table-group-divider">
            @for (MembershipFeeOpenForm mf : membershipFeesOpen)
                <tr>
                    <td>${mf.getName()}</td>
                    <td>${mf.getAddress()}</td>
                    <td>${mf.getRoleName()}</td>
                    <td class="text-center">
                        <a class="btn btn-outline-primary btn-sm bi-plus-lg"
                            href="/membership-fees/new?memberId=${mf.getMemberId()}" role="button"
                            title="Mitgliedsbeitrag erstellen"></a>
                    </td>
                </tr>
            @endfor
        </tbody>
    </table>
    <br/>

    @template.components.display-statusmessage(successMessage, errorMessage)

    <script>
        document.addEventListener("DOMContentLoaded", () => {
            const table = document.getElementById("memberTable");
            const tbody = table.querySelector("tbody");
            const sortState = {}; // tracks current sort direction per column index

            table.querySelectorAll("th[data-col]").forEach(th => {
                th.addEventListener("click", () => {
                    const colIndex = parseInt(th.getAttribute("data-col"));
                    const asc = sortState[colIndex] !== true; // toggle
                    sortState[colIndex] = asc;

                    // Reset all sortable headers to ↕, then set active one
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
        });
    </script>

`)