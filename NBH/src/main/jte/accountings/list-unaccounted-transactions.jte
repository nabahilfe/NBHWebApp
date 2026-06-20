<!--
  Copyright (c) 2025–2026 Maximilian Weißböck
  Licensed under the MIT License (see LICENSE file).
-->

@import java.util.List
@import static eu.nabahilfe.webapp.DateFormatter.*
@import static eu.nabahilfe.webapp.NumberFormatter.*
@import eu.nabahilfe.webapp.accountings.Transaction
@import eu.nabahilfe.webapp.accountings.TransactionType

@param List<Transaction> transactions
@param String selectedType = ""

@param java.lang.String successMessage = null
@param java.lang.String errorMessage = null

@param eu.nabahilfe.webapp.security.CsrfHiddenInput csrfHiddenInput
@param eu.nabahilfe.webapp.security.ViewContext ctxt


@template.layout.mainpage(
    csrfHiddenInput = csrfHiddenInput,
    ctxt = ctxt,
    content = @`

    <h3>Sonstige offene Buchungen</h3>
    <br/>

    <!-- Filter -->
    <form method="get" action="/accountings/misc-unaccounted" class="row g-2 align-items-center mb-3">
        <div class="col-auto">
            <label for="typeFilter" class="col-form-label">Anzeigen:</label>
        </div>
        <div class="col-auto">
            <select class="form-select" id="typeFilter" name="type" onchange="this.form.submit()">
                <option value="" selected="${selectedType.isEmpty()}">-- Alle --</option>
                <option value="${TransactionType.INCOME.name()}" selected="${selectedType.equals(TransactionType.INCOME.name())}">Einnahmen</option>
                <option value="${TransactionType.EXPENSE.name()}" selected="${selectedType.equals(TransactionType.EXPENSE.name())}">Ausgaben</option>
            </select>
        </div>
    </form>

    @if(transactions.isEmpty())
        <div class="alert alert-success" role="alert">
            Keine offenen Buchungen vorhanden.
        </div>
    @else
        <table class="table table-hover table-sm" id="txTable">
            <thead>
                <tr>
                    <th scope="col" data-col="0" style="cursor:pointer;">Datum ↕</th>
                    <th scope="col">Art</th>
                    <th scope="col" data-col="2" style="cursor:pointer;">Beschreibung ↕</th>
                    <th scope="col" class="text-end">Betrag</th>
                    @if(ctxt.isTreasurer() || ctxt.isAdmin())
                        <th scope="col" class="text-center">Buchen</th>
                    @endif
                </tr>
            </thead>
            <tbody class="table-group-divider">
                @for(Transaction tx : transactions)
                    <tr>
                        <td>${dateReverseDE(tx.getTransactionDate())}</td>
                        <td>
                            @if(tx.getTransactionType().equals(TransactionType.INCOME.name()))
                                <span class="badge bg-success">Einnahme</span>
                            @else
                                <span class="badge bg-danger">Ausgabe</span>
                            @endif
                        </td>
                        <td>${tx.getDescription()}</td>
                        <td class="text-end">€ ${numberDE(tx.getAmount())}</td>
                        @if(ctxt.isTreasurer() || ctxt.isAdmin())
                            <td class="text-center">
                                <a href="/accountings/book-transaction/${tx.getId()}"
                                   class="btn btn-outline-primary btn-sm bi-plus-lg" title="Buchen"></a>
                            </td>
                        @endif
                    </tr>
                @endfor
            </tbody>
        </table>
    @endif

    <br/>
    @template.components.display-statusmessage(successMessage, errorMessage)

    <script>
        document.addEventListener("DOMContentLoaded", () => {
            const table = document.getElementById("txTable");
            if (!table) return;
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
        });
    </script>

`)
