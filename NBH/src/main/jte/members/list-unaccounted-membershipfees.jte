<!--
  Copyright (c) 2025–2026 Maximilian Weißböck
  Licensed under the MIT License (see LICENSE file).
-->

@import java.util.List
@import java.math.BigDecimal
@import java.time.LocalDate

@import static eu.nabahilfe.webapp.DateFormatter.*
@import static eu.nabahilfe.webapp.NumberFormatter.*

@import eu.nabahilfe.webapp.NbhConst
@import eu.nabahilfe.webapp.members.MembershipFee
@import eu.nabahilfe.webapp.accountings.TransactionType
@import eu.nabahilfe.webapp.accountings.AccountableRowSelectionForm

@param AccountableRowSelectionForm formRowData
@param List<MembershipFee> membershipFees

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

    <h3>Offene noch nicht verbuchte Mitgliedsbeiträge</h3>

    <br/>

    <!-- Display all MembershipFee records -->

    <table class="table table-hover table-sm" id="memberTable">
        <thead>
            <tr>
                <th scope="col">Erstellungsdatum</th>
                <th scope="col">erstellt von</th>
                <th scope="col" id="sortByMember" style="cursor:pointer;">für Mitglied ↕</th>
                <th scope="col">Austrittsdatum</th>
                <th scope="col" class="text-end">Betrag</th>
                <th scope="col" class="text-center">Buchen</th>
            </tr>
        </thead>

        <tbody class="table-group-divider">
            @for (MembershipFee mf : membershipFees)
                <form method="post" action="/accountings/new-accountable">
                    ${csrfHiddenInput}
                    <tr>
                        <td scope="row">${dateDE(mf.getTransactionDate())}</td>
                        <td scope="row">${mf.getCreatedBy().getName()}</td>
                        <td scope="row">${mf.getMember().getNameAndAddress()}</td>
                        <td scope="row">@if(mf.getMember().getResignationDate() != null) ${dateReverseDE(mf.getMember().getResignationDate())} @else @endif</td>
                        <td scope="row" class="text-end">€ ${numberDE(mf.getAmount())}</td>
                        <td>
                            <input type="hidden" name="accountableName" value="${mf.getAccountableName()}">
                            <input type="hidden" name="accountableId" value="${mf.getAccountableId()}">
                            <input type="hidden" name="accountableMemberId" value="${mf.getAccountableMemberId()}">
                            <input type="hidden" name="transactionType" value="${mf.getTransactionType()}">
                            <input type="hidden" name="transactionDate" value="${mf.getTransactionISODate()}">
                            <input type="hidden" name="transactionAmount" value="${mf.getTransactionAmount()}">
                            @if(ctxt.isTreasurer() || ctxt.isAdmin())
                                <div class="text-center">
                                    <button type="submit" class="btn btn-outline-primary btn-sm bi-plus-lg"></button>
                                </div>
                            @else
                                <div></div>
                            @endif
                        </td>
                    </tr>
                </form>
            @endfor
        </tbody>
    </table>
    <br/>

    @template.components.display-statusmessage(successMessage, errorMessage)

    <script>
        document.addEventListener("DOMContentLoaded", () => {
            const th = document.getElementById("sortByMember");
            const tbody = document.querySelector("#memberTable tbody");
            let asc = true;

            th.addEventListener("click", () => {
                const rows = Array.from(tbody.querySelectorAll("tr"));
                rows.sort((a, b) => {
                    const aText = a.cells[1].textContent.trim().toLowerCase();
                    const bText = b.cells[1].textContent.trim().toLowerCase();
                    return asc ? aText.localeCompare(bText, "de") : bText.localeCompare(aText, "de");
                });
                rows.forEach(row => tbody.appendChild(row));
                th.textContent = "für Mitglied " + (asc ? "↑" : "↓");
                asc = !asc;
            });
        });
    </script>

`)